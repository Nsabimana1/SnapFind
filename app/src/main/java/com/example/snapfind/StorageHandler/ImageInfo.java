package com.example.snapfind.StorageHandler;


import android.graphics.Bitmap;

import java.io.File;
import java.lang.reflect.Array;

public class ImageInfo {
    public String imageLabel;
    public File imageFile;
//    public Array imageBuffer;

    public ImageInfo(String imageLabel, File imageFile){
        this.imageLabel = imageLabel;
        this.imageFile = imageFile;
    }

//    public void constructImage(){
//        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
//        Bitmap bitmap
//    }
    public File getBitmap() {
        return imageFile;
    }

    public String getImageLabel() {
        return imageLabel;
    }
}
