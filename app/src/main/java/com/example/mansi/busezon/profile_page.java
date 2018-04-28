package com.example.mansi.busezon;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mansi.busezon.data.dbContract;
import com.example.mansi.busezon.data.dbHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class profile_page extends AppCompatActivity {

    private TextView profileName,profileEmail,profilePhoneno,profileAddress;
    private  dbHelper mDbHelper;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        try {
            profileName = findViewById(R.id.profile_name);
            profileEmail = findViewById(R.id.profile_email);
            profileAddress = findViewById(R.id.profile_address);
            profilePhoneno = findViewById(R.id.profile_number);
            firebaseAuth = FirebaseAuth.getInstance();
            PutValuesInFields();

            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            //getActionBar().setTitle("BuSezon");
            setSupportActionBar(myToolbar);

            ActionBar ab = getSupportActionBar();

            ab.setDisplayHomeAsUpEnabled(true);
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
            Button Wishlist = (Button) findViewById(R.id.wishlist);
            Wishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent(getApplicationContext(), WishlistActivity.class);
                    startActivity(i);
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(profile_page.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    void PutValuesInFields() {
        mDbHelper = new dbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String Query = "Select * from " + dbContract.userEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            Toast.makeText(profile_page.this, "no", Toast.LENGTH_LONG).show();
        } else
            {
//            String userId = firebaseAuth.getCurrentUser().getUid();
               String userId = getIntent().getStringExtra("Id");
       //         Toast.makeText(profile_page.this, userId, Toast.LENGTH_LONG).show();
            String name = "", email = "", phoneno = "", address = "", bio = "";

            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(0);
                    if (id.equals(userId)) {
                       // Toast.makeText(profile_page.this,id,Toast.LENGTH_LONG).show();
                        name = cursor.getString(1);
                        email = cursor.getString(2);
                        phoneno = cursor.getString(3);
                        address = cursor.getString(5);
                        break;
                    }

                } while (cursor.moveToNext());
            }
            profileName.setText(name);
            profileEmail.setText(email);
            profilePhoneno.setText(phoneno);
            profileAddress.setText(address);

            cursor.close();
        }
    }
}
