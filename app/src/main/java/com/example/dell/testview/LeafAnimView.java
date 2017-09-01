package com.example.dell.testview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by dell on 2017/8/25.
 */

public class LeafAnimView extends View {

    private static final String TAG = LeafAnimView.class.getSimpleName();

    private ValueAnimator mValueAnimator;
    private Paint mPaint;
    private LeafAtom mLeafAtom;

    public LeafAnimView(Context context) {
        super(context);
        init();
    }

    public LeafAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LeafAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
//        mPaint.setMaskFilter(new BlurMaskFilter(BLUR_SIZE, BlurMaskFilter.Blur.SOLID));

        //动画引擎
        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setDuration(5000);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (null != mLeafAtom) {
                    mLeafAtom.endAndClear();
                }
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout : " + System.currentTimeMillis());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GREEN) ;
        if (null == mLeafAtom) {
            //传入总时长
            mLeafAtom = new LeafAtom(getWidth(), getHeight(), mValueAnimator.getDuration());
        }
        if (!mValueAnimator.isStarted()) {
            //发动引擎
            mValueAnimator.start();
        }
        //开始绘制
        mLeafAtom.drawGraph(canvas, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged : ");
//        mLeafAtom.setWidthAndHeight(w, h);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (getWidth() == 0) {
            return;
        }
        if (visibility == VISIBLE) {
            mValueAnimator.start();
        } else {
            mValueAnimator.end();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow : ");
        if (null != mValueAnimator) {
            mValueAnimator.end();
        }
    }

    public void start() {
        mLeafAtom.start();
    }

    public void stop() {
        mLeafAtom.stop();
    }
}
