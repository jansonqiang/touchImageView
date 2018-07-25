package com.janson.touchView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;

@SuppressLint("ObjectAnimatorBinding")
public class TouchImageView extends AppCompatImageView {


    private static final String TAG = TouchImageView.class.getSimpleName();
    private int iconImage;

    private float mScreenWidth, mScreenHeight; // 屏幕宽高，不包含状态栏
    private float mMarginY; // 上下方留出的空间
    private float mMarginX; // 上下方留出的空间

    private int windowWidth,windowHeight;



    private boolean isInit = false;
    private boolean initPos = false;

    private float lastX, lastY;
    private float downX, downY;
    float upX=0,upY=0;
    long downTime=0,upTime=0;

    private boolean suspendedInLeft = true;

    private int mStayPosY;

    private int mPercentX; //向左右按image百分比缩放

    private Handler mHandler;

    private int mDelayMillis;
    public TouchImageView(Context context) {
        this(context, null);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context,attrs);
    }


    private void initView(Context context, AttributeSet attrs) {
        // 获取屏幕宽高
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            statusBarHeight = getResources().getDimensionPixelSize(
                    Integer.parseInt(field.get(obj).toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mHandler = new Handler();
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        // mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels - statusBarHeight;



        // 获取属性
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TouchView, 0, 0);
        iconImage = ta.getResourceId(R.styleable.TouchView_tv_image,
                R.mipmap.ic_launcher);



        mMarginX  = ta.getDimension(R.styleable.TouchImageView_tiv_marginX,0);
        mMarginY  = ta.getDimension(R.styleable.TouchImageView_tiv_marginY,0);
        mPercentX = ta.getInt(R.styleable.TouchImageView_tiv_percentX,0);
        mDelayMillis = ta.getInteger(R.styleable.TouchImageView_tiv_percentX,500);


        float minScreenWH = Math.min(mScreenWidth, mScreenHeight);
        // 上下方留出的空间，默认分别小于宽高中较小者的一半
        // mMarginY = 50;

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    int viewWidth = ((View)getParent()).getWidth();
                    int  viewHeight = ((View)getParent()).getHeight();

                    windowWidth =viewWidth;
                    windowHeight = viewHeight;

                    moveAnimSingle(this, 0f, 0f, 0f, windowHeight/ 2, 0, false);

                    Log.d(TAG, "onGlobalLayout: "+windowWidth+" "+windowHeight);

                    if(mPercentX!=0&&mMarginX==0){
                        mMarginX = getWidth() * Math.abs(mPercentX/100f);
                    }

                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    isInit = true;
                    if (initPos) {
                        movePosition(!suspendedInLeft, mStayPosY);
                    }
                }
            });
        }



    }

    public void setPosition(boolean isRight, int stayPosY) {
        suspendedInLeft = !isRight;
        if (isInit) {
            movePosition(isRight, stayPosY);
        } else {
            initPos = true;
            mStayPosY = stayPosY;
        }
    }

    private void moveAnimSingle(final Object object, final float startX, float startY, final float endX, float endY,
                                final long duration, final boolean isAll) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(object, "translationX", startX, endX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(object, "translationY", startY, endY);
        final AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorX, animatorY);
        set.setDuration(duration);
        set.start();
        //   set.addListener(new Animator.AnimatorListener() {

    }

    private void movePosition(boolean isRight, int stayPosY) {

        float endX = 0f;
        if (isRight) {
            endX = mScreenWidth - getHeight();
        }

        if (stayPosY > 100) {
            stayPosY = 100;
        }

        //TODO 这里要改一改
        float endY = mMarginY +
                (mScreenHeight - mMarginY * 2 - getWidth()) * ((float) stayPosY / 100);

        moveAnimSingle(this, 0f, 0f, endX, endY, 0, false);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float viewX = getX();
        float viewY = getY();
        float eventX = event.getRawX();
        float eventY = event.getRawY();
        float startX, startY, endX, endY;




        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downX = eventX;
                downY = eventY;
                downTime = System.currentTimeMillis();

                lastX = eventX;
                lastY = eventY;

                break;

            case MotionEvent.ACTION_MOVE:
                startX = viewX;
                startY = viewY;
                endX = viewX + (eventX - lastX);
                endY = viewY + (eventY - lastY);


                if (endY > 0 && endY <windowHeight - getTop() - getHeight())
                    moveAnimSingle(this, startX, startY, endX, endY, 0, false);
                lastX = eventX;
                lastY = eventY;
                break;

            case MotionEvent.ACTION_UP:

                upX = eventX;
                upY = eventY;
                upTime = System.currentTimeMillis();

                startX = viewX;
                startY = viewY;
                // 判断左右
                if ((viewX + (getWidth() / 2)) < (mScreenWidth / 2)) { // 左
                    suspendedInLeft = true;
                    endX = 0;
                } else { // 右
                    suspendedInLeft = false;
                    endX = mScreenWidth - getWidth();
                }



                Log.d(TAG," viewY="+viewY+" mMarginY="+mMarginY+" getHeight()=  "+getHeight());
                // 判断上下
                if (viewY < mMarginY) {
                    endY = mMarginY;
                } else if ((viewY + getHeight()) > (windowHeight - mMarginY)) {
                    endY = windowHeight - mMarginY - getHeight();
                } else {
                    endY = viewY;
                }
                Log.d(TAG," viewY="+viewY+" endY="+endY);

                //endY = viewY;
                moveAnimSingle(this, startX, startY, endX, endY, 0, false);

                final float tempStartX= endX;
                float tempEndX2;
                if(suspendedInLeft){
                    tempEndX2= endX-mMarginX;
                }else {
                    tempEndX2 = endX+mMarginX;
                }

                final  float tempEndX = tempEndX2;

                final float tempStartY =  startY;
                final float tempEndY =  endY;

                if(mMarginX!=0){
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moveAnimSingle(this, tempStartX, tempStartY, tempEndX, tempEndY, 0, false);
                        }
                    },mDelayMillis);
                }

              //  Log.d(TAG, String.format("downX = %f upX=%f downY=%f upY=%f  upTime = %d  downTime=%d", downX,upX,downY,upY,upTime,downTime));
                if(Math.abs(downX-upX)<20 && Math.abs(downY-upY)<10 &&Math.abs(upTime-downTime)<500  )
                    performClick();
                break;

        }

        return true;
    }






}
