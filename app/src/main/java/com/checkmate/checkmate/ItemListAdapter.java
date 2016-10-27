package com.checkmate.checkmate;

import android.content.Context;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.util.List;

/**
 * Created by Brady on 10/23/2016.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private Context context;
    private CartActivity listActivity;
    private List<String> itemStrList;
    private int selected_position;
    private ActionMode.Callback actionCallBack;
    private ActionMode actionMode;

    public ItemListAdapter(Context context, List<String> itemStrList, CartActivity listActivity) {
        this.context = context;
        this.itemStrList = itemStrList;
        this.listActivity = listActivity;

        this.actionCallBack = new ActionMode.Callback(){
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                actionMode =  mode;
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.action_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_remove:
                        removeItem(selected_position);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
            }
        };
    }

    public void removeItem(int pos){

        if (itemStrList.size() > 0){
            itemStrList.remove(pos);
            this.notifyItemRemoved(pos);
        }
    }

    public void addItem(String itemName){
        if (itemStrList != null){
            itemStrList.add(itemName);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ItemView itemView = new ItemView(this.context);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final String itemName = this.itemStrList.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (actionMode != null) {
                    return false;
                }
                actionMode = listActivity.startSupportActionMode(actionCallBack);
                selected_position = holder.getAdapterPosition();
                return true;
            }
        });
        holder.bind(itemName);
    }

    @Override
    public int getItemCount() {
        if (itemStrList == null)
            return 0;
        else
            return itemStrList.size();
    }
}
