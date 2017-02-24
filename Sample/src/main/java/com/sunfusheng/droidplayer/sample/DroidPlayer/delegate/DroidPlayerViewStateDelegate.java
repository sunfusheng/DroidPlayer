package com.sunfusheng.droidplayer.sample.DroidPlayer.delegate;

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
    
    public int state; // 当前视频状态
    public boolean isShowBottomLayout;

    public DroidProgressBarDelegate progressBarDelegate;
    public DroidPlayerBottomLayoutDelegate bottomLayoutDelegate;

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

    // 空闲状态
    public void setIdleState() {
        setVisible(true, fullScreenTransparentBg, ivCenterPlay, llBottomLayout);
        setVisible(false, loadingView, bottomProgressBar, tvTipUp, tvTipDown, ivReplay);
    }

    // 加载状态
    public void setLoadingState() {
        setVisible(true, fullScreenTransparentBg, loadingView, llBottomLayout, bottomProgressBar);
        setVisible(false, ivCenterPlay, tvTipUp, tvTipDown, ivReplay);
    }

    // 播放状态
    public void setPlayingState() {
        setVisible(true, bottomProgressBar);
        setVisible(false, fullScreenTransparentBg, ivCenterPlay, loadingView, tvTipUp, tvTipDown, ivReplay);
        bottomLayoutDelegate.setPlayingState(true);
    }

    // 暂停状态
    public void setPauseState() {
        setVisible(true, fullScreenTransparentBg, ivCenterPlay, llBottomLayout);
        setVisible(false, loadingView, bottomProgressBar, tvTipUp, tvTipDown, ivReplay);
    }

    // 完成状态
    public void setCompleteState() {
        setVisible(true, fullScreenTransparentBg, tvTipDown, ivReplay);
        setVisible(false, ivCenterPlay, loadingView, llBottomLayout, bottomProgressBar, tvTipUp);
        setText(tvTipDown, R.string.player_replay_tip);
        progressBarDelegate.init();
    }

    // 错误状态
    public void setErrorState() {
        setVisible(true, fullScreenTransparentBg, tvTipDown, ivReplay);
        setVisible(false, ivCenterPlay, loadingView, llBottomLayout, bottomProgressBar, tvTipUp);
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

    public void setLlBottomLayoutVisible(boolean isVisible) {
        setVisible(isVisible, llBottomLayout);
        setVisible(!isVisible, bottomProgressBar);
    }

    public void showLlBottomLayout() {
        isShowBottomLayout = !isShowBottomLayout;
        setLlBottomLayoutVisible(isShowBottomLayout);
    }

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
