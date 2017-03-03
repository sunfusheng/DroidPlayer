package com.sunfusheng.droidplayer.sample.DroidPlayer.delegate;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidPlayerView;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidTextureView;
import com.sunfusheng.droidplayer.sample.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by sunfusheng on 2017/1/16.
 */
public class DroidPlayerViewStateDelegate extends BaseViewDelegate implements IViewDelegate {

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
    public ImageView ivCoverImage;
    public ProgressBar loadingView;
    public ImageView ivCenterPlay;
    public ImageView ivReplay;
    public TextView tvTitle;
    public TextView tvTip;
    private ProgressBar bottomProgressBar;
    private LinearLayout llBottomLayout;

    public int state; // 播放器状态
    public boolean isShowBottomLayout;

    public static final int TIME_DELAY = 1000; // 进度更新的时间延时
    public static final int TIME_INTERVAL = 1000; // 进度更新的时间间隔

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
                    setVisible(state == STATE.LOADING, tvTitle);
                    break;
            }
        }
    };

    public DroidPlayerViewStateDelegate(DroidPlayerView playView) {
        this.state = STATE.IDLE;
        this.playView = playView;
        bottomLayoutDelegate = new DroidPlayerBottomLayoutDelegate();
    }

    @Override
    public void init() {
        bottomLayoutDelegate.init();
    }

    @Override
    public void unInit() {
        removeBottomLayoutMessage();
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
                Log.d(TAG, "STATE IDLE");
                setIdleState();
                break;
            case STATE.LOADING:
                Log.d(TAG, "STATE LOADING");
                setLoadingState();
                break;
            case STATE.PLAYING:
                Log.d(TAG, "STATE PLAYING");
                setPlayingState();
                break;
            case STATE.PAUSE:
                Log.d(TAG, "STATE PAUSE");
                setPauseState();
                break;
            case STATE.COMPLETE:
                Log.d(TAG, "STATE COMPLETE");
                setCompleteState();
                break;
            case STATE.ERROR:
                Log.d(TAG, "STATE ERROR");
                setErrorState();
                break;
        }
    }

    // 隐藏所有的Views
    public void hideAllViews() {
        setVisible(false, ivCoverImage, ivCenterPlay, llBottomLayout, loadingView, bottomProgressBar, tvTitle, tvTip, ivReplay);
    }

    // 空闲状态
    public void setIdleState() {
        setVisible(true, ivCoverImage, ivCenterPlay);
        showTitle();
    }

    // 加载状态
    public void setLoadingState() {
        if (DroidMediaPlayer.getInstance().getCurrentPosition() <= 0) {
            setVisible(true, ivCoverImage);
        }
        setVisible(true, loadingView, bottomProgressBar);
        showTitle();
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
        setVisible(true, ivCenterPlay, llBottomLayout);
        showTitle();
    }

    // 完成状态
    public void setCompleteState() {
        setVisible(true, ivCoverImage, tvTip, ivReplay);
        setText(tvTip, R.string.player_replay_tip);
        showTitle();
    }

    // 错误状态
    public void setErrorState() {
        setVisible(true, ivCoverImage, tvTip, ivReplay);
        setText(tvTip, R.string.player_error_tip);
        showTitle();
    }

    public void setBottomProgressBar(ProgressBar progressBar) {
        this.bottomProgressBar = progressBar;
        bottomLayoutDelegate.setProgressBar(progressBar);
    }

    public void setLlBottomLayout(LinearLayout llBottomLayout) {
        this.llBottomLayout = llBottomLayout;
        bottomLayoutDelegate.setLlBottomLayout(llBottomLayout);
    }

    public void setTitle(String title) {
        setText(tvTitle, title);
        DroidMediaPlayer.getInstance().setTitle(title);
        setVisible(!TextUtils.isEmpty(title), tvTitle);
    }

    public void showTitle() {
        setTitle(DroidMediaPlayer.getInstance().getTitle());
    }

    public boolean showBottomLayout() {
        if (isShowBottomLayout) return false;
        if (state != STATE.PLAYING) return false;
        isShowBottomLayout = true;
        addBottomLayoutMessage();
        setVisible(true, llBottomLayout);
        setVisible(false, bottomProgressBar);
        showTitle();
        return true;
    }

    public void addBottomLayoutMessage() {
        removeBottomLayoutMessage();
        mHandler.sendEmptyMessageDelayed(TYPE_HIDE_BOTTOM_LAYOUT, TIME_HIDE_BOTTOM_LAYOUT);
    }

    public void removeBottomLayoutMessage() {
        if (mHandler.hasMessages(TYPE_HIDE_BOTTOM_LAYOUT)) {
            mHandler.removeMessages(TYPE_HIDE_BOTTOM_LAYOUT);
        }
    }

    // 设置视频时长
    public void setDuration(long duration) {
        DroidMediaPlayer.getInstance().setDuration(duration);
        bottomLayoutDelegate.setDuration(duration);
    }

    // 设置当前播放位置
    public void setCurrentPosition(long currentPosition) {
        DroidMediaPlayer.getInstance().setCurrentPosition(currentPosition);
        bottomLayoutDelegate.setCurrentPosition(currentPosition);
    }

    // 设置缓冲进度
    public void setBufferingProgress(int progress) {
        bottomLayoutDelegate.setBufferingProgress(progress);
    }

    // 显示封面图片
    public void showCoverImage(String url) {
        if (ivCoverImage == null) return;
        if (TextUtils.isEmpty(url)) {
            ivCoverImage.setVisibility(View.INVISIBLE);
            return;
        }
        ivCoverImage.setVisibility(View.VISIBLE);
        Glide.with(ivCoverImage.getContext())
                .load(url)
                .crossFade()
                .fallback(R.color.player_transparent)
                .error(R.color.player_transparent)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivCoverImage);
    }

}
