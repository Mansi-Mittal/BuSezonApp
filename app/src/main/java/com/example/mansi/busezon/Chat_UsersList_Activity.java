package com.example.mansi.busezon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Chat_UsersList_Activity extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    Firebase reference1;
String loginUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_userlist);
        Intent intent=getIntent();
        loginUser= UserInformation.name;

        usersList = (ListView) findViewById(R.id.usersList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);

        pd = new ProgressDialog(Chat_UsersList_Activity.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://busezon-57985.firebaseio.com/messages.json";

try
{
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s)
            {
                doOnSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Chat_UsersList_Activity.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chat_UserDetails.chatWith = al.get(position);
                Chat_UserDetails.username=loginUser;
//                Toast.makeText(Chat_UsersList_Activity.this,Chat_UserDetails.chatWith+" "+Chat_UserDetails.username,Toast.LENGTH_LONG).show();
                startActivity(new Intent(Chat_UsersList_Activity.this, Chat_Message_Acitivty.class));
            }
        });

//            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//            setSupportActionBar(myToolbar);
//            ActionBar ab = getSupportActionBar();
//            ab.setDisplayHomeAsUpEnabled(true);
//
//            BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
//            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
//                @Override
//                public void onTabSelected(@IdRes int tabId) {
//                    if (tabId == R.id.tab_wishList) {
//                        Intent i = new Intent(getApplicationContext(), WishlistActivity.class);
////                    i.putExtra("User_Name",User_Name);
//                        startActivity(i);
//                    } else if (tabId == R.id.tab_profile) {
//                        Intent i = new Intent(getApplicationContext(), profile_page.class);
//                        startActivity(i);
//                    }
//
//                }
//            });

        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                if(!key.equals(Chat_UserDetails.username))
                {
                    String users[]=key.split("_");
                    String messenger=users[0]+"_"+loginUser;
                    if((!al.contains(users[0]))&&(!users[0].equals(loginUser))&&(key.equals(messenger)))
                    {
                        al.add(users[0]);
                    }
                }
                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
//            noUsersText.setText("No chats available");
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }
}