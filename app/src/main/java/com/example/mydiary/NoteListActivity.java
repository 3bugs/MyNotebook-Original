package com.example.mydiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mydiary.adapter.NoteListAdapter;
import com.example.mydiary.db.DatabaseHelper;
import com.example.mydiary.model.NoteItem;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {

    private ListView mNotebookListView;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    protected static final ArrayList<NoteItem> mNoteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        mNotebookListView = findViewById(R.id.notebook_list_view);

        mDbHelper = new DatabaseHelper(this);
        mDb = mDbHelper.getWritableDatabase();

        //loadDataFromDatabase();
/*
        ArrayAdapter adapter = new ArrayAdapter<>(
                this,
                R.layout.item_note,
                R.id.note_title_text_view,
                mNoteList
        );
*/

/*
        NoteListAdapter adapter = new NoteListAdapter(
                this,
                R.layout.item_note,
                mNoteList
        );
        mNotebookListView.setAdapter(adapter);
*/

        mNotebookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), NoteDetailsActivity.class);
                intent.putExtra("note_position", position);
                startActivity(intent);
            }
        });

        mNotebookListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final NoteItem noteItem = mNoteList.get(position);

                String msg = String.format("Delete '%s'?", noteItem.title);
                new AlertDialog.Builder(NoteListActivity.this)
                        .setMessage(msg)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int deleteResult = mDb.delete(
                                        DatabaseHelper.TABLE_NOTEBOOK,
                                        DatabaseHelper.COL_ID + "=?",
                                        new String[]{String.valueOf(noteItem.id)}
                                );
                                if (deleteResult > 0) {
                                    loadDataFromDatabase();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        Cursor cursor = mDb.query(
                DatabaseHelper.TABLE_NOTEBOOK,
                null,
                null,
                null,
                null,
                null,
                null
        );

        mNoteList.clear();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TITLE));
            String details = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DETAILS));
            String picture = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PICTURE));

            NoteItem item = new NoteItem(id, title, details, picture);
            mNoteList.add(item);
        }

        NoteListAdapter adapter = new NoteListAdapter(
                this,
                R.layout.item_note,
                mNoteList,
                new NoteListAdapter.OnDeleteNoteListener() {
                    @Override
                    public void onNoteDeleted(long noteId) {
                        loadDataFromDatabase();
                    }
                }
        );
        mNotebookListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_note) {
            Intent i = new Intent(this, AddNoteActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
