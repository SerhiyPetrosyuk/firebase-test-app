package com.mlsdev.serhii.shoplist.view.adapter;

import android.support.v7.widget.RecyclerView;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by serhii on 5/4/16.
 */
public abstract class BaseShoppingListAdapter<T extends ViewHolder> extends RecyclerView.Adapter<T> {
    protected List<DataSnapshot> dataSnapshots = new ArrayList<>();
    protected OnItemClickListener onItemClickListener;
    protected String parentKey;

    @Override
    public int getItemCount() {
        return dataSnapshots.size();
    }

    public void setData(List<DataSnapshot> data) {
        dataSnapshots = data;
        notifyDataSetChanged();
    }

    public void addItem(DataSnapshot item, String previousKey) {
        int position = 0;

        if (previousKey != null)
            position = getIndexForKey(previousKey) + 1;

        dataSnapshots.add(position, item);
        notifyItemInserted(dataSnapshots.size() - 1);
    }

    public void onItemChanged(DataSnapshot dataSnapshot) {
        int position = getIndexForKey(dataSnapshot.getKey());
        dataSnapshots.remove(position);
        dataSnapshots.add(position, dataSnapshot);
        notifyItemChanged(position);
    }

    public void onItemRemoved(String key) {
        int position = getIndexForKey(key);
        dataSnapshots.remove(position);
        notifyItemRemoved(position);
    }

    private int getIndexForKey(String key) {
        int index = 0;

        for (DataSnapshot dataSnapshot : dataSnapshots) {
            if (dataSnapshot.getKey().equals(key))
                return index;
            else
                index++;
        }

        throw new IllegalArgumentException("Key not found");
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClicked(String key);
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getParentKey() {
        return parentKey;
    }
}
