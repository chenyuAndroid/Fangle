package com.chenyu.fangle.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.chenyu.fangle.MainActivity;
import com.chenyu.fangle.R;

/**
 *  启动页
 */

public class SplashActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123){
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        initViews();
        startAnimation();
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        Log.d("cylog","Density is "+displayMetrics.density+" densityDpi is "+displayMetrics.densityDpi+" height: "+displayMetrics.heightPixels+
//                " width: "+displayMetrics.widthPixels);
    }


    private void startAnimation(){
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mImageView,"scaleX",1f,1.50f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mImageView,"scaleY",1f,1.50f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleXAnimator,scaleYAnimator);
        set.setDuration(4000);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.sendEmptyMessage(0x123);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();

    }

    private void initViews() {
        mImageView = (ImageView) findViewById(R.id.launcher_image);

    }
}
