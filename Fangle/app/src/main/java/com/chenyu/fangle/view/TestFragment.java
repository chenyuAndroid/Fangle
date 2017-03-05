package com.chenyu.fangle.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chenyu.fangle.R;
import com.chenyu.fangle.beans.ZhiHuTopStory;
import com.chenyu.fangle.widgets.bannerviewpager.BannerViewPager;
import com.chenyu.fangle.widgets.bannerviewpager.ViewPagerAdapter;
import com.chenyu.fangle.widgets.bannerviewpager.interfaces.OnPageClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */

public class TestFragment extends Fragment {

    private BannerViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<View> mTopViews;
    private List<ZhiHuTopStory> mTopStories;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopStories = new ArrayList<>();
        mTopViews = new ArrayList<>();
        View view01 = LayoutInflater.from(getContext()).inflate(R.layout.banner_item,mViewPager,false);
        View view02 = LayoutInflater.from(getContext()).inflate(R.layout.banner_item,mViewPager,false);
        View view03 = LayoutInflater.from(getContext()).inflate(R.layout.banner_item,mViewPager,false);
        View view04 = LayoutInflater.from(getContext()).inflate(R.layout.banner_item,mViewPager,false);
        View view05 = LayoutInflater.from(getContext()).inflate(R.layout.banner_item,mViewPager,false);

        mTopViews.add(view01);
        mTopViews.add(view02);
        mTopViews.add(view03);
        mTopViews.add(view04);
        mTopViews.add(view05);

        mViewPagerAdapter = new ViewPagerAdapter(mTopViews, new OnPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                Log.d("cylog","click position:"+position);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment_main,container,false);
        mViewPager = (BannerViewPager) view.findViewById(R.id.BannerViewPagerId);
        mViewPager.setAdapter(mViewPagerAdapter);
        return view;

    }


}
