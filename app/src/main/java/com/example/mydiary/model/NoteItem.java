package com.example.mydiary.model;

/**
 * Created by Promlert on 2018-03-21.
 */

public class NoteItem {

    public final long id;
    public final String title;
    public final String details;
    public final String picture;

    public NoteItem(long id, String title, String details, String picture) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.picture = picture;
    }

    @Override
    public String toString() {
        return title;
    }
}
