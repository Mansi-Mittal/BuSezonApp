package com.example.mansi.busezon;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    SearchView searchView;
    String URL = "http://172.20.10.9:3000/products/search?search=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //getActionBar().setTitle("BuSezon");
        mDrawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
        ab.setHomeAsUpIndicator(R.drawable.ic_shopping_cart_black_24dp);


        ab.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        //searchView = findViewById(R.id.action_search);

        ArrayList<offers> offersList=new ArrayList<>();
        //offersList.add(new offers(R.drawable.img1,"Great discounts"));
        //offersList.add(new offers(R.drawable.img2,"Great discounts"));
        //offersList.add(new offers(R.drawable.img3,"Great discounts"));
        //offersList.add(new offers(R.drawable.img4,"Great discounts"));
        //offersList.add(new offers(R.drawable.img5,"Great discounts"));

        ListView offersListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        final offersAdapter adapter = new offersAdapter(this, offersList);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        offersListView.setAdapter(adapter);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_wishList) {
                    Intent i = new Intent(getApplicationContext(), WishlistActivity.class);
                    startActivity(i);
                }
                else if (tabId == R.id.tab_profile) {
                    Intent i = new Intent(getApplicationContext(), profile_page.class);
                    startActivity(i);
                }
                else if (tabId == R.id.tab_chat) {
                    try {
                        Intent i = new Intent(HomeActivity.this, Chat_UsersList_Activity.class);
                        i.putExtra("user", UserInformation.UserId);
                        i.putExtra("User_Name", UserInformation.name);
                        startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;  // Return true to expand action view
            }
        };


        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String search) {
                //String search = searchView.getQuery().toString();
                /*Intent appInfo = new Intent(HomeActivity.this, productDisplay.class);
                Bundle d = new Bundle();
                d.putBoolean("search",true);
                d.putString("urlParam",search); //Your id
                appInfo.putExtras(d); //Put your id to your next Intent
                startActivity(appInfo);*/
                //URL = "http://172.20.10.9:3000/products/search?search=";
                //URL += search;
                Intent appInfo = new Intent(HomeActivity.this, productDisplay.class);
                appInfo.putExtra("urlParam",search);
                startActivity(appInfo);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    return true;
            case R.id.action_cart:
                Intent i = new Intent(this, shoppingCart.class);
                startActivity(i);
                return true;

            case R.id.action_search:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    public void sendJsonRequest() {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (URL, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //productDisplay.onResponse(response);
                            serverParams.responeArray = response;
                            Intent appInfo = new Intent(HomeActivity.this, productDisplay.class);
                            //appInfo.putExtra("JsonArray",response.toString());
                            startActivity(appInfo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
    }


