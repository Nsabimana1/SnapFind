package com.example.snapfind.networking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.snapfind.StorageHandler.ImageInfo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DataCleaner {
    public String objectToSend;
    public ArrayList<ImageInfo> imageInfoList;
    public HashMap<String, ArrayList<String>> allData;
    public  DataCleaner(HashMap<String, ArrayList<String>> allData){
        imageInfoList = new ArrayList<>();
        this.allData = allData;
        extractInformation();
    }

    public void extractInformation(){
        for (String label: allData.keySet()){
            for(String imagePath: allData.get(label)){
                imageInfoList.add(new ImageInfo(label, new File(imagePath)));
            }
        }
    }

    public Bitmap createBitmapFromFile(String filePath){
        File imgFile = new  File(filePath);
        Bitmap myBitmap = null;
        if(imgFile.exists()){
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return myBitmap;
    }

    public JSONObject getObject(String label, File file){
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject();
            jsonObject.put("id", null);
            jsonObject.put("imageLabel", label);
            jsonObject.put("imageFile", file);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONArray getObjectToSend() {
        JSONArray jsonArray= new JSONArray();
        for (ImageInfo im: imageInfoList) {
            jsonArray.put(getObject(im.imageLabel, im.imageFile));
//            jsonObject.put(im.imageLabel, im.imageFile);
            System.out.println(" the current image is" + im.imageLabel);
        }



//        Gson gson = new Gson();
//        JSONArray jsArray = new JSONArray(imageInfoList);
//        objectToSend = gson.toJson(imageInfoList);
//        try{
//            return new JSONObject(objectToSend);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
        return jsonArray;
    }
}
