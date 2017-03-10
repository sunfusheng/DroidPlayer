package com.sunfusheng.droidplayer.sample.DroidPlayer.delegate;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidBasePlayerView;
import com.sunfusheng.droidplayer.sample.DroidPlayer.util.PlayerUtil;

/**
 * Created by sunfusheng on 2017/3/9.
 */
public class DroidPlayerOrientationDelegate {

    private Context mContext;
    private Activity mActivity;
    private DroidBasePlayerView mPlayerView;
    private ViewGroup mPlayerViewParent;
    private ViewGroup.LayoutParams mPlayerViewLayoutParams;
    private ViewGroup mFullScreenContainer;
    private int mFullScreenContainerId = View.NO_ID;

    private boolean isFullScreen; // 是否是全屏
    private boolean isAutoRotationEnable; // 是否允许自动旋转屏幕
    private int mScreenOrientation; // 当前屏幕方向
    private OrientationEventListener mOrientationEventListener;

    public DroidPlayerOrientationDelegate(DroidBasePlayerView playerView) {
        this.mPlayerView = playerView;
        this.mContext = playerView.getContext();
        this.mActivity = PlayerUtil.getActivity(mContext);
        initListener();
    }

    private void initListener() {
        if (mActivity == null) return;
        mScreenOrientation = mActivity.getRequestedOrientation();
        mOrientationEventListener = new OrientationEventListener(mActivity) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation > 225 && orientation < 315) { // 横屏
                    if (mScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) return;
                    if (isAutoRotationEnable) {
                        setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                } else if (orientation > 45 && orientation < 135) { // 反向横屏
                    if (mScreenOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
                        return;
                    if (isAutoRotationEnable) {
                        setOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    }
                }
            }
        };
        setAutoRotationEnable(isAutoRotationEnable);
    }

    // 进入全屏
    public void enterFullScreen() {
        if (isFullScreen || mActivity == null) return;
        isFullScreen = true;
        hideActionBar(mActivity);

        mPlayerViewParent = (ViewGroup) mPlayerView.getParent();
        mPlayerViewLayoutParams = mPlayerView.getLayoutParams();
        mPlayerViewParent.removeView(mPlayerView);
        mFullScreenContainer = getContentView(mActivity);
        mFullScreenContainer.addView(mPlayerView);

        setAutoRotationEnable(true);
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    // 退出全屏
    public void quitFullScreen() {
        if (!isFullScreen || mActivity == null) return;
        isFullScreen = false;
        showActionBar(mActivity);

        mFullScreenContainer.removeView(mPlayerView);
        mPlayerViewParent.addView(mPlayerView, mPlayerViewLayoutParams);

        setAutoRotationEnable(false);
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public boolean isAutoRotationEnable() {
        return isAutoRotationEnable;
    }

    public int getScreenOrientation() {
        return mScreenOrientation;
    }

    public void setAutoRotationEnable(boolean isEnable) {
        isAutoRotationEnable = isEnable;
        if (isEnable) {
            mOrientationEventListener.enable();
        } else {
            mOrientationEventListener.disable();
        }
    }

    public void setOrientation(int orientation) {
        if (mActivity == null) return;
        mScreenOrientation = orientation;
        mActivity.setRequestedOrientation(orientation);
    }

    private ViewGroup getContentView(Activity activity) {
        return (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
    }

    private void showActionBar(Activity activity) {
        if (activity == null) return;
        if (activity instanceof AppCompatActivity) {
            android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) actionBar.show();
        } else {
            android.app.ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) actionBar.show();
        }
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void hideActionBar(Activity activity) {
        if (activity == null) return;
        if (activity instanceof AppCompatActivity) {
            android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) actionBar.hide();
        } else {
            android.app.ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) actionBar.hide();
        }
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
