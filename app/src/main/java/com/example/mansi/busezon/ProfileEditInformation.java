package com.example.mansi.busezon;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mansi.busezon.data.dbContract;
import com.example.mansi.busezon.data.dbHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.jar.Attributes;

public class ProfileEditInformation extends AppCompatActivity implements View.OnClickListener{

    private Button continueButton;
    private EditText name,phoneno,address,password;
    private TextView email;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private  dbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile_edit_information);
            progressDialog = new ProgressDialog(this);

            continueButton = (Button) findViewById(R.id.continueButton);
            String Name = UserInformation.name;
            String Email = UserInformation.email;
            String Id = UserInformation.UserId;
            email = (TextView) findViewById(R.id.editTextEmail);
            email.setText(Email);
            name = (EditText) findViewById(R.id.editTextname);
            name.setText(Name);
            phoneno = (EditText) findViewById(R.id.editTextPhoneno);
            address = (EditText) findViewById(R.id.editTextAddress);
            password = (EditText) findViewById(R.id.editTextPassword);
            firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
       continueButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view)
    {
        if(view==continueButton)
            registerUser();
    }
    private void saveUserDetails(final String Name, String Email, final String PhoneNo, final String Address, final String Password)
    {
        try {
            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            //database reference pointing to demo node
//        DatabaseReference demoRef = rootRef.child("CI8hvEW0sfZ0oU1GziTpGYPJv2z2");
//        String value = "User22";
//        //push creates a unique id in database
//        demoRef.push().setValue(value);
//            Toast.makeText(ProfileEditInformation.this, "hi!", Toast.LENGTH_SHORT).show();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                String EmailIntent = UserInformation.email;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String email = ds.child("email").getValue(String.class);
//                        Toast.makeText(SELL_BUY.this, email + "  " + EmailIntent, Toast.LENGTH_SHORT).show();
                        if (email.equals(EmailIntent)) {
                            String User_Id=UserInformation.UserId;
                            databaseReference.child(User_Id).child("name").setValue(Name);
                            UserInformation.name=Name;
                            if (ds.hasChild("address") == true)
                                databaseReference.child(User_Id).child("address").setValue(Address);
                            if (ds.hasChild("phoneno"))
                                databaseReference.child(User_Id).child("phoneno").setValue(PhoneNo);
                            if (ds.hasChild("password"))
                                databaseReference.child(User_Id).child("password").setValue(Password);
                            //        insertUser(userId,name,email,address,phoneno,password);
                            //Toast.makeText(SELL_BUY.this, email + " " + ds.getKey(), Toast.LENGTH_SHORT).show();
                            try
                            {
                                int c = CheckIsDataAlreadyInDBorNot(dbContract.userEntry.TABLE_NAME, dbContract.userEntry.COLUMN_EMAIL, email,Name,Address,PhoneNo,Password);
                                if (c == 0) {
                                    insertUser(User_Id,Name, email, Address, PhoneNo, Password);
                                }
                                else if (c == 2) {
//                                          Toast.makeText(ProfileEditInformation.this, "user already exists", Toast.LENGTH_SHORT).show();
                                    //no insertion in local database;
                                }
                                else
                                {
                                    Toast.makeText(ProfileEditInformation.this, "user error", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(ProfileEditInformation.this, "error  :"+e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ProfileEditInformation.this, "user 1111", Toast.LENGTH_SHORT).show();
        }
    }

    public int CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue,String Name,String Address,String PhoneNo, String Password)
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
                        if (cursor.getString(2).equals(fieldValue))
                        {

                            f = 2;
                            updateUser(TableName,UserInformation.UserId,Name,fieldValue,Address,PhoneNo,Password);
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
            Toast.makeText(ProfileEditInformation.this, e.getMessage(), Toast.LENGTH_LONG).show();
            //Toast.makeText(SELL_BUY.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return f;
//        return true;
    }

    private void updateUser(String TableName,String userId, String name, String fieldValue, String address, String phoneNo, String password) {

        dbHelper mDbHelper = new dbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(dbContract.userEntry.COLUMN_USER_ID, userId);
        values.put(dbContract.userEntry.COLUMN_USER, name);
        values.put(dbContract.userEntry.COLUMN_EMAIL, fieldValue);
        values.put(dbContract.userEntry.COLUMN_number, phoneNo);
        values.put(dbContract.userEntry.COLUMN_address, address);
        values.put(dbContract.userEntry.COLUMN_password, password);
        UserInformation.name= name;
        long newRowId=db.update(dbContract.userEntry.TABLE_NAME, values, dbContract.userEntry.COLUMN_EMAIL+"=?", new String[]{fieldValue});
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving user", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
//                Toast.makeText(this, "user saved " + newRowId, Toast.LENGTH_LONG).show();
        }
        db.close();

        return;
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


    private void registerUser()
    {
        final String Email = email.getText().toString().trim();
        final String Name = name.getText().toString().trim();
        final String PhoneNo = phoneno.getText().toString().trim();
        final String Address = address.getText().toString().trim();
        final String Password = password.getText().toString().trim();

        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!Email.equals(UserInformation.email))
        {
            Toast.makeText(this, "You cannot change email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!Name.equals(UserInformation.name))
        {
            Toast.makeText(this, "You cannot change name", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String z[] = Name.split(" ");
            if (z.length <= 1) {
                Toast.makeText(this, "Enter Firstname and Lastname", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (TextUtils.isEmpty(PhoneNo) || PhoneNo.length() != 10) {
            Toast.makeText(this, "Enter Phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Address)) {
            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Password)) {
            if(Password.length() < 5)
                Toast.makeText(this, "Password should be atleast 6 characters long", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText ConfirmPassword=(EditText)findViewById(R.id.editTextCnfPassword);
        String confirmPassword=ConfirmPassword.getText().toString().trim();
        if(!confirmPassword.equals(Password))
        {
            Toast.makeText(this, "Passwords do not match!!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        saveUserDetails(Name, Email, PhoneNo, Address, Password);
        Toast.makeText(ProfileEditInformation.this,"Changes Saved Successfully",Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();

        Intent intent=new Intent(this,SELL_BUY.class);
//        intent.putExtra("restartActivity","true");
        startActivity(intent);


    }


}
