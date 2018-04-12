package com.example.mansi.busezon;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
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

public class MainActivity extends AppCompatActivity {


    private   Button buttonlogin;
    private EditText email,password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private SignInButton signInButtongoogle;
    private static final int RC_SIGN_IN=1;
    private GoogleApiClient googleApiClient;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //ActionBar ab = getSupportActionBar();
       // ab.setDisplayHomeAsUpEnabled(true);

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
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginclick(view);
            }
        });

        signInButtongoogle=(SignInButton)findViewById(R.id.SignUpGoogle);
        signInButtongoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUserGoogle();
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
        if (TextUtils.isEmpty(Password)) {
            if(Password.length() < 5)
                Toast.makeText(this, "Password should be atleast 6 characters long", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
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
                    String userId=firebaseAuth.getCurrentUser().getUid();
                    Toast.makeText(MainActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(getApplicationContext(),SELL_BUY.class);
                    i.putExtra("Id",userId);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Try again",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //SIGNIN WITH GOOGLE
    @SuppressLint("RestrictedApi")
    public void registerUserGoogle()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleApiClient=new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        signIn();
    }
    private void signIn() {


        @SuppressLint("RestrictedApi") Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            @SuppressLint("RestrictedApi") Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(MainActivity.this,"Error google signin failed",Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            // updateUI(user);
                            String Name=user.getDisplayName();
                            String Email=user.getEmail();
                            String Id=user.getUid();
                            check(Id,Name,Email);
                            //Toast.makeText(SigningUpActivity.this,"name= "+Name+" email= "+Email+" id= "+Id,Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(MainActivity.this,"Error google signin failed",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    private void check(final String userId,final String Name,final String Email)
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                int f=1;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getKey();
                    if (userId.equals(id)) {
                        Intent i=new Intent(MainActivity.this,SELL_BUY.class);
                        startActivity(i);
                        f=0;
                    }

                }
                if(f==1) {
                    Intent i = new Intent(MainActivity.this, GoogleSignInInformation.class);
                    i.putExtra("Name", Name);
                    i.putExtra("Email", Email);
                    i.putExtra("Id", userId);
                    startActivity(i);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
    }

}

