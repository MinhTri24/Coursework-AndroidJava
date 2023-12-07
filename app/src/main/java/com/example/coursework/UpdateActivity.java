package com.example.coursework;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Objects;

public class UpdateActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView dohControl, tohControl;
    EditText name_input, location_input, length_input, description_input, participants_input;
    String id, name, location, doh, length, description, toh, participants;
    RadioGroup radioGroup;
    RadioButton yes_button, no_button;
    Button confirm_button, delete_button;
    Spinner sp;
    String difficulty_level, parking_available;

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
            LocalDate d = LocalDate.now();
            int year = d.getYear();
            int month = d.getMonthValue();
            int day = d.getDayOfMonth();
            return new DatePickerDialog(requireActivity(), this, year, --month, day);}
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day){
            LocalDate doh = LocalDate.of(year, ++month, day);
            ((UpdateActivity) requireActivity()).updateDOH(doh);
        }
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
            return new TimePickerDialog(getActivity(), this, 0, 0, true);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String toh = hourOfDay + ":" + minute;
            ((UpdateActivity) requireActivity()).updateTOH(toh);
        }
    }

    public void updateDOH(LocalDate doh){
        dohControl = findViewById(R.id.doh_control);
        dohControl.setText(doh.toString());
    }

    public void updateTOH(String toh){
        tohControl = findViewById(R.id.toh_control);
        tohControl.setText(toh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sp = findViewById(R.id.spinner);
        radioGroup = findViewById(R.id.radio_group);
        yes_button = findViewById(R.id.yes_button);
        no_button = findViewById(R.id.no_button);

        name_input = findViewById(R.id.name_input);
        location_input = findViewById(R.id.location_input);
        dohControl = findViewById(R.id.doh_control);
        dohControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker"); }
        });
        length_input = findViewById(R.id.length_input);
        tohControl = findViewById(R.id.toh_control);
        tohControl.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DialogFragment timePicker = new TimePickerFragment();
               timePicker.show(getSupportFragmentManager(), "time picker"); }
        });
        participants_input = findViewById(R.id.participants_input);
        description_input = findViewById(R.id.description_input);

        getAndSetIntentData();

        confirm_button = findViewById(R.id.update_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                name = name_input.getText().toString().trim();
                location = location_input.getText().toString().trim();
                doh = dohControl.getText().toString().trim();
                if (yes_button.isChecked()){
                    parking_available = "Yes";
                }else if (no_button.isChecked()){
                    parking_available = "No";
                }
                length = length_input.getText().toString().trim();
                toh = tohControl.getText().toString().trim();
                participants = participants_input.getText().toString().trim();
                difficulty_level = sp.getSelectedItem().toString();
                description = description_input.getText().toString().trim();

                if (name.equals("") || location.equals("") || doh.equals("") || length.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Error");
                    builder.setMessage("Please fill in all require fields.");
                    builder.setNeutralButton("Back", null);
                    builder.show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Name: " + name + "\n" + "Location: " + location + "\n" + "Date of The Hike: " + doh + "\n" +
                            "Parking Available: " + parking_available + "\n" + "Length (km): " + length + "\n" +
                            "Hike Duration: " + toh + "\n" + "Participants: " + participants + "\n" +
                            "Difficulty Level: " + difficulty_level + "\n" + "Description: " + description);
                    builder.setNeutralButton("Back", null);
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myDB.updateData(id, name, location, doh, parking_available, length, toh, participants, difficulty_level, description);
                            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }
        });

        delete_button = findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("location") && getIntent().hasExtra("doh") &&
                getIntent().hasExtra("length") && getIntent().hasExtra("description")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            location = getIntent().getStringExtra("location");
            doh = getIntent().getStringExtra("doh");
            length = getIntent().getStringExtra("length");
            description = getIntent().getStringExtra("description");

            //Setting Intent Data
            name_input.setText(name);
            location_input.setText(location);
            dohControl.setText(doh);
            length_input.setText(length);
            description_input.setText(description);
        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete " + name + " ?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
            myDB.deleteOneRow(id);
            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
            startActivity(intent);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
        });
        builder.create().show();
    }
}