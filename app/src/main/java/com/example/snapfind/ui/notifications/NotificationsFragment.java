package com.example.snapfind.ui.notifications;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.snapfind.R;
import com.example.snapfind.StorageHandler.PhotoStorage;
import com.example.snapfind.common.MySingleton;
import com.example.snapfind.networking.DataCleaner;
import com.example.snapfind.networking.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import java.util.Base64;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private Context context;
    private Button callAPI;
    private TextView msgView;

    public PhotoStorage photoStorage;
    public DataCleaner dataCleaner;

    public String serverIPAddress = "172.17.7.142";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getParentFragment().getContext();
        photoStorage = PhotoStorage.getInstance(getParentFragment().getContext());
//        dataCleaner = new DataCleaner(photoStorage.getPhotoData());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        final ListView listViewForLabels = root.findViewById(R.id.listView_for_lables);
        callAPI = root.findViewById(R.id.call_button);
        msgView = root.findViewById(R.id.msgView);

        final ArrayList<String> labels = new ArrayList<>();
        labels.add("SLTC");
        labels.add("MSN");
        labels.add("MCR");
        labels.add("SLTC");
        labels.add("MSN");
        labels.add("MCR");
        labels.add("SLTC");
        labels.add("MSN");
        labels.add("MCR");
        labels.add("SLTC");
        labels.add("MSN");
        labels.add("MCR");

        ArrayAdapter listViewAdapter = new ArrayAdapter(getParentFragment().getContext(), android.R.layout.simple_list_item_1, labels);
        listViewForLabels.setAdapter(listViewAdapter);

        callAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getParentFragment().getContext(),"API is called", Toast.LENGTH_SHORT).show();
//                postData();
                callRestAIP();
//                makeCallWithJsonObject();
//                makeCall();
//                uploadImage(dataCleaner.currentImageLabel, dataCleaner.currentImageBitMap);
            }
        });

        listViewForLabels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getParentFragment().getContext(),"Clicked Item:" + i + " " + labels.get(i),Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    public void postData(){
        String result = Utilities.callRestAPIWithStringRequest(context, "http://172.17.7.142:8080/hi");
        msgView.setText(result);
    }

    public void callRestAIP(){
//        String url = "http://192.168.19.97:8080/hi";
        String url = "http://" + serverIPAddress+ ":8080/hi";
//        RequestQueue requestQueue =  Volley.newRequestQueue(context);

        RequestQueue requestQueue = MySingleton.getInstance(context).getRequestQueue();
        StringRequest objectRequest = new StringRequest(Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getParentFragment().getContext(),"THE RESPONSE IS" + response.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Recieving from a call", "recevived message is" + response.toString());
                        msgView.setText("Response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getParentFragment().getContext(),"THE ERROR IS" + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("Request Response", error.toString());
                    }
        });
//        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Toast.makeText(getParentFragment().getContext(),"Listening to end point", Toast.LENGTH_SHORT).show();
        requestQueue.add(objectRequest);
    }


    public void makeCallWithJsonObject() {
        String url = "http://172.17.7.142:8080/multipleImages";
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject();
            jsonObject.put("imageLabel", "goodMorning");
            jsonObject.put("filePath", "D:\\Classes Seneior Year\\Senior Seminar\\Senior Capstone\\ImageClassificationRestAPI\\h");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONArray jsonArray = dataCleaner.getObjectToSend();

        JsonArrayRequest objectRequest = new JsonArrayRequest(Method.POST,url,jsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.d("Recieving from a call", "recevived message is" + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Request Response", error.toString());
                    }})
                {
                        @Override
                        protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {

                            if (response.data == null || response.data.length == 0) {
                                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
                            } else {
                                return super.parseNetworkResponse(response);
                            }
                        }
                };
        Toast.makeText(getParentFragment().getContext(),"Listening to end point", Toast.LENGTH_SHORT).show();
        requestQueue.add(objectRequest);
    }


    public void makeCall(){
        String url = "http://192.168.19.97:8080/hi";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Recieving from a call", "recevived message is" + response.toString());
                        msgView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        // Access the RequestQueue through your singleton class.
        Log.d("Making a call", "call was made");
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void uploadImage(final String fileName, final Bitmap bitmap){
        String url = "http://"+serverIPAddress+":8080/upload";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       try {
                           JSONObject jsonObject = new JSONObject(response);
                           String Response = jsonObject.getString("response");
                           Toast.makeText(context, Response, Toast.LENGTH_LONG).show();
                       }catch (JSONException e){
                           e.printStackTrace();
                       }
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