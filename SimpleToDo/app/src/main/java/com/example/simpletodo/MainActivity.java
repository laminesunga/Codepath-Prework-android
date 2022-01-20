package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity<MaintActivity> extends AppCompatActivity {


    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;


    List<String> items;

    Button btnAdd;
    EditText editText;
    RecyclerView recycleViewItem;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        editText = findViewById(R.id.editText);
        recycleViewItem = findViewById(R.id.recycleViewItem);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
                // delete the item from the model
                items.remove(position);

                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener(){
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position" + position);
                //create the new activity : use attend intend to open camera, url read more about
                Intent i = new Intent( MainActivity.this, EditActivity.class ); // MainActivity refer to the current instance of the class, edit refer to the class to edit
                //pass the data being edited
                //
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);

            }
        };

        itemsAdapter = new ItemsAdapter(items,onLongClickListener,onClickListener);
        recycleViewItem.setAdapter(itemsAdapter);
        recycleViewItem.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String todoItem = editText.getText().toString();
                //add item to the model( add the new element to list items
                items.add(todoItem);
                // Notify adapter that an item is inserted to reload the page
                itemsAdapter.notifyItemInserted(items.size()-1);
                editText.setText("");
                Toast.makeText(getApplicationContext(),"Item was added", Toast.LENGTH_SHORT).show();
                saveItems();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // retrieve the update text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //get the initial position of the edited item
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // update the item at that position

            items.set(position, itemText);

            //notify the adapter of the change
            itemsAdapter.notifyItemChanged(position);
            // save the change
            saveItems();
            Toast.makeText(getApplicationContext(), "Item Updated Successfully", Toast.LENGTH_SHORT).show();

        } else {
            Log.w("MainActivity", "Unkown call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File (getFilesDir(), "data.txt");

    }

    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch (IOException e){
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();

        }
    }


    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        }catch (IOException e) {
            Log.e( "MainActivity", "Error writing items", e);

        }
    }
}