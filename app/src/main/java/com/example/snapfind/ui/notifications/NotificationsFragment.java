package com.example.snapfind.ui.notifications;

import android.content.Context;
import android.os.Bundle;
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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
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
import com.example.snapfind.networking.DataCleaner;
import com.example.snapfind.networking.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private Context context;
    private Button callAPI;
    private TextView msgView;

    public PhotoStorage photoStorage;
    public DataCleaner dataCleaner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getParentFragment().getContext();
        photoStorage = PhotoStorage.getInstance(getParentFragment().getContext());
        dataCleaner = new DataCleaner(photoStorage.getPhotoData());
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
//                callRestAIP();
                makeCallWithJsonObject();
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
        String url = "http://172.17.7.142:8080/hi";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest objectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Recieving from a call", "recevived message is" + response.toString());
                        msgView.setText("Response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Request Response", error.toString());
                    }

        });

//        JsonObjectRequest objectRequest = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("Recieving from a call", "recevived message is" + response.toString());
//                        msgView.setText("Response: " + response.toString());
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Request Response", error.toString());
//                    }
//                }
//        );
        Toast.makeText(getParentFragment().getContext(),"Listening to end point", Toast.LENGTH_SHORT).show();
        requestQueue.add(objectRequest);
    }

    public void makeCallWithJsonObject() {
        String url = "http://172.17.7.142:8080/multipleImages";
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject();
            jsonObject.put("imageLabel", "goodMorning");
            jsonObject.put("imageFile", "D:\\Classes Seneior Year\\Senior Seminar\\Senior Capstone\\ImageClassificationRestAPI\\h");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONArray jsonArray = dataCleaner.getObjectToSend();

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.POST,url,jsonArray,
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


//        String json = new Gson().toJson(dataCleaner.imageInfoList);
//        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
//        new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("Recieving from a call", "recevived message is" + response.toString());
//                msgView.setText("Response: " + response.toString());
//            }
//        },
//        new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Request Response", error.toString());
//            }
//        }
//        );
        Toast.makeText(getParentFragment().getContext(),"Listening to end point", Toast.LENGTH_SHORT).show();
        requestQueue.add(objectRequest);
    }


    public void makeCall(){
        String url = "http://172.17.7.142:8080/hi";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

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
}