package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    static ArrayAdapter<String> arrayAdapter;
   static ArrayList<String> noteHeading=new ArrayList<String>();
    static ArrayList<String> note=new ArrayList<String>();
    public void preference1()
    {
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        try {
            sharedPreferences.edit().putString("notesHeading",ObjectSerializer.serialize(MainActivity.noteHeading)).apply();
            sharedPreferences.edit().putString("notes",ObjectSerializer.serialize(MainActivity.note)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void preference2()
    {
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        try{
            noteHeading=(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notesHeading",ObjectSerializer.serialize(new ArrayList<String>())));
            note=(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes",ObjectSerializer.serialize(new ArrayList<String>())));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(noteHeading.size()>0&&note.size()==noteHeading.size())
        {
            ;
        }
        else
        {
            noteHeading.add("Example note");
            note.add("");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_res,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case R.id.addNote: Intent intent=new Intent(getApplicationContext(),notesActivity.class);
                note.add("");
                noteHeading.add("");
                arrayAdapter.notifyDataSetChanged();
                intent.putExtra("positionNote",note.size()-1);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        noteHeading.clear();
        note.clear();
       preference2();
         arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,noteHeading);
         listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),notesActivity.class);
                intent.putExtra("positionNote",position);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_menu_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to permanently delete this note?")
                        .setPositiveButton("No",null)
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            note.remove(position);
                            noteHeading.remove(position);
                            arrayAdapter.notifyDataSetChanged();
                              preference1();
                            }
                        }).show();
                return true;
            }
        });
    }
}