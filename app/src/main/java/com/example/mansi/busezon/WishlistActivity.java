package com.example.mansi.busezon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    int id =0;
    String url = "http://192.168.1.6:3000/wishlists?user_id=1234";
    ArrayList<offers> offersList;
    private offersAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //getActionBar().setTitle("BuSezon");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_wishList) {
                    Intent i = new Intent(getApplicationContext(), WishlistActivity.class);
                    startActivity(i);
                } else if (tabId == R.id.tab_profile) {
                    Intent i = new Intent(getApplicationContext(), profile_page.class);
                    startActivity(i);
                }else if (tabId == R.id.tab_chat) {
                    try {
                        Intent i = new Intent(WishlistActivity.this, Chat_UsersList_Activity.class);
                        i.putExtra("user", UserInformation.UserId);
                        i.putExtra("User_Name", UserInformation.name);
                        startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(WishlistActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        offersList = new ArrayList<>();
        GridView offersListView =findViewById(R.id.list1);
        adapter = new offersAdapter(this, offersList);
        offersListView.setAdapter(adapter);
        //adapter.hideButton();

        sendJsonRequest();
        offersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent appInfo = new Intent(WishlistActivity.this, ProductDesc.class);
                Bundle b = new Bundle();
                b.putInt("Product_id", offersList.get(i).getID()); //Your id
                appInfo.putExtras(b); //Put your id to your next Intent
                startActivity(appInfo);
            }
        });

        Button profile=(Button)findViewById(R.id.Profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), profile_page.class);
                startActivity(i);
            }
        });
    }

    public void sendJsonRequest() {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (url, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i <= response.length(); i++) {
                                JSONObject info = response.getJSONObject(i);
                                id = info.getInt("id");
                                String name = info.getString("name");
                                String img = info.getString("IMAGE_URL");
                                String url = "http://192.168.1.6:3000" + img;
                                //int sellerID=info.getInt(""); //complete
                                offersList.add(new offers(id,url, name, 1234));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}
