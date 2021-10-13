package io.smileyjoe.icons;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
interface IconDataDao {

    @Query("SELECT * FROM icondata WHERE id LIKE :id LIMIT 1")
    IconData findById(String id);

    @Query("SELECT * FROM icondata WHERE name LIKE :name LIMIT 1")
    IconData findByName(String name);

    @Query("SELECT * FROM icondata WHERE name IN (:names)")
    List<IconData> findByNames(ArrayList<String> names);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<IconData> icons);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IconData icon);

}
