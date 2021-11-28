package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.database.Cursor;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
     private ArrayList<String> items;
     private ArrayAdapter<String> itemsAdapter;
     private ListView lvItems;
     private DatabaseHelper db;
     private EditText textItem;
     private Button addItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textItem = findViewById(R.id.textItem);
        addItemButton = findViewById(R.id.addItemButton);
        db = new DatabaseHelper(this);

        lvItems = findViewById(R.id.lvitems);
        items= new ArrayList<String>();
        itemsAdapter= new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        // List View
        Cursor res = db.getData();
        if(res.getCount()==0){
            Toast.makeText(MainActivity.this, "No Item Exists", Toast.LENGTH_SHORT).show();
            return;
        }
        while(res.moveToNext()){
            items.add(res.getString(1));
        }

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String val =(String) adapterView.getItemAtPosition(position);
                Log.d("here7 ", val);

                Boolean checkIfDeleted = db.deleteData(val);
                if(checkIfDeleted==true) {
                    Toast.makeText(MainActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                    items.remove(position);
                    itemsAdapter.notifyDataSetChanged();
                }
                else {
                    Log.d("here8 ", val);
                        Toast.makeText(MainActivity.this, "Item Not Deleted", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoText = textItem.getText().toString();

                Boolean checkIfInserted = db.insertData(todoText);
                if(checkIfInserted==true) {
                    Toast.makeText(MainActivity.this, "New Item Inserted", Toast.LENGTH_SHORT).show();
                    itemsAdapter.add(todoText);
                    textItem.setText("");
                }
                else{
                    Toast.makeText(MainActivity.this, "New Item Not Inserted", Toast.LENGTH_SHORT).show();
                }
            }        });

    }
}