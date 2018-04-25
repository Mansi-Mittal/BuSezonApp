package com.example.mansi.busezon;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class Add_product extends AppCompatActivity {
    private EditText productName;
    private TextView select_photos ;

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
            select_photos = (TextView) findViewById(R.id.btn);
            select_photos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create intent to open the activity
                    Intent BUYintent = new Intent(Add_product.this, select_photos.class);
                    //start the new activity
                    startActivity(BUYintent);
                }
            });
        }

    }