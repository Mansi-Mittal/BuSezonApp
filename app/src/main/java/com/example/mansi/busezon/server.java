package com.example.mansi.busezon;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class server {
    public static void serverRequest(String URL,final int sellerID,final int prodID){
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
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
                parameters.put("Seller_id",sellerID+"");
                parameters.put("Product_id",prodID+"");
                return parameters;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }



    public  static void addToWishlist(int sellerID, int prodID) {
        String URL = "http://172.20.10.9:3000/wishlists/new";
        serverRequest(URL, sellerID, prodID);

    }
    public static void addToBag(int sellerID, int prodID) {
            String URL = "http://172.20.10.9:3000/carts/new";
            serverRequest(URL,sellerID,prodID);
    }


}
