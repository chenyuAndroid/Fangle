package com.chenyu.fangle.presenter;

import android.util.Log;

import com.chenyu.fangle.beans.ZhiHuArticleBean;
import com.chenyu.fangle.beans.ZhiHuStory;
import com.chenyu.fangle.beans.ZhiHuTopStory;
import com.chenyu.fangle.model.ZhiHuArticleModel;
import com.chenyu.fangle.utils.DateUtils;
import com.chenyu.fangle.view.interfaces.IArticleView;

import java.util.List;

public class ZhiHuArticlePresenter {

    private IArticleView mArticleView;
    private ZhiHuArticleModel mArticleModel;
    private int mCount = 1;

    public ZhiHuArticlePresenter(IArticleView view){
        this.mArticleView = view;
        mArticleModel = new ZhiHuArticleModel(this);
    }

    /**
     * 获取文章数据，先从数据库获取，如果获取不成功则从网络上获取
     */
    public void obtainArticles(){
        mArticleView.showLoading();
        List<ZhiHuStory> stories =  mArticleModel.loadStoryFromDB(DateUtils.getDate());
        List<ZhiHuTopStory> topStories = mArticleModel.loadTopStoryFromDB(DateUtils.getDate());
        if(stories.size() != 0 && topStories.size() != 0){
            mArticleView.hideLoading();
            mArticleView.showStoryList(stories);
            mArticleView.showTopStories(topStories);
        }else {
            mArticleModel.loadDataFromNet();
        }
    }

    public void refreshArticles(){
        mCount = 1;
        obtainArticles();
    }


    /**
     * 加载更多数据，先从数据库获取，如果查询为空则从网络上获取
     */
    public void loadMoreArticles(){
        //Log.d("cylog","loadmorearticles mCount:"+mCount);
        List<ZhiHuStory> stories = mArticleModel.loadStoryFromDB(DateUtils.getPreviousDate(mCount));
//        List<ZhiHuTopStory> topStories = null;
//        if(mCount == 1){
//            topStories = mArticleModel.loadTopStoryFromDB(DateUtils.getPreviousDate(mCount));
//
//            if(stories.size() != 0 && topStories != null &&topStories.size() !=0){
//                mCount++;
//                mArticleView.hideBottomLoading();
//                mArticleView.showStoryList(stories);
//                mArticleView.showTopStories(topStories);
//                return;
//            }
//        }

        if(stories.size() != 0){
            mCount++;
            mArticleView.hideBottomLoading();
            mArticleView.showStoryList(stories);
        }else{
            Log.d("cylog","else mCount:"+mCount);
            mArticleModel.loadMoreDataFromNet(mCount);
        }

    }

    /**
     * 对文章数据进行处理，最后交给View层显示数据
     * @param bean
     */
    public void obtainArticlesFinished(ZhiHuArticleBean bean){
        setArticleDate(bean);
        mArticleView.hideLoading();
        mArticleView.showTopStories(bean.top_stories);
        mArticleView.showStoryList(bean.stories);

        mArticleModel.saveAllData(bean);
    }

    public void loadMoreArticlesFinished(ZhiHuArticleBean bean){
        setArticleDate(bean);
        mCount++;
        mArticleView.hideBottomLoading();
        mArticleView.showStoryList(bean.stories);

        mArticleModel.saveAllData(bean);
    }

    private void setArticleDate(ZhiHuArticleBean bean){
        String date = bean.date;
        for(int i = 0;i < bean.stories.size(); i++){
            bean.stories.get(i).date = date;
        }

        if(bean.top_stories == null)
            return;

        for(int i = 0;i < bean.top_stories.size(); i++){
            bean.top_stories.get(i).date = date;
        }
    }
}
