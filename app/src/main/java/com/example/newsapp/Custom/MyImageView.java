package com.example.newsapp.Custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 *
 * Created by 晨阳大帅比 on 2018/3/21.
 */

public class MyImageView extends android.support.v7.widget.AppCompatImageView{

    private int mDy;
    private int mMinDy;
    private int pDy;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setDy(int dy,int py){
        if(getDrawable()==null)
            return;
        pDy = py;
        mDy = dy;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        mMinDy = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        int w = getWidth();
        int h = (int)(getWidth() * 1.0f / drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight());
        drawable.setBounds(0,0,w,h);
        canvas.save();

        //根据RecyclerView滑动距离匹配图片滑动距离
        int tDy = mDy * (h - mMinDy) / (pDy + mMinDy);
        if(tDy<0)
            tDy = 0;
        if(tDy>getDrawable().getBounds().height() - mMinDy)
            tDy = getDrawable().getBounds().height() - mMinDy;

        canvas.translate(0,-tDy);
        drawable.draw(canvas);
        //super.onDraw(canvas);
        canvas.restore();
    }


}
