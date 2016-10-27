package com.checkmate.checkmate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Brady on 10/23/2016.
 */

public class ItemView extends LinearLayout {
    TextView itemDescTextView, itemPriceTextView;
    Button button;
    String itemName, itemPrice;

    EditText numItemsEditText;

    public ItemView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(params);
        inflater.inflate(R.layout.item_view, this, true);
        this.itemDescTextView = (TextView) findViewById(R.id.item_name_tv);
        this.itemPriceTextView = (TextView) findViewById(R.id.price_text_view);
        this.requestLayout();

    }

    public void setItemName(String itemName){
        this.itemName = itemName;
        this.itemDescTextView.setText(itemName);
    }

    public void setItemPrice(String itemPrice){
        this.itemPrice = itemPrice;
        this.itemPriceTextView.setText(itemName);
    }

}
