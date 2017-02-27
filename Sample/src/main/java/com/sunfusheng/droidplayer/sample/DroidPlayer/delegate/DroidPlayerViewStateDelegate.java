package com.sunfusheng.droidplayer.sample.DroidPlayer.delegate;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidPlayerView;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidTextureView;
import com.sunfusheng.droidplayer.sample.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by sunfusheng on 2017/1/16.
 */
public class DroidPlayerViewStateDelegate extends DroidBaseViewDelegate implements IBaseDelegate {

    @IntDef({STATE.IDLE, STATE.LOADING, STATE.PLAYING, STATE.PAUSE, STATE.COMPLETE, STATE.ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATE {
        int IDLE = 0;
        int LOADING = 1;
        int PLAYING = 2;
        int PAUSE = 3;
        int COMPLETE = 4;
        int ERROR = 5;
    }

    private static final String TAG = "----> StateDelegate";

    public DroidPlayerView playView;
    public DroidTextureView textureView;
    public View fullScreenTransparentBg;
    public ProgressBar loadingView;
    private ProgressBar bottomProgressBar;
    public ImageView ivCenterPlay;
    public ImageView ivReplay;
    public TextView tvTipUp;
    public TextView tvTipDown;
    private LinearLayout llBottomLayout;

    public int state; // 当前视频状态
    public boolean isShowBottomLayout;

    public DroidProgressBarDelegate progressBarDelegate;
    public DroidPlayerBottomLayoutDelegate bottomLayoutDelegate;

    private static final int TYPE_HIDE_BOTTOM_LAYOUT = 10000;
    private static final int TIME_HIDE_BOTTOM_LAYOUT = 3000; // 3s

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_HIDE_BOTTOM_LAYOUT:
                    isShowBottomLayout = false;
                    setVisible(true, bottomProgressBar);
                    setVisible(false, llBottomLayout);
                    break;
            }
        }
    };

    public DroidPlayerViewStateDelegate(DroidPlayerView playView) {
        this.state = STATE.IDLE;
        this.playView = playView;
        progressBarDelegate = new DroidProgressBarDelegate();
        bottomLayoutDelegate = new DroidPlayerBottomLayoutDelegate();
    }

    @Override
    public void init() {
        progressBarDelegate.init();
        bottomLayoutDelegate.init();
    }

    @Override
    public void unInit() {
        progressBarDelegate.unInit();
        bottomLayoutDelegate.unInit();
    }

    public void setState(int state) {
        this.state = state;
        DroidMediaPlayer.getInstance().setState(state);
        bottomLayoutDelegate.setPlayingState(false);
        hideAllViews();
        isShowBottomLayout = false;
        switch (state) {
            case STATE.IDLE:
                setIdleState();
                break;
            case STATE.LOADING:
                setLoadingState();
                break;
            case STATE.PLAYING:
                setPlayingState();
                break;
            case STATE.PAUSE:
                setPauseState();
                break;
            case STATE.COMPLETE:
                setCompleteState();
                break;
            case STATE.ERROR:
                setErrorState();
                break;
        }
    }

    // 隐藏所有的Views
    public void hideAllViews() {
        setVisible(false, fullScreenTransparentBg, ivCenterPlay, llBottomLayout, loadingView, bottomProgressBar, tvTipUp, tvTipDown, ivReplay);
    }

    // 空闲状态
    public void setIdleState() {
        setVisible(true, fullScreenTransparentBg, ivCenterPlay);
    }

    // 加载状态
    public void setLoadingState() {
        setVisible(true, fullScreenTransparentBg, loadingView, bottomProgressBar);
    }

    // 播放状态
    public void setPlayingState() {
        setVisible(false, ivCenterPlay);
        showBottomLayout();
        bottomLayoutDelegate.setPlayingState(true);
    }

    // 暂停状态
    public void setPauseState() {
        if (mHandler.hasMessages(TYPE_HIDE_BOTTOM_LAYOUT)) {
            mHandler.removeMessages(TYPE_HIDE_BOTTOM_LAYOUT);
        }
        isShowBottomLayout = true;
        setVisible(true, fullScreenTransparentBg, ivCenterPlay, llBottomLayout);
    }

    // 完成状态
    public void setCompleteState() {
        setVisible(true, fullScreenTransparentBg, tvTipDown, ivReplay);
        setText(tvTipDown, R.string.player_replay_tip);
        progressBarDelegate.init();
    }

    // 错误状态
    public void setErrorState() {
        setVisible(true, fullScreenTransparentBg, tvTipDown, ivReplay);
        setText(tvTipDown, R.string.player_error_tip);
        progressBarDelegate.init();
    }

    public void setBottomProgressBar(ProgressBar progressBar) {
        this.bottomProgressBar = progressBar;
        progressBarDelegate.setProgressBar(progressBar);
    }

    public void setLlBottomLayout(LinearLayout llBottomLayout) {
        this.llBottomLayout = llBottomLayout;
        bottomLayoutDelegate.setLlBottomLayout(llBottomLayout);
    }

    public boolean showBottomLayout() {
        if (isShowBottomLayout || mHandler.hasMessages(TYPE_HIDE_BOTTOM_LAYOUT)) return false;
        if (!(state == STATE.LOADING || state == STATE.PLAYING)) return false;
        isShowBottomLayout = true;
        setVisible(true, llBottomLayout);
        setVisible(false, bottomProgressBar);
        mHandler.sendEmptyMessageDelayed(TYPE_HIDE_BOTTOM_LAYOUT, TIME_HIDE_BOTTOM_LAYOUT);
        return true;
    }

    public boolean hideBottomLayout() {
        if (!isShowBottomLayout || mHandler.hasMessages(TYPE_HIDE_BOTTOM_LAYOUT)) return false;
        mHandler.sendEmptyMessageDelayed(TYPE_HIDE_BOTTOM_LAYOUT, TIME_HIDE_BOTTOM_LAYOUT);
        return true;
    }

    // 设置视频时长
    public void setDuration(long duration) {
        progressBarDelegate.setDuration(duration);
        bottomLayoutDelegate.setDuration(duration);
    }

    // 设置缓冲进度
    public void setBufferingProgress(int progress) {
        progressBarDelegate.setBufferingProgress(progress);
        bottomLayoutDelegate.setBufferingProgress(progress);
    }

}
