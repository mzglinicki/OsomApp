package com.mzglinicki.ossomapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.mzglinicki.ossomapp.webService.ListItem;

/**
 * Created by Marcin on 02.02.2017.
 */

public class ListItemDecorator implements Parcelable {

    private int id;
    private String name;

    public ListItemDecorator(final ListItem listItem) {
        this.id = listItem.getId();
        this.name = listItem.getName();
    }

    private ListItemDecorator(final Parcel input) {
        this.id = input.readInt();
        this.name = input.readString();
    }

    public static final Creator<ListItemDecorator> CREATOR = new Creator<ListItemDecorator>() {
        @Override
        public ListItemDecorator createFromParcel(Parcel in) {
            return new ListItemDecorator(in);
        }

        @Override
        public ListItemDecorator[] newArray(int size) {
            return new ListItemDecorator[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
    }
}