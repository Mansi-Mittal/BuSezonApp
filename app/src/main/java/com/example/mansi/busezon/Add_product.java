package com.example.mansi.busezon;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Add_product extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_product);
            TextView add = (TextView) findViewById(R.id.btn);
            add.setOnClickListener(new View.OnClickListener() {
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