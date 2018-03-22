package com.example.mansi.busezon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SELL_BUY extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                // Set the content of the activity to use the activity_main.xml layout file
                setContentView(R.layout.activity_sell__buy);
                TextView BUY= (TextView) findViewById(R.id.buy);
                BUY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //create intent to open the activity
                        Intent BUYintent= new Intent(SELL_BUY.this,BUY.class);
                        //start the new activity
                        startActivity(BUYintent);
                    }
                });

                TextView SELL= (TextView) findViewById(R.id.sell);
                SELL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //create intent to open the activity
                        Intent SELLintent= new Intent(SELL_BUY.this,SELL.class);
                        //start the new activity
                        startActivity(SELLintent);
                    }
                });
            }
    public void sendMessage(View view)
    {
        Intent intent = new Intent(SELL_BUY.this, profile_page.class);
        startActivity(intent);
    }

        }

