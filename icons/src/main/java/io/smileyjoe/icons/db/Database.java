package io.smileyjoe.icons.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.smileyjoe.icons.IconData;
import io.smileyjoe.icons.listener.SetupListener;
import io.smileyjoe.icons.util.Api;
import io.smileyjoe.icons.util.Scheduler;

/**
 * Helper class for using the database
 */
public class Database {
    private static AppDatabase sAppDatabase;

    private Database() {
    }

    /**
     * Setups up the db instance
     * <br/>
     * NOTE: this shouldn't be used directly, use
     * {@link io.smileyjoe.icons.Icon#setup(Context, ArrayList, SetupListener)} instead
     *
     * @param applicationContext current context
     * @param preloadNames icons to pre-load into the db
     * @param listener callback
     */
    public static void setup(Context applicationContext, ArrayList<String> preloadNames, SetupListener listener) {
        // Pause any other icon loading in case the db needs to be setup
        Scheduler.getInstance().pause();
        sAppDatabase = Room.databaseBuilder(applicationContext, AppDatabase.class, "icons")
                .addCallback(new RoomCallback(applicationContext, preloadNames, listener))
                .build();

        // this is to force the db to open, RoomDatabase.Callback.onOpen only fires when
        // there is a transaction on the db, and we use that for setup, so this just forces the
        // db to open at this point to get that process starting
        ScheduledExecutorService backgroundExecutor = Executors.newSingleThreadScheduledExecutor();
        backgroundExecutor.execute(() -> sAppDatabase.iconDataDao().getRowCount());
    }

    public static IconDataDao getIconData() {
        return sAppDatabase.iconDataDao();
    }

    /**
     * Callback for when the room db instance is created.
     * <br/>
     * Checks if the initial setup completed on open, if the table is empty, setup will run again
     */
    private static class RoomCallback extends RoomDatabase.Callback{

        private Context mApplicationContext;
        // icons to pre-load, these will download and be added to the db, with no callback
        private ArrayList<String> mPreloadNames;
        private SetupListener mListener;
        // This is just used to make sure setup doesn't fire more then once
        private boolean mSetupRunning = false;

        /**
         * Callback that runs setup when needed.
         *
         * @param applicationContext current context
         * @param preloadNames names of icons to download
         * @param listener listener for when the process is done
         */
        public RoomCallback(Context applicationContext, ArrayList<String> preloadNames, SetupListener listener){
            mApplicationContext = applicationContext;
            mPreloadNames = preloadNames;
            mListener = listener;
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            // if there are no rows something went wrong with the last setup
            if(getRowCount(db) <= 0){
                setup(db);
            } else {
                // if everything is setup, we can continue with any other icon loading
                Scheduler.getInstance().resume();
            }
        }

        /**
         * Set up the db by populating it will all available icon meta data
         *
         * @param db database instance
         */
        private void setup(SupportSQLiteDatabase db){
            if(!mSetupRunning) {
                mSetupRunning = true;
                // get all meta data, then get any pre-load icons
                Api.getAll(mApplicationContext, () -> {
                    // check if the meta data was inserted correctly
                    if (getRowCount(db) > 0) {
                        if(mPreloadNames != null && mPreloadNames.size() > 0) {
                            // get the meta data for the icons we want to preload
                            List<IconData> icons = sAppDatabase.iconDataDao().findByNames(mPreloadNames);

                            for (IconData icon : icons) {
                                // get and save the icon path data to the db
                                Api.getIcon(mApplicationContext, icon.getId(), null);
                            }
                        }

                        if (mListener != null) {
                            mListener.setupComplete();
                        }
                    } else if (mListener != null) {
                        mListener.setupFailed();
                    }

                    // setup is complete, so continue any other icon loading
                    Scheduler.getInstance().resume();
                });
            }
        }

        /**
         * To validate that the meta data was inserted, and setup was complete
         * we check the number of rows in the db
         *
         * @param db database instance
         * @return the number of rows
         */
        private long getRowCount(@NonNull SupportSQLiteDatabase db){
            String sql = "SELECT COUNT(*) FROM icondata";
            SupportSQLiteStatement statement = db.compileStatement(sql);
            return statement.simpleQueryForLong();
        }
    }
}
