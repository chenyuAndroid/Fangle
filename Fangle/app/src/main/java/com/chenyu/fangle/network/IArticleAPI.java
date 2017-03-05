package com.chenyu.fangle.network;

import com.chenyu.fangle.beans.ZhiHuArticleBean;
import com.chenyu.fangle.beans.ZhiHuDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2016/10/6.
 */
public interface IArticleAPI {
    @GET("/api/4/news/{place}")
    Call<ZhiHuArticleBean> articleList(@Path("place")String place);

    @GET("/api/4/news/before/{date}")
    Call<ZhiHuArticleBean> articleListBefore(@Path("date")String date);

    @GET("/api/4/news/{articleId}")
    Call<ZhiHuDetail> articleDetail(@Path("articleId")int articleId);
}
