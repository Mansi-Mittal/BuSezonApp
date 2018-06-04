package com.example.mansi.busezon;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProductDesc extends AppCompatActivity {

    private static ViewPager mPager;
    private static final Integer[] XMEN= {R.drawable.p_img,R.drawable.p_img1};
    private ArrayList<Integer> XMENArray = new ArrayList<>();
    RequestQueue rq ;
    TextView prodName, price;
    String url = "";
    private static ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_desc);
        //init();

        Bundle b = getIntent().getExtras();
        int value = 0; // or other values
        if(b != null)
            value = b.getInt("Product_id");

        url = "http://172.20.10.9:3000/products/show/?id="+value;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);


        /*Button Prod=(Button)findViewById(R.id.editBtn);
        Prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), Add_product.class);
                startActivity(i);
            }
        });*/

        rq= Volley.newRequestQueue(this);
        sendJsonRequest();
        prodName=(TextView)findViewById(R.id.prodName);
        price=(TextView)findViewById(R.id.price);
        imageView=(ImageView)findViewById(R.id.imgview);

    }

    /*private void init() {
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new productImageAdapter(ProductDesc.this,XMENArray));
        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent i=new Intent(this,shoppingCart.class);
                startActivity(i);
                return true;

            case R.id.action_search:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return false;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void sendJsonRequest(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            prodName.setText(response.getString("name"));
                            price.setText(response.getString("category"));
                            String img = response.getString("IMAGE_URL");
                            String url = "http://172.20.10.9:3000" + img;
                            Glide.with(ProductDesc.this).load(url).into(imageView);
                            //Toast.makeText(ProductDesc.this, url, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        rq.add(jsonObjectRequest);
    }

}
