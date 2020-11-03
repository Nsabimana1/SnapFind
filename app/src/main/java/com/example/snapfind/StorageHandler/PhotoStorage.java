package com.example.snapfind.StorageHandler;

import android.content.Context;
import android.database.Cursor;
import android.icu.text.SymbolTable;
import android.util.Log;

import androidx.room.Room;

import com.example.snapfind.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class PhotoStorage {
    HashMap<String, ArrayList<String>> photoData;
    HashMap<String, Integer> labelToCurrentPicture;
    private String currentImagePath;

    private static PhotoStorage instance = null;
    private static DatabaseHelper databaseHelper = null;

    private AppDatabase db;

    private PhotoStorage(Context context){
        photoData = new HashMap<>();
        labelToCurrentPicture = new HashMap<>();
//        db = Room.databaseBuilder(context,
//                AppDatabase.class, "ImageLabelsDatabase.db").build();
        databaseHelper = new DatabaseHelper(context);
    }


    public void loadData(){
        Cursor data = databaseHelper.getData();
        System.out.println("I was herrrrerere");
        Log.d("Checking method", "I was hereeee");
        while(data.getCount() > 0 && data.moveToNext()){
            String label = data.getString(data.getColumnIndex(DatabaseHelper.COL1));
            String imagePath = data.getString(data.getColumnIndex(DatabaseHelper.COL2));
            if(!labelToCurrentPicture.containsKey(label)){
                labelToCurrentPicture.put(label, 0);
            }

            if(!photoData.containsKey(label)){
                photoData.put(label, new ArrayList<String>());
            }
            photoData.get(label).add(imagePath);
            Log.d( "data is",  data.getString(1));
            System.out.print(" the data from data base is" + data.getString(1));
        }
        data.close();
    }

    public static PhotoStorage getInstance(Context context){
        if(instance == null){
            instance = new PhotoStorage(context);
        }
        return instance;
    }

    public String getCurrentImagePath(String label) {
        int currSize = photoData.get(label).size();
        /// this code need to change
        if (currSize == 0) return null;
        int index = labelToCurrentPicture.get(label);
        if(photoData.get(label).size() == 0) return null;
        ArrayList<String> imagePaths = photoData.get(label);
        String photoPath = imagePaths.get(index);
        return photoPath;
    }

    public String getNexImagePath(String label){
        int currSize = photoData.get(label).size();
        if (currSize == 0) return null;
        int nextIndex = (labelToCurrentPicture.get(label) + 1) % currSize;
        labelToCurrentPicture.put(label, nextIndex);
        int index = labelToCurrentPicture.get(label);
        ArrayList<String> imagePaths = photoData.get(label);
        String photoPath = imagePaths.get(index);
        return photoPath;
    }

    public String getPrevImagePath(String label){
        int currSize = photoData.get(label).size();
        if (currSize == 0) return null;
        int prevIndex = (labelToCurrentPicture.get(label) - 1) % currSize;
        if(prevIndex == -1) prevIndex = currSize - 1;
        labelToCurrentPicture.put(label, prevIndex);
        int index = labelToCurrentPicture.get(label);
        ArrayList<String> imagePaths = photoData.get(label);
        String photoPath = imagePaths.get(index);
        return photoPath;
    }

    public void addLabel(String label){
        if(!labelToCurrentPicture.containsKey(label)){
            labelToCurrentPicture.put(label, 0);
        }

        if(!photoData.containsKey(label)){
            photoData.put(label, new ArrayList<String>());
        }
    }

    public ArrayList<String> getAllLabels(){
        final ArrayList<String> labelsList = new ArrayList<>();
        for (String l: photoData.keySet()) {
            labelsList.add(l);
        }
        return labelsList;
    }

    public void addImage(String label, String imagePath){
        photoData.get(label).add(imagePath);
        databaseHelper.addNewImage(label, imagePath);
    }

    public HashMap<String, ArrayList<String>> getPhotoData() {
        return photoData;
    }
}
