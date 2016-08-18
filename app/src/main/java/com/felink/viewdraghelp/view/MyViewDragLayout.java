package com.felink.viewdraghelp.view;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MyViewDragLayout extends LinearLayout {

    private ViewDragHelper mDragger;
    private View mDragView;
    private View mAutoBackView;
    private View mEdgeTrackerView;

    private Point mAutoBackOriginPos = new Point();

    public MyViewDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initData() {

        mDragger = ViewDragHelper.create(this,1.0f,new ViewDragHelper.Callback(){
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mDragView || child == mAutoBackView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {

                final  int leftBound = getPaddingLeft();
                final  int rightBound = getWidth() - child.getWidth() - getPaddingRight();
                int newLeft = Math.min(Math.max(leftBound,left),rightBound);
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final  int topBound = getPaddingTop();
                final  int bottom = getHeight() - child.getHeight() - getPaddingBottom();
                int newTop = Math.min(Math.max(topBound,top),bottom);
                return newTop;
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mDragger.captureChildView(mEdgeTrackerView,pointerId);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == mAutoBackView){
                    mDragger.settleCapturedViewAt(mAutoBackOriginPos.x,mAutoBackOriginPos.y);
                    invalidate();
                }
            }
        });
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public void computeScroll() {
        if (mDragger.continueSettling(true)){
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        mDragView = getChildAt(0);
        mAutoBackView = getChildAt(1);
        mEdgeTrackerView = getChildAt(2);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mAutoBackOriginPos.x = (int) mAutoBackView.getX();
        mAutoBackOriginPos.y = (int) mAutoBackView.getY();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return mDragger.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }
}
