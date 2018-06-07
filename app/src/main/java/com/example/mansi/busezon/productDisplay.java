package com.example.mansi.busezon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class productDisplay extends AppCompatActivity {

    String URL = "";
    String urlParam,subCategory;
    int id =0;
    boolean search ;
    public offersAdapter adapter;
    ArrayList<offers> offersList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        if (b != null)
            urlParam = b.getString("urlParam");
            search = b.getBoolean("search");

        if(search){
            URL = server.URL+"products/search?search=" + urlParam;
        }else{
            subCategory = b.getString("subCat");
            URL = server.URL+"products?category=" + urlParam+"&subCategory="+subCategory;
        }

        offersList = new ArrayList<>();
        GridView offersListView =findViewById(R.id.list1);
        adapter = new offersAdapter(this, offersList);
        offersListView.setAdapter(adapter);

        sendJsonRequest();

        offersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent openPdtDesc = new Intent(productDisplay.this, ProductDesc.class);
                Bundle b = new Bundle();
                b.putInt("Product_id", offersList.get(i).getID());
                openPdtDesc.putExtras(b);
                startActivity(openPdtDesc);
            }
        });


        BottomBar bottomBar =findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_wishList) {
                    Intent i = new Intent(getApplicationContext(), WishlistActivity.class);
                    startActivity(i);
                } else if (tabId == R.id.tab_profile) {
                    Intent i = new Intent(getApplicationContext(), profile_page.class);
                    startActivity(i);
                }

            }
        });
    }

    public void sendJsonRequest() {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (URL, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i <= response.length(); i++) {
                                JSONObject info = response.getJSONObject(i);
                                id = info.getInt("id");
                                String name = info.getString("name");
                                String img = info.getString("IMAGE_URL");
                                String url = server.ImageURL + img;
                                int price = info.getInt("price");
                                String sellerID=info.getString("user_id"); //complete
                                offersList.add(new offers(id,url, name, sellerID, price));
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
