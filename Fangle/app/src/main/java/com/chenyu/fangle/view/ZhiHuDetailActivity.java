package com.chenyu.fangle.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenyu.fangle.R;
import com.chenyu.fangle.beans.ZhiHuDetail;
import com.chenyu.fangle.network.IArticleAPI;
import com.chenyu.fangle.network.RetrofitManager;
import com.chenyu.fangle.presenter.ZhiHuDetailPresenter;
import com.chenyu.fangle.view.interfaces.IDetailView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 展示知乎文章的Activity.
 */

public class ZhiHuDetailActivity extends AppCompatActivity implements IDetailView {

    private ZhiHuDetailPresenter mPresenter;
    private int articleId;
    private boolean isFavorite;
    private ZhiHuDetail mDetail;
    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mImageView;
    private TextView mTextTitle;
    private TextView mTextImgSrc;
    private WebView mWebView;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPresenter = new ZhiHuDetailPresenter(this);
        articleId = getIntent().getIntExtra("articleId", 0);
        initViews();
        mPresenter.obtainDetails(articleId);

    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorRoot);
        mImageView = (ImageView) findViewById(R.id.iv_detail_bg);
        mTextTitle = (TextView) findViewById(R.id.tv_detail_title);
        mTextImgSrc = (TextView) findViewById(R.id.tv_detail_imgsrc);
        mWebView = (WebView) findViewById(R.id.detail_webview);


        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        Log.d("cylog", "分享....");
                        break;
                    case R.id.action_favorite:
                        if (isFavorite)
                            dislikeArticle();
                        else
                            favorArticle();;
                        break;
                }
                return true;
            }
        });

        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

        mWebView.getSettings().setJavaScriptEnabled(true);  //支持与JavaScript的交互
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setAppCacheEnabled(true);

    }

    private void show(){
        isFavorite = mDetail.isFavorite == 1? true : false;
        Picasso.with(this).load(mDetail.image).into(mImageView);
        mTextTitle.setText(mDetail.title);
        mTextImgSrc.setText(mDetail.image_source);

        String css = "<link rel=\"stylesheet\" href=\"http://news-at.zhihu.com/css/news_qa.auto.css?v=4b3e3\" type=\"text/css\" />";
        String html ="<html><head>" + css +"</head><body>" + mDetail.body +"</body><html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        mWebView.loadData(html, "text/html;charset=UTF-8", null);
    }

    private void favorArticle(){
        MenuItem item = menu.findItem(R.id.action_favorite);
        item.setIcon(R.mipmap.ic_favorite_red);
        boolean isSuccessful = mPresenter.favorThisActicle(articleId);
        if (isSuccessful) {
            isFavorite = true;
//            Toast.makeText(ZhiHuDetailActivity.this, "收藏成功！", Toast.LENGTH_SHORT).show();
            Snackbar.make(mCoordinatorLayout,"已收藏该文章",Snackbar.LENGTH_SHORT).show();
        }
    }

    private  void dislikeArticle(){
        MenuItem item = menu.findItem(R.id.action_favorite);
        item.setIcon(R.mipmap.ic_favorite_white);
        boolean isSuccessful = mPresenter.dislikeThisArticle(articleId);
        if (isSuccessful){
            isFavorite = false;
//            Toast.makeText(ZhiHuDetailActivity.this, "你已成功取消收藏该文章", Toast.LENGTH_SHORT).show();
            Snackbar.make(mCoordinatorLayout,"已取消收藏该文章",Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showDetail(ZhiHuDetail bean) {
        mDetail = bean;
        show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        MenuItem item = menu.findItem(R.id.action_favorite);
        if (mDetail != null && item != null){
            if (isFavorite){
                item.setIcon(R.mipmap.ic_favorite_red);
            }
        }
        return true;
    }
}

