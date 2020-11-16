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

    public String currentImageLabel;
    public Bitmap currentImageBitMap;

    public  DataCleaner(HashMap<String, ArrayList<String>> allData){
        imageInfoList = new ArrayList<>();
        this.allData = allData;
        extractInformation();
    }

    public void extractInformation(){
        for (String label: allData.keySet()){
            for(String imagePath: allData.get(label)){
                imageInfoList.add(new ImageInfo(label, imagePath));
                currentImageLabel = label;
                currentImageBitMap = createBitmapFromFile(imagePath);
                break;
            }
            break;
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

//    from stack overflow
    public JSONArray output(double[][] input) {
//        double[] out = new double[input.length * input[0].length];
        JSONArray jsonArray= new JSONArray();
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
//                out[i + (j * input.length)] = input[i][j];
                try {
                    jsonArray.put( input[i][j]);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray;
    }

    public JSONObject getObject(String label, double[][] imagePixels2D){
//        for (int i = 0; i < imagePixels.length; i++) {
//            for (int j = 0; j < imagePixels[0].length ; j++) {
//                System.out.println(imagePixels[i][j]);
//            }
//        }

//        double[] imagePixels = Stream.of(imagePixels2D)
//                .flatMap(Stream::of)
//                .toArray(double[]::new);

        JSONArray imagePixels = output(imagePixels2D);
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject();
            jsonObject.put("id", null);
            jsonObject.put("imageLabel", label);
//            jsonObject.put("imageFile", file);
            jsonObject.put("imagePixels", imagePixels);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONArray getObjectToSend() {
        JSONArray jsonArray= new JSONArray();
        for (ImageInfo im: imageInfoList) {
            jsonArray.put(getObject(im.imageLabel, im.imagePixels2D));
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

//        System.out.println(jsonArray.toString());
        return jsonArray;
    }
}
