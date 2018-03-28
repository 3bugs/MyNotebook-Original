package com.example.mydiary.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydiary.R;
import com.example.mydiary.db.DatabaseHelper;
import com.example.mydiary.model.NoteItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Promlert on 2018-03-21.
 */

public class NoteListAdapter extends ArrayAdapter<NoteItem> {

    private static final String TAG = NoteListAdapter.class.getName();

    private Context mContext;
    private int mLayoutResId;
    private ArrayList<NoteItem> mNoteList;
    private OnDeleteNoteListener mCallback;

    public interface OnDeleteNoteListener {
        void onNoteDeleted(long noteId);
    }

    public NoteListAdapter(@NonNull Context context, int resource,
                           @NonNull ArrayList<NoteItem> noteList, OnDeleteNoteListener callback) {
        super(context, resource, noteList);

        this.mContext = context;
        this.mLayoutResId = resource;
        this.mNoteList = noteList;
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View v = convertView;
        if (v == null) {
            v = inflater.inflate(mLayoutResId, null);
        }

        ImageView notePictureImageView = v.findViewById(R.id.note_picture_image_view);
        TextView noteTitleTextView = v.findViewById(R.id.note_title_text_view);
        final ImageView overflowImageView = v.findViewById(R.id.overflow_image_view);

        final NoteItem noteItem = mNoteList.get(position);
        noteTitleTextView.setText(noteItem.title);
        if (noteItem.picture != null) {
            AssetManager am = mContext.getAssets();
            try {
                InputStream stream = am.open(noteItem.picture);
                Drawable drawable = Drawable.createFromStream(stream, null);
                notePictureImageView.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();

                File pictureFile = new File(mContext.getFilesDir(), noteItem.picture);
                Drawable drawable = Drawable.createFromPath(pictureFile.getAbsolutePath());
                notePictureImageView.setImageDrawable(drawable);

                //Log.e(TAG, "Error opening picture file: " + noteItem.picture);
            }
        }

        overflowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                Toast.makeText(
                        mContext,
                        noteItem.details,
                        Toast.LENGTH_LONG
                ).show();
*/
                PopupMenu popup = new PopupMenu(mContext, overflowImageView);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyMenuItemClickListener(noteItem.id));
                popup.show();
            }
        });

        return v;
    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private final long mNoteId;

        public MyMenuItemClickListener(long noteId) {
            mNoteId = noteId;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete_note:
                    DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    int deleteResult = db.delete(
                            DatabaseHelper.TABLE_NOTEBOOK,
                            DatabaseHelper.COL_ID + "=?",
                            new String[]{String.valueOf(mNoteId)}
                    );

                    if (deleteResult > 0) {
                        Toast.makeText(
                                mContext,
                                "Note deleted successfully.",
                                Toast.LENGTH_SHORT
                        ).show();

                        mCallback.onNoteDeleted(mNoteId);

                    } else {

                    }

                    return true;
                default:
            }
            return false;
        }
    }
}
