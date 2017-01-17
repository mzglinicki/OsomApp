package com.mzglinicki.ossomapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mzglinicki.ossomapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by User on 2017-01-16.
 */

public class ListItemViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.listItem)
    protected RelativeLayout listItem;
    @Bind(R.id.name)
    protected TextView nameField;

    public ListItemViewHolder(final View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public RelativeLayout getListItem() {
        return listItem;
    }

    public TextView getNameField() {
        return nameField;
    }
}