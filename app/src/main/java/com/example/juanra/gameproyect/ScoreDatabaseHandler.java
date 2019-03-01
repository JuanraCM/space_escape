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
    private static final String DATABASE_NAME = "ScorePlayerDB.db";
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
            + "score INTEGER )";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

    public List<PlayerInfo> allPlayers() {
        List<PlayerInfo> players = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_SCORE + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        PlayerInfo player;

        if (cursor.moveToFirst()) {
            do {

                player = new PlayerInfo();
                player.setAlias(cursor.getString(1));
                player.setHiscore(Integer.parseInt(cursor.getString(2)));

                players.add(player);

            } while (cursor.moveToNext());
        }
        return players;
    }

    public boolean exists(String alias) {
        List<PlayerInfo> players = allPlayers();
        for (PlayerInfo player : players)
            if (player.getAlias().equals(alias))
                return true;
        return false;
    }

    public PlayerInfo getPlayerInfo(String alias) {
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

        PlayerInfo result = new PlayerInfo(cursor.getString(0), Integer.parseInt(cursor.getString(1)));

        return result;
    }

    public void addPlayer(String alias, String score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ALIAS, alias);
        values.put(KEY_SCORE, score);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int updatePlayer(PlayerInfo player) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ALIAS, player.getAlias());
        values.put(KEY_SCORE, player.getHiscore());

        int i = db.update(TABLE_NAME,
                values,
                "alias = ?",
                 new String[] { player.getAlias() });

        db.close();

        return i;
    }
}
