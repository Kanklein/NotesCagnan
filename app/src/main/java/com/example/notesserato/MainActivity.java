package com.example.notesserato;

import static com.example.notesserato.Note.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditNoteDialogFragment.EditNoteDialogListener {
    ArrayList<Note> notes;
    NotesAdapter notes_adapter;
    NotesOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new NotesOpenHelper(this, NotesOpenHelper.DATABASE_NAME,
                null, NotesOpenHelper.DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(NotesOpenHelper.DATABASE_TABLE, null,null,null,
                null,null,null);

        setListAdapterMethod();
        btnAddListenerMethod();
        etNoteEnterListenerMethod();

        int INDEX_NOTE = cursor.getColumnIndexOrThrow(KEY_NOTE_COLUMN);
        int INDEX_ID = cursor.getColumnIndexOrThrow(KEY_ID);
        while (cursor.moveToNext()){
            String note = cursor.getString(INDEX_NOTE);
            int id = cursor.getInt(INDEX_ID);
            Note n = new Note(note);
            n.id = id;
            notes.add(n);
        }
    }

    public void addNoteMethod(){
        EditText etNote = findViewById(R.id.etNote);
        String note = etNote.getText().toString();
        etNote.setText("");

        ContentValues cv = new ContentValues();
        cv.put(KEY_NOTE_COLUMN, note);

        SQLiteDatabase db = helper.getWritableDatabase();
        int id = (int) db.insert(NotesOpenHelper.DATABASE_TABLE,null, cv);

        Note n = new Note((note));
        n.id = id;
        notes.add(new Note(note));
        notes_adapter.notifyDataSetChanged();
    }

    private void etNoteEnterListenerMethod() {
        EditText etNote = findViewById(R.id.etNote);
        etNote.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN ||
                        keyEvent.getAction() == KeyEvent.KEYCODE_ENTER ||
                        keyEvent.getAction() == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                        addNoteMethod();
                    return true;
                }
                return false;
            }
        });
    }

    private void btnAddListenerMethod() {
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNoteMethod();
            }
        });
    }

    private void setListAdapterMethod() {
        ListView lvList = findViewById(R.id.lvList);
        notes = new ArrayList<>();
        notes_adapter = new NotesAdapter(getBaseContext(), R.layout.note_layout, notes, getSupportFragmentManager(), helper);
        lvList.setAdapter(notes_adapter);
    }

    @Override
    public void onEditListenerMethod(DialogFragment dialog) {
        notes_adapter.onEditListenerMethod(dialog);
    }

    @Override
    public void onCancelListenerMethod(DialogFragment dialog) {
        notes_adapter.onCancelListenerMethod(dialog);
    }
}