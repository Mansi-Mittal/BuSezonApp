package com.example.mansi.busezon;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.amazon.identity.auth.device.api.authorization.User;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
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

    //AMAZON LOGIN VARIABLES
    private static final String TAG = MainActivity.class.getName();
    private TextView mProfileText;
    private TextView mLogoutTextView;
    private ProgressBar mLogInProgress;
    private RequestContext requestContext;
    private boolean mIsLoggedIn;
    private View mLoginButton;


    private Button buttonlogin;
    private EditText email, password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private SignInButton signInButtongoogle;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient googleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference databaseReference;
    private String user_Id,user_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_Name="";
        setContentView(R.layout.activity_main);
        try {
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            SpannableString ss = new SpannableString("New to BuSeZon? SignUp Now");
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    startActivity(new Intent(MainActivity.this, SigningUpActivity.class));
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };
            ss.setSpan(clickableSpan, 16, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            TextView SingInView = (TextView) findViewById(R.id.signin_info);
            SingInView.setText(ss);
            SingInView.setMovementMethod(LinkMovementMethod.getInstance());
            SingInView.setHighlightColor(Color.TRANSPARENT);

            buttonlogin = (Button) findViewById(R.id.login);
            progressDialog = new ProgressDialog(this);
            email = (EditText) findViewById(R.id.editTextloginEmail);
            password = (EditText) findViewById(R.id.editTextloginPassword);
            buttonlogin = (Button) findViewById(R.id.login);
            firebaseAuth = FirebaseAuth.getInstance();
            buttonlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginclick(view);
                }
            });
            databaseReference = FirebaseDatabase.getInstance().getReference();

            signInButtongoogle = (SignInButton) findViewById(R.id.SignUpGoogle);
            signInButtongoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registerUserGoogle();
                }
            });


//login with amazon

            requestContext = RequestContext.create(this);
               requestContext.registerListener(new AuthorizeListener() {
                        /* Authorization was completed successfully. */
                        @Override
                        public void onSuccess(AuthorizeResult authorizeResult) {
                            fetchUserProfile();
                        }

                        /* There was an error during the attempt to authorize the application */
                        @Override
                        public void onError(AuthError authError) {
                            Log.e(TAG, "AuthError during authorization", authError);
                        }

                        /* Authorization was cancelled before it could be completed. */
                        @Override
                        public void onCancel(AuthCancellation authCancellation) {
                            Log.e(TAG, "User cancelled authorization");
                           resetProfileView(false);
                        }
                    });
                    initializeUI();
        }
        catch(Exception e)
                {
                    Log.d(TAG, e.getMessage());
                }

    }
//LOGIN WITH AMAZON


    @Override
    protected void onResume() {
        super.onResume();
        requestContext.onResume();
    }


    private void fetchUserProfile() {
        try {
            UserInformation.token="amazon";
            User.fetch(this, new Listener<User, AuthError>() {

                /* fetch completed successfully. */
                @Override
                public void onSuccess(User user) {
                    final String name = user.getUserName();
                    final String email = user.getUserEmail();
                    final String account = user.getUserId();
                    final String zipCode = user.getUserPostalCode();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {

                                String id = updateProfileData(name, email, account, zipCode);

                        }
                    });
                }

                /* There was an error during the attempt to get the profile. */
                @Override
                public void onError(AuthError ae) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // setLoggedOutState();
                            String errorMessage = "Error retrieving profile information.\nPlease log in again";
                            Toast errorToast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG);
                            errorToast.setGravity(Gravity.CENTER, 0, 0);
                            errorToast.show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private String updateProfileData(final String name, final String email, final String account, String zipCode) {

        mLoginButton.setVisibility(Button.GONE);
        /*

        ............................STORE USER DATA INTO FIREBASE.......................................

         */
    try
    {
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            int f = 0;
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                String uId=ds.getKey();
                String DBemail = ds.child("email").getValue(String.class);
                if(DBemail==null)
                    break;
                if (DBemail.equals(email)) {
                    String id = ds.getKey();
                    databaseReference.child(id).child("name").setValue(name);
                    databaseReference.child(id).child("email").setValue(email);
                    user_Id = id;
                    f = 1;
                    break;
                }
            }
            if (f == 1)
            {
                Toast.makeText(MainActivity.this, " user details added ", Toast.LENGTH_LONG).show();
                UserInformation.name=name;
                UserInformation.email=email;
                UserInformation.UserId=user_Id;
                startIntent(name, email,user_Id);
            }
            else {
                // User not present in db store it with amazon account
                FirebaseUserInformation userInformation = new FirebaseUserInformation(name, "", email, "", "");
                user_Id = account.replace("amzn1.account.", "");
                databaseReference.child(user_Id).setValue(userInformation);
                Toast.makeText(MainActivity.this, "user details added !", Toast.LENGTH_LONG).show();
                UserInformation.name=name;
                UserInformation.email=email;
                UserInformation.UserId=user_Id;
                startIntent(name, email,user_Id);

                f = 1;

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }

    });

}
catch(Exception e)
{
    Toast.makeText(MainActivity.this, "  user details error!!", Toast.LENGTH_LONG).show();
}

        return  user_Id;
}


private void startIntent(String name,String email,String id)
{
    Intent i = new Intent(MainActivity.this, SELL_BUY.class);
    i.putExtra("Name", name);
    i.putExtra("Email", email);
    i.putExtra("Id",id);
    UserInformation.name=name;
    UserInformation.email=email;
    UserInformation.UserId=id;
    startActivity(i);
}


    /**
     * Initializes all of the UI elements in the activity
     */
    private void initializeUI()
    {

        try {

            mLoginButton = findViewById(R.id.login_with_amazon);
            // Toast.makeText(MainActivity.this,"msg",Toast.LENGTH_LONG).show();
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AuthorizationManager.authorize(
                            new AuthorizeRequest.Builder(requestContext)
                                    .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
                                    .build()
                    );
                }
            });
            String logoutText = getString(R.string.logout);
            mProfileText = (TextView) findViewById(R.id.profile_info);
            mLogoutTextView.setText(logoutText);
            mLogInProgress = (ProgressBar) findViewById(R.id.log_in_progress);
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error");
        }
    }

    /**
     * Sets the text in the mProfileText {@link TextView} to the prompt it originally displayed.
     */
    private void resetProfileView(Boolean a)
    {
        setLoggingInState(a);
        mProfileText.setText(getString(R.string.default_message));
    }

    private void setLoggedInButtonsVisibility(int visibility) {
        mLogoutTextView.setVisibility(visibility);
    }

    private void setLoggingInState(final boolean loggingIn) {
        if (loggingIn) {
            mLoginButton.setVisibility(Button.GONE);
            setLoggedInButtonsVisibility(Button.GONE);
            mLogInProgress.setVisibility(ProgressBar.VISIBLE);
            mProfileText.setVisibility(TextView.GONE);
        } else {
            if (mIsLoggedIn) {
                setLoggedInButtonsVisibility(Button.VISIBLE);
            } else {
                mLoginButton.setVisibility(Button.VISIBLE);
            }
            mLogInProgress.setVisibility(ProgressBar.GONE);
            mProfileText.setVisibility(TextView.VISIBLE);
        }
    }
//LOGIN WITH AMAZON COMPLETE



    private void loginclick(View view)
    {
       UserInformation.token="normal";
        final String Email=email.getText().toString().trim();
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
                if (task.isSuccessful()) {
                    try {
                        FirebaseUser currentUser=firebaseAuth.getCurrentUser();
                        String userId=currentUser.getUid();
                        Toast.makeText(MainActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),SELL_BUY.class);
                        i.putExtra("Id",userId);
                        i.putExtra("Email",Email);
                        UserInformation.name=currentUser.getDisplayName();
                        UserInformation.email=Email;
                        UserInformation.UserId=userId;
//                        Toast.makeText(MainActivity.this,userId+" "+tokenToCheckLoginType+" "+Email,Toast.LENGTH_SHORT).show();
                        startActivity(i);

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        }



    //SIGNIN WITH GOOGLE
    @SuppressLint("RestrictedApi")
    public void registerUserGoogle()
    {
       UserInformation.token="google";
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
        try {
            Scope[] scopes = {ProfileScope.profile(), ProfileScope.postalCode()};
            AuthorizationManager.getToken(this, scopes, new Listener<AuthorizeResult, AuthError>() {
                @Override
                public void onSuccess(AuthorizeResult result) {
                    if (result.getAccessToken() != null) {
                    /* The user is signed in */
                    mLoginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fetchUserProfile();
                        }
                    });
                    } else {
                    /* The user is not signed in */
                    }
                }

                @Override
                public void onError(AuthError ae) {
                /* The user is not signed in */
                }
            });
        }
        catch (Exception e)
        {
            Log.d(TAG,e.getMessage());
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        progressDialog.setMessage("Logging In using Google");
        progressDialog.show();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            String Name=user.getDisplayName();
                            String Email=user.getEmail();
                            String Id=user.getUid();
                            check(Id,Name,Email);
                     } else {
                            Toast.makeText(MainActivity.this,"Error google signin failed",Toast.LENGTH_SHORT).show();
                        }
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
                        UserInformation.name=Name;
                        UserInformation.email=Email;
                        UserInformation.UserId=userId;
                        startActivity(i);
                        f=0;
                    }

                }
                if(f==1) {
                    Intent i = new Intent(MainActivity.this, GoogleSignInInformation.class);
                    i.putExtra("Name", Name);
                    i.putExtra("Email", Email);
                    i.putExtra("Id", userId);
                    UserInformation.name=Name;
                    UserInformation.email=Email;
                    UserInformation.UserId=userId;
                    progressDialog.dismiss();
                    startActivity(i);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
    }

}

