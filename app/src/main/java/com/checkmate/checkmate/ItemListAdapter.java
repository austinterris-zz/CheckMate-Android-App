package com.checkmate.checkmate;

import android.content.Context;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Brady on 10/23/2016.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private Context context;
    private CartActivity listActivity;
    private List<Item> itemList;
    private int selected_position;
    private ActionMode.Callback actionCallBack;
    private ActionMode actionMode;


    public ItemListAdapter(final Context context, List<Item> itemList, final CartActivity listActivity) {
        this.context = context;
        this.itemList = itemList;
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

        if (itemList.size() > 0){
            itemList.remove(pos);
            this.notifyItemRemoved(pos);
        }
    }

    public void addItem(Item item){
        if (itemList != null){
            itemList.add(item);
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
        final Item item = this.itemList.get(position);
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
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        if (itemList == null)
            return 0;
        else
            return itemList.size();
    }
}
