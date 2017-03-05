package com.chenyu.fangle.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.chenyu.fangle.beans.ZhiHuDetail;
import com.chenyu.fangle.db.DatabaseHelper;
import com.chenyu.fangle.db.DatabaseManager;
import com.chenyu.fangle.network.IArticleAPI;
import com.chenyu.fangle.network.RetrofitManager;
import com.chenyu.fangle.presenter.ZhiHuDetailPresenter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZhiHuDetailModel {

    private String tableName;
    private ZhiHuDetailPresenter mPresenter;
    private IArticleAPI iArticleAPI;

    public ZhiHuDetailModel(ZhiHuDetailPresenter presenter){
        mPresenter = presenter;
        iArticleAPI = RetrofitManager.getZhiHuRetrofit().create(IArticleAPI.class);
    }

    public void loadDataFromNet(int id){
        Call<ZhiHuDetail> call = iArticleAPI.articleDetail(id);
        call.enqueue(new Callback<ZhiHuDetail>() {
            @Override
            public void onResponse(Call<ZhiHuDetail> call, Response<ZhiHuDetail> response) {
                ZhiHuDetail bean = response.body();
                mPresenter.obtainDetailsFinished(bean);
            }

            @Override
            public void onFailure(Call<ZhiHuDetail> call, Throwable t) {

            }
        });
    }

    public ZhiHuDetail loadDataFromDB(int id){
        Cursor cursor = DatabaseManager.getDatabase().query(DatabaseHelper.DETAIL_TABLE,null,"id = ?",new String[]{String.valueOf(id)},null,null,null);
        return praseResult(cursor);
    }

    public void saveData(ZhiHuDetail bean){
        if (bean != null){
            DatabaseManager.getDatabase().insertWithOnConflict(DatabaseHelper.DETAIL_TABLE,null,toContentValue(bean), SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public boolean favorSpecificArticle(int id){
        ContentValues values = new ContentValues();
        values.put("isFavorite",1);
        int row = DatabaseManager.getDatabase().update(DatabaseHelper.DETAIL_TABLE,values,"id = ?",new String[]{String.valueOf(id)});
        if (row == 1)
            return true;
        else
            return false;
    }

    public boolean dislikeSpecificArticle(int id){
        ContentValues values = new ContentValues();
        values.put("isFavorite",0);
        int row = DatabaseManager.getDatabase().update(DatabaseHelper.DETAIL_TABLE,values,"id = ?",new String[]{String.valueOf(id)});
        if (row == 1)
            return true;
        else
            return false;
    }

    private ContentValues toContentValue(ZhiHuDetail bean){
        ContentValues values = new ContentValues();
        values.put("id",bean.id);
        values.put("type",bean.type);
        values.put("title",bean.title);
        values.put("body",bean.body);
        values.put("image",bean.image);
        values.put("image_source",bean.image_source);
        values.put("ga_prefix",bean.ga_prefix);
        values.put("share_url",bean.share_url);
        values.put("css",bean.css.get(0));
        values.put("images",bean.images.get(0));
        if (bean.js.size() != 0)
            values.put("js",bean.js.get(0));
        return values;
    }

    private ZhiHuDetail praseResult(Cursor cursor){
        ZhiHuDetail bean = new ZhiHuDetail();
        if (!cursor.moveToNext())
            return null;
        bean.id = cursor.getInt(0);
        bean.isFavorite = cursor.getInt(1);
        bean.type = cursor.getInt(2);
        bean.title = cursor.getString(3);
        bean.body = cursor.getString(4);
        bean.image_source = cursor.getString(5);
        bean.image = cursor.getString(6);
        bean.share_url = cursor.getString(7);
        bean.ga_prefix = cursor.getString(8);
        bean.js = new ArrayList<>();
        bean.js.add(cursor.getString(9));
        bean.images = new ArrayList<>();
        bean.images.add(cursor.getString(10));
        bean.css = new ArrayList<>();
        bean.css.add(cursor.getString(11));
        cursor.close();
        Log.d("cylog","Test————id："+bean.id+",title:"+bean.title);
        return bean;
    }
}
