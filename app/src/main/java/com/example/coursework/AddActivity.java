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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalDate;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView dohControl, tohControl;
    RadioGroup radioGroup;
    RadioButton yes_button, no_button;
    Button confirm_button;
    EditText name_input, location_input, length_input, description_input, participants_input;
    String name, location, doh, length, description, toh, participants;
    Spinner sp;
    String difficulty_level, parking_available;

    // DatePicker Fragment inside MainActivity
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
            ((AddActivity) requireActivity()).updateDOH(doh);
        }
    }

    public void updateDOH(LocalDate doh){
        dohControl = findViewById(R.id.doh_control);
        dohControl.setText(doh.toString());
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
            ((AddActivity) requireActivity()).updateTOH(toh);
        }
    }

    public void updateTOH(String toh) {
        tohControl = findViewById(R.id.toh_control);
        tohControl.setText(toh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sp = findViewById(R.id.spinner);

        dohControl = findViewById(R.id.doh_control);
        tohControl = findViewById(R.id.toh_control);

        dohControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        tohControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        name_input = findViewById(R.id.name_input);
        location_input = findViewById(R.id.location_input);
        length_input = findViewById(R.id.length_input);
        description_input = findViewById(R.id.description_input);
        participants_input = findViewById(R.id.participants_input);

        radioGroup = findViewById(R.id.radio_group);
        yes_button = findViewById(R.id.yes_button);
        no_button = findViewById(R.id.no_button);

        confirm_button = findViewById(R.id.add_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper db = new MyDatabaseHelper(AddActivity.this);

                name = name_input.getText().toString();
                location = location_input.getText().toString();
                doh = dohControl.getText().toString();

                if (yes_button.isChecked()){
                    parking_available = "Yes";
                }else if (no_button.isChecked()){
                    parking_available = "No";
                }

                length = length_input.getText().toString();
                toh = tohControl.getText().toString();
                participants = participants_input.getText().toString();
                difficulty_level = sp.getSelectedItem().toString();
                description = description_input.getText().toString();

                if (name.equals("") || location.equals("") || doh.equals("") || length.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Error");
                    builder.setMessage("Please fill in all require fields.");
                    builder.setNeutralButton("Back", null);
                    builder.show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
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
                            db.addHike(name, location, doh, parking_available, length, toh, participants, difficulty_level, description);
                            Intent intent = new Intent(AddActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }
        });
    }
}