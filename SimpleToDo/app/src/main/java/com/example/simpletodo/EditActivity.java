package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editTextItem;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTextItem = findViewById(R.id.editTextItem);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit item");

        editTextItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // after editing
        btnSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // create an intent which will contain the new text and
                Intent intent = new Intent();

                //Past the new inputed data into the intent
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, editTextItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION,getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                //set the new data of intent
                setResult(RESULT_OK, intent);

                //finish activity : return to the main list
                finish();


            }
        });

    }
}