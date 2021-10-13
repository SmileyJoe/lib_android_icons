package io.smileyjoe.icons;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private static AppDatabase sAppDatabase;

    private Database() {
    }

    public static void setup(Context applicationContext, ArrayList<String> preloadNames){
        sAppDatabase = Room.databaseBuilder(applicationContext, AppDatabase.class, "icons")
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        Api.getAll(applicationContext, () -> {
                            List<IconData> icons = sAppDatabase.iconDataDao().findByNames(preloadNames);

                            for(IconData icon:icons){
                                Api.getIcon(applicationContext, icon.getId(), null);
                            }
                        });
                    }
                })
                .build();
    }

    public static IconDataDao getIconData(){
        return sAppDatabase.iconDataDao();
    }
}
