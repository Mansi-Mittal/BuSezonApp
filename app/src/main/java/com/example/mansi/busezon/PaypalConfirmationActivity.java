package com.example.mansi.busezon;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.identity.auth.device.api.authorization.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

public class PaypalConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypalconfirmation);

        //Getting Intent
        Intent intent = getIntent();


        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus= (TextView) findViewById(R.id.paymentStatus);
      TextView textViewAmountINR = (TextView) findViewById(R.id.paymentAmountINR);
        TextView textViewAmountUSD = (TextView) findViewById(R.id.paymentAmountUSD);
        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
       textViewAmountUSD.setText(paymentAmount+" USD");
       int amount=Integer.valueOf(paymentAmount)*67;
       textViewAmountINR.setText(amount+" INR");
       if(!jsonDetails.getString("state").equals("approved"))
       {
           Toast.makeText(this,"Payment failed!!! Going back to cart.",Toast.LENGTH_LONG).show();
           UserInformation.payment=false;
           Intent intent=new Intent(PaypalConfirmationActivity.this,shoppingCart.class);
           startActivity(intent);
       }
        UserInformation.payment=true;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.cart);
        mBuilder.setContentTitle("BuSezon Notification Alert!");
        mBuilder.setContentText("Hi, Your payment was successful! You will recieve your order in 2 working days!");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
 int notificationID=1;
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    public void startHomeActivity(View view)
    {
        Intent intent=new Intent(PaypalConfirmationActivity.this,SELL_BUY.class);
        startActivity(intent);
    }
}
