package com.example.m_hike_app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mhike.db";
    private static final int DB_VERSION = 1;

    // hikes table
    private static final String TABLE_HIKES = "hikes";
    private static final String H_ID = "id";
    private static final String H_NAME = "name";
    private static final String H_LOCATION = "location";
    private static final String H_DATE = "date";
    private static final String H_PARKING = "parking";
    private static final String H_LENGTH = "length";
    private static final String H_DIFFICULTY = "difficulty";
    private static final String H_DESCRIPTION = "description";

    // observations table
    private static final String TABLE_OBS = "observations";
    private static final String O_ID = "id";
    private static final String O_HIKE_ID = "hike_id";
    private static final String O_TEXT = "text";
    private static final String O_TIME = "timestamp";
    private static final String O_COMMENT = "comment";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createHikes = "CREATE TABLE " + TABLE_HIKES + " (" +
                H_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                H_NAME + " TEXT NOT NULL, " +
                H_LOCATION + " TEXT NOT NULL, " +
                H_DATE + " TEXT NOT NULL, " +
                H_PARKING + " TEXT NOT NULL, " +
                H_LENGTH + " REAL NOT NULL, " +
                H_DIFFICULTY + " TEXT NOT NULL, " +
                H_DESCRIPTION + " TEXT)";
        db.execSQL(createHikes);

        String createObs = "CREATE TABLE " + TABLE_OBS + " (" +
                O_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                O_HIKE_ID + " INTEGER, " +
                O_TEXT + " TEXT NOT NULL, " +
                O_TIME + " TEXT NOT NULL, " +
                O_COMMENT + " TEXT, " +
                "FOREIGN KEY(" + O_HIKE_ID + ") REFERENCES " + TABLE_HIKES + "(" + H_ID + ") ON DELETE CASCADE)";
        db.execSQL(createObs);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES);
        onCreate(db);
    }

    // CRUD for hikes
    public long insertHike(String name, String location, String date, String parking, double length, String difficulty, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(H_NAME, name);
        cv.put(H_LOCATION, location);
        cv.put(H_DATE, date);
        cv.put(H_PARKING, parking);
        cv.put(H_LENGTH, length);
        cv.put(H_DIFFICULTY, difficulty);
        cv.put(H_DESCRIPTION, description);
        long id = db.insert(TABLE_HIKES, null, cv);
        db.close();
        return id;
    }

    public List<Hike> getAllHikes() {
        List<Hike> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_HIKES + " ORDER BY " + H_ID + " DESC", null);
        if (c.moveToFirst()) {
            do {
                Hike h = new Hike();
                h.setId(c.getInt(c.getColumnIndexOrThrow(H_ID)));
                h.setName(c.getString(c.getColumnIndexOrThrow(H_NAME)));
                h.setLocation(c.getString(c.getColumnIndexOrThrow(H_LOCATION)));
                h.setDate(c.getString(c.getColumnIndexOrThrow(H_DATE)));
                h.setParking(c.getString(c.getColumnIndexOrThrow(H_PARKING)));
                h.setLength(c.getDouble(c.getColumnIndexOrThrow(H_LENGTH)));
                h.setDifficulty(c.getString(c.getColumnIndexOrThrow(H_DIFFICULTY)));
                h.setDescription(c.getString(c.getColumnIndexOrThrow(H_DESCRIPTION)));
                list.add(h);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public Hike getHikeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_HIKES + " WHERE " + H_ID + " = ?", new String[]{String.valueOf(id)});
        Hike h = null;
        if (c.moveToFirst()) {
            h = new Hike();
            h.setId(c.getInt(c.getColumnIndexOrThrow(H_ID)));
            h.setName(c.getString(c.getColumnIndexOrThrow(H_NAME)));
            h.setLocation(c.getString(c.getColumnIndexOrThrow(H_LOCATION)));
            h.setDate(c.getString(c.getColumnIndexOrThrow(H_DATE)));
            h.setParking(c.getString(c.getColumnIndexOrThrow(H_PARKING)));
            h.setLength(c.getDouble(c.getColumnIndexOrThrow(H_LENGTH)));
            h.setDifficulty(c.getString(c.getColumnIndexOrThrow(H_DIFFICULTY)));
            h.setDescription(c.getString(c.getColumnIndexOrThrow(H_DESCRIPTION)));
        }
        c.close();
        db.close();
        return h;
    }

    public int updateHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(H_NAME, hike.getName());
        cv.put(H_LOCATION, hike.getLocation());
        cv.put(H_DATE, hike.getDate());
        cv.put(H_PARKING, hike.getParking());
        cv.put(H_LENGTH, hike.getLength());
        cv.put(H_DIFFICULTY, hike.getDifficulty());
        cv.put(H_DESCRIPTION, hike.getDescription());
        int rows = db.update(TABLE_HIKES, cv, H_ID + " = ?", new String[]{String.valueOf(hike.getId())});
        db.close();
        return rows;
    }

    public int deleteHike(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_HIKES, H_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public void deleteAllHikes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIKES, null, null);
        db.close();
    }

    // CRUD for observations
    public long insertObservation(int hikeId, String text, String time, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(O_HIKE_ID, hikeId);
        cv.put(O_TEXT, text);
        cv.put(O_TIME, time);
        cv.put(O_COMMENT, comment);
        long id = db.insert(TABLE_OBS, null, cv);
        db.close();
        return id;
    }

    public List<Observation> getObservationsForHike(int hikeId) {
        List<Observation> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_OBS + " WHERE " + O_HIKE_ID + " = ? ORDER BY " + O_ID + " DESC", new String[]{String.valueOf(hikeId)});
        if (c.moveToFirst()) {
            do {
                Observation o = new Observation();
                o.setId(c.getInt(c.getColumnIndexOrThrow(O_ID)));
                o.setHikeId(c.getInt(c.getColumnIndexOrThrow(O_HIKE_ID)));
                o.setText(c.getString(c.getColumnIndexOrThrow(O_TEXT)));
                o.setTimestamp(c.getString(c.getColumnIndexOrThrow(O_TIME)));
                o.setComment(c.getString(c.getColumnIndexOrThrow(O_COMMENT)));
                list.add(o);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public Observation getObservationById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_OBS + " WHERE " + O_ID + " = ?", new String[]{String.valueOf(id)});
        Observation o = null;
        if (c.moveToFirst()) {
            o = new Observation();
            o.setId(c.getInt(c.getColumnIndexOrThrow(O_ID)));
            o.setHikeId(c.getInt(c.getColumnIndexOrThrow(O_HIKE_ID)));
            o.setText(c.getString(c.getColumnIndexOrThrow(O_TEXT)));
            o.setTimestamp(c.getString(c.getColumnIndexOrThrow(O_TIME)));
            o.setComment(c.getString(c.getColumnIndexOrThrow(O_COMMENT)));
        }
        c.close();
        db.close();
        return o;
    }

    public int updateObservation(Observation obs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(O_TEXT, obs.getText());
        values.put(O_TIME, obs.getTimestamp());
        values.put(O_COMMENT, obs.getComment());
        int rows = db.update(TABLE_OBS, values, O_ID + " = ?", new String[]{String.valueOf(obs.getId())});
        db.close();
        return rows;
    }

    public int deleteObservation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_OBS, O_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }
    public List<Hike> searchHikes(String keyword) {
        List<Hike> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_HIKES +
                " WHERE " + H_NAME + " LIKE ? OR " +
                H_LOCATION + " LIKE ? OR " +
                H_DIFFICULTY + " LIKE ?" +
                " ORDER BY " + H_ID + " DESC";

        String like = "%" + keyword + "%";

        Cursor c = db.rawQuery(query, new String[]{like, like, like});

        if (c.moveToFirst()) {
            do {
                Hike h = new Hike();
                h.setId(c.getInt(c.getColumnIndexOrThrow(H_ID)));
                h.setName(c.getString(c.getColumnIndexOrThrow(H_NAME)));
                h.setLocation(c.getString(c.getColumnIndexOrThrow(H_LOCATION)));
                h.setDate(c.getString(c.getColumnIndexOrThrow(H_DATE)));
                h.setParking(c.getString(c.getColumnIndexOrThrow(H_PARKING)));
                h.setLength(c.getDouble(c.getColumnIndexOrThrow(H_LENGTH)));
                h.setDifficulty(c.getString(c.getColumnIndexOrThrow(H_DIFFICULTY)));
                h.setDescription(c.getString(c.getColumnIndexOrThrow(H_DESCRIPTION)));
                list.add(h);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

}
