package com.example.snapfind.networking;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Utilities {
    // From: https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code
    public static String getLocalIpAddress() throws SocketException {
        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                    return inetAddress.getHostAddress();
                }
            }
        }
        return "No IP address found";
    }

    public static String callRestAPIWithStringRequest(Context context, String url){
//        String url = "http://10.253.192.101:8080/hi";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String[] responseString = {""};
        StringRequest objectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Recieving from a call", "recevived message is" + response.toString());
                        responseString[0] = response.toString();
//                        msgView.setText("Response: " + response.toString());
//                        return response.toString();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Request Response", error.toString());
            }
        });
        Toast.makeText(context,"Listening to end point", Toast.LENGTH_SHORT).show();
        requestQueue.add(objectRequest);
        return responseString[0];
    }

    public static JSONObject callRestAPIWithObjectRequest(Context context, String url, JSONObject body){
//        String url = "http://10.253.192.101:8080/hi";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final JSONObject[] responseJsonObject = new JSONObject[1];
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Recieving from a call", "recevived message is" + response.toString());
                        responseJsonObject[0] = response;
//                        msgView.setText("Response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Request Response", error.toString());
                    }
                }
        );
        Toast.makeText(context,"Listening to end point", Toast.LENGTH_SHORT).show();
        requestQueue.add(objectRequest);
        return responseJsonObject[0];
    }

}
