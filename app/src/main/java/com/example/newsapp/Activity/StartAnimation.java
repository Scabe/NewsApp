package com.example.newsapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.newsapp.R;

/**
 * 打开APP时的简单动画
 * Created by 晨阳大帅比 on 2018/3/14.
 */

public class StartAnimation extends Activity{

    private ImageView img;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.startanimation);
        initImage();
        if(!this.isTaskRoot()){
            finish();
            return;
        }
    }

    private void initImage() {
        img = findViewById(R.id.start);
        img.setImageResource(R.drawable.startpc);
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f,1.0f,1.4f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(4000);
        //渐入
        AlphaAnimation startalphaAnimation = new AlphaAnimation(0,1);
        startalphaAnimation.setDuration(1000);
        //渐出
        AlphaAnimation endalphaAnimation = new AlphaAnimation(1,0);
        endalphaAnimation.setDuration(500);
        endalphaAnimation.setStartOffset(3500);
        //组合动画，将所有动画组合起来
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(startalphaAnimation);
        animationSet.addAnimation(endalphaAnimation);
        //动画播放完毕后保持形状，并跳转到MainActivity
        animationSet.setFillAfter(true);
        final Intent intent = new Intent(StartAnimation.this,MainActivity.class);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img.startAnimation(animationSet);
    }
}
