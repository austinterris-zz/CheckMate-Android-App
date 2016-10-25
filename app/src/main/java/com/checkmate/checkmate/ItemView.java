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
    TextView itemTextView;
    Button button;
    String itemName;
    EditText numItemsEditText;

    public ItemView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(params);
        inflater.inflate(R.layout.item_view, this, true);
        this.itemTextView = (TextView) findViewById(R.id.item_name_tv);
        this.button = (Button) findViewById(R.id.button_plus);
        this.numItemsEditText = (EditText) findViewById(R.id.num_items_et);
        this.requestLayout();

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementItemCount(v);
            }
        });
    }
    public void incrementItemCount(View view){
        if (this.numItemsEditText == null){
            Log.d("EditText", "null");
            this.numItemsEditText = (EditText) findViewById(R.id.num_items_et);
            if (this.numItemsEditText != null){
                Log.d("EditText", "not null after finding");
            }

        }
        else {
            Log.d("EditText", "not null");
        }
        String strNum = this.numItemsEditText.getText().toString();
        int num = Integer.parseInt(strNum);
        String newNum = String.valueOf(num + 1);
        this.numItemsEditText.setText(newNum);
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
        this.itemTextView.setText(itemName);
    }

}
