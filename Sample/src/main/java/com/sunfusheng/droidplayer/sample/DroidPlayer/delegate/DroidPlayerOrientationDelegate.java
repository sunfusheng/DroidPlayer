package com.sunfusheng.droidplayer.sample.DroidPlayer.delegate;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.OrientationEventListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidBasePlayerView;
import com.sunfusheng.droidplayer.sample.DroidPlayer.util.PlayerUtil;

import static com.sunfusheng.droidplayer.sample.DroidPlayer.util.PlayerUtil.getActivity;

/**
 * Created by sunfusheng on 2017/3/9.
 */
public class DroidPlayerOrientationDelegate {

    private Context mContext;
    private Activity mActivity;
    private DroidBasePlayerView mPlayerView;
    private ViewGroup mFullScreenContainer;
    private ViewGroup mParent; // 播放器父容器
    private int mIndexInParent = 0;
    private ViewGroup.LayoutParams mLayoutParams; // 播放器布局参数

    private boolean isFullScreen; // 是否是全屏
    private boolean isAutoRotate; // 是否自动旋转屏幕
    private int mCurrentScreenType; // 当前屏幕状态
    private OrientationEventListener mOrientationEventListener;

    public DroidPlayerOrientationDelegate(DroidBasePlayerView playerView) {
        this.mPlayerView = playerView;
        this.mContext = playerView.getContext();
        this.mActivity = PlayerUtil.getActivity(mContext);
        initListener();
    }

    private void initListener() {
        if (mActivity == null) return;
        mCurrentScreenType = mActivity.getRequestedOrientation();
        mOrientationEventListener = new OrientationEventListener(mActivity) {
            @Override
            public void onOrientationChanged(int orientation) {
                if ((orientation > 0 && orientation < 30) || orientation > 330) {   //竖屏
                    //如果当前已经是竖屏状态，直接返回
                    if (mCurrentScreenType == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                        return;
                    }

                    //只有在自动旋转状态处理逻辑
                    if (isAutoRotate) {
                        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }

                } else if (orientation > 240 && orientation < 310) {                //横屏
                    //如果当前已经是横屏状态，直接返回
                    if (mCurrentScreenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        return;
                    }

                    //当前是自动模式 或者 手动模式但是屏幕是反向横屏模式
                    if (isAutoRotate ||
                            (!isAutoRotate && mCurrentScreenType == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)) {
                        setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }

                } else if (orientation > 50 && orientation < 130) {                 //反向横屏
                    //如果当前已经是反向横屏状态，直接返回
                    if (mCurrentScreenType == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                        return;
                    }

                    //当前是自动模式 或者 手动模式但是屏幕是横屏模式
                    if (isAutoRotate ||
                            (!isAutoRotate && mCurrentScreenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)) {

                        setOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    }
                }
            }
        };
        setAutoRotateEnable(isAutoRotate);
    }

    // 进入全屏
    public void enterFullScreen() {
        if (isFullScreen || mActivity == null) return;
        isFullScreen = true;
        hideActionBar(mContext);
        mParent = ((ViewGroup) mPlayerView.getParent());
        mIndexInParent = mParent.indexOfChild(mPlayerView);
        mLayoutParams = mPlayerView.getLayoutParams();
        mParent.removeView(mPlayerView);
        mFullScreenContainer = getContentView(mActivity);
        mFullScreenContainer.addView(mPlayerView);
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    // 退出全屏
    public void quitFullScreen() {
        if (!isFullScreen || mActivity == null) return;
        isFullScreen = false;
        showActionBar(mContext);
        mFullScreenContainer.removeView(mPlayerView);
        mParent.addView(mPlayerView, mLayoutParams);
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private ViewGroup getContentView(Activity activity) {
        return (ViewGroup) (activity).findViewById(Window.ID_ANDROID_CONTENT);
    }

    public void setAutoRotateEnable(boolean isEnable) {
        isAutoRotate = isEnable;
        if (isEnable) {
            mOrientationEventListener.enable();
        } else {
            mOrientationEventListener.disable();
        }
    }

    public void setOrientation(int orientation) {
        if (mActivity == null) return;
        mCurrentScreenType = orientation;
        mActivity.setRequestedOrientation(orientation);
    }

    private void showActionBar(Context context) {
        if (context == null) return;
        if (getActivity(context) == null) return;
        if (context instanceof AppCompatActivity) {
            android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        } else if (context instanceof Activity) {
            android.app.ActionBar actionBar = ((Activity) context).getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
        getActivity(context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void hideActionBar(Context context) {
        if (context == null) return;
        if (getActivity(context) == null) return;
        if (context instanceof AppCompatActivity) {
            android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        } else if (context instanceof Activity) {
            android.app.ActionBar actionBar = ((Activity) context).getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
        getActivity(context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
