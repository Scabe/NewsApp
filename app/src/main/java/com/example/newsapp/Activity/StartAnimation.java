package com.example.newsapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
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
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f,1.0f,1.4f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(4000);
        //动画播放完毕后保持形状
        scaleAnimation.setFillAfter(true);
        final Intent intent = new Intent(StartAnimation.this,MainActivity.class);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(intent);
                //overridePendingTransition(and);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img.startAnimation(scaleAnimation);
    }
}
