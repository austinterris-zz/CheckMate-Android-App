package com.checkmate.checkmate;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity {
    private ItemListAdapter adapter;
    private RecyclerView recyclerView;
    private ItemObserver itemObserver;

    private NfcAdapter mNfcAdapter;
    private PendingIntent pendingIntent;


    private Retrofit retrofit;

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

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Set up NFC
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        itemObserver = (ItemObserver) getLastCustomNonConfigurationInstance();

        if (itemObserver == null){
            itemObserver = new ItemObserver();
        }

        adapter = new ItemListAdapter(this, itemObserver.getItems(), this);
        itemObserver.bind(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        String hfid = new String();
        for (int i = 0; i < tagId.length; i++)
        {
            String x = Integer.toHexString(((int) tagId[i] & 0xff));
            if (x.length() == 1) {
                x = '0' + x;
            }
            hfid += x;
        }
        Log.d("CheckMate", "Scanned Tag ID " + hfid);

        getItemInfo(hfid);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        itemObserver.unbind();
        return itemObserver;
    }

    public void checkout()
    {
        Log.d("Checkout", "items in cart: " + itemObserver.getItems().toString());
        for (Item temp : itemObserver.getItems())
        {
            Log.d("CheckMate", "Purchasing " + temp.getName() + " with ID " + temp.getHf());
            RetrofitHelper.get()
                    .buyItem(temp.getHf())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(itemObserver);
        }

        Toast.makeText(this, "Checkout Complete!", Toast.LENGTH_LONG).show();

        this.onBackPressed();
    }

    public void getItemInfo(String hf)
    {
        Log.d("CheckMate", "Retrieving Item " + hf);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            RetrofitHelper.get()
                    .getItem(hf)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(itemObserver);
        }
    }

    private static class ItemObserver implements Observer<Item> {
        private CartActivity cartActivity;
        private ArrayList<Item> items = new ArrayList<Item>();

        public ArrayList<Item> getItems(){return items;}

        private void bind(CartActivity cartActivity){
            this.cartActivity = cartActivity;}

        private void unbind(){
            cartActivity = null;}

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Log.e("ERROR", "A terrible error has occurred", e);
        }

        @Override
        public void onNext(Item item) {
            Log.d("onNext", "item hf: " + item.getHf() + "\nitem name: " + item.getName());
            int index = items.size();
            items.add(item);
            cartActivity.adapter.notifyItemInserted(index);
        }
    }
}
