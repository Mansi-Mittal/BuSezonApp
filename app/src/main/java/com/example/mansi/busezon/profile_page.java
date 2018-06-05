package com.example.mansi.busezon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.example.mansi.busezon.data.dbHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import static com.android.volley.Request.Method.HEAD;
import static com.example.mansi.busezon.data.dbContract.userEntry.TABLE_NAME;



public class profile_page extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;

    private TextView profileName,profileEmail,profilePhoneno,profileAddress;
    private  dbHelper mDbHelper;
    private FirebaseAuth firebaseAuth;
    private Button logoutButton;
    private static final String TAG = MainActivity.class.getName();
    private static final int SELECT_PHOTO = 100;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);


        //accessing the firebase storage
        storage = FirebaseStorage.getInstance();
        //creates a storage reference
        storageRef = storage.getReference();
        final String tokenToCheckLoginType =UserInformation.token;
//        String userId=getIntent().getStringExtra("Id");
        try {
            logoutButton = (Button) findViewById(R.id.logout);
//            Toast.makeText(this,"profile "+UserInformation.name,Toast.LENGTH_LONG).show();
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

            TextView Wishlist = (TextView) findViewById(R.id.wishlist);
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
        imageView=(ImageView)findViewById(R.id.UserProfileImage);
        StorageReference storageRef1 = storage.getReferenceFromUrl("gs://busezon-57985.appspot.com/images").child(UserInformation.UserId+".jpg");
        try {
           final File localFile = File.createTempFile("images", UserInformation.UserId+"jpg");
            storageRef1.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {
            Toast.makeText(profile_page.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(profile_page.this,"Image selected, click on upload button",Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                }
        }
    }
    public void selectImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
    public void uploadImage(View view) {
        try {
            //create reference to images folder and assing a name to the file that will be uploaded
            imageRef = storageRef.child("images/" +UserInformation.UserId+".jpg");
            //creating and showing progress dialog
            progressDialog = new ProgressDialog(this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Uploading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);
            if(selectedImage!=null) {
                //starting upload
                uploadTask = imageRef.putFile(selectedImage);

                // Observe state change events such as progress, pause, and resume
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        //sets and increments value of progressbar
                        progressDialog.incrementProgressBy((int) progress);
                    }
                });
                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(profile_page.this, "Error in uploading! " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(profile_page.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        //showing the uploaded image in ImageView using the download url
                       Picasso.with(profile_page.this).load(downloadUrl).into(imageView);
                    }
                });
            }
        }
        catch (Exception e)
        {
            Toast.makeText(profile_page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
//               String userId = getIntent().getStringExtra("Id");
                String userId=UserInformation.UserId;
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
