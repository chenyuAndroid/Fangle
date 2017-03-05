package com.chenyu.fangle.network;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static Retrofit mZhiHuRetrofit;
    private static OkHttpClient client = new OkHttpClient();

    public static void init(){
        if(mZhiHuRetrofit == null){
            mZhiHuRetrofit = new Retrofit.Builder()
                    .baseUrl("http://news-at.zhihu.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

    }

    public static Retrofit getZhiHuRetrofit(){
        return mZhiHuRetrofit;
    }

}
