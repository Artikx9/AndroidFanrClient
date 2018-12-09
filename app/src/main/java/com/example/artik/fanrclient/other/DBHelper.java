package com.example.artik.fanrclient.other;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.artik.fanrclient.Model.Recipe;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipe.db";
    private static final int SCHEMA = 1;
    public static final String TABLE = "recipe";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CONTENT = "contents";
    public static final String COLUMN_COOK = "cooking";
    public static final String COLUMN_VERSION = "version";
    public static final String COLUMN_DATE = "date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +TABLE+ "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY ," + COLUMN_NAME
                + " TEXT, " + COLUMN_CONTENT
                + " TEXT, "+COLUMN_COOK
                + " TEXT, "+COLUMN_VERSION
                +" INTEGER, "+COLUMN_DATE
                +" INTEGER);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ContentValues saveRecipe(Recipe recipe)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, recipe.getId());
        cv.put(COLUMN_NAME, recipe.getName());
        cv.put(COLUMN_CONTENT, recipe.getContents());
        cv.put(COLUMN_COOK, recipe.getCooking());
        cv.put(COLUMN_VERSION, recipe.getVersion());
        cv.put(COLUMN_DATE, recipe.getDate());

        return cv;
    }


}
