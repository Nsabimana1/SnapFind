package com.example.snapfind.StorageHandler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageRecordDao {
    @Query("SELECT * FROM imageRecord")
    List<ImageRecord> getAll();

    @Query("SELECT * FROM imageRecord WHERE id IN (:imageRecordIds)")
    List<ImageRecord> loadAllByIds(int[] imageRecordIds);

    @Query("SELECT * FROM imagerecord WHERE image_label LIKE :image_label AND " +
            "image_file_path LIKE :image_file_path LIMIT 1")
    ImageRecord findByLabelAndPath(String image_label, String image_file_path);

    @Insert
    void insertAll(ImageRecord... imageRecord);

    @Delete
    void delete(ImageRecord imageRecord);
}
