package com.example.snapfind.ui.labels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.snapfind.MainActivity;
import com.example.snapfind.R;
import com.example.snapfind.StorageHandler.PhotoStorage;
import com.example.snapfind.common.MySingleton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class LabelsFragment extends Fragment {

    private LabelsViewModel labelsViewModel;
    private HashSet<String> labelsList;
    private ListView listViewForLabels;
    private Button sendAll;
    private Context context;
    public String serverIPAddress =  "172.17.7.142";

    private PhotoStorage photoStorage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getParentFragment().getContext();
        photoStorage = PhotoStorage.getInstance(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        labelsViewModel =
                ViewModelProviders.of(this).get(LabelsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_labels, container, false);
        final ImageButton addLabel = root.findViewById(R.id.imageButton_label_adder);
        final TextInputEditText addLabelInputText = root.findViewById(R.id.add_label_input_text);

        listViewForLabels = root.findViewById(R.id.listView_for_lables);
        sendAll = root.findViewById(R.id.button_send_all);
//        MainActivity.data.put("SLTC", new ArrayList<String>());
//        MainActivity.data.put("MSN", new ArrayList<String>());
//        MainActivity.data.put("MCR", new ArrayList<String>());

        photoStorage.addLabel("SLTC");
        photoStorage.addLabel("MSN");
        labelsList = new HashSet<>();
//        photoStorage.loadData();
        renderListView();

        addLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputValue = addLabelInputText.getText().toString();
                photoStorage.addLabel(inputValue);
//                MainActivity.data.put(inputValue, new ArrayList<String>());
                renderListView();
            }
        });

        listViewForLabels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getParentFragment().getContext(),"Clicked Item:" + i + " " + labelsList.toArray()[i],Toast.LENGTH_SHORT).show();
            }
        });

//        final List<String> labels = Arrays.asList((String) photoStorage.getPhotoData().keySet().toArray().toString());
        final  ArrayList<String> labels = new ArrayList<>();
        labels.addAll(photoStorage.getPhotoData().keySet());
        final int[] i = {0};
        final int[] j = {0};
        sendAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        if(i[0] < labels.size()){
                            String currentLabel = labels.get(i[0]);
                            String currentImagePath = photoStorage.getPhotoData().get(currentLabel).get(j[0]);
                            Bitmap currentImageBitMap = createBitmapFromFile(currentImagePath);
                            uploadImage(currentLabel, currentImageBitMap);
                            Log.d(TAG, "Doing task");
                            Toast.makeText(context, "Doing task", Toast.LENGTH_LONG).show();
                            if(j[0] + 1 < photoStorage.getPhotoData().get(currentLabel).size()){
                                j[0] += 1;
                            }else{
                                j[0] = 0;
                                i[0] += 1;
                            }
                        }
//                        handler.postDelayed(this, 1000);
                        handler.postDelayed(this, 25000);
                    }
                };
                handler.post(task);

//                for(String label: photoStorage.getPhotoData().keySet()){
//                    String currentLabel = label;
//                    delay();
//                    for(String path: photoStorage.getPhotoData().get(label)){
//                        String currentImagePath = path;
//                        Bitmap currentImageBitMap = createBitmapFromFile(currentImagePath);
//                        delay();
//                        uploadImage(currentLabel, currentImageBitMap);
//                    }
//
//                }
            }
        });

        return root;
    }

 public void delay(){
     final Handler handler = new Handler();
     Runnable runnable = new Runnable() {
         @Override
         public void run() {
             // do something
             handler.postDelayed(this, 10000L);  // 10 second delay
         }
     };
     handler.post(runnable);
 }

    public void renderListView(){
//        labelsList.addAll(MainActivity.data.keySet());
        labelsList.addAll(photoStorage.getAllLabels());
        ArrayAdapter listViewAdapter = new ArrayAdapter(getParentFragment().getContext(), android.R.layout.simple_list_item_1, labelsList.toArray());
        listViewForLabels.setAdapter(listViewAdapter);
    }


    public Bitmap createBitmapFromFile(String filePath){
        File imgFile = new  File(filePath);
        Bitmap myBitmap = null;
        if(imgFile.exists()){
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return myBitmap;
    }

    private void uploadImage(final String fileName, final Bitmap bitmap){
        String url = "http://"+serverIPAddress+":8080/upload";
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
        RequestQueue requestQueue = MySingleton.getInstance(context).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
//                        try {
////                            JSONObject jsonObject = new JSONObject(response);
////                            String Response = jsonObject.getString("response");
//                            Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
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

}