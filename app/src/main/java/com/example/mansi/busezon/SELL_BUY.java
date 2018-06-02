package com.example.mansi.busezon;

import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.identity.auth.device.api.authorization.User;
import com.example.mansi.busezon.data.dbContract;
import com.example.mansi.busezon.data.dbHelper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.security.spec.ECField;

public class SELL_BUY extends AppCompatActivity {
    private  dbHelper mDbHelper;
    private  String User_Id,User_Name;
    private DrawerLayout mDrawerLayout;
    private String token;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
                // Set the content of the activity to use the activity_main.xml layout file
                setContentView(R.layout.activity_sell__buy);

    mDrawerLayout = findViewById(R.id.drawer_layout);
    User_Id = getIntent().getStringExtra("Id");
    token = getIntent().getStringExtra("token");
        try {
            check();
        }
        catch (Exception e)
        {
            Toast.makeText(SELL_BUY.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    User_Name = getIntent().getStringExtra("Name");
//        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
//        if(firebaseUser!=null)
//        {
//            String userId=firebaseUser.getDisplayName();
//            Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();
//        }
//        Toast.makeText(SELL_BUY.this,User_Id, Toast.LENGTH_LONG).show();
    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    // Add code here to update the UI based on the item selected
                    // For example, swap UI fragments here

                    return true;
                }
            });



try {
    Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);
    ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
//        Toast.makeText(SELL_BUY.this, "hi!!", Toast.LENGTH_SHORT).show();

    TextView BUY = (TextView) findViewById(R.id.buy);
    BUY.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //create intent to open the activity
            Intent BUYintent = new Intent(SELL_BUY.this, HomeActivity.class);
            //start the new activity
            startActivity(BUYintent);
        }
    });

    TextView SELL = (TextView) findViewById(R.id.sell);
    SELL.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //create intent to open the activity
            Intent SELLintent = new Intent(SELL_BUY.this, SellHomepage.class);
            //start the new activity
            startActivity(SELLintent);
        }
    });
//    Toast.makeText(this,"check 2",Toast.LENGTH_LONG).show();
    BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
    bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
        @Override
        public void onTabSelected(@IdRes int tabId) {
            if (tabId == R.id.tab_wishList) {
                Intent i = new Intent(getApplicationContext(), WishlistActivity.class);
//                    i.putExtra("User_Name",User_Name);
                startActivity(i);
            } else if (tabId == R.id.tab_profile) {
                Intent i = new Intent(getApplicationContext(), profile_page.class);


//                    Toast.makeText(SELL_BUY.this,User_Id, Toast.LENGTH_LONG).show();
//                    if(!User_Id.equals("")) {
                i.putExtra("UserId", User_Id);
//                       Toast.makeText(SELL_BUY.this,token+"   "+User_Id, Toast.LENGTH_LONG).show();
                i.putExtra("token", token);
//                    }
                startActivity(i);
            } else if (tabId == R.id.tab_chat) {
                try {
                    Intent i = new Intent(SELL_BUY.this, Chat_UsersList_Activity.class);
                    i.putExtra("user", User_Id);
                    i.putExtra("User_Name", User_Name);
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(SELL_BUY.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }
    });

}
catch (Exception e)
{
    Toast.makeText(SELL_BUY.this,e.getMessage(),Toast.LENGTH_LONG).show();
}


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(SELL_BUY.this, profile_page.class);
        intent.putExtra("Id",User_Id);
        intent.putExtra("token",token);
        startActivity(intent);
    }


    private void check() {
        try {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            //database reference pointing to demo node
//        DatabaseReference demoRef = rootRef.child("CI8hvEW0sfZ0oU1GziTpGYPJv2z2");
//        String value = "User22";
//        //push creates a unique id in database
//        demoRef.push().setValue(value);
//            Toast.makeText(SELL_BUY.this, "hi!", Toast.LENGTH_SHORT).show();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                String EmailIntent = getIntent().getStringExtra("Email");

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String email = ds.child("email").getValue(String.class);
//                        Toast.makeText(SELL_BUY.this, email + "  " + EmailIntent, Toast.LENGTH_SHORT).show();
                        if (email.equals(EmailIntent)) {
                            String name = ds.child("name").getValue(String.class);
                            User_Name=name;
                            // String email = ds.child("email").getValue(String.class);
                            String userId = ds.getKey();
                            String address = "";
                            if (ds.hasChild("address") == true)
                                address = ds.child("address").getValue(String.class);
                            String phoneno = "";
                            if (ds.hasChild("phoneno"))
                                phoneno = ds.child("phoneno").getValue(String.class);
                            String password = "";
                            if (ds.hasChild("password"))
                                password = ds.child("password").getValue(String.class);
                            User_Id = userId;
                            //        insertUser(userId,name,email,address,phoneno,password);
                            //Toast.makeText(SELL_BUY.this, email + " " + ds.getKey(), Toast.LENGTH_SHORT).show();
                            try
                            {
                            int c = CheckIsDataAlreadyInDBorNot(dbContract.userEntry.TABLE_NAME, dbContract.userEntry.COLUMN_EMAIL, email);
                            if (c == 0) {
                                insertUser(userId, name, email, address, phoneno, password);
                            }
                            else if (c == 2) {
                          //      Toast.makeText(SELL_BUY.this, "user already exists", Toast.LENGTH_SHORT).show();
                                //no insertion in local database;
                            }
                            else
                            {
                                Toast.makeText(SELL_BUY.this, "user error", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(SELL_BUY.this, "error  :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }
        catch (Exception e)
        {
            Toast.makeText(SELL_BUY.this, "user 1111", Toast.LENGTH_SHORT).show();
        }
    }

    public int CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue)
    {

    int f=1;
        try {
            mDbHelper = new dbHelper(this);
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String Query = "Select * from " + TableName;
            Cursor cursor = db.rawQuery(Query, null);
            //Toast.makeText(SELL_BUY.this, "111", Toast.LENGTH_LONG).show();
            if (cursor.getCount() <= 0) {
                f = 0;
              //  Toast.makeText(SELL_BUY.this, "222", Toast.LENGTH_LONG).show();
            } else if (cursor.getCount() > 0) {
                //cursor.close();
                //return true;
                //  Toast.makeText(SELL_BUY.this,"333",Toast.LENGTH_LONG).show();
                if (cursor.moveToFirst()) {
                    do {
                //        Toast.makeText(SELL_BUY.this, "444", Toast.LENGTH_LONG).show();
                        if (cursor.getString(2).equals(fieldValue)) {
                            f = 2;
                          //  Toast.makeText(SELL_BUY.this, cursor.getString(2)+" "+ fieldValue, Toast.LENGTH_LONG).show();
                            break;
                        }
                        else
                            f=0;
                    } while (cursor.moveToNext());
                }
               // cursor.close();
               // Toast.makeText(SELL_BUY.this, "aaa", Toast.LENGTH_LONG).show();
            }

          //  Toast.makeText(SELL_BUY.this,"bbbb"+ f, Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(SELL_BUY.this, e.getMessage(), Toast.LENGTH_LONG).show();
            //Toast.makeText(SELL_BUY.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return f;
//        return true;
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
        }
        else
            {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
        //    Toast.makeText(this, "user saved " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }


}

