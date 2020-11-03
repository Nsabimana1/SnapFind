package com.example.snapfind.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.snapfind.MainActivity;
import com.example.snapfind.R;
import com.example.snapfind.StorageHandler.PhotoStorage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Spinner dropDownSpinner;
    ImageButton prevButton;
    ImageButton nextButton;
    ImageButton cameraButton;
    ImageButton addImage;
    ImageView imageViewer;
    ArrayList<String> dropDownValues;

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public String currentPhotoPath;
    private String currentLabel;
    private String currentVisibleImagePath;

    private PhotoStorage photoStorage;

//    public HomeFragment() {
//        dropDownValues = new ArrayList<>();
////        photoStorage.loadData();
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoStorage = PhotoStorage.getInstance(getParentFragment().getContext());
        dropDownValues = new ArrayList<>();
    }




    public void renderCurrentImage (){
        if(currentVisibleImagePath != null){
            File file = new File(currentVisibleImagePath);
            imageViewer.setImageURI(Uri.fromFile(file));
        }
    }

    public void setCurrentVisibleImagePath(){
        if(currentLabel != null) {
            currentVisibleImagePath = photoStorage.getCurrentImagePath(currentLabel);
        }
//        if(!MainActivity.data.isEmpty()){
//            for(String label: MainActivity.data.keySet()){
//                currentVisibleLabel = label;
//                break;
//            }
//            if(MainActivity.data.get(currentVisibleLabel).size() > 0){
//                currentVisibleImagePath = MainActivity.data.get(currentVisibleLabel).get(0);
//            }
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        dropDownSpinner = root.findViewById(R.id.spinner);
        prevButton = root.findViewById(R.id.imageButtonPrev);
        nextButton = root.findViewById(R.id.imageButtonNext);
        cameraButton = root.findViewById(R.id.imageButton_camera);
        imageViewer = root.findViewById(R.id.imageView_renderer);
        addImage = root.findViewById(R.id.imageButton_add_Image);

        photoStorage.loadData();
        refreshSpinner();
        setCurrentVisibleImagePath();
        renderCurrentImage();
        addImage.setEnabled(false);


//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getParentFragment().getContext(), android.R.layout.simple_list_item_1, dropDownValues);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dropDownSpinner.setAdapter(spinnerAdapter);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getParentFragment().getContext(), "Hello camera", Toast.LENGTH_SHORT).show();
                askCameraPermission();
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MainActivity.data.get(currentVisibleLabel).add(currentVisibleImagePath);
//                MainActivity.databaseHelper.addNewImage(currentVisibleLabel, currentVisibleImagePath);
//                System.out.println("the size of the current images is " + MainActivity.data.get(currentVisibleLabel).size());

                photoStorage.addImage(currentLabel, currentVisibleImagePath);
                addImage.setEnabled(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLabel != null) {
                    currentVisibleImagePath = photoStorage.getNexImagePath(currentLabel);
//                getNextImagePath();
                    renderCurrentImage();
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getPrevImagePath();
                if(currentLabel != null) {
                    currentVisibleImagePath = photoStorage.getPrevImagePath(currentLabel);
                    renderCurrentImage();
                }
            }
        });


        dropDownSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position >= 0){
                    currentLabel = parent.getSelectedItem().toString();
                    Toast.makeText(getParentFragment().getContext(), "The selected label is " + currentLabel, Toast.LENGTH_SHORT).show();
                    currentVisibleImagePath = photoStorage.getCurrentImagePath(currentLabel);
                    renderCurrentImage();
//                    renderFirstImage(currentVisibleLabel);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    public void renderFirstImage(String label){
        if(MainActivity.data.get(label).size() > 0){
            currentVisibleImagePath = MainActivity.data.get(label).get(0);
        }
        renderCurrentImage();
    }


    public void getNextImagePath(){
        if(currentLabel != null) {
            ArrayList<String> currentPhotoList = MainActivity.data.get(currentLabel);
            if (currentPhotoList.size() > 0) {
                for (int i = 0; i < currentPhotoList.size(); i++) {
                    if (currentVisibleImagePath.equals(currentPhotoList.get(i))) {
                        currentVisibleImagePath = currentPhotoList.get((i + 1) % currentPhotoList.size());
                    }
                }
            }
        }
    }

    public void getPrevImagePath(){
        if( currentLabel != null){
            ArrayList<String> currentPhotoList = MainActivity.data.get(currentLabel);
            if(currentPhotoList.size() > 0){
                for (int i = currentPhotoList.size()-1; i >= 0;  i--) {
                    if(currentVisibleImagePath.equals(currentPhotoList.get(i))){
                        currentVisibleImagePath = currentPhotoList.get((i - 1) % currentPhotoList.size());
                    }
                }
            }
        }
    }

    public void refreshSpinner(){
//        for(String S: MainActivity.data.keySet()){
//            dropDownValues.add(S);
//        }

        dropDownValues.addAll(photoStorage.getAllLabels());

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getParentFragment().getContext(), android.R.layout.simple_list_item_1, dropDownValues);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownSpinner.setAdapter(spinnerAdapter);
    }

    private void askCameraPermission() {
        if(ContextCompat.checkSelfPermission(getParentFragment().getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getParentFragment().getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else{
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else{
                Toast.makeText(getParentFragment().getContext(), "Camera permission required", Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CAMERA_REQUEST_CODE){
//            Bitmap image = (Bitmap) data.getExtras().get("data");
//            imageViewer.setImageBitmap(image);
            if(resultCode == Activity.RESULT_OK){
                /// for add images
                addImage.setEnabled(true);
                currentVisibleImagePath = currentPhotoPath;

                File file = new File(currentPhotoPath);
//                imageViewer.setImageURI(Uri.fromFile(file));
                renderCurrentImage();

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);

//                /// for add images
//                addImage.setEnabled(true);
//                currentVisibleImagePath = currentPhotoPath;

            }
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getParentFragment().getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getParentFragment().getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getParentFragment().getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


}