package com.example.mydiary;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydiary.model.NoteItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.example.mydiary.NoteListActivity.mNoteList;

public class NoteDetailsActivity extends AppCompatActivity {

    private static final String TAG = NoteDetailsActivity.class.getName();

    private NoteItem mNoteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        Intent i = getIntent();
        int notePosition = i.getIntExtra("note_position", -1);

        if (notePosition == -1) {
            Toast.makeText(
                    this,
                    "Position in Intent not found.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        mNoteItem = mNoteList.get(notePosition);

        TextView noteDetailsTextView = findViewById(R.id.note_details_text_view);
        ImageView notePictureImageView = findViewById(R.id.note_picture_image_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mNoteItem.title);
        }
        noteDetailsTextView.setText(mNoteItem.details);

        if (mNoteItem.picture != null) {
            AssetManager am = getAssets();
            try {
                InputStream stream = am.open(mNoteItem.picture);
                Drawable drawable = Drawable.createFromStream(stream, null);
                notePictureImageView.setImageDrawable(drawable);
            } catch (IOException e) {
                e.printStackTrace();

                File pictureFile = new File(getFilesDir(), mNoteItem.picture);
                Drawable drawable = Drawable.createFromPath(pictureFile.getAbsolutePath());
                notePictureImageView.setImageDrawable(drawable);

                //Log.e(TAG, "Error opening picture file: " + mNoteItem.picture);
            }
        }
    }
}
