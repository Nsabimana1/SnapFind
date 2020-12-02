package com.example.snapfind.ui.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.snapfind.R;
import com.example.snapfind.StorageHandler.PhotoStorage;
import com.example.snapfind.common.MySingleton;
import com.example.snapfind.networking.DataCleaner;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public String currentPhotoPath;
    private DashboardViewModel dashboardViewModel;

    private ImageView imageViewer;
    private Button cameraButton;
    private TextInputEditText inputEditTextLabel;
    private Button trainButton;
    private Button findButton;
    private Button newImageCaptureButton;

    public String serverIPAddress = "172.17.7.142";
    public String currentImageLabel;
    public Bitmap currentImageBitMap;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getParentFragment().getContext();
//        photoStorage = PhotoStorage.getInstance(getParentFragment().getContext());
//        dataCleaner = new DataCleaner(photoStorage.getPhotoData());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        imageViewer = root.findViewById(R.id.imageViewer);
        cameraButton = root.findViewById(R.id.camera_button);
        inputEditTextLabel = root.findViewById(R.id.imageLabelText);
        trainButton = root.findViewById(R.id.train_button);
        findButton = root.findViewById(R.id.find_button);

        askCameraPermission();

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getParentFragment().getContext(), "Hello camera", Toast.LENGTH_SHORT).show();
                askCameraPermission();
            }
        });

        trainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImageLabel = inputEditTextLabel.getText().toString();
                if(currentImageLabel.length() < 2){
                    Toast.makeText(getParentFragment().getContext(), "Please provide the image label", Toast.LENGTH_SHORT).show();
                }else{
                    String result = checkServerConnection();
                    if(result.equals("NoConnection")){
                        Toast.makeText(getParentFragment().getContext(), "Server not Found", Toast.LENGTH_SHORT).show();
                    }else {
                        uploadImage(currentImageLabel, currentImageBitMap);
                    }
                    uploadImage(currentImageLabel, currentImageBitMap);
                }
            }
        });


        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImageLabel = "noName";
                uploadImage(currentImageLabel, currentImageBitMap);
            }
        });


        return root;
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

//    private void openCamera() {
//        Intent  camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(camera, CAMERA_REQUEST_CODE);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CAMERA_REQUEST_CODE){
//            Bitmap image = (Bitmap) data.getExtras().get("data");
//            imageViewer.setImageBitmap(image);
            if(resultCode == Activity.RESULT_OK){
                File file = new File(currentPhotoPath);
                imageViewer.setImageURI(Uri.fromFile(file));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);

                currentImageLabel = inputEditTextLabel.getText().toString();
                currentImageBitMap = createBitmapFromFile(currentPhotoPath);

//                UserResult nextFrag= new UserResult()

//                View root = inflater.inflate(R.layout.fragment_home, container, false);
//                Intent i = new Intent(getActivity(), R.layout.fragment_dashboard);
//                startActivity(i);

//                Fragment myFragment = new MyFragment();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.navigation_home, this).addToBackStack(null).commit();
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


    public Bitmap createBitmapFromFile(String filePath){
        File imgFile = new  File(filePath);
        Bitmap myBitmap = null;
        if(imgFile.exists()){
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return myBitmap;
    }


    public void messageStatusDialog(String notificationMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("The predicted label for the image is:");
        builder.setMessage(notificationMessage);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // nothing needs to be implemented here
            }
        });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private void uploadImage(final String fileName, final Bitmap bitmap){
        String url = "http://"+serverIPAddress+":8080/upload";
//        RequestQueue requestQueue = Volley.newRequestQueue(context);

        RequestQueue requestQueue = MySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response from upload is", "recevived message is" + response.toString());
                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        if(!response.toString().equals("File uploaded successfully")){
                            messageStatusDialog(response.toString());
                        }

//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String Response = jsonObject.getString("response");
//                            Toast.makeText(context, Response, Toast.LENGTH_LONG).show();
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", fileName);
                params.put("image", imageToString(bitmap));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Log.d("Making a call", "call was made");
        requestQueue.add(stringRequest);
//        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte [] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.NO_WRAP);
    }


    public String checkServerConnection(){
        String url = "http://"+serverIPAddress+":8080/hi";
//        String url = "http://10.253.192.101:8080/hi";
        final String[] responseResult = {"NoConnection"};
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest objectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Recieving from a call", "recevived message is" + response.toString());
                        responseResult[0] =  response.toString();
//                        msgView.setText("Response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Request Response", error.toString());
                    }

                });

        Toast.makeText(getParentFragment().getContext(),"Listening to end point", Toast.LENGTH_SHORT).show();
        requestQueue.add(objectRequest);
        return  responseResult[0];
    }

}