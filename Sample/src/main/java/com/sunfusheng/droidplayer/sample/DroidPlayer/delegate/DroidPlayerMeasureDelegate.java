package com.sunfusheng.droidplayer.sample.DroidPlayer.delegate;

import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by sunfusheng on 2017/2/23.
 */
public class DroidPlayerMeasureDelegate {

    private WeakReference<ViewGroup> mViewGroup;

    private int mPlayerWidth;
    private int mPlayerHeight;

    private int mChildWidth;
    private int mChildHeight;

    private float mRatio; // 宽高比

    public DroidPlayerMeasureDelegate(ViewGroup viewGroup, int widthRatio, int heightRatio) {
        this.mViewGroup = new WeakReference<ViewGroup>(viewGroup);
        calculateRatio(widthRatio, heightRatio);
    }

    public ViewGroup getViewGroup() {
        if (mViewGroup != null) {
            return mViewGroup.get();
        }
        return null;
    }

    public void setRatio(int widthRatio, int heightRatio) {
        if (getViewGroup() == null) return;
        calculateRatio(widthRatio, heightRatio);
        getViewGroup().requestLayout();
    }

    private void calculateRatio(int widthRatio, int heightRatio) {
        if (widthRatio <= 0 || heightRatio <= 0) {
            this.mRatio = (9.0f) / 16;
        } else {
            this.mRatio = (heightRatio * 1.0f) / widthRatio;
        }
    }

    public boolean measure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getViewGroup() == null) return false;
        if (getViewGroup().getChildAt(0) == null) return false;
        this.mPlayerWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        this.mPlayerHeight = (int) (mPlayerWidth * mRatio);
        this.mChildWidth = View.MeasureSpec.makeMeasureSpec(mPlayerWidth, View.MeasureSpec.EXACTLY);
        this.mChildHeight = View.MeasureSpec.makeMeasureSpec(mPlayerHeight, View.MeasureSpec.EXACTLY);
        return true;
    }

    public boolean measureChild() {
        if (getViewGroup() == null) return false;
        View view = getViewGroup().getChildAt(0);
        if (view == null) return false;
        view.measure(mChildWidth, mChildHeight);
        return true;
    }

    public int getPlayerWidth() {
        return mPlayerWidth;
    }

    public int getPlayerHeight() {
        return mPlayerHeight;
    }

    public int getChildWidth() {
        return mChildWidth;
    }

    public int getChildHeight() {
        return mChildHeight;
    }

}
