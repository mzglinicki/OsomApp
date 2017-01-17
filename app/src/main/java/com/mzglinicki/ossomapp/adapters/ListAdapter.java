package com.mzglinicki.ossomapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mzglinicki.ossomapp.webService.ListItem;
import com.mzglinicki.ossomapp.R;

import java.util.List;

import static android.view.View.*;

/**
 * Created by User on 2017-01-16.
 */

public class ListAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

    private final List<ListItem> listForAdapter;
    private final ListClickable clickListener;

    public ListAdapter(final List<ListItem> listForAdapter, final ListClickable clickListener) {

        this.listForAdapter = listForAdapter;
        this.clickListener = clickListener;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_model, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final int position) {

        final ListItem model = listForAdapter.get(position);
        holder.getNameField().setText(model.getName());

        setupItemClickListener(holder, model);
    }

    private void setupItemClickListener(final ListItemViewHolder holder, final ListItem model) {
        if (clickListener == null) {
            return;
        }
        holder.getListItem().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                clickListener.onItemClickListener(holder, model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listForAdapter.size();
    }

    public interface ListClickable {
        void onItemClickListener(final ListItemViewHolder holder, final ListItem model);
    }
}