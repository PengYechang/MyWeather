package com.myweather.user.myweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 2016/8/26.
 */
public class WeatherOpenHelper extends SQLiteOpenHelper{

    //create City table
    public static final String CREATE_CITY = "create table City (" +
            "id integer primary key autoincrement," +
            "prov text," +
            "city_name text," +
            "city_code text)";

    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                             int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
