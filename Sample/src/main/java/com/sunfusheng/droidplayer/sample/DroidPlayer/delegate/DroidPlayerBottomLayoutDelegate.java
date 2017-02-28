package com.sunfusheng.droidplayer.sample.DroidPlayer.delegate;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.DroidPlayer.util.TimeUtil;
import com.sunfusheng.droidplayer.sample.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import static com.sunfusheng.droidplayer.sample.DroidPlayer.delegate.DroidPlayerViewStateDelegate.TIME_INTERVAL;

/**
 * Created by sunfusheng on 2017/2/24.
 */
public class DroidPlayerBottomLayoutDelegate extends DroidBaseViewDelegate implements IBaseDelegate {

    private WeakReference<ProgressBar> progressBar;
    private WeakReference<LinearLayout> llBottomLayout;

    private ImageView ivPlay;
    private TextView tvCurrentPosition;
    private SeekBar sbCurrentProgress;
    private TextView tvDuration;

    private int mLastBufferingProgress; // 上一次缓冲进度

    private Timer mTimer;
    private ProgressTimerTask mTimerTask;

    public DroidPlayerBottomLayoutDelegate() {
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = new WeakReference<ProgressBar>(progressBar);
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

    @Override
    public void init() {
        mLastBufferingProgress = 0;
        if (sbCurrentProgress != null) {
            setText(tvCurrentPosition, TimeUtil.getTimeString(0));
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
        mTimer.schedule(mTimerTask, DroidPlayerViewStateDelegate.TIME_DELAY, DroidPlayerViewStateDelegate.TIME_INTERVAL);
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

    public ProgressBar getProgressBar() {
        if (progressBar.get() != null) {
            return progressBar.get();
        }
        return null;
    }

    public LinearLayout getLinearLayout() {
        if (llBottomLayout.get() != null) {
            return llBottomLayout.get();
        }
        return null;
    }

    public void setDuration(long duration) {
        setText(tvDuration, TimeUtil.getTimeString(duration));
    }

    public void setPlayingState(boolean isPlaying) {
        setImageResource(ivPlay, isPlaying? R.mipmap.droid_player_pause: R.mipmap.droid_player_play);
    }

    // 设置缓冲进度
    public void setBufferingProgress(int progress) {
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        if (mLastBufferingProgress >= progress) return;
        this.mLastBufferingProgress = progress;

        if (sbCurrentProgress != null) {
            sbCurrentProgress.setSecondaryProgress(progress);
        }
        if (getProgressBar() != null) {
            getProgressBar().setSecondaryProgress(progress);
        }
    }

    // 设置播放进度
    public void setCurrentPosition(long currentPosition) {
        long duration = DroidMediaPlayer.getInstance().getDuration();
        if (duration == 0) return;
        if (currentPosition > duration) currentPosition = duration;
        DroidMediaPlayer.getInstance().setCurrentPosition(currentPosition);

        setText(tvCurrentPosition, TimeUtil.getTimeString(currentPosition));

        int progress = (int) (currentPosition * 100f / duration);
        if (progress > 100) progress = 100;

        if (sbCurrentProgress != null) {
            sbCurrentProgress.setProgress(progress);
        }
        if (getProgressBar() != null) {
            getProgressBar().setProgress(progress);
        }
    }

    public class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            if (DroidMediaPlayer.getInstance().isPlaying()) {
                DroidMediaPlayer.getInstance().getHandler().post(() -> {
                    long currentPosition = DroidMediaPlayer.getInstance().getCurrentPosition();
                    currentPosition += TIME_INTERVAL;
                    setCurrentPosition(currentPosition);
                });
            }
        }
    }

}
