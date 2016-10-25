package com.checkmate.checkmate;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    ItemView itemView;
    private String itemName;

    public ItemViewHolder(ItemView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void bind(String itemName, int pos){
        this.itemName = itemName;
        itemView.setItemName(itemName);
        if (pos % 2 == 0){
            itemView.setBackgroundColor(Color.LTGRAY);
        }
    }
}