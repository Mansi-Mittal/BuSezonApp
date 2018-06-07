package com.example.mansi.busezon;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class server {
    static String Response="";
    static String URL = "http://192.168.1.6:3000/";//http://172.20.10.9:3000/";
    static String ImageURL = "http://192.168.1.6:3000";//"http://172.20.10.9:3000";
    public static void serverRequest(String URL,final String userID,final int prodID,final boolean check){
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    if(check) {
                        JSONObject jsonObject = new JSONObject(response);
                        Response = jsonObject.getString("message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("Seller_id",userID);
                parameters.put("Product_id",prodID+"");
                return parameters;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    public  static String addToWishlist(String userID, int prodID) {
        String url = URL+"wishlists/new";
        serverRequest(url,userID,prodID,true);
        return Response;
    }
    public static String addToBag(String userID, int prodID) {
        String url = URL+"carts/new";
            serverRequest(url,userID,prodID,true);
            return Response;
    }

    public static String removeFromBag(String userId, int prodID){
        String url = URL+"carts/remove";
        serverRequest(url,userId,prodID,false);
        return Response;
    }

    public static String removeFromWish(String userId, int prodID){
        String url = URL+"wishlists/remove";
        serverRequest(url,userId,prodID,false);
        return Response;
    }
}
