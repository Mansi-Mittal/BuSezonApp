package com.example.mansi.busezon;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Add_product extends AppCompatActivity {
    private EditText productName;
    private Button choose, upload ;
    private ImageView image;
    private final int PICK_IMAGE_REQUEST = 1;
    String URL ="http://172.20.10.9:3000/products/new";
    private Bitmap bitmap;
    ProgressDialog progressDialog;
    RequestQueue rQueue;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_product);
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            //getActionBar().setTitle("BuSezon");
            setSupportActionBar(myToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar ab = getSupportActionBar();

            ab.setDisplayHomeAsUpEnabled(true);
            productName=(EditText)findViewById(R.id.productName);

        image = (ImageView)findViewById(R.id.image);
        choose = (Button)findViewById(R.id.choose);
        upload = (Button)findViewById(R.id.upload);

        rQueue = Volley.newRequestQueue(this);

        //opening image chooser option
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Add_product.this);
                progressDialog.setMessage("Uploading, please wait...");
                //progressDialog.show();

                //sending image to server
                uploadImage();

            }
        });

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
                }
            });

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                image.setVisibility(View.VISIBLE);                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage(){
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    Toast.makeText(Add_product.this, Response, Toast.LENGTH_LONG).show();
                    //image.setImageURI(Response);
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
                String name = productName.getText().toString();
                parameters.put("name",name);
                parameters.put("category","clothing");
                String imageString = imageToString(bitmap);
                parameters.put("image", imageString);
                return parameters;
            }
        };

        rQueue.add(request);
    }
    public String imageToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }

    }