package com.checkmate.checkmate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
                Snackbar.make(view, "Checkout", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void getItemInfo(String hf)
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
                JSONArray jsonItemsList = getJSONArrayFromURL("http://caltec.dyndns.org:3000/items/info/" + hf);

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

                    //adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtname, items);
                    //ListView list = (ListView) findViewById(R.id.itemList);
                    //list.setAdapter(adapter);
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
