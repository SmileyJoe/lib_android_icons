package io.smileyjoe.icons;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {IconData.class}, version = 1)
abstract class AppDatabase extends RoomDatabase {
    public abstract IconDataDao iconDataDao();
}
