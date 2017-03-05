package com.chenyu.fangle.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.chenyu.fangle.beans.ZhiHuArticleBean;
import com.chenyu.fangle.beans.ZhiHuData;
import com.chenyu.fangle.beans.ZhiHuStory;
import com.chenyu.fangle.beans.ZhiHuTopStory;
import com.chenyu.fangle.db.DatabaseHelper;
import com.chenyu.fangle.db.DatabaseManager;
import com.chenyu.fangle.network.IArticleAPI;
import com.chenyu.fangle.network.RetrofitManager;
import com.chenyu.fangle.presenter.ZhiHuArticlePresenter;
import com.chenyu.fangle.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 知乎文章列表数据层，负责从网络加载数据、从数据库加载数据、保存数据到数据库等操作
 */
public class ZhiHuArticleModel {

    private String tableName;
    private ZhiHuArticlePresenter mPresenter;
    private IArticleAPI iArticleAPI;

    public ZhiHuArticleModel(ZhiHuArticlePresenter presenter){
        this.mPresenter = presenter;
        iArticleAPI = RetrofitManager.getZhiHuRetrofit().create(IArticleAPI.class);
    }

    /**
     * 从网络加载资源
     */
    public void loadDataFromNet(){
        Call<ZhiHuArticleBean> call = iArticleAPI.articleList("latest");
        call.enqueue(new Callback<ZhiHuArticleBean>() {
            @Override
            public void onResponse(Call<ZhiHuArticleBean> call, Response<ZhiHuArticleBean> response) {
                ZhiHuArticleBean bean = response.body();
                mPresenter.obtainArticlesFinished(bean); //加载完毕，通知presenter
            }

            @Override
            public void onFailure(Call<ZhiHuArticleBean> call, Throwable t) {
                mPresenter.loadMoreArticles();
            }
        });
    }

    /**
     * 根据dayOffset从网络加载更多的资源
     * @param dayOffset 相差的天数
     */
    public void loadMoreDataFromNet(int dayOffset){
        Call<ZhiHuArticleBean> call = iArticleAPI.articleListBefore(DateUtils.getPreviousDate(--dayOffset));
        call.enqueue(new Callback<ZhiHuArticleBean>() {
            @Override
            public void onResponse(Call<ZhiHuArticleBean> call, Response<ZhiHuArticleBean> response) {
                ZhiHuArticleBean bean = response.body();
                mPresenter.loadMoreArticlesFinished(bean); //加载完毕，通知presenter
            }

            @Override
            public void onFailure(Call<ZhiHuArticleBean> call, Throwable t) {

            }
        });
    }

    public List<ZhiHuStory> loadStoryFromDB(String date){
        List<ZhiHuStory> result = loadDataFromDB(DatabaseHelper.STORY_TABLE,date);
        return result;
    }

    public List<ZhiHuTopStory> loadTopStoryFromDB(String date){
        List<ZhiHuTopStory> result = loadDataFromDB(DatabaseHelper.TOP_STORY_TABLE,date);
        return result;
    }

    private List loadDataFromDB(String tableName,String date){
        Cursor cursor = DatabaseManager.getDatabase().query(tableName,null,"date = ?",new String[]{date},null,null,null);
        List result = praseResult(cursor, tableName);
        cursor.close();
        return result;
    }

    /**
     * 保存数据到数据库
     * @param datas
     */
    public void saveItem(List<? extends ZhiHuData> datas){
        for(ZhiHuData data : datas){
            saveItem(data);
        }
    }

    private void saveItem(ZhiHuData item){
        if(item instanceof ZhiHuStory){
            tableName = DatabaseHelper.STORY_TABLE;
        }else if(item instanceof ZhiHuTopStory){
            tableName = DatabaseHelper.TOP_STORY_TABLE;
        }
        DatabaseManager.getDatabase().insertWithOnConflict(tableName, null, toContentValues(item), SQLiteDatabase.CONFLICT_REPLACE);
    }

    private ContentValues toContentValues(ZhiHuData item){
        ContentValues values = new ContentValues();
        if(item instanceof ZhiHuStory){
            ZhiHuStory itemCopy = (ZhiHuStory)item;
            values.put("id",itemCopy.id);
            values.put("type",itemCopy.type);
            values.put("images",itemCopy.images.get(0));
            values.put("ga_prefix",itemCopy.ga_prefix);
            values.put("title",itemCopy.title);
            values.put("date", itemCopy.date);
            //Log.d("cylog","insert date:"+itemCopy.date);
        }else if(item instanceof ZhiHuTopStory){
            ZhiHuTopStory itemCopy = (ZhiHuTopStory)item;
            values.put("id",itemCopy.id);
            values.put("type",itemCopy.type);
            values.put("title",itemCopy.title);
            values.put("image",itemCopy.image);
            values.put("ga_prefix",itemCopy.ga_prefix);
            values.put("multipic",itemCopy.multipic);
            values.put("date",itemCopy.date);
        }
        return values;
    }

    private List praseResult(Cursor cursor,String tableName){
        if(tableName == DatabaseHelper.STORY_TABLE){
            List<ZhiHuStory> stories = new ArrayList<>();
            while (cursor.moveToNext()){
                ZhiHuStory item = new ZhiHuStory();
                item.id = cursor.getInt(0);
                item.title = cursor.getString(1);
                item.type = cursor.getInt(2);
                item.images = new ArrayList<>();
                item.images.add(cursor.getString(3));
//                item.multipic = cursor.getInt(4);
                item.ga_prefix = cursor.getString(5);
                item.date = cursor.getString(6);
                stories.add(item);
            }
            return stories;
        }else if(tableName == DatabaseHelper.TOP_STORY_TABLE){
            List<ZhiHuTopStory> topStories = new ArrayList<>();
            while (cursor.moveToNext()){
                ZhiHuTopStory item = new ZhiHuTopStory();
                item.id = cursor.getInt(0);
                item.title = cursor.getString(1);
                item.type = cursor.getInt(2);
                item.image = cursor.getString(3);
                item.ga_prefix = cursor.getString(4);
                item.date = cursor.getString(5);
                topStories.add(item);
            }
            return topStories;
        }
        return null;
    }

    public void saveAllData(ZhiHuArticleBean bean){
        List<ZhiHuStory> stories = bean.stories;
        List<ZhiHuTopStory> topStories = bean.top_stories;
        if(stories != null && stories.size() != 0)
            saveItem(stories);
        if(topStories != null && topStories.size() != 0)
            saveItem(topStories);
    }
}
