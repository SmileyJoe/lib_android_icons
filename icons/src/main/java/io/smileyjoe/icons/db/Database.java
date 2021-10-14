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

public class Database {
    private static AppDatabase sAppDatabase;

    private Database() {
    }

    public static void setup(Context applicationContext, ArrayList<String> preloadNames, SetupListener listener) {
        Scheduler.getInstance().pause();
        sAppDatabase = Room.databaseBuilder(applicationContext, AppDatabase.class, "icons")
                .addCallback(new RoomCallback(applicationContext, preloadNames, listener))
                .build();

        // this is to force the db to open //
        ScheduledExecutorService backgroundExecutor = Executors.newSingleThreadScheduledExecutor();
        backgroundExecutor.execute(new OpenDb());
    }

    public static IconDataDao getIconData() {
        return sAppDatabase.iconDataDao();
    }

    private static class OpenDb implements Runnable{
        @Override
        public void run() {
            sAppDatabase.iconDataDao().getRowCount();
        }
    }

    private static class RoomCallback extends RoomDatabase.Callback{

        private Context mApplicationContext;
        private ArrayList<String> mPreloadNames;
        private SetupListener mListener;
        private boolean mSetupRunning = false;

        public RoomCallback(Context applicationContext, ArrayList<String> preloadNames, SetupListener listener){
            mApplicationContext = applicationContext;
            mPreloadNames = preloadNames;
            mListener = listener;
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            if(getRowCount(db) <= 0){
                setup(db);
            } else {
                Scheduler.getInstance().resume();
            }
        }

        private void setup(SupportSQLiteDatabase db){
            if(!mSetupRunning) {
                mSetupRunning = true;
                Api.getAll(mApplicationContext, () -> {
                    if (getRowCount(db) > 0) {
                        List<IconData> icons = sAppDatabase.iconDataDao().findByNames(mPreloadNames);

                        for (IconData icon : icons) {
                            Api.getIcon(mApplicationContext, icon.getId(), null);
                        }

                        Scheduler.getInstance().resume();

                        if (mListener != null) {
                            mListener.setupComplete();
                        }
                    } else if (mListener != null) {
                        mListener.setupFailed();
                    }
                });
            }
        }

        private long getRowCount(@NonNull SupportSQLiteDatabase db){
            String sql = "SELECT COUNT(*) FROM icondata";
            SupportSQLiteStatement statement = db.compileStatement(sql);
            return statement.simpleQueryForLong();
        }
    }
}
