package com.checkmate.checkmate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class CartActivity extends AppCompatActivity {
    ItemListAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Item> cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_payment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout();
            }
        });

        cart = new ArrayList<Item>();
        adapter = new ItemListAdapter(this, cart, this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        getItemInfo("111");
        getItemInfo("222");
        getItemInfo("333");
    }

    public void checkout()
    {
        //iterate through all items in cart array list
        //JSONArray jsonItemsList = getJSONArrayFromURL("http://caltec.dyndns.org:3000/items/buy/" + cart.get(i).hf);

        this.onBackPressed();
    }

    public void getItemInfo(String hf)
    {
        Log.d("CheckMate", "Retrieving Item " + hf);
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
                JSONArray jsonItemsList = getJSONArrayFromURL("http://caltec.dyndns.org:3000/items/info/" + hf);

                if (jsonItemsList != null)
                {
                    JSONObject json_data = null;

                    for (int i = 0; i < jsonItemsList.length(); i++)
                    {
                        json_data = jsonItemsList.getJSONObject(i);
                        String name = json_data.getString("name");
                        //int hfid = json_data.getInt("")
                        Log.d("CheckMate", "Retrieved " + name);
                        String price = json_data.getString("price");
                        Item newItem = new Item(0, name, "$" + price, 1);
                        cart.add(newItem);
                    }
                    adapter.notifyDataSetChanged();
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
                    return new JSONArray("[" + sb.toString() + "]");
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
