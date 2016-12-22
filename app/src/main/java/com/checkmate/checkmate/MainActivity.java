package com.checkmate.checkmate;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayAdapter<String> adapter;
    EditText editText;
    ArrayList <String> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_shopping_cart);
        final Context context = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CartActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getItemsList();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            getItemsList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            // Handle the camera action
        } else if (id == R.id.nav_coupons) {

        } else if (id == R.id.nav_shopping_list) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getItemsList()
    {
        Log.d("CheckMate", "Retrieving Items List");
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //http://stackoverflow.com/questions/13136539/caused-by-android-os-networkonmainthreadexception
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (networkInfo != null && networkInfo.isConnected())
        {
            try
            {
                JSONArray jsonItemsList = getJSONArrayFromURL("http://caltec.dyndns.org:3000/items");

                if (jsonItemsList != null) {
                    Log.d("CheckMate", "Retrieve Complete");


                    JSONObject json_data = null;

                    ArrayList<String> items = new ArrayList<String>();
                    for (int i = 0; i < jsonItemsList.length(); i++)
                    {
                        json_data = jsonItemsList.getJSONObject(i);
                        String name = json_data.getString("name");
                        items.add(name);
                    }

                    adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtname, items);
                    ListView list = (ListView) findViewById(R.id.itemList);
                    list.setAdapter(adapter);
                }
                else
                {
                    Log.d("CheckMate", "Failed to Retrieve Items");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            // display error
        }
    }

    public static JSONArray getJSONArrayFromURL(String urlString) throws IOException, JSONException
    {
        HttpURLConnection c = null;
        try
        {
            URL u = new URL(urlString);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(2000);
            c.setReadTimeout(2000);
            c.connect();
            int status = c.getResponseCode();

            switch (status)
            {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null)
                        sb.append(line+"\n");
                    br.close();
                    return new JSONArray(sb.toString());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (c != null)
            {
                try
                {
                    c.disconnect();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
