package com.example.snapfind.StorageHandler;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ImageRecord.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ImageRecordDao imageRecordDao();
}
