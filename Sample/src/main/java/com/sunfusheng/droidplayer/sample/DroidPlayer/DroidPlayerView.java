package com.sunfusheng.droidplayer.sample.DroidPlayer;

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

import com.sunfusheng.droidplayer.sample.DroidPlayer.listener.DroidOnPlayerViewListener;
import com.sunfusheng.droidplayer.sample.DroidPlayer.util.PlayerUtil;
import com.sunfusheng.droidplayer.sample.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by sunfusheng on 2017/3/8.
 */
public class DroidPlayerView extends DroidBasePlayerView implements View.OnClickListener,
        DroidOnPlayerViewListener,
        SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "----> PlayerView";

    public ProgressBar loadingView;
    public ProgressBar bottomProgressBar;
    public ImageView ivCenterPlay;
    public ImageView ivReplay;
    public TextView tvTip;
    public LinearLayout llTopLayout;
    public ImageView ivBack;
    public ImageView ivBackArrow;
    public TextView tvTitle;
    public LinearLayout llBottomLayout;
    public ImageView ivPlay;
    public TextView tvCurrentPosition;
    public SeekBar sbCurrentProgress;
    public TextView tvDuration;
    public ImageView ivFullScreen;

    public boolean isTopBottomLayoutShown;
    private boolean fromUser; // 是否是用户滑动SeekBar
    private int preState; // 滑动SeekBar前，播放器状态

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_HIDE_TOP_BOTTOM_LAYOUT:
                    hideTopBottomLayout();
                    break;
            }
        }
    };

    public DroidPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public DroidPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DroidPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.droid_player_layout, this, false);
        ivCenterPlay = (ImageView) view.findViewById(R.id.iv_center_play);
        loadingView = (ProgressBar) view.findViewById(R.id.loading_view);
        bottomProgressBar = (ProgressBar) view.findViewById(R.id.bottom_progress_bar);
        ivReplay = (ImageView) view.findViewById(R.id.iv_replay);
        tvTip = (TextView) view.findViewById(R.id.tv_tip);
        llTopLayout = (LinearLayout) view.findViewById(R.id.ll_top_layout);
        ivBackArrow = (ImageView) view.findViewById(R.id.iv_back_arrow);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        llBottomLayout = (LinearLayout) view.findViewById(R.id.ll_bottom_layout);
        ivPlay = (ImageView) view.findViewById(R.id.iv_play);
        tvCurrentPosition = (TextView) view.findViewById(R.id.tv_current_position);
        sbCurrentProgress = (SeekBar) view.findViewById(R.id.sb_current_progress);
        tvDuration = (TextView) view.findViewById(R.id.tv_duration);
        ivFullScreen = (ImageView) view.findViewById(R.id.iv_fullscreen);

        showTopLayout();
        initBottomLayout();
        initListener();
        addDecorationView(view);
    }

    private void initBottomLayout() {
        llBottomLayout.setVisibility(GONE);
        setText(tvCurrentPosition, PlayerUtil.getTimeString(0));
        sbCurrentProgress.setSecondaryProgress(0);
        sbCurrentProgress.setProgress(0);
        bottomProgressBar.setVisibility(GONE);
        bottomProgressBar.setSecondaryProgress(0);
        bottomProgressBar.setProgress(0);
    }

    private void initListener() {
        ivCenterPlay.setOnClickListener(this);
        ivReplay.setOnClickListener(this);
        ivBackArrow.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        sbCurrentProgress.setOnSeekBarChangeListener(this);
        ivFullScreen.setOnClickListener(this);
        setOnPlayerViewListener(this);
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
        } else if (id == R.id.iv_back_arrow || id == R.id.iv_back || id == R.id.tv_title) {
            if (isFullScreen()) {
                quitFullScreen();
            }
        } else if (id == R.id.iv_fullscreen) {
            if (isFullScreen()) {
                quitFullScreen();
            } else {
                enterFullScreen();
            }
        }
    }

    @Override
    public void play(String url) {
        super.play(url);
    }

    @Override
    public void onInfoCallback(IMediaPlayer mp, int what, int extra) {
        setText(tvDuration, PlayerUtil.getTimeString(mDuration));
        setText(tvCurrentPosition, PlayerUtil.getTimeString(mCurrentPosition));
    }

    @Override
    public void onSingleTouch() {
        if (isTopBottomLayoutShown) {
            hideTopBottomLayout();
        } else {
            showTopBottomLayout();
        }
    }

    @Override
    public void onStateChange(@DroidPlayerState int state) {
        hideAllViews();
        setImageResource(ivPlay, R.mipmap.droid_player_play);
        isTopBottomLayoutShown = false;
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
        setVisible(false, ivCenterPlay, llBottomLayout, loadingView, bottomProgressBar, llTopLayout, tvTip, ivReplay);
    }

    // 空闲状态
    public void setIdleState() {
        setVisible(true, ivCenterPlay);
        showTopLayout();
    }

    // 加载状态
    public void setLoadingState() {
        if (mCurrentPosition <= 0) {
            setVisible(true);
        }
        setVisible(true, loadingView, bottomProgressBar);
        showTopLayout();
    }

    // 播放状态
    public void setPlayingState() {
        setVisible(false, ivCenterPlay);
        showTopBottomLayout();
        setImageResource(ivPlay, R.mipmap.droid_player_pause);
    }

    // 暂停状态
    public void setPauseState() {
        if (mHandler.hasMessages(TYPE_HIDE_TOP_BOTTOM_LAYOUT)) {
            mHandler.removeMessages(TYPE_HIDE_TOP_BOTTOM_LAYOUT);
        }
        isTopBottomLayoutShown = true;
        setVisible(true, ivCenterPlay, llBottomLayout);
        showTopLayout();
    }

    // 完成状态
    public void setCompleteState() {
        setVisible(true, ivCenterPlay);
        showTopLayout();
        initBottomLayout();
    }

    // 错误状态
    public void setErrorState() {
        setVisible(true, tvTip, ivReplay);
        setText(tvTip, R.string.player_error_tip);
        showTopLayout();
        initBottomLayout();
    }

    @Override
    public void setVideoTitle(String title) {
        super.setVideoTitle(title);
        showTopLayout();
    }

    public void showTopBottomLayout() {
        showTopLayout();
        showBottomLayout();
        addBottomLayoutMessage();
        isTopBottomLayoutShown = true;
    }

    public void hideTopBottomLayout() {
        hideTopLayout();
        hideBottomLayout();
        removeBottomLayoutMessage();
        isTopBottomLayoutShown = false;
    }

    public void showTopLayout() {
        setVisible(!TextUtils.isEmpty(mTitle) || isFullScreen(), llTopLayout);
        if (!TextUtils.isEmpty(mTitle)) {
            setGone(true, ivBackArrow);
            setGone(!isFullScreen(), ivBack);
            setGone(false, tvTitle);
            setText(tvTitle, mTitle);
        } else {
            setGone(!isFullScreen(), ivBackArrow);
            setGone(true, ivBack, tvTitle);
        }
    }

    public void hideTopLayout() {
        setVisible((!isPlaying() && !TextUtils.isEmpty(mTitle)), llTopLayout);
    }

    public void showBottomLayout() {
        setVisible(true, llBottomLayout);
        setVisible(false, bottomProgressBar);
    }

    public void hideBottomLayout() {
        setVisible(false, llBottomLayout);
        setVisible(isPlaying(), bottomProgressBar);
    }

    public void addBottomLayoutMessage() {
        removeBottomLayoutMessage();
        mHandler.sendEmptyMessageDelayed(TYPE_HIDE_TOP_BOTTOM_LAYOUT, TIME_HIDE_TOP_BOTTOM_LAYOUT);
    }

    public void removeBottomLayoutMessage() {
        if (mHandler.hasMessages(TYPE_HIDE_TOP_BOTTOM_LAYOUT)) {
            mHandler.removeMessages(TYPE_HIDE_TOP_BOTTOM_LAYOUT);
        }
    }

    @Override
    public void onCacheChange(int progress) {
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
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
        removeBottomLayoutMessage();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (fromUser) {
            preState = state;
            int time = (int) (seekBar.getProgress() * mDuration / 100);
            onPositionChange(time);
            showBottomLayout();
            seekTo(time);
        }
    }

    @Override
    public void enterFullScreen() {
        super.enterFullScreen();
        setImageResource(ivFullScreen, R.mipmap.droid_player_quit_fullscreen);
        addBottomLayoutMessage();
        showTopLayout();
    }

    @Override
    public void quitFullScreen() {
        super.quitFullScreen();
        setImageResource(ivFullScreen, R.mipmap.droid_player_enter_fullscreen);
        addBottomLayoutMessage();
        showTopLayout();
    }
}
