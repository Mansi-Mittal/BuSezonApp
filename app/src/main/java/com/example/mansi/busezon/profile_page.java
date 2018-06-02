package com.example.mansi.busezon;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.amazon.identity.auth.device.datastore.DatabaseHelper;
import com.example.mansi.busezon.data.dbContract;
import com.example.mansi.busezon.data.dbHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import static com.example.mansi.busezon.data.dbContract.userEntry.TABLE_NAME;

public class profile_page extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;

    private TextView profileName,profileEmail,profilePhoneno,profileAddress;
    private  dbHelper mDbHelper;
    private FirebaseAuth firebaseAuth;
    private Button logoutButton;
    private static final String TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        final String tokenToCheckLoginType =getIntent().getStringExtra("token");
        String userId=getIntent().getStringExtra("Id");
        try {
            Button logoutButton = (Button) findViewById(R.id.logout);
            Toast.makeText(this,"profile "+tokenToCheckLoginType+"  "+userId,Toast.LENGTH_LONG).show();
            logoutButton.setOnClickListener(new View.OnClickListener() {

               @Override
               public void onClick(View v) {


                   if (tokenToCheckLoginType.equals("amazon"))
                   {

                       AuthorizationManager.signOut(getApplicationContext(), new Listener<Void, AuthError>() {
                           @Override
                           public void onSuccess(Void response) {
                               runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       try {
                                           Intent intent = new Intent(profile_page.this, MainActivity.class);
//                               intent.putExtra("logout","true");
                                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                           startActivity(intent);
                                       }
                                       catch (Exception e)
                                       {
                                           Toast.makeText(profile_page.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                       }
                                   }
                               });
                           }

                           @Override
                           public void onError(AuthError authError) {
//                               Log.e(TAG, "Error clearing authorization state.", authError);
                           }
                       });
                    } else if (tokenToCheckLoginType.equals("google")||(tokenToCheckLoginType.equals("normal"))) {
                        FirebaseAuth.getInstance().signOut();
                       Intent intent=new Intent(profile_page.this,MainActivity.class);
//                               intent.putExtra("logout","true");
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                       startActivity(intent);
                    }

//                   Toast.makeText(profile_page.this, "done", Toast.LENGTH_LONG).show();
//                    finish();
               }
//                    globalVariables.setTokenToCheckLoginType("null");
//            }
//
            });
        }

        catch (Exception e)
        {
            Toast.makeText(profile_page.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

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
    public void removeAll()throws SQLException {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL ("drop table "+TABLE_NAME);
        db.close ();
        db.execSQL("create table "+TABLE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
                ImageView imageView = (ImageView) findViewById(R.id.imgView);
                imageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
            } catch (FileNotFoundException e) {
                // Handle the error
            } finally {
                if (imageStream != null) {
                    try {
                        imageStream.close();
                    } catch (IOException e) {
                        // Ignore the exception
                    }
                }
            }
        }
    }
    void PutValuesInFields() {
        mDbHelper = new dbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME;
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
