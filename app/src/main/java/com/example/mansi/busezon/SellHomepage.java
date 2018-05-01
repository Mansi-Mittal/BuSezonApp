package com.example.mansi.busezon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

public class SellHomepage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_homepage);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //getActionBar().setTitle("BuSezon");
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
        ArrayList<offers> offersList=new ArrayList<>();
        //offersList.add(new offers(R.drawable.img1,"Great discounts"));
        offersList.add(new offers(R.drawable.pdt1,""));
        offersList.add(new offers(R.drawable.pdt2,""));
        offersList.add(new offers(R.drawable.pdt3,""));
        offersList.add(new offers(R.drawable.pdt4,""));
        offersList.add(new offers(R.drawable.pdt5,""));
        offersList.add(new offers(R.drawable.pdt6,""));
        offersList.add(new offers(R.drawable.pdt7,""));
        offersList.add(new offers(R.drawable.pdt8,""));

        ListView offersListView = (ListView) findViewById(R.id.list1);

        // Create a new {@link ArrayAdapter} of earthquakes
        final offersAdapter adapter = new offersAdapter(this, offersList);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        offersListView.setAdapter(adapter);


       /* ImageView SellPdt1=(ImageView) findViewById(R.id.sellpdt1);
        SellPdt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), ProductDescription.class);
                startActivity(i);
            }
        });*/
        Button Prod=(Button)findViewById(R.id.addBtn);
        Prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), Add_product.class);
                startActivity(i);
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

    }

