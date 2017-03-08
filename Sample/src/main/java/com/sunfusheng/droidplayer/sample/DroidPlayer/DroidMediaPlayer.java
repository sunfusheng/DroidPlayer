package com.sunfusheng.droidplayer.sample.DroidPlayer;

import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;

import com.sunfusheng.droidplayer.sample.DroidPlayer.listener.DroidMediaPlayerListener;
import com.sunfusheng.droidplayer.sample.DroidPlayer.listener.IDroidMediaPlayer;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by sunfusheng on 2017/2/20.
 */
public class DroidMediaPlayer implements IDroidMediaPlayer,
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnVideoSizeChangedListener,
        IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnSeekCompleteListener,
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnErrorListener {

    private static final String TAG = "----> MediaPlayer";

    private IjkMediaPlayer mMediaPlayer;
    private DroidMediaPlayerListener mMediaPlayerListener;
    private Surface mSurface;

    private int mState; // 播放器状态
    private boolean isPlaying; // 是否在播放中
    private boolean isPausedWhenPlaying; // 当播放中是否被暂停

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private static final class Holder {
        static DroidMediaPlayer instance = new DroidMediaPlayer();
    }

    public static DroidMediaPlayer getInstance() {
        return Holder.instance;
    }

    private DroidMediaPlayer() {
        init();
    }

    private void init() {
        this.mState = DroidPlayerState.IDLE;
        resetData();
    }

    private void resetData() {
        this.isPlaying = false;
        this.isPausedWhenPlaying = false;
    }

    private void initPlayer(String url) throws Exception {
        mMediaPlayer = new IjkMediaPlayer();
        mMediaPlayer.setDataSource(url);
        mMediaPlayer.setScreenOnWhilePlaying(true);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        initListener();
        mMediaPlayer.prepareAsync();
    }

    private void initListener() {
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public boolean play(String video_url) {
        try {
            initPlayer(video_url);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onVideoStart();
        }
    }

    @Override
    public void resume() {
        if (mMediaPlayer != null) {
            if (isPausedWhenPlaying) {
                mMediaPlayer.start();
            }
        }
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onVideoResume();
        }
        isPausedWhenPlaying = false;
    }

    @Override
    public void pause() {
        isPausedWhenPlaying = isPlaying();
        if (mMediaPlayer != null) {
            if (isPlaying()) {
                mMediaPlayer.pause();
            }
        }
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onVideoPause();
        }
    }

    @Override
    public void release() {
        this.mState = DroidPlayerState.IDLE;
        releaseMediaPlayer();
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onVideoRelease();
        }
    }

    @Override
    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }

    @Override
    public void seekTo(long time) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(time);
        }
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onPrepared();
        }
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onInfo(iMediaPlayer, what, extra);
        }
        return false;
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int sar_num, int sar_den) {
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onVideoSizeChanged(width, height, sar_num, sar_den);
        }
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onBufferingUpdate(percent);
        }
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onSeekComplete();
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        releaseMediaPlayer();
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onCompletion();
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        releaseMediaPlayer();
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onError(what, extra);
        }
        return true;
    }

    private void releaseMediaPlayer() {
        resetData();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public IjkMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void setMediaPlayer(IjkMediaPlayer mediaPlayer) {
        this.mMediaPlayer = mediaPlayer;
    }

    public DroidMediaPlayerListener getMediaPlayerListener() {
        return mMediaPlayerListener;
    }

    public void setMediaPlayerListener(DroidMediaPlayerListener mediaPlayerListener) {
        this.mMediaPlayerListener = mediaPlayerListener;
    }

    public Surface getSurface() {
        return mSurface;
    }

    public void setSurface(Surface surface) {
        this.mSurface = surface;
        if (mMediaPlayer != null) {
            mMediaPlayer.setSurface(surface);
        }
    }

    public int getState() {
        return mState;
    }

    public void setState(@DroidPlayerState int state) {
        this.mState = state;
        setPlaying(state == DroidPlayerState.PLAYING);
    }

    public boolean isPause() {
        return mState == DroidPlayerState.PAUSE;
    }

    public boolean isPlaying() {
        isPlaying = (mState == DroidPlayerState.PLAYING);
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isPausedWhenPlaying() {
        return isPausedWhenPlaying;
    }

    public Handler getHandler() {
        return mHandler;
    }

}
