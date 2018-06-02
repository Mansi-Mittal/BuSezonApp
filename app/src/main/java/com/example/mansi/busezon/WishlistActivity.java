package com.example.mansi.busezon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class WishlistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //getActionBar().setTitle("BuSezon");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
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

        Button profile=(Button)findViewById(R.id.Profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), profile_page.class);
                startActivity(i);
            }
        });
    }
//    public void startChat(View view)
//    {
//        try {
//            TextView seller_name=(TextView)findViewById(R.id.sellerName);
//            String sellerName= (String) seller_name.getText();
//            Chat_UserDetails.chatWith =sellerName;
//            String loginUser=getIntent().getStringExtra("User_Name");
//            Chat_UserDetails.username=loginUser;
////            Toast.makeText(WishlistActivity.this,Chat_UserDetails.chatWith+" "+Chat_UserDetails.username,Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(WishlistActivity.this, Chat_Message_Acitivty.class);
//           startActivity(intent);
//        }
//        catch (Exception e)
//        {
//            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
//        }
//    }
}
