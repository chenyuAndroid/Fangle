package com.chenyu.fangle.view.interfaces;

import com.chenyu.fangle.beans.ZhiHuData;
import com.chenyu.fangle.beans.ZhiHuStory;

import java.util.List;

/**
 * Created by Administrator on 2016/10/6.
 */
public interface IArticleView {
    /**
     * 显示顶部的信息
     * @param data
     */
    void showTopStories(List<?> data);

    /**
     * 显示信息列表
     * @param data
     */
    void showStoryList(List<ZhiHuStory> data);

    /**
     * 显示加载进度条
     */
    void showLoading();

    /**
     * 隐藏加载进度条
     */
    void hideLoading();

    /**
     * 隐藏底部的加载进度条
     */
    void hideBottomLoading();
}
