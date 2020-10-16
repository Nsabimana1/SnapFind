package com.example.snapfind.StorageHandler;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ImageRecord {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "image_label")
    public String imageLabel;

    @ColumnInfo(name = "image_file_path")
    public String imageFilePath;
}
