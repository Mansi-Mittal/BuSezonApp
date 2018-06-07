package com.example.mansi.busezon;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class serverProduct {
    public static void setSold(Context context,int prod_id, int qty){
        String URL=server.URL+"products/sold";
        JSONArray soldjsonArray = new JSONArray();
        JSONObject soldjsonObject = new JSONObject();
        try{
            soldjsonObject.put("Product_id",prod_id);
            soldjsonObject.put("Qty", qty);
            soldjsonArray.put(soldjsonObject);

            //Log.i("jsonString", jsonObject.toString());


        }catch(Exception e){

        }

        postRequest(context,URL,soldjsonObject);

    }

    public static void postRequest(final Context context, String URL, JSONObject jsonObject ) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Response", response.toString());
                        try {
                            JSONArray arrData = response.getJSONArray("data");
                            //parseData(arrData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("Error.Response", error.toString());
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 400:

                                    json = new String(response.data);
                                    System.out.println(json);
                                    break;
                            }
                            //Additional cases
                        }
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}
