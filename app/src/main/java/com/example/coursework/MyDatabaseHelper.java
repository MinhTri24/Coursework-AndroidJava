package com.example.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "Hiking.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "hikes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_DOH = "doh";
    private static final String COLUMN_PARKING = "parking";
    private static final String COLUMN_LENGTH = "length";
    private static final String COLUMN_TOH = "toh";
    private static final String COLUMN_PARTICIPANTS = "participants";
    private static final String COLUMN_DIFFICULTY = "difficulty";
    private static final String COLUMN_DESCRIPTION = "description";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_DOH + " TEXT, " +
                COLUMN_PARKING + " TEXT, " +
                COLUMN_LENGTH + " TEXT, " +
                COLUMN_TOH + " TEXT, " +
                COLUMN_PARTICIPANTS + " TEXT, " +
                COLUMN_DIFFICULTY + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addHike(String name, String location, String doh, String parking, String length, String toh, String participants, String difficulty, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_LOCATION, location);
        cv.put(COLUMN_DOH, doh);
        cv.put(COLUMN_PARKING, parking);
        cv.put(COLUMN_LENGTH, length);
        cv.put(COLUMN_TOH, toh);
        cv.put(COLUMN_PARTICIPANTS, participants);
        cv.put(COLUMN_DIFFICULTY, difficulty);
        cv.put(COLUMN_DESCRIPTION, description);

        long result = db.insert(TABLE_NAME, null, cv);

        if(result == -1){
            new AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage("Error adding new hike")
                    .setPositiveButton("Back", null)
                    .show();
        }else{
            new AlertDialog.Builder(context)
                    .setTitle("Success")
                    .setMessage("New hike saved")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "id=?", new String[]{row_id});

        if(result == -1){
            new AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage("Error deleting hike")
                    .setPositiveButton("Back", null)
                    .show();
        }else{
            new AlertDialog.Builder(context)
                    .setTitle("Success")
                    .setMessage("Hike deleted")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    public void updateData(String row_id, String name, String location, String doh, String parking_available, String length, String toh, String participants, String difficulty_level, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_LOCATION, location);
        cv.put(COLUMN_DOH, doh);
        cv.put(COLUMN_PARKING, parking_available);
        cv.put(COLUMN_LENGTH, length);
        cv.put(COLUMN_TOH, toh);
        cv.put(COLUMN_PARTICIPANTS, participants);
        cv.put(COLUMN_DIFFICULTY, difficulty_level);
        cv.put(COLUMN_DESCRIPTION, description);

        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
