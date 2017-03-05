package com.chenyu.fangle;

import android.app.Application;
import android.util.Log;

import com.chenyu.fangle.db.DatabaseManager;
import com.chenyu.fangle.network.OkHttp3Downloader;
import com.chenyu.fangle.network.RetrofitManager;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;


public class FangleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化webSQL查看工具
        Stetho.initializeWithDefaults(this);
        //初始化数据库
        DatabaseManager.init(this);
        //初始化网络请求库
        RetrofitManager.init();
        //初始化图片加载库
        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(this))
                .build();
        Picasso.setSingletonInstance(picasso);
    }
}
