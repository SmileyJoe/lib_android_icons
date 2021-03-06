package io.smileyjoe.icons.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import io.smileyjoe.icons.IconData;

@Database(entities = {IconData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract IconDataDao iconDataDao();
}
