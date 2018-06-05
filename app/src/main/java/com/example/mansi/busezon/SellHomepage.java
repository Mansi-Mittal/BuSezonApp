package com.example.mansi.busezon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellHomepage extends AppCompatActivity {

    int id =0;
    String url = "http://172.20.10.9:3000/products?user_id=1234";
    ArrayList<offers> offersList;

    private offersAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_homepage);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        offersList = new ArrayList<>();
        GridView offersListView =findViewById(R.id.list1);
        adapter = new offersAdapter(this, offersList);
        offersListView.setAdapter(adapter);

        sendJsonRequest();

        offersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent appInfo = new Intent(SellHomepage.this, ProductDesc.class);
                Bundle b = new Bundle();
                b.putInt("Product_id", offersList.get(i).getID()); //Your id
                appInfo.putExtras(b); //Put your id to your next Intent
                startActivity(appInfo);
            }
        });

        /*ImageView SellPdt1=(ImageView) findViewById(R.id.sellpdt1);
        SellPdt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), ProductDescription.class);
                startActivity(i);
            }
        });*/
        /*Button Prod = (Button) findViewById(R.id.addBtn);
        Prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), Add_product.class);
                startActivity(i);
            }
        });
*/
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_wishList) {
                    Intent i = new Intent(getApplicationContext(), WishlistActivity.class);
                    startActivity(i);
                }
                else if (tabId == R.id.tab_profile) {
                    Intent i = new Intent(getApplicationContext(), profile_page.class);
                    startActivity(i);
                }
                else if (tabId == R.id.tab_chat) {
                    try {
                        Intent i = new Intent(SellHomepage.this, Chat_UsersList_Activity.class);
                        i.putExtra("user", UserInformation.UserId);
                        i.putExtra("User_Name", UserInformation.name);
                        startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(SellHomepage.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        FloatingActionButton fab = findViewById(R.id.addproc);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent idd = new Intent(SellHomepage.this,Add_product.class);
                startActivity(idd);
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
                                    String url = "http://172.20.10.9:3000" + img;
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


   /* public void openOnImgClick(int ID) {
        Intent appInfo = new Intent(SellHomepage.this, ProductDesc.class);
        Bundle b = new Bundle();
        b.putInt("Product_id", ID); //Your id
        appInfo.putExtras(b); //Put your id to your next Intent
        startActivity(appInfo);
    }*/
}

