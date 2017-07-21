package com.sunfusheng.droidplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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

import com.sunfusheng.droidplayer.dialog.DroidBrightnessDialog;
import com.sunfusheng.droidplayer.dialog.DroidProgressDialog;
import com.sunfusheng.droidplayer.dialog.DroidVolumeDialog;
import com.sunfusheng.droidplayer.listener.IDroidOnPlayerViewListener;
import com.sunfusheng.droidplayer.util.PlayerUtil;

import tv.danmaku.ijk.media.player.IMediaPlayer;

import static com.sunfusheng.droidplayer.util.PlayerUtil.getTimeString;

/**
 * Created by sunfusheng on 2017/3/8.
 */
public class DroidPlayerView extends DroidBasePlayerView implements View.OnClickListener,
        IDroidOnPlayerViewListener,
        SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "----> PlayerView";

    public ProgressBar loadingView;
    public ProgressBar bottomProgressBar;
    public ImageView ivCenterPlay;
    public ImageView ivReplay;
    public TextView tvTip;
    public LinearLayout llTopLayout;
    public ImageView ivBack;
    public TextView tvTitle;
    public LinearLayout llBottomLayout;
    public ImageView ivPlay;
    public TextView tvCurrentPosition;
    public SeekBar sbCurrentProgress;
    public TextView tvDuration;
    public ImageView ivFullScreen;

    public boolean isTopBottomLayoutShown;
    private boolean fromUser; // 是否是用户滑动SeekBar

    private static int DIALOG_DISMISS_TIME_MS = 100;

    private DroidBrightnessDialog mBrightnessDialog;
    private DroidProgressDialog mProgressDialog;
    private DroidVolumeDialog mVolumeDialog;
    private int curBrightness = -1;
    private float curProgress = -1;
    private int curVolume = -1;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
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
        View view = inflater.inflate(R.layout.layout_player_view, this, false);
        ivCenterPlay = (ImageView) view.findViewById(R.id.iv_center_play);
        loadingView = (ProgressBar) view.findViewById(R.id.loading_view);
        bottomProgressBar = (ProgressBar) view.findViewById(R.id.bottom_progress_bar);
        ivReplay = (ImageView) view.findViewById(R.id.iv_replay);
        tvTip = (TextView) view.findViewById(R.id.tv_tip);
        llTopLayout = (LinearLayout) view.findViewById(R.id.ll_top_layout);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        llBottomLayout = (LinearLayout) view.findViewById(R.id.ll_bottom_layout);
        ivPlay = (ImageView) view.findViewById(R.id.iv_play);
        tvCurrentPosition = (TextView) view.findViewById(R.id.tv_current_position);
        sbCurrentProgress = (SeekBar) view.findViewById(R.id.sb_current_progress);
        tvDuration = (TextView) view.findViewById(R.id.tv_duration);
        ivFullScreen = (ImageView) view.findViewById(R.id.iv_fullscreen);

        showTopLayout();
        hideBottomLayout();
        initBottomLayout();
        initListener();
        addDecorationView(view);
    }

    private void initBottomLayout() {
        setText(tvCurrentPosition, getTimeString(0));
        sbCurrentProgress.setSecondaryProgress(0);
        sbCurrentProgress.setProgress(0);
        bottomProgressBar.setVisibility(GONE);
        bottomProgressBar.setSecondaryProgress(0);
        bottomProgressBar.setProgress(0);
    }

    private void initListener() {
        ivCenterPlay.setOnClickListener(this);
        ivReplay.setOnClickListener(this);
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
            initBottomLayout();
            play();
        } else if (id == R.id.iv_play) {
            play();
        } else if (id == R.id.iv_back || id == R.id.tv_title) {
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
        setText(tvDuration, getTimeString(mDuration));
        setText(tvCurrentPosition, getTimeString(mCurrentPosition));
    }

    @Override
    public void onSingleTouch() {
        if (!canShowTopBottomLayout()) return;
        if (isTopBottomLayoutShown) {
            hideAllViews();
            removeHideTopBottomLayoutMessage();
        } else {
            showTopBottomLayout();
            addHideTopBottomLayoutMessage();
        }
    }

    @Override
    public void onStateChange(@DroidPlayerState int state) {
        hideAllViews();
        setImageResource(ivPlay, R.mipmap.icon_player_play);
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
        isTopBottomLayoutShown = false;
        setVisible(false, ivCenterPlay, llBottomLayout, loadingView, bottomProgressBar, llTopLayout, tvTip, ivReplay);
    }

    // 空闲状态
    public void setIdleState() {
        setVisible(true, ivCenterPlay);
        showTopLayout();
        hideBottomLayout();
    }

    // 加载状态
    public void setLoadingState() {
        if (mCurrentPosition <= 0) {
            setVisible(true);
        }
        setVisible(true, loadingView, bottomProgressBar);
        showTopLayout();
        showBottomLayout();
    }

    // 播放状态
    public void setPlayingState() {
        setVisible(false, ivCenterPlay);
        showTopBottomLayout();
        setImageResource(ivPlay, R.mipmap.icon_player_pause);
    }

    // 暂停状态
    public void setPauseState() {
        showTopBottomLayout();
    }

    // 完成状态
    public void setCompleteState() {
        setVisible(true, ivReplay);
        hideTopBottomLayout();
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
        addHideTopBottomLayoutMessage();
        isTopBottomLayoutShown = true;
    }

    public void hideTopBottomLayout() {
        hideTopLayout();
        hideBottomLayout();
        removeHideTopBottomLayoutMessage();
        isTopBottomLayoutShown = false;
    }

    public boolean canShowTopBottomLayout() {
        if (state == DroidPlayerState.IDLE || state == DroidPlayerState.COMPLETE || state == DroidPlayerState.ERROR)
            return false;
        return true;
    }

    public void showTopLayout() {
        setVisible(!TextUtils.isEmpty(mTitle) || isFullScreen(), llTopLayout);
        setGone(!isFullScreen(), ivBack);
        if (!TextUtils.isEmpty(mTitle)) {
            setGone(false, tvTitle);
            setText(tvTitle, mTitle);
        } else {
            setGone(true, tvTitle);
        }
    }

    public void hideTopLayout() {
        setVisible(false, llTopLayout);
    }

    public void showBottomLayout() {
        setVisible(true, llBottomLayout);
        setVisible(false, bottomProgressBar);
    }

    public void hideBottomLayout() {
        setVisible(false, llBottomLayout);
        setVisible(canShowTopBottomLayout(), bottomProgressBar);
    }

    public void addHideTopBottomLayoutMessage() {
        removeHideTopBottomLayoutMessage();
        mHandler.sendEmptyMessageDelayed(TYPE_HIDE_TOP_BOTTOM_LAYOUT, TIME_HIDE_TOP_BOTTOM_LAYOUT);
    }

    public void removeHideTopBottomLayoutMessage() {
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
        setText(tvCurrentPosition, getTimeString(position));
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
        removeHideTopBottomLayoutMessage();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (fromUser) {
            showBottomLayout();
            seekToPosition(seekBar.getProgress() * 1.0f / 100);
        }
    }

    private void seekToPosition(float endPercent) {
        if (endPercent < 0f) endPercent = 0f;
        if (endPercent > 1f) endPercent = 1f;
        int curPosition = (int) (endPercent * mDuration);
        onPositionChange(curPosition);
        seekTo(curPosition);
        if (!isPlaying()) {
            start();
        }
    }

    @Override
    public void onScrollBrightness(float stepPercent, boolean isUp) {
        super.onScrollBrightness(stepPercent, isUp);
        if (curBrightness == -1) {
            curBrightness = PlayerUtil.getWindowBrightness(getContext());
        }
        int endBrightness = (int) (curBrightness + stepPercent * 255);
        PlayerUtil.changeWindowBrightness(getContext(), endBrightness);

        if (mBrightnessDialog == null)
            mBrightnessDialog = new DroidBrightnessDialog(getContext());
        mBrightnessDialog.show(endBrightness);

        if (isUp) {
            curBrightness = -1;
            mHandler.postDelayed(() -> {
                mBrightnessDialog.dismiss();
            }, DIALOG_DISMISS_TIME_MS);
        }
    }

    @Override
    public void onScrollProgress(float stepPercent, boolean isUp) {
        super.onScrollProgress(stepPercent, isUp);
        if (getMediaPlayer() == null) return;
        if (curProgress == -1) {
            curProgress = sbCurrentProgress.getProgress() * 1.0f / 100;
        }
        float endPercent = curProgress + stepPercent;
        long endPosition = (long) (endPercent * mDuration);

        if (isTopBottomLayoutShown) {
            showBottomLayout();
            addHideTopBottomLayoutMessage();
        }
        onPositionChange(endPosition);

        if (mProgressDialog == null)
            mProgressDialog = new DroidProgressDialog(getContext());
        mProgressDialog.show(getMediaPlayer(), endPosition, endPercent);

        if (isUp) {
            curProgress = -1;
            seekToPosition(endPercent);
            mHandler.postDelayed(() -> {
                mProgressDialog.dismiss();
            }, DIALOG_DISMISS_TIME_MS);
        }
    }

    @Override
    public void onScrollVolume(float stepPercent, boolean isUp) {
        super.onScrollVolume(stepPercent, isUp);
        if (mVolumeDialog == null) {
            mVolumeDialog = new DroidVolumeDialog(getContext());
        }
        if (curVolume == -1) {
            curVolume = mVolumeDialog.getCurVolume();
        }
        mVolumeDialog.show((int) (curVolume + stepPercent * mVolumeDialog.getMaxVolume()));
        if (isUp) {
            curVolume = -1;
            mHandler.postDelayed(() -> {
                mVolumeDialog.dismiss();
            }, DIALOG_DISMISS_TIME_MS);
        }
    }

    @Override
    public void enterFullScreen() {
        super.enterFullScreen();
        setImageResource(ivFullScreen, R.mipmap.icon_player_quit_fullscreen);
        addHideTopBottomLayoutMessage();
        showTopLayout();
    }

    @Override
    public void quitFullScreen() {
        super.quitFullScreen();
        setImageResource(ivFullScreen, R.mipmap.icon_player_enter_fullscreen);
        addHideTopBottomLayoutMessage();
        showTopLayout();
    }
}
