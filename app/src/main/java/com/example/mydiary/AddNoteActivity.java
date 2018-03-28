package com.example.mydiary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mydiary.db.DatabaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class AddNoteActivity extends AppCompatActivity {

    private static final String TAG = AddNoteActivity.class.getName();

    private ImageView mAddPictureImageView;
    private File mSelectedPictureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mAddPictureImageView = findViewById(R.id.add_picture_image_view);
        mAddPictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(
                        AddNoteActivity.this,
                        "Take a photo or choose one from a gallery.",
                        0
                );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                if (imageFiles.size() > 0) {
                    mSelectedPictureFile = imageFiles.get(0);
                    Drawable drawable = Drawable.createFromPath(mSelectedPictureFile.getAbsolutePath());
                    mAddPictureImageView.setImageDrawable(drawable);
                    Log.i(TAG, "SRC: " + mSelectedPictureFile.getAbsolutePath());
                }
            }

            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                new AlertDialog.Builder(AddNoteActivity.this)
                        .setMessage("An error occurred while choosing a picture.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save_note) {
            if (mSelectedPictureFile != null) {
                File dstFile = new File(
                        getApplicationContext().getFilesDir(),
                        mSelectedPictureFile.getName()
                );
                Log.i(TAG, "DEST: " + dstFile.getAbsolutePath());

                try {
                    copyFile(mSelectedPictureFile, dstFile);
                    saveNote();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "An error occurred while copying a picture file.");
                }
            } else {
                saveNote();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        EditText noteTitleEditText = findViewById(R.id.note_title_edit_text);
        EditText noteDetailsEditText = findViewById(R.id.note_details_edit_text);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_TITLE, noteTitleEditText.getText().toString());
        cv.put(DatabaseHelper.COL_DETAILS, noteDetailsEditText.getText().toString());
        if (mSelectedPictureFile != null) {
            cv.put(DatabaseHelper.COL_PICTURE, mSelectedPictureFile.getName());
        }
        long insertResult = db.insert(
                DatabaseHelper.TABLE_NOTEBOOK,
                null,
                cv
        );
        if (insertResult != -1) {
            Toast.makeText(
                    this,
                    "Note added successfully.",
                    Toast.LENGTH_LONG
            ).show();
            finish();
        } else {
            Toast.makeText(
                    this,
                    "Error occurred while adding a note.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    public static void copyFile(File src, File dst) throws IOException {
        FileInputStream inputStream = new FileInputStream(src);
        FileOutputStream outputStream = new FileOutputStream(dst);
        byte[] buffer = new byte[1024];

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
    }
}
