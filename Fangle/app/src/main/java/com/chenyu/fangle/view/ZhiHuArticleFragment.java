package com.chenyu.fangle.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenyu.fangle.MainActivity;
import com.chenyu.fangle.R;
import com.chenyu.fangle.adapter.ZhiHuArticleAdapter;
import com.chenyu.fangle.beans.ZhiHuStory;
import com.chenyu.fangle.beans.ZhiHuTopStory;
import com.chenyu.fangle.event.FangleEvent;
import com.chenyu.fangle.presenter.ZhiHuArticlePresenter;
import com.chenyu.fangle.view.interfaces.IArticleView;
import com.chenyu.fangle.widgets.bannerviewpager.BannerViewPager;
import com.chenyu.fangle.widgets.bannerviewpager.ViewPagerAdapter;
import com.chenyu.fangle.widgets.bannerviewpager.interfaces.OnPageClickListener;
import com.chenyu.fangle.widgets.superrecyclerview.EnhancedRecyclerView;
import com.chenyu.fangle.widgets.superrecyclerview.interfaces.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class ZhiHuArticleFragment extends Fragment implements IArticleView {

    private LayoutInflater mInflater;
    private EnhancedRecyclerView mRecyclerView;
    private BannerViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private ZhiHuArticleAdapter mAdapter;
    private LinearLayout rootView;
    private ZhiHuArticlePresenter mPresenter;
    private List<View> mTopViews;
    private List<ZhiHuTopStory> mTopStories;
    private List<ZhiHuStory> mStoryList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("cylog","fragment oncreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        mInflater = inflater;
        rootView = (LinearLayout) inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (EnhancedRecyclerView) rootView.findViewById(R.id.recycler_view);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new ZhiHuArticlePresenter(this);
        EventBus.getDefault().register(this);
        initViewPager();
        initRecyclerView();
        mPresenter.obtainArticles();
    }

    private void initRecyclerView() {
        mStoryList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addHeaderView(mViewPager);
        mRecyclerView.addOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoad() {
                //load more
                mPresenter.loadMoreArticles();
            }
        });
        mAdapter = new ZhiHuArticleAdapter(getContext(), mStoryList);
        mAdapter.setOnItemClickListener(new ZhiHuArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("cylog","adapter" +
                        " position:"+position);
                startDetailActivity(mStoryList.get(position).id);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

    }

    public void initViewPager(){
        Log.d("cylog","创建了一个新的BannerViewPager.");
        mViewPager = new BannerViewPager(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,650);
        lp.bottomMargin = 10;
        mViewPager.setLayoutParams(lp);

        mTopStories = new ArrayList<>();
        mTopViews = new ArrayList<>();
        View view01 = mInflater.inflate(R.layout.banner_item,mViewPager,false);
        View view02 = mInflater.inflate(R.layout.banner_item,mViewPager,false);
        View view03 = mInflater.inflate(R.layout.banner_item,mViewPager,false);
        View view04 = mInflater.inflate(R.layout.banner_item,mViewPager,false);
        View view05 = mInflater.inflate(R.layout.banner_item,mViewPager,false);

        mTopViews.add(view01);
        mTopViews.add(view02);
        mTopViews.add(view03);
        mTopViews.add(view04);
        mTopViews.add(view05);

        mViewPagerAdapter = new ViewPagerAdapter(mTopViews, new OnPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                Log.d("cylog","click position:"+position);
                startDetailActivity(mTopStories.get(position).id);
            }
        });
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    public void startDetailActivity(int articleId){
        Intent intent = new Intent(getContext(),ZhiHuDetailActivity.class);
        intent.putExtra("articleId",articleId);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FangleEvent event){
        if (event.getEvent() == FangleEvent.REFRESH_ARTICLE_EVENT) {
            mStoryList.clear();
            mTopStories.clear();
            mPresenter.refreshArticles();
        }
    }

    @Override
    public void showTopStories(List data) {
        //取消显示刷新进度条
        EventBus.getDefault().post(new FangleEvent(FangleEvent.REFRESH_ARTICLE_DONE_EVENT));

        mTopStories.clear();
        mTopStories.addAll(data);
        for(int i = 0;i < mTopStories.size();i++){
            TextView tv = (TextView) mTopViews.get(i).findViewById(R.id.tv_top);
            ImageView iv = (ImageView) mTopViews.get(i).findViewById(R.id.iv_top);
            tv.setText(mTopStories.get(i).title);
            Picasso.with(getContext()).load(mTopStories.get(i).image).into(iv);
        }
        mViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showStoryList(List<ZhiHuStory> data) {
        if(mStoryList.size() != 0 && data.get(0).date == mStoryList.get(0).date){
            return;
        }
        mStoryList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void hideBottomLoading() {
        mRecyclerView.setLoaded();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("cylog","fragment onsaveinstanceState");
        outState.putInt("currentPosition",mViewPager.getCurrentPosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("cylog","fragment onviewStateRestored");
        if (savedInstanceState != null)
            mViewPager.setCurrentItem(savedInstanceState.getInt("currentPosition"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
