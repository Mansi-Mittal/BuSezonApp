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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class Add_product extends AppCompatActivity {
    private EditText productName,squant,mquant,lquant,xlquant;
    private EditText color;
    private EditText brand;
    private EditText tags;
    private EditText start_date;
    private EditText last_date,price;
    private Button choose, upload;
    private ImageView image;
    private final int PICK_IMAGE_REQUEST = 1;

    String URL = server.URL+"products/new";
    private Bitmap bitmap;
    ProgressDialog progressDialog;
    RequestQueue rQueue;
    Spinner category ,subCategory, subcategoryE;
    String selectedCategory, selectedSubCategory;
    String sizes = "";
    String qty = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        productName = (EditText) findViewById(R.id.productName);
        color = (EditText) findViewById(R.id.color);
        brand = (EditText) findViewById(R.id.brand);
        tags = (EditText) findViewById(R.id.tags);
        start_date = (EditText) findViewById(R.id.startdate);
        last_date = (EditText) findViewById(R.id.lastdate);
        price = (EditText) findViewById(R.id.Pprice);
        image = (ImageView) findViewById(R.id.image);
        choose = (Button) findViewById(R.id.choose);
        upload = (Button) findViewById(R.id.upload);
        squant = findViewById(R.id.squant);
        mquant = findViewById(R.id.mquant);
        lquant = findViewById(R.id.lquant);
        xlquant = findViewById(R.id.xlquant);

        category = findViewById(R.id.category_btn);
        subCategory = findViewById(R.id.subCategoryF);
        subcategoryE = findViewById(R.id.subCategoryE);


        final LinearLayout subCatLayout = findViewById(R.id.subCategoryFLayout);
        final LinearLayout subCatELayout = findViewById(R.id.subCategoryELayout);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = category.getSelectedItem().toString();
                if(selectedCategory.equalsIgnoreCase("clothing")) {
                    subCatLayout.setVisibility(View.VISIBLE);
                    subCatELayout.setVisibility(View.GONE);
                    //subCategory = findViewById(R.id.subCategoryF);
                }else {
                    subCatELayout.setVisibility(View.VISIBLE);
                    subCatLayout.setVisibility(View.GONE);
                    //subCategory = findViewById(R.id.subCategoryE);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        subCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)  {
                selectedSubCategory = subCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        subcategoryE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)  {
                selectedSubCategory = subcategoryE.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        rQueue = Volley.newRequestQueue(this);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Add_product.this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.show();

                String tagsArray = tags.getText().toString();

                JSONArray prodJsonArray = new JSONArray();
                JSONObject parameters = new JSONObject();
                try{
                    String name = productName.getText().toString();
                    parameters.put("name", name);
                    parameters.put("category", selectedCategory);
                    parameters.put("subCategory",selectedSubCategory);
                    parameters.put("colour",color.getText().toString());
                    String imageString = imageToString(bitmap);
                    parameters.put("image", imageString);
                    parameters.put("price",price.getText());
                    parameters.put("brand",brand.getText());
                    parameters.put("user_id",UserInformation.UserId);
                    parameters.put("tags",tagsArray);
                    parameters.put("sizes",sizes);
                    parameters.put("sold",false);
                    prodJsonArray.put(parameters);
                    uploadImage(parameters);
                }catch(Exception e){

                }
                progressDialog.dismiss();

            }
        });

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
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setVisibility(View.VISIBLE);
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }

    public void onCheckboxClickedsmall(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        if (checked) {
            sizes+= "S ";
            qty += squant.getText().toString()+ " ";
        }else{
            sizes +="N ";
            qty += "0 ";
        }
    }
    public void onCheckboxClickedmedium(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            sizes+= "M ";
            qty += mquant.getText().toString()+ " ";
        }else{
            sizes +="N ";
            qty += "0 ";
        }
    }
    public void onCheckboxClickedlarge(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            sizes+= "L ";
            qty += lquant.getText().toString()+ " ";
        }else{
            sizes +="N ";
            qty += "0 ";
        }
    }
    public void onCheckboxClickedextralarge(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            sizes+= "XL ";
            qty += xlquant.getText().toString()+ " ";
        }else{
            sizes +="N ";
            qty += "0 ";
        }
    }

    public void uploadImage(JSONObject jsonObject){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                       finish();
                       startActivity(getIntent());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finish();
                        startActivity(getIntent());

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

}