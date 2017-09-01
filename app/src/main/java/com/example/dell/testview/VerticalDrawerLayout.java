package com.example.dell.testview;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dell on 2017/8/28.
 */

public class VerticalDrawerLayout extends ViewGroup{

    private ViewDragHelper viewDragHelper;

    private View mContentView;
    private ViewGroup mDrawerView;
    private boolean canScroll = true;
    private boolean mIsOpen = true;
    private int sourceHeight = 80;
    private OpenChangeListener openChangeListener;

    public VerticalDrawerLayout(Context context) {
        this(context, null);
    }

    public VerticalDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, 1.5f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {

                if (child == mDrawerView && !canScroll)
                    return false;
                return child == mDrawerView;
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {

                viewDragHelper.captureChildView(mDrawerView, pointerId);
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {

                return super.clampViewPositionHorizontal(child, left, dx);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {

                return Math.max(Math.min(top, 0), -mDrawerView.getHeight() + sourceHeight);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {

                float movePercentage = (releasedChild.getHeight() + releasedChild.getTop()) / (float) releasedChild.getHeight();
                int finalTop = (xvel >= 0 && movePercentage > 0.5f) ? 0 : -releasedChild.getHeight() + sourceHeight;
                viewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), finalTop);
                invalidate();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (openChangeListener != null) {
                    openChangeListener.onShowHeightChanging(mDrawerView.getMeasuredHeight() + top);
                }
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                if (mDrawerView == null) return 0;
                return (mDrawerView == child) ? mDrawerView.getHeight() : 0;
            }

            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
                if (mDrawerView == null)
                    return;
                if (state == ViewDragHelper.STATE_IDLE) {
                    mIsOpen = (mDrawerView.getTop() == 0);
                    if (openChangeListener != null) {
                        openChangeListener.isOpen(mIsOpen);
                    }

                }
            }
        });
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        MarginLayoutParams params = (MarginLayoutParams) mContentView.getLayoutParams();
        mContentView.layout(params.leftMargin, params.topMargin,
                mContentView.getMeasuredWidth() + params.leftMargin,
                mContentView.getMeasuredHeight() + params.topMargin);
        if (b) {
            params = (MarginLayoutParams) mDrawerView.getLayoutParams();
            mDrawerView.layout(params.leftMargin, params.topMargin - mDrawerView.getMeasuredHeight() + sourceHeight,
                    mDrawerView.getMeasuredWidth() + params.leftMargin,
                    params.topMargin + sourceHeight);
        }
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public void closeDrawer() {
        if (mDrawerView == null)
            return;
        if (mIsOpen) {
            viewDragHelper.smoothSlideViewTo(mDrawerView, mDrawerView.getLeft(), -mDrawerView.getHeight() + sourceHeight);
            invalidate();
        }
    }

    public void openDrawer() {
        if (mDrawerView == null)
            return;
        if (!mIsOpen) {
            viewDragHelper.smoothSlideViewTo(mDrawerView, mDrawerView.getLeft(), 0);
            invalidate();
        }
    }

    public boolean isDrawerOpened() {
        return mIsOpen;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);

        MarginLayoutParams params = (MarginLayoutParams) mContentView.getLayoutParams();
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                measureWidth - (params.leftMargin + params.rightMargin), MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                measureHeight - (params.topMargin + params.bottomMargin), MeasureSpec.EXACTLY);
        mContentView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

        mDrawerView.measure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mDrawerView = (ViewGroup) getChildAt(1);
    }

    public void setOpenChangeListener(OpenChangeListener openChangeListener) {
        this.openChangeListener = openChangeListener;
    }

    public void setSourceHeight(int sourceHeight) {
        if (this.sourceHeight == sourceHeight)
            return;
        this.sourceHeight = sourceHeight;
        invalidate();
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    public interface OpenChangeListener {

        void isOpen(boolean isOpen);

        void onShowHeightChanging(int showHeight);
    }
}
