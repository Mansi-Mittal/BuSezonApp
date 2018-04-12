package com.example.mansi.busezon;


import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
/**
 * Created by mansi on 23/2/18.
 */

public class ProductDesc extends AppCompatActivity {

    private static ViewPager mPager;
    private static final Integer[] XMEN= {R.drawable.p_img,R.drawable.p_img1};
    private ArrayList<Integer> XMENArray = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_desc);
        init();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        Button Prod=(Button)findViewById(R.id.editBtn);
        Prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), Add_product.class);
                startActivity(i);
            }
        });

        TextView prodName=(TextView)findViewById(R.id.prodName);
        prodName.setText("Blue Denim Jeans");

        TextView price=(TextView)findViewById(R.id.price);
        price.setText("Rs 1500");
    }
    private void init() {
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new productImageAdapter(ProductDesc.this,XMENArray));
        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

    }

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

}
