package com.chenyu.fangle.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chenyu.fangle.R;
import com.chenyu.fangle.beans.ZhiHuTopStory;
import com.chenyu.fangle.widgets.bannerviewpager.BannerViewPager;
import com.chenyu.fangle.widgets.bannerviewpager.ViewPagerAdapter;
import com.chenyu.fangle.widgets.bannerviewpager.interfaces.OnPageClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */

public class TestActivity extends AppCompatActivity {

    private BannerViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<View> mTopViews;
    private List<ZhiHuTopStory> mTopStories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);
//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lll);
//        Log.d("cylog","activity on create");
//
//        mViewPager = new BannerViewPager(this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,650);
//        lp.bottomMargin = 10;
//        mViewPager.setLayoutParams(lp);
//        linearLayout.addView(mViewPager);
//
//        mTopStories = new ArrayList<>();
//        mTopViews = new ArrayList<>();
//        View view01 = LayoutInflater.from(this).inflate(R.layout.banner_item,mViewPager,false);
//        View view02 = LayoutInflater.from(this).inflate(R.layout.banner_item,mViewPager,false);
//        View view03 = LayoutInflater.from(this).inflate(R.layout.banner_item,mViewPager,false);
//        View view04 = LayoutInflater.from(this).inflate(R.layout.banner_item,mViewPager,false);
//        View view05 = LayoutInflater.from(this).inflate(R.layout.banner_item,mViewPager,false);
//
//        mTopViews.add(view01);
//        mTopViews.add(view02);
//        mTopViews.add(view03);
//        mTopViews.add(view04);
//        mTopViews.add(view05);
//
//        mViewPagerAdapter = new ViewPagerAdapter(mTopViews, new OnPageClickListener() {
//            @Override
//            public void onPageClick(View view, int position) {
//                Log.d("cylog","click position:"+position);
//            }
//        });
//        mViewPager.setAdapter(mViewPagerAdapter);

        if (savedInstanceState == null){
            Fragment fragment = new TestFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.lll,fragment).commit();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("cylog","activity onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("cylog","activity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("cylog","activity onDestory");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("cylog","activity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("cylog","activity onResume");
    }
}
