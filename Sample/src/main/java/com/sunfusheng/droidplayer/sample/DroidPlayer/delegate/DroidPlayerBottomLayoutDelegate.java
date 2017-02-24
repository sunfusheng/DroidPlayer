package com.sunfusheng.droidplayer.sample.DroidPlayer.delegate;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.DroidPlayer.util.TimeUtil;
import com.sunfusheng.droidplayer.sample.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sunfusheng on 2017/2/24.
 */
public class DroidPlayerBottomLayoutDelegate extends DroidBaseViewDelegate implements IBaseDelegate {

    public WeakReference<LinearLayout> llBottomLayout;
    public ImageView ivPlay;
    public TextView tvCurrentPosition;
    public SeekBar sbCurrentProgress;
    public TextView tvDuration;

    private static final int TIME_DELAY = 1000; // 延时时间
    private static final int TIME_INTERVAL = 1000; // 间隔时间
    public long mDuration; // 视频总时间，毫秒
    private long mCurrentPosition; // 当前的播放位置
    private int mLastBufferingProgress; // 上一次缓冲进度

    private Timer mTimer;
    private ProgressTimerTask mTimerTask;

    public DroidPlayerBottomLayoutDelegate() {
    }

    public void setLlBottomLayout(LinearLayout llBottomLayout) {
        this.llBottomLayout = new WeakReference<LinearLayout>(llBottomLayout);
        if (getLinearLayout() != null) {
            ivPlay = (ImageView) getLinearLayout().findViewById(R.id.iv_play);
            tvCurrentPosition = (TextView) getLinearLayout().findViewById(R.id.tv_current_position);
            sbCurrentProgress = (SeekBar) getLinearLayout().findViewById(R.id.sb_current_progress);
            tvDuration = (TextView) getLinearLayout().findViewById(R.id.tv_duration);
        }
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
        setText(tvDuration, TimeUtil.getTimeString(duration));
    }

    @Override
    public void init() {
        mCurrentPosition = 0;
        mLastBufferingProgress = 0;
        if (sbCurrentProgress != null) {
            setText(tvCurrentPosition, TimeUtil.getTimeString(mCurrentPosition));
            sbCurrentProgress.setSecondaryProgress(0);
            sbCurrentProgress.setProgress(0);
        }
        startTimer();
    }

    @Override
    public void unInit() {
        cancelTimer();
    }

    public void startTimer() {
        cancelTimer();
        mTimer = new Timer();
        mTimerTask = new ProgressTimerTask();
        mTimer.schedule(mTimerTask, TIME_DELAY, TIME_INTERVAL);
    }

    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    public LinearLayout getLinearLayout() {
        if (llBottomLayout.get() != null) {
            return llBottomLayout.get();
        }
        return null;
    }

    public void setPlayingState(boolean isPlaying) {
        setImageResource(ivPlay, isPlaying? R.mipmap.droid_player_pause: R.mipmap.droid_player_play);
    }

    // 设置缓冲进度
    public void setBufferingProgress(int progress) {
        if (sbCurrentProgress == null) return;
        if (mLastBufferingProgress == progress) return;
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        this.mLastBufferingProgress = progress;
        sbCurrentProgress.setSecondaryProgress(progress);
    }

    // 设置播放进度
    public void setPlayedProgress() {
        if (sbCurrentProgress == null || mDuration == 0) return;
        mCurrentPosition += TIME_INTERVAL;
        if (mCurrentPosition > mDuration) mCurrentPosition = mDuration;
        setText(tvCurrentPosition, TimeUtil.getTimeString(mCurrentPosition));

        int playedProgress = (int) (mCurrentPosition * 100f / mDuration);
        if (playedProgress > 100) playedProgress = 100;
        sbCurrentProgress.setProgress(playedProgress);
    }

    public class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            if (DroidMediaPlayer.getInstance().isPlaying()) {
                DroidMediaPlayer.getInstance().getHandler().post(DroidPlayerBottomLayoutDelegate.this::setPlayedProgress);
            }
        }
    }

}
