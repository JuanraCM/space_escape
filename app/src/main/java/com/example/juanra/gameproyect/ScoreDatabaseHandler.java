package com.example.juanra.gameproyect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ScoreDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ScorePlayerDB";
    private static final String TABLE_NAME = "ScorePlayers";
    private static final String KEY_ID = "id";
    private static final String KEY_ALIAS = "alias";
    private static final String KEY_SCORE = "score";
    private static final String[] COLUMNS = { KEY_ID, KEY_SCORE };

    public ScoreDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "( "
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "alias TEXT, "
            + "score TEXT )";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public List<String[]> allPlayers() {
        List<String[]> players = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String[] player = null;

        if (cursor.moveToFirst()) {
            do {

                player = new String[2];
                player[0] = cursor.getString(1);
                player[1] = cursor.getString(2);

                players.add(player);

            } while (cursor.moveToNext());
        }
        return players;
    }

    public String[] getPlayerInfo(String alias) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                "alias = ?",
                new String[] { alias },
                null,
                null,
                null,
                null
        );

        if (cursor != null) cursor.moveToFirst();

        String[] result = new String[] { cursor.getString(1), cursor.getString(2) };

        return result;
    }

    public void addPlayer(String alias, String score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ALIAS, alias);
        values.put(KEY_SCORE, score);

        db.insert(TABLE_NAME, null, values);
    }
}
