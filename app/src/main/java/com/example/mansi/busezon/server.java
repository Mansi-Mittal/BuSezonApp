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
    static String URL = "http://172.20.10.9:3000/";
    static String ImageURL = "http://172.20.10.9:3000";
    public static void serverRequest(String URL,final String userID,final int prodID,final boolean check){
        //String Response="";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    //Log.d("output",response);
                    if(check) {
                        JSONObject jsonObject = new JSONObject(response);
                        Response = jsonObject.getString("message");
                    }
                    //Toast.makeText(AppController.getInstance(),Response,Toast.LENGTH_LONG).show();
                    //Response = Data.getString(0);
                    //progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            //adding parameters to send
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

    public  static void addToWishlist(String userID, int prodID) {
        String url = URL+"wishlists/new";
        serverRequest(url, userID, prodID,false);


    }
    public static void addToBag(String userID, int prodID) {
        String url = URL+"carts/new";
            serverRequest(url,userID,prodID,false);
    }

    public static void removeFromBag(String userId, int prodID){
        String url = URL+"carts/remove";
        serverRequest(url,userId,prodID,false);
    }

    public static void removeFromWish(String userId, int prodID){
        String url = URL+"wishlists/remove";
        serverRequest(url,userId,prodID,false);
    }

    public static boolean checkIfAlreadyExist(String userId,int prodId){
        String url = URL+"wishlists/find";
        serverRequest(url,userId,prodId,true);
        if(Response.equalsIgnoreCase("Found")){
            return true;
        }
        else{
            return false;
        }

        //return false;
    }

    public static boolean checkIfAlreadyExistCart(String userId,int prodId){
        String url = URL+"carts/find";
        serverRequest(url,userId,prodId,true);

        if(Response.equalsIgnoreCase("Found")){
            return true;
        }
        else{
            return false;
        }

       //return false;
    }


}
