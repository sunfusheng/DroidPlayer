package com.sunfusheng.droidplayer.sample.DroidPlayer.delegate;

import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by sunfusheng on 2017/2/20.
 */
public class DroidTextureViewMeasureDelegate {

    private WeakReference<View> mView;
    private boolean isAssigned; // 是否赋过值

    private int mViewWidth;
    private int mViewHeight;

    private int mMeasureWidth;
    private int mMeasureHeight;

    public DroidTextureViewMeasureDelegate(View view) {
        this.isAssigned = false;
        this.mView = new WeakReference<View>(view);
    }

    public View getView() {
        if (mView != null) {
            return mView.get();
        }
        return null;
    }

    public void setViewSize(int viewWidth, int viewHeight) {
        if (getView() == null) return;
        if (viewWidth <= 0 || viewHeight <= 0) return;
        if (isAssigned && mViewWidth == viewWidth && mViewHeight == viewHeight) return;

        this.isAssigned = true;
        this.mViewWidth = viewWidth;
        this.mViewHeight = viewHeight;
        getView().requestLayout();
    }

    public void measure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getView() == null) return;

        int width = View.getDefaultSize(mViewWidth, widthMeasureSpec);
        int height = View.getDefaultSize(mViewHeight, heightMeasureSpec);

        if (mViewWidth > 0 && mViewHeight > 0) {
            int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
            int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);

            int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == View.MeasureSpec.EXACTLY && heightSpecMode == View.MeasureSpec.EXACTLY) {
                width = widthSpecSize;
                height = heightSpecSize;

                if (mViewWidth * height < width * mViewHeight) {
                    width = height * mViewWidth / mViewHeight;
                } else if (mViewWidth * height > width * mViewHeight) {
                    height = width * mViewHeight / mViewWidth;
                }
            } else if (widthSpecMode == View.MeasureSpec.EXACTLY) {
                width = widthSpecSize;
                height = width * mViewHeight / mViewWidth;
                if (heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize) {
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == View.MeasureSpec.EXACTLY) {
                height = heightSpecSize;
                width = height * mViewWidth / mViewHeight;
                if (widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize) {
                    width = widthSpecSize;
                }
            } else {
                width = mViewWidth;
                height = mViewHeight;
                if (heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize) {
                    height = heightSpecSize;
                    width = height * mViewWidth / mViewHeight;
                }
                if (widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize) {
                    width = widthSpecSize;
                    height = width * mViewHeight / mViewWidth;
                }
            }
        }

        mMeasureWidth = width;
        mMeasureHeight = height;
    }

    public int getViewWidth() {
        return mViewWidth;
    }

    public int getViewHeight() {
        return mViewHeight;
    }

    public int getMeasureWidth() {
        return mMeasureWidth;
    }

    public int getMeasureHeight() {
        return mMeasureHeight;
    }
}
