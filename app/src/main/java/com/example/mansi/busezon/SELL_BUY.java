package com.example.mansi.busezon;

import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mansi.busezon.data.dbContract;
import com.example.mansi.busezon.data.dbHelper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class SELL_BUY extends AppCompatActivity {
    private  dbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                // Set the content of the activity to use the activity_main.xml layout file
                setContentView(R.layout.activity_sell__buy);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        check();
                TextView BUY= (TextView) findViewById(R.id.buy);
                BUY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //create intent to open the activity
                        Intent BUYintent= new Intent(SELL_BUY.this,HomeActivity.class);
                        //start the new activity
                        startActivity(BUYintent);
                    }
                });

                TextView SELL= (TextView) findViewById(R.id.sell);
                SELL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //create intent to open the activity
                        Intent SELLintent= new Intent(SELL_BUY.this,SellHomepage.class);
                        //start the new activity
                        startActivity(SELLintent);
                    }
                });

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_wishList) {
                    Intent i=new Intent(getApplicationContext(),WishlistActivity.class);
                    startActivity(i);
                }
                else if (tabId == R.id.tab_profile) {
                    Intent i = new Intent(getApplicationContext(), profile_page.class);
                    startActivity(i);
                }

            }
        });

            }
    public void sendMessage(View view)
    {
        Intent intent = new Intent(SELL_BUY.this, profile_page.class);
        startActivity(intent);
    }


    private void check()
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to demo node
//        DatabaseReference demoRef = rootRef.child("CI8hvEW0sfZ0oU1GziTpGYPJv2z2");
//        String value = "User22";
//        //push creates a unique id in database
//        demoRef.push().setValue(value);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            String userId=getIntent().getStringExtra("Id");
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if(ds.getKey().equals(userId))
                        {
                            String name = ds.child("name").getValue(String.class);
                            String email = ds.child("email").getValue(String.class);
                            String address = ds.child("address").getValue(String.class);
                            String phoneno = ds.child("phoneno").getValue(String.class);
                            String password = ds.child("password").getValue(String.class);
                            Toast.makeText(SELL_BUY.this, email + " " + ds.getKey(), Toast.LENGTH_SHORT).show();
                            boolean c = CheckIsDataAlreadyInDBorNot(dbContract.userEntry.TABLE_NAME, dbContract.userEntry.COLUMN_number,phoneno);
                            if (c == false) {
                                insertUser(userId,name,email,address,phoneno,password);
                            }
                            else {
                                Toast.makeText(SELL_BUY.this, "user already exists", Toast.LENGTH_SHORT).show();
                                //no insertion in local database;
                            }
                        }
                    }

                }

            @Override
        public void onCancelled(DatabaseError databaseError) {}

        });
    }
    public boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue) {
        mDbHelper = new dbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    private void insertUser(String user_Id,String nameString,String EMAILString,String addressString,String numbertString,String pass) {
        // Create database helper
        dbHelper mDbHelper = new dbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(dbContract.userEntry.COLUMN_USER_ID, user_Id);
        values.put(dbContract.userEntry.COLUMN_USER, nameString);
        values.put(dbContract.userEntry.COLUMN_EMAIL, EMAILString);
        values.put(dbContract.userEntry.COLUMN_number, numbertString);
        values.put(dbContract.userEntry.COLUMN_address, addressString);
        values.put(dbContract.userEntry.COLUMN_password, pass);

        // Insert a new row for user in the database, returning the ID of that new row.
        long newRowId = db.insert(dbContract.userEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving user", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "user saved " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

        }

