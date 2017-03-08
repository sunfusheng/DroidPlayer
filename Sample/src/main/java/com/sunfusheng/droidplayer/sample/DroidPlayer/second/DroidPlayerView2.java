package com.sunfusheng.droidplayer.sample.DroidPlayer.second;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidPlayerState;
import com.sunfusheng.droidplayer.sample.DroidPlayer.listener.DroidOnPlayerViewListener;
import com.sunfusheng.droidplayer.sample.DroidPlayer.util.PlayerUtil;
import com.sunfusheng.droidplayer.sample.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by sunfusheng on 2017/3/8.
 */
public class DroidPlayerView2 extends DroidBasePlayerView implements View.OnClickListener,
        DroidOnPlayerViewListener,
        SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "----> PlayerView2";

    public ProgressBar loadingView;
    public ProgressBar bottomProgressBar;
    public ImageView ivCenterPlay;
    public ImageView ivReplay;
    public TextView tvTitle;
    public TextView tvTip;
    public LinearLayout llBottomLayout;
    public ImageView ivPlay;
    public TextView tvCurrentPosition;
    public SeekBar sbCurrentProgress;
    public TextView tvDuration;

    public static final int TIME_DELAY = 1000; // 进度更新的时间延时
    public static final int TIME_INTERVAL = 1000; // 进度更新的时间间隔

    public boolean isShowBottomLayout;
    private boolean fromUser; // 是否是用户滑动SeekBar
    private int preState; // 滑动SeekBar前，播放器状态
    private int mLastBufferingProgress; // 上一次缓冲进度

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
                    setVisible(state == DroidPlayerState.LOADING, tvTitle);
                    break;
            }
        }
    };

    public DroidPlayerView2(@NonNull Context context) {
        this(context, null);
    }

    public DroidPlayerView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DroidPlayerView2(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.droid_player_layout, this, false);

        ivCenterPlay = (ImageView) view.findViewById(R.id.iv_center_play);
        loadingView = (ProgressBar) view.findViewById(R.id.loading_view);
        bottomProgressBar = (ProgressBar) view.findViewById(R.id.bottom_progress_bar);
        ivReplay = (ImageView) view.findViewById(R.id.iv_replay);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTip = (TextView) view.findViewById(R.id.tv_tip);
        llBottomLayout = (LinearLayout) view.findViewById(R.id.ll_bottom_layout);
        ivPlay = (ImageView) view.findViewById(R.id.iv_play);
        tvCurrentPosition = (TextView) view.findViewById(R.id.tv_current_position);
        sbCurrentProgress = (SeekBar) view.findViewById(R.id.sb_current_progress);
        tvDuration = (TextView) view.findViewById(R.id.tv_duration);

        ivCenterPlay.setOnClickListener(this);
        ivReplay.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        sbCurrentProgress.setOnSeekBarChangeListener(this);
        setOnPlayerViewListener(this);

        setText(tvCurrentPosition, PlayerUtil.getTimeString(0));
        sbCurrentProgress.setSecondaryProgress(0);
        sbCurrentProgress.setProgress(0);
        bottomProgressBar.setSecondaryProgress(0);
        bottomProgressBar.setProgress(0);

        addDecorationView(view);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_center_play) {
            play();
        } else if (id == R.id.iv_replay) {
            play();
        } else if (id == R.id.iv_play) {
            play();
        }
    }

    @Override
    public void onInfoCallback(IMediaPlayer mp, int what, int extra) {
        setText(tvDuration, PlayerUtil.getTimeString(mDuration));
        setText(tvCurrentPosition, PlayerUtil.getTimeString(mCurrentPosition));
    }

    @Override
    public void onTextureViewClick() {
        showBottomLayout();
    }

    @Override
    public void onStateChange(@DroidPlayerState int state) {
        hideAllViews();
        setImageResource(ivPlay, R.mipmap.droid_player_play);
        isShowBottomLayout = false;
        switch (state) {
            case DroidPlayerState.IDLE:
                setIdleState();
                break;
            case DroidPlayerState.LOADING:
                setLoadingState();
                break;
            case DroidPlayerState.PLAYING:
                setPlayingState();
                break;
            case DroidPlayerState.PAUSE:
                setPauseState();
                break;
            case DroidPlayerState.COMPLETE:
                setCompleteState();
                break;
            case DroidPlayerState.ERROR:
                setErrorState();
                break;
        }
    }

    // 隐藏所有的Views
    public void hideAllViews() {
        setVisible(false, ivCenterPlay, llBottomLayout, loadingView, bottomProgressBar, tvTitle, tvTip, ivReplay);
    }

    // 空闲状态
    public void setIdleState() {
        setVisible(true, ivCenterPlay);
        showTitle();
    }

    // 加载状态
    public void setLoadingState() {
        if (mCurrentPosition <= 0) {
            setVisible(true);
        }
        setVisible(true, loadingView, bottomProgressBar);
        showTitle();
    }

    // 播放状态
    public void setPlayingState() {
        setVisible(false, ivCenterPlay);
        showBottomLayout();
        setImageResource(ivPlay, R.mipmap.droid_player_pause);
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
        setVisible(true, tvTip, ivReplay);
        setText(tvTip, R.string.player_replay_tip);
        showTitle();
    }

    // 错误状态
    public void setErrorState() {
        setVisible(true, tvTip, ivReplay);
        setText(tvTip, R.string.player_error_tip);
        showTitle();
    }

    @Override
    public void setVideoTitle(String title) {
        super.setVideoTitle(title);
        showTitle();
    }

    public void showTitle() {
        setText(tvTitle, mTitle);
        setVisible(!TextUtils.isEmpty(mTitle), tvTitle);
    }

    public boolean showBottomLayout() {
        if (isShowBottomLayout) return false;
        if (state != DroidPlayerState.PLAYING) return false;
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

    @Override
    public void onCacheChange(int progress) {
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        if (mLastBufferingProgress >= progress) return;
        this.mLastBufferingProgress = progress;
        sbCurrentProgress.setSecondaryProgress(progress);
        bottomProgressBar.setSecondaryProgress(progress);
    }

    @Override
    public void onPositionChange(long position) {
        if (position > mDuration) position = mDuration;
        mCurrentPosition = position;
        setText(tvCurrentPosition, PlayerUtil.getTimeString(position));
        int progress = (int) (position * 100f / mDuration);
        if (progress > 100) progress = 100;
        sbCurrentProgress.setProgress(progress);
        bottomProgressBar.setProgress(progress);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.fromUser = fromUser;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (fromUser) {
            preState = state;
            onPositionChange(seekBar.getProgress());
            showBottomLayout();
            int time = (int) (seekBar.getProgress() * mDuration / 100);
            seekTo(time);
        }
    }
}