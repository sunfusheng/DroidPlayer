package com.sunfusheng.droidplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sunfusheng.droidplayer.delegate.DroidViewMeasureDelegate;

/**
 * Created by sunfusheng on 2017/3/3.
 */
public class DroidImageView extends ImageView {

    private DroidViewMeasureDelegate mMeasureDelegate;

    public DroidImageView(Context context) {
        this(context, null);
    }

    public DroidImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DroidImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mMeasureDelegate = new DroidViewMeasureDelegate(this);
    }

    public void setVideoSize(int videoWidth, int videoHeight) {
        mMeasureDelegate.setViewSize(videoWidth, videoHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasureDelegate.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mMeasureDelegate.getMeasureWidth(), mMeasureDelegate.getMeasureHeight());
    }

    public int getMeasureWidth() {
        return mMeasureDelegate.getMeasureWidth();
    }

    public int getMeasureHeight() {
        return mMeasureDelegate.getMeasureHeight();
    }
}
