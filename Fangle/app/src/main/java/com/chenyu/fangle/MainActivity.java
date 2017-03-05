package com.chenyu.fangle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.chenyu.fangle.adapter.MyFragmentAdapter;
import com.chenyu.fangle.event.FangleEvent;
import com.chenyu.fangle.view.TestActivity;
import com.chenyu.fangle.view.ZhiHuArticleFragment;
import com.chenyu.fangle.view.ZhiHuDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Fragment> mFragment;
    private List<String> mTitle;
    private Fragment mZhiHuArticleFragment;
    private Fragment mNormalFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        Log.d("cylog","activity oncreate");
        initViews();
    }

    private void initViews() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipereRreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF0AAFFC"));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventBus.getDefault().post(new FangleEvent(FangleEvent.REFRESH_ARTICLE_EVENT));
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTitle = new ArrayList<>();
        mTitle.add("知乎日报");
        mTitle.add("新鲜事儿");

        //mTabLayout = (TabLayout) findViewById(R.id.tabs);
        //mTabLayout.addTab(mTabLayout.newTab().setText(mTitle.get(0)));
        //mTabLayout.addTab(mTabLayout.newTab().setText(mTitle.get(1)));
        mFragment = new ArrayList<>();

        mZhiHuArticleFragment = new ZhiHuArticleFragment();
        //mNormalFragment = new Fragment();
        mFragment.add(mZhiHuArticleFragment);
        //mFragment.add(mNormalFragment);
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),mFragment,mTitle);
        mViewPager.setAdapter(adapter);
        //mTabLayout.setupWithViewPager(mViewPager);
        //mTabLayout.setTabsFromPagerAdapter(adapter);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){

                }
                return true;
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onEvent(FangleEvent event){
        if (event.getEvent() == FangleEvent.REFRESH_ARTICLE_DONE_EVENT){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
