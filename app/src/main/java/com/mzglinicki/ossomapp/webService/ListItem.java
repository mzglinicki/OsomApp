package com.mzglinicki.ossomapp.webService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Marcin on 16.01.2017.
 */

public class ListItem {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("speaker_name")
    @Expose
    private String speakerName;
    @SerializedName("speaker_photo")
    @Expose
    private String speakerPhoto;
    @SerializedName("score")
    @Expose
    private int score;

    public int getId() {
        return id;
    }

    public ListItem(final int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public int getScore() {
        return score;
    }

    public String getSpeakerPhoto() {
        return speakerPhoto;
    }
}