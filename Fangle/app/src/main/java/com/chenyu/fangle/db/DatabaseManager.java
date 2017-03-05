package com.chenyu.fangle.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    private static SQLiteDatabase mDatabase;
    public static DatabaseHelper mDatabaseHelper;

    /**
     * 初始化数据库
     */
    public static void init(Context context){
        if(mDatabase == null && context != null){
            synchronized (DatabaseManager.class){
                mDatabaseHelper = new DatabaseHelper(context.getApplicationContext());
                mDatabase = mDatabaseHelper.getWritableDatabase();
            }
        }
    }

    /**
     * 获取数据库的实例
     * @return
     */
    public static SQLiteDatabase getDatabase(){
        if(!mDatabase.isOpen()){
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

}
