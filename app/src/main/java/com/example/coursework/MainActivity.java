package com.example.coursework;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add_button, delete_all_button;
    RecyclerView recyclerView;
    MyDatabaseHelper myDb;
    ArrayList<String> demo_id, demo_name, demo_doh, demo_location, demo_parking, demo_length, demo_toh, demo_participants, demo_difficulty, demo_description;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_button = findViewById(R.id.add_button);
        delete_all_button = findViewById(R.id.delete_all_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);

                startActivity(intent);
            }
        });

        delete_all_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        myDb = new MyDatabaseHelper(MainActivity.this);
        demo_id = new ArrayList<>();
        demo_name = new ArrayList<>();
        demo_location = new ArrayList<>();
        demo_doh = new ArrayList<>();
        demo_parking = new ArrayList<>();
        demo_length = new ArrayList<>();
        demo_difficulty = new ArrayList<>();
        demo_toh = new ArrayList<>();
        demo_participants = new ArrayList<>();
        demo_description = new ArrayList<>();

        StoreDataInArrays();

        customAdapter = new CustomAdapter(MainActivity.this, demo_id, demo_name, demo_location, demo_doh, demo_parking, demo_length, demo_difficulty, demo_toh, demo_participants, demo_description);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    public void StoreDataInArrays(){
        Cursor cursor = myDb.readAllData();
        if(cursor.getCount() == 0){
            // If there is no data, then display a toast message to the user.
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                demo_id.add(cursor.getString(0));
                demo_name.add(cursor.getString(1));
                demo_location.add(cursor.getString(2));
                demo_doh.add(cursor.getString(3));
                demo_parking.add(cursor.getString(4));
                demo_length.add(cursor.getString(5));
                demo_difficulty.add(cursor.getString(6));
                demo_toh.add(cursor.getString(7));
                demo_participants.add(cursor.getString(8));
                demo_description.add(cursor.getString(9));
            }
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all ?");
        builder.setMessage("Are you sure you want to delete all ?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            MyDatabaseHelper myDb = new MyDatabaseHelper(MainActivity.this);
            myDb.deleteAllData();
            //Refresh Activity
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
        });
        builder.create().show();
    }
}