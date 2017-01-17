package com.mzglinicki.ossomapp.webService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 2017-01-16.
 */

public class ServerData {

    @SerializedName("data")
    @Expose
    private ListItem[] data;

    public ListItem[] getData() {
        return data;
    }
}