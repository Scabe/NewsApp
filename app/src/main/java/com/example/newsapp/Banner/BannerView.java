package com.example.newsapp.Banner;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.newsapp.Activity.ContentActivity;
import com.example.newsapp.Json.RecentItem;
import com.example.newsapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by 晨阳大帅比 on 2018/3/9.
 */

public class BannerView extends FrameLayout {

    private LinearLayout linearLayout = null;
    private FrameLayout frameLayout = null;
    private ViewPager viewPager = null;
    private int viewSize;

    public boolean once = true;

    private List<View> viewList = new ArrayList<>();
    private List<RecentItem> recentItemList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private List<View> list = new ArrayList<>();

    private BannerHandler mBannerHandler;

    private static final int MSG_LOOP = 1000;
    private static int LOOP_INTERVAL = 5000;
    private static float mTouch = 1;

    private static class BannerHandler extends Handler{
        private WeakReference<BannerView> weakReference = null;

        public  BannerHandler(BannerView bannerView){
            super(Looper.getMainLooper());
            weakReference = new WeakReference<BannerView>(bannerView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(weakReference == null)
                return;
            BannerView bannerView = weakReference.get();
            if(bannerView == null || bannerView.viewPager == null || bannerView.viewPager.getAdapter() == null || bannerView.viewPager.getAdapter().getCount() <= 0)
                return;
            int curpos = bannerView.viewPager.getCurrentItem();
            bannerView.viewPager.setCurrentItem(curpos+1);
            if(hasMessages(MSG_LOOP))
                removeMessages(MSG_LOOP);
            sendEmptyMessageDelayed(MSG_LOOP,LOOP_INTERVAL);
        }
    }

    public void startLoop(boolean flag){
        if(flag){
            if(mBannerHandler == null)
                mBannerHandler = new BannerHandler(this);
            if (once){
                mBannerHandler.sendEmptyMessageDelayed(MSG_LOOP,LOOP_INTERVAL);
                once = false;
            }
            else
                mBannerHandler.sendEmptyMessage(MSG_LOOP);
        }
        else{
            if(mBannerHandler!=null){
                mBannerHandler.removeMessages(MSG_LOOP);
                mBannerHandler = null;
            }
        }
    }

    public BannerView(@NonNull Context context) {
        super(context);
        init();
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        if(hasFocus)
            once = true;
        startLoop(hasFocus);
        System.out.println("dispatchWindowFocusChanged");
        super.dispatchWindowFocusChanged(hasFocus);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        System.out.println("onAttachedToWindow");
        startLoop(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        System.out.println("onDetachedFromWindow");
        startLoop(false);
    }

    private void init() {
        initViewPager();
        initLinearLayout();
        this.addView(viewPager);
        this.addView(linearLayout);
    }

    private void initViewPager() {
        viewPager = new ViewPager(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        viewPager.setLayoutParams(layoutParams);
        //设置页面轮播速度
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(),
                    new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(600);
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updatelayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //触碰拦截，如果用户滑动轮播图，则等待滑动完毕之后再进行自动轮播
        //同时判断用户的点击和滑动事件,touchflag=1则为点击事件
        viewPager.setOnTouchListener(new OnTouchListener() {
            int touchflag = 0;
            float x = 0,y = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        touchflag = 0;
                        x = event.getX();
                        y = event.getY();
                        if(mBannerHandler!=null){
                            if(mBannerHandler.hasMessages(MSG_LOOP))
                                mBannerHandler.removeMessages(MSG_LOOP);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xDiff = Math.abs(event.getX()-x);
                        float yDiff = Math.abs(event.getY()-y);
                        if(xDiff<mTouch&&xDiff>=yDiff)
                            touchflag = 1;
                        else
                            touchflag = 0;
                        break;
                    case MotionEvent.ACTION_UP:
                        if ((touchflag == 0)){
                            if(mBannerHandler!=null)
                                mBannerHandler.sendEmptyMessageDelayed(MSG_LOOP,LOOP_INTERVAL);
                        }
                        else if(touchflag ==1){
                            int curpos = viewPager.getCurrentItem();
                            int cur = curpos%viewSize;
                            final Intent intent = new Intent(getContext(),ContentActivity.class);
                            intent.putExtra("flag",2);
                            intent.putExtra("uri",recentItemList.get(cur).geturl());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initLinearLayout() {
        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
        layoutParams.bottomMargin = 9;
        linearLayout.setPadding(9,0,0,0);
        linearLayout.setLayoutParams(layoutParams);
    }


    public void setList(List<RecentItem> recentItemList1){
        this.recentItemList = recentItemList1;
        for(final RecentItem item:recentItemList1){
            try{
                MyThread thread = new MyThread(item);
                thread.start();
                thread.join();
            }catch (Exception e){
                e.printStackTrace();
            }
            /*try {
                StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(item.geturl());
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(response);
                String str = jsonObject.getString("image");
                String title = jsonObject.getString("title");
                titleList.add(title);
                URL url = new URL(str);
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(6000);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                InputStream is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                ImageView image = new ImageView(getContext());
                image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setImageBitmap(bitmap);
                viewList.add(image);
            }catch (Exception e){
                e.printStackTrace();
            }*/
        }
        if(viewList!=null&&viewList.size()!=0){
            viewSize = viewList.size();
            if(viewSize==1){
                viewList.add(viewList.get(0));
                titleList.add(titleList.get(0));
                viewSize = 2;
            }
            if(viewSize==2){
                viewList.add(viewList.get(0));
                viewList.add(viewList.get(1));
                titleList.add(titleList.get(0));
                titleList.add(titleList.get(1));
                viewSize = 4;
            }
            if(viewSize>4){
                for(int i=4;i<viewSize;i++){
                    viewList.remove(4);
                    titleList.remove(4);
                }
                viewSize = 4;
            }
            initList();
            //BannerAdapter adapter = new BannerAdapter(viewList);
            BannerAdapter adapter = new BannerAdapter(list);
            setAdapter(adapter);
        }
    }

    private void initList() {
        for(int i=0;i<viewSize;i++){
            frameLayout= new FrameLayout(getContext());
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            frameLayout.setLayoutParams(layoutParams);
            frameLayout.addView(viewList.get(i));

            LinearLayout mlinearLayout = new LinearLayout(getContext());
            mlinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams mlayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            mlayoutParams.gravity = Gravity.BOTTOM;
            mlayoutParams.bottomMargin = 35;
            mlinearLayout.setPadding(9,0,0,0);
            mlinearLayout.setLayoutParams(mlayoutParams);

            TextView tx = new TextView(getContext());
            tx.setText(titleList.get(i));
            tx.setTextSize(18);
            tx.setBackgroundColor(Color.argb(0, 0, 0, 0));
            tx.setTextColor(Color.WHITE);
            mlinearLayout.addView(tx);

            frameLayout.addView(mlinearLayout);
            list.add(frameLayout);
        }
    }

    public void setAdapter(PagerAdapter adapter){
        viewPager.setAdapter(adapter);
        updatelayout();
    }

    private void updatelayout() {
        if(viewPager!=null && viewList.size()!=0){
            if(linearLayout.getChildCount()!=viewSize){
                int diffCnt = linearLayout.getChildCount() - viewSize;
                diffCnt = Math.abs(diffCnt);
                for(int i = 0;i<diffCnt;i++){
                    ImageView img = new ImageView(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.rightMargin = 9;
                    img.setLayoutParams(layoutParams);
                    img.setBackgroundResource(R.drawable.banner_point);
                    linearLayout.addView(img);
                }
            }
            int curpos = viewPager.getCurrentItem();
            for(int i = 0;i<linearLayout.getChildCount();i++){
                if(i==(curpos%viewSize))
                    linearLayout.getChildAt(i).setBackgroundResource(R.drawable.banner_point_select);
                else
                    linearLayout.getChildAt(i).setBackgroundResource(R.drawable.banner_point);
            }
        }
    }

    public void setTransformAnim (boolean flag){
        if (flag){
            viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
                private static final float MIN_SCALE = 0.75f;
                @Override
                public void transformPage(View view, float position) {
                    int pageWidth = view.getWidth();
                    if (position < -1)
                    { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        view.setRotation(0);

                    } else if (position <= 1)
                    { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        if (position < 0)
                        {

                            float mRot = (20f * position);
                            view.setPivotX(view.getMeasuredWidth() * 0.5f);
                            view.setPivotY(view.getMeasuredHeight());
                            view.setRotation(mRot);
                        } else
                        {

                            float mRot = (20f * position);
                            view.setPivotX(view.getMeasuredWidth() * 0.5f);
                            view.setPivotY(view.getMeasuredHeight());
                            view.setRotation(mRot);
                        }

                        // Scale the page down (between MIN_SCALE and 1)

                        // Fade the page relative to its size.

                    } else
                    { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        view.setRotation(0);
                    }
                }
            });
        }
    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 1000;

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }
    }

    private class MyThread extends Thread{

        private RecentItem recentItem;

        public MyThread(RecentItem item){
            recentItem = item;
        }

        public void run(){
            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(recentItem.geturl());
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(response);
                String str = jsonObject.getString("image");
                String title = jsonObject.getString("title");
                URL url = new URL(str);
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(6000);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                InputStream is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                ImageView image = new ImageView(getContext());
                image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setImageBitmap(bitmap);
                titleList.add(title);
                viewList.add(image);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
