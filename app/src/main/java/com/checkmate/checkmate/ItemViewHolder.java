package com.checkmate.checkmate;

import android.support.v7.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    ItemView itemView;
    private String m_itemName;
    private int m_HFID;
    private int m_UHFID;
    private String m_price;

    public ItemViewHolder(ItemView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void bind(Item item){
        this.m_itemName = item.getName();

        itemView.setItemName(this.m_itemName);
        itemView.setItemPrice(item.getPrice());
    }
}