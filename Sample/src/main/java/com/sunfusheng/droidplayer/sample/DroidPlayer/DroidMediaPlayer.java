package com.sunfusheng.droidplayer.sample.DroidPlayer;

import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;

import com.sunfusheng.droidplayer.sample.DroidPlayer.delegate.DroidPlayerViewStateDelegate;
import com.sunfusheng.droidplayer.sample.DroidPlayer.listener.DroidMediaMediaPlayerListener;
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

    private DroidMediaMediaPlayerListener mMediaPlayerListener;

    private Surface mSurface;

    private int mState; // 视频状态
    private boolean isPlaying; // 是否在播放中

    private int mVideoWidth; // 视频宽度
    private int mVideoHeight; // 视频高度

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
        initData();
    }

    private void initData() {
        this.mState = DroidPlayerViewStateDelegate.STATE.IDLE;
        this.isPlaying = false;
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
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
    public boolean play(String url) {
        try {
            initPlayer(url);
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
            if (mState == DroidPlayerViewStateDelegate.STATE.PAUSE) {
                mMediaPlayer.start();
            }
        }
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onVideoResume();
        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onVideoPause();
        }
    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
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
        this.mVideoWidth = width;
        this.mVideoHeight = height;
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
        release();
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onCompletion();
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        release();
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onError(what, extra);
        }
        return false;
    }

    public IjkMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void setMediaPlayer(IjkMediaPlayer mediaPlayer) {
        this.mMediaPlayer = mediaPlayer;
    }

    public DroidMediaMediaPlayerListener getMediaPlayerListener() {
        return mMediaPlayerListener;
    }

    public void setMediaPlayerListener(DroidMediaMediaPlayerListener mediaPlayerListener) {
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

    public void setState(@DroidPlayerViewStateDelegate.STATE int state) {
        this.mState = state;
        setPlaying(state == DroidPlayerViewStateDelegate.STATE.PLAYING);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getVideoWidth() {
        return mVideoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.mVideoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return mVideoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.mVideoHeight = videoHeight;
    }

    public Handler getHandler() {
        return mHandler;
    }
}
