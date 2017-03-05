package com.chenyu.fangle.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "frangle.db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_STORY_TABLE_SQL = "create table if not exists zhihu_story_table" +
            "(id integer primary key unique,title varchar(50),type int(2),images varchar(255)," +
            "multipic boolean,ga_prefix varchar(20),date varchar(20))";

    private static final String CREATE_TOP_STORY_TABLE_SQL = "create table if not exists zhihu_top_story_table"+
            "(id integer primary key unique,title varchar(50),type int(2),image varchar(255)," +
            "multipic boolean,ga_prefix varchar(20),date varchar(20))";

    private static final String CREATE_STORY_DETAIL_TABLE_SQL = "create table if not exists zhihu_detail_table " +
            "(id integer primary key unique,isFavorite int(1),type int(2),title varchar(50),body varchar(65525),image_source varchar(255)," +
            "image varchar(255),share_url varchar(255),ga_prefix varchar(20),js varchar(65525),images varchar(255),css varchar(65525))";

    public static final String STORY_TABLE = "zhihu_story_table";
    public static final String TOP_STORY_TABLE = "zhihu_top_story_table";
    public static final String DETAIL_TABLE = "zhihu_detail_table";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STORY_TABLE_SQL);
        db.execSQL(CREATE_TOP_STORY_TABLE_SQL);
        db.execSQL(CREATE_STORY_DETAIL_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
