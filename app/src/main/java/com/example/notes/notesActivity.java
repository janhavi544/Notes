package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class notesActivity extends AppCompatActivity {
    EditText editText;
    EditText editTextMulti;
    public void preference()
    {
        SharedPreferences sharedPreferences=notesActivity.this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        try {
            sharedPreferences.edit().putString("notesHeading",ObjectSerializer.serialize(MainActivity.noteHeading)).apply();
            sharedPreferences.edit().putString("notes",ObjectSerializer.serialize(MainActivity.note)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Intent intent1=getIntent();
        final int position=intent1.getIntExtra("positionNote",0);
        // Toast.makeText(this, Integer.toString(position), Toast.LENGTH_SHORT).show();
        editText=findViewById(R.id.editText);
        editTextMulti=findViewById(R.id.editTextMultiLine);
        if(MainActivity.noteHeading.get(position).length()!=0)
        editText.setText(MainActivity.noteHeading.get(position));
        if(MainActivity.note.get(position).length()!=0)
            editTextMulti.setText(MainActivity.note.get(position));
        final String note1=editText.getText().toString();
        final String noteMulti1=editTextMulti.getText().toString();

        //Toast.makeText(this, note1, Toast.LENGTH_SHORT).show();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.noteHeading.set(position,String.valueOf(s));
                MainActivity.arrayAdapter.notifyDataSetChanged();
                preference();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextMulti.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.note.set(position,String.valueOf(s));
                preference();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}