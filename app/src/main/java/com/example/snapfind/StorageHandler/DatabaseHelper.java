package com.example.snapfind.StorageHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.snapfind.MainActivity;
import com.example.snapfind.ui.home.HomeFragment;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "image_table";
    public static final String COL0 = "id";
    public static final String COL1 = "label";
    public static final String COL2 = "image_file_path";

    // Database version
    private static final int DATABASE_VERSION = 1;
    // Database name
    private static final String DATABASE_NAME = "ImageLabelsDatabase.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL1 + " TEXT," +
                    COL2 + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        String createTable = "CREATE TABLE" + TABLE_NAME + "(ID INTEGER KEY AUTOINCREMENT ," + COL1 + " TEXT," + COL2 +" TEXT)";
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
//        String createTable = String.format("CREATE TABLE%s(ID INTEGER KEY AUTOINCREMENT ,%s TEXT,%s TEXT)", TABLE_NAME, COL1, COL2);
//        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
//        sqLiteDatabase.execSQL(String.format("DROP IF TABLE EXISTS %s", TABLE_NAME));

        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean addLabel(String label){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, label);

        Log.d(TAG, "addData: Adding " + label + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1) return false;
        else return true;
    }

    public boolean addNewImage(String label, String imagePath){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, label);
        contentValues.put(COL2, imagePath);
        Log.d(TAG, "addData: Adding " + label + " and image path " + imagePath + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1) return false;
        else return true;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}
