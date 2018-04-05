package com.example.mansi.busezon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    private   Button buttonlogin;
    private EditText email,password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button SignUp=(Button)findViewById(R.id.signup);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), SigningUpActivity.class);
                startActivity(i);
            }
        });
        buttonlogin=(Button)findViewById(R.id.login);

        progressDialog=new ProgressDialog(this);
        email=(EditText)findViewById(R.id.editTextloginEmail);
        password=(EditText)findViewById(R.id.editTextloginPassword);
        buttonlogin=(Button)findViewById(R.id.login);
        firebaseAuth=FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser() != null) {
//            Toast.makeText(this,"Moving to wishlist",Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(MainActivity.this, WishlistActivity.class));
//            finish();
//        }
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginclick(view);
            }
        });
    }

    private void loginclick(View view)
    {
        String Email=email.getText().toString().trim();
        String Password=password.getText().toString().trim();
        if(TextUtils.isEmpty(Email))
        {
            Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(Password)&&Password.length()>5)
        {
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("logging user in");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(getApplicationContext(),SELL_BUY.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

