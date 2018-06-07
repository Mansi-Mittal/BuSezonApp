package com.example.mansi.busezon;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mansi.busezon.data.dbContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProductDesc extends AppCompatActivity {

    private static ImageView imageView;
    private Button button,addToCart;
    int value = 0,id =0;
    RequestQueue rq ;
    TextView prodName;
    TextView price;
    TextView sellerName;
    String url = "";
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_desc);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Bundle b = getIntent().getExtras();
        if (b != null)
            value = b.getInt("Product_id");
        url =server.URL+"products/show?id=" + value;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        rq = Volley.newRequestQueue(this);
        sendJsonRequest();
        prodName = (TextView) findViewById(R.id.prodName);
        price = (TextView) findViewById(R.id.price);
        imageView = (ImageView) findViewById(R.id.imgview);
        button = findViewById(R.id.addToWish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                server.addToWishlist(UserInformation.UserId, value);
            }
        });

        sellerName=(TextView)findViewById(R.id.sellerName);

        addToCart =findViewById(R.id.addToBag);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String response = server.addToBag(UserInformation.UserId, id);
                Toast.makeText(ProductDesc.this,response,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent i = new Intent(this, shoppingCart.class);
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

    public void sendJsonRequest() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            id = response.getInt("id");
                            prodName.setText(response.getString("name"));
                            price.setText("₹"+response.getString("price"));
                            String img = response.getString("IMAGE_URL");
                            String url = server.ImageURL+ img;
                            Glide.with(ProductDesc.this).load(url).into(imageView);
                            final String seller_id = response.getString("user_id");
                            try {
                                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            String dbId = ds.getKey();
                                            if (dbId.equals(seller_id))
                                            {
                                                String seller_Name;
                                                if (ds.hasChild("name") == true) {
                                                    seller_Name = ds.child("name").getValue(String.class);
                                                    sellerName.setText(seller_Name);
                                                    break;
                                                }

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }

                                });
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(ProductDesc.this, "user 1111", Toast.LENGTH_SHORT).show();
                            }
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


    public void startChat(View view)
    {
        try {
            TextView seller_name=(TextView)findViewById(R.id.sellerName);
            String sellerName= seller_name.getText().toString();
            Chat_UserDetails.chatWith =sellerName;
            String loginUser=UserInformation.name;
            Chat_UserDetails.username=loginUser;
//            Toast.makeText(WishlistActivity.this,Chat_UserDetails.chatWith+" "+Chat_UserDetails.username,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ProductDesc.this, Chat_Message_Acitivty.class);
            startActivity(intent);
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

}
