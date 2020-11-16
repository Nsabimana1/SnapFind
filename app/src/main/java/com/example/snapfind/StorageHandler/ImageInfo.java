package com.example.snapfind.StorageHandler;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImageInfo {
    public String imageLabel;
    public File imageFile;
    public String imageFilePath;
    public double [][] imagePixels2D;
    public double[] imagePixels;
//    public Array imageBuffer;

    public ImageInfo(String imageLabel, String imageFilePath){
        this.imageLabel = imageLabel;
//        this.imageFile = imageFile;
        Bitmap currentImageBitMap = createBitmapFromFile(imageFilePath);
//        int[] out = new int[input.length * input[0].length]
        this.imagePixels = new double[currentImageBitMap.getWidth() * currentImageBitMap.getHeight()];
        this.imagePixels2D = new double[currentImageBitMap.getWidth()][currentImageBitMap.getHeight()];

        for (int i = 0; i < currentImageBitMap.getWidth() ; i++) {
            for (int j = 0; j < currentImageBitMap.getHeight() ; j++) {
//                imagePixels[i + (j * imagePixels.length)] = currentImageBitMap.getPixel(i, j);
//                imagePixels.add((double) currentImageBitMap.getPixel(i, j));
                    imagePixels2D[i][j] = currentImageBitMap.getPixel(i, j);
            }
        }
    }

    private Bitmap createBitmapFromFile(String filePath){
        File imgFile = new  File(filePath);
        Bitmap myBitmap = null;
        if(imgFile.exists()){
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return myBitmap;
    }

//    public void constructImage(){
//        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
//        Bitmap bitmap
//    }
    public File getImagePixels() {
        return imageFile;
    }
    public String getImageLabel() {
        return imageLabel;
    }
}
