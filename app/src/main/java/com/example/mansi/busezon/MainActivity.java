package com.example.mansi.busezon;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
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
import android.widget.ImageButton;
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
    private String user_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            //ActionBar ab = getSupportActionBar();
            // ab.setDisplayHomeAsUpEnabled(true);


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


//            Button SignUp = (Button) findViewById(R.id.signup);
//            SignUp.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View arg0) {
//                    Intent i = new Intent(getApplicationContext(), SigningUpActivity.class);
//                    startActivity(i);
//                }
//            });

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
//ImageButton amazonlogin=(ImageButton)findViewById(R.id.login_with_amazon);
//amazonlogin.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//       // registerUserAmazon();

               requestContext.registerListener(new AuthorizeListener() {
                        /* Authorization was completed successfully. */
                        @Override
                        public void onSuccess(AuthorizeResult authorizeResult) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            // At this point we know the authorization completed, so remove the ability to return to the app to sign-in again
//                            setLoggingInState(true);
//                        }
//                    });
                            fetchUserProfile();
                        }

                        /* There was an error during the attempt to authorize the application */
                        @Override
                        public void onError(AuthError authError) {
                            Log.e(TAG, "AuthError during authorization", authError);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            showAuthToast("Error during authorization.  Please try again.");
//                            resetProfileView();
//                            setLoggingInState(false);
//                        }
//                    });
                        }

                        /* Authorization was cancelled before it could be completed. */
                        @Override
                        public void onCancel(AuthCancellation authCancellation) {
                            Log.e(TAG, "User cancelled authorization");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            showAuthToast("Authorization cancelled");
                           resetProfileView();
//                        }
//                    });
                        }
                    });
//    }
//});

//            setContentView(R.layout.activity_main);
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
                                startIntent(name, email,id);
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
//        StringBuilder profileBuilder = new StringBuilder();
//        profileBuilder.append(String.format("Welcome, %s!\n", name));
//        profileBuilder.append(String.format("Your email is %s\n", email));
//        profileBuilder.append(String.format("Your zipCode is %s\n", zipCode));
//        final String profile = profileBuilder.toString();
//        Log.d(TAG, "Profile Response: " + profile);
      //  Toast.makeText(MainActivity.this, "  user 1234", Toast.LENGTH_LONG).show();
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
                String DBemail = ds.child("email").getValue(String.class);
                if (DBemail.equals(email)) {
                    String id = ds.getKey();
                    databaseReference.child(id).child("name").setValue(name);
                    databaseReference.child(id).child("email").setValue(email);
                    user_Id = id;
                    f = 1;
                    break;
                }
            }
            if (f == 1) {
                Toast.makeText(MainActivity.this, " user details added", Toast.LENGTH_LONG).show();
            }
            else {
                // User not present in db store it with amazon account
                UserInformation userInformation = new UserInformation(name, "", email, "", "");
                user_Id = account.replace("amzn1.account.", "");
                databaseReference.child(user_Id).setValue(userInformation);
                Toast.makeText(MainActivity.this, "  user details added", Toast.LENGTH_LONG).show();
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
    //Toast.makeText(MainActivity.this, id+"    user", Toast.LENGTH_LONG).show();
    i.putExtra("Id",id);
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

            // Find the button with the logout ID and set up a click handler
//            View logoutButton = findViewById(R.id.logout);
//            logoutButton.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    AuthorizationManager.signOut(getApplicationContext(), new Listener<Void, AuthError>() {
//                        @Override
//                        public void onSuccess(Void response) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    setLoggedOutState();
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onError(AuthError authError) {
//                            Log.e(TAG, "Error clearing authorization state.", authError);
//                        }
//                    });
//                }
//            });

            String logoutText = getString(R.string.logout);
            mProfileText = (TextView) findViewById(R.id.profile_info);
          //  mLogoutTextView = (TextView) logoutButton;
            mLogoutTextView.setText(logoutText);
            mLogInProgress = (ProgressBar) findViewById(R.id.log_in_progress);
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error");
        }
    }

    /**
     * Sets the text in the mProfileText {@link TextView} to the value of the provided String.
     *
     * @param profileInfo the String with which to update the {@link TextView}.
     */
//    private void updateProfileView(String profileInfo)
//    {
//        Log.d(TAG, "Updating profile view");
//        mProfileText.setText(profileInfo);
//
//
//    }

    /**
     * Sets the text in the mProfileText {@link TextView} to the prompt it originally displayed.
     */
    private void resetProfileView() {
        setLoggingInState(false);
        mProfileText.setText(getString(R.string.default_message));
    }

    /**
     * Sets the state of the application to reflect that the user is currently authorized.
     */
    private void setLoggedInState() {
        mLoginButton.setVisibility(Button.GONE);
        setLoggedInButtonsVisibility(Button.VISIBLE);
        mIsLoggedIn = true;
        setLoggingInState(false);
    }

    /**
     * Sets the state of the application to reflect that the user is not currently authorized.
     */
    private void setLoggedOutState() {
        mLoginButton.setVisibility(Button.VISIBLE);
        setLoggedInButtonsVisibility(Button.GONE);
        mIsLoggedIn = false;
        resetProfileView();
    }

    /**
     * Changes the visibility for both of the buttons that are available during the logged in state
     *
     * @param visibility the visibility to which the buttons should be set
     */
    private void setLoggedInButtonsVisibility(int visibility) {
        mLogoutTextView.setVisibility(visibility);
    }

    /**
     * Turns on/off display elements which indicate that the user is currently in the process of logging in
     *
     * @param loggingIn whether or not the user is currently in the process of logging in
     */
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

    private void showAuthToast(String authToastMessage) {
        Toast authToast = Toast.makeText(getApplicationContext(), authToastMessage, Toast.LENGTH_LONG);
        authToast.setGravity(Gravity.CENTER, 0, 0);
        authToast.show();
    }





//LOGIN WITH AMAZON COMPLETE

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
                        i.putExtra("Name", Name);
                        i.putExtra("Email", Email);
                        i.putExtra("Id", userId);
                        startActivity(i);
                        f=0;
                    }

                }
                if(f==1) {
                    Intent i = new Intent(MainActivity.this, GoogleSignInInformation.class);
                    i.putExtra("Name", Name);
                    i.putExtra("Email", Email);
                    i.putExtra("Id", userId);
                    progressDialog.dismiss();
                    startActivity(i);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
    }

}

