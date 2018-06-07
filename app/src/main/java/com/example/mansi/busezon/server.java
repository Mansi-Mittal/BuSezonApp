package com.example.mansi.busezon;

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
    static String Response;
    public static void serverRequest(String URL,final String userID,final int prodID){
        //String Response="";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray Data = jsonObject.getJSONArray("data");
                    Response = Data.getString(0);
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

    public static void serverGetRequest(String url){
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    public  static void addToWishlist(String userID, int prodID) {
        String URL = "http://192.168.1.6:3000/wishlists/new";
        serverRequest(URL, userID, prodID);

    }
    public static void addToBag(String userID, int prodID) {
            String URL = "http://192.168.1.6:3000/carts/new";
            serverRequest(URL,userID,prodID);
    }

    public static void removeFromBag(String userId, int prodID){
        String URL = "http://192.168.1.6:3000/carts/remove";
        serverRequest(URL,userId,prodID);
    }

    public static void removeFromWish(String userId, int prodID){
        String URL = "http://192.168.1.6:3000/wishlists/remove";
        serverRequest(URL,userId,prodID);
    }

    public static boolean checkIfAlreadyExist(String userId,int prodId){
        String URL = "http://192.168.1.6:3000/wishlists/find";
        serverRequest(URL,userId,prodId);

        if(Response.equalsIgnoreCase("Found")){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean checkIfAlreadyExistCart(String userId,int prodId){
        String URL = "http://192.168.1.6:3000/carts/find";
        serverRequest(URL,userId,prodId);

        if(Response.equalsIgnoreCase("Found")){
            return true;
        }
        else{
            return false;
        }
    }


}
