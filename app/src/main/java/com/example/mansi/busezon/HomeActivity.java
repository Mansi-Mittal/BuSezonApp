package com.example.mansi.busezon;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    SearchView searchView;
    Button like;
    int id = 0;
    String category = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        like = (Button)findViewById(R.id.like);
        ArrayList<offerLayout> offersList=new ArrayList<>();
        offersList.add(new offerLayout(R.drawable.img2,like));
        offersList.add(new offerLayout(R.drawable.img3,like));
        offersList.add(new offerLayout(R.drawable.img4,like));
        offersList.add(new offerLayout(R.drawable.img5,like));
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
        ab.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        final Menu menuNav =  navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener()
                {
                    Dialog myDialog=new Dialog(HomeActivity.this);
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        MenuItem fashion=menuNav.findItem(R.id.nav_fashion);
                        MenuItem electronics=menuNav.findItem(R.id.nav_electronics);
                        MenuItem account=menuNav.findItem(R.id.nav_account);
                        MenuItem mybag=menuNav.findItem(R.id.nav_bag);
                        fashion.setChecked(false);
                        mybag.setChecked(false);
                        account.setChecked(false);
                        electronics.setChecked(false);
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        if(fashion.isChecked())
                        {
                            fashion.setChecked(false);
                            mybag.setChecked(false);
                            account.setChecked(false);
                            electronics.setChecked(false);
                            TextView txtclose;
                            category ="clothing";
                            myDialog.setContentView(R.layout.custompopupfashion);
                            txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
                            txtclose.setText("close");
                            txtclose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialog.dismiss();
                                }
                            });
                            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            myDialog.show();

                        }
                        else if(electronics.isChecked()) {
                            fashion.setChecked(false);
                            mybag.setChecked(false);
                            account.setChecked(false);
                            electronics.setChecked(false);
                            TextView txtclose;
                            category = "electronics";
                            myDialog.setContentView(R.layout.custompopupelectronics);
                            txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
                            txtclose.setText("close");
                            txtclose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialog.dismiss();
                                }
                            });
                            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            myDialog.show();

                        }
                        else if(account.isChecked())
                        {
                            fashion.setChecked(false);
                            mybag.setChecked(false);
                            account.setChecked(false);
                            electronics.setChecked(false);
                            startActivity(new Intent(HomeActivity.this,profile_page.class));

                        }
                        else if(mybag.isChecked())
                        {
                            fashion.setChecked(false);
                            mybag.setChecked(false);
                            account.setChecked(false);
                            electronics.setChecked(false);
                            startActivity(new Intent(HomeActivity.this,shoppingCart.class));
                        }

                        fashion.setChecked(false);
                        mybag.setChecked(false);
                        account.setChecked(false);
                        electronics.setChecked(false);
                        return true;
                    }
                });



        ListView offersListView = (ListView) findViewById(R.id.list);
        final offerLayoutAdapter adapter = new offerLayoutAdapter(this, offersList);
        offersListView.setAdapter(adapter);

        offersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent appInfo = new Intent(HomeActivity.this, productDisplay.class);
                appInfo.putExtra("offer_id",id);
                startActivity(appInfo);
            }
        });

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
                return true;
            }
        };

        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String search) {
                searchView.setQuery("", false);
                searchView.clearFocus();
                Intent appInfo = new Intent(HomeActivity.this, productDisplay.class);
                appInfo.putExtra("urlParam",search);
                appInfo.putExtra("search",true);
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
    public void electronicsMobile(View view){
        boolean checked = ((CheckBox) view).isChecked();

        if (checked) {
            Intent app = new Intent(HomeActivity.this,productDisplay.class);
            app.putExtra("urlParam",category);
            app.putExtra("subCat","Mobile");
            app.putExtra("search",false);
            startActivity(app);
        }else{

        }
    }
}


