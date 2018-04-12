package com.example.mansi.busezon;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SigningUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonsignup;
    private EditText email,name,phoneno,address,password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing_up);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        progressDialog=new ProgressDialog(this);

        buttonsignup=(Button)findViewById(R.id.signupButton);
        email=(EditText)findViewById(R.id.editTextEmail);
        name=(EditText)findViewById(R.id.editTextname);
        phoneno=(EditText)findViewById(R.id.editTextPhoneno);
        address=(EditText)findViewById(R.id.editTextAddress);
        password=(EditText)findViewById(R.id.editTextPassword);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        buttonsignup.setOnClickListener(this);


    }
    @Override
    public void onClick(View view)
    {
        if(view==buttonsignup)
            registerUser();
    }
    private void saveUserDetails(String Name,String Email,String PhoneNo,String Address,String Password)
    {
        UserInformation userInformation=new UserInformation(Name,Address,Email,PhoneNo,Password);
        FirebaseUser user=firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(userInformation);
    }

    private void registerUser()
    {
        final String Email=email.getText().toString().trim();
        final String Name=name.getText().toString().trim();
        final String PhoneNo=phoneno.getText().toString().trim();
        final String Address=address.getText().toString().trim();
        final String Password=password.getText().toString().trim();
        if(TextUtils.isEmpty(Email))
        {
            Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(Name))
        {
                Toast.makeText(this,"Enter Name",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            String z[]=Name.split(" ");
            if(z.length<=1) {
                Toast.makeText(this, "Enter Firstname and Lastname", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(TextUtils.isEmpty(PhoneNo)||PhoneNo.length()!=10)
        {
            Toast.makeText(this,"Enter Phone number",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(Address))
        {
            Toast.makeText(this,"Enter Address",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Password)) {
            if(Password.length() < 5)
                Toast.makeText(this, "Password should be atleast 6 characters long", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    saveUserDetails(Name,Email,PhoneNo,Address,Password);
                    Toast.makeText(SigningUpActivity.this,"Resgistered Successfully",Toast.LENGTH_SHORT).show();

                    //progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(SigningUpActivity.this,"Try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}

