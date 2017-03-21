package com.sunfusheng.droidplayer.sample.DroidPlayer;

import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

    private DroidBasePlayerView mBasePlayerView;
    private int mState; // 播放器状态
    private boolean isPlaying; // 是否在播放中
    private boolean isPausedWhenPlaying; // 当播放中是否被暂停
    private String mVideoUrl; // 视频地址
    private int mPositionInList = -1; // 视频在List或RecyclerView中的位置

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
        this.mBasePlayerView = null;
        this.isPlaying = false;
        this.isPausedWhenPlaying = false;
        this.mVideoUrl = null;
        mPositionInList = -1;
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
    public void play(String url) {
        try {
            this.mVideoUrl = url;
            initPlayer(url);
        } catch (Exception e) {
            e.printStackTrace();
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
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public boolean onBackPressed() {
        if (isFullScreen()) {
            quitFullScreen();
            return true;
        }
        release();
        return false;
    }

    public boolean scrollPositionInList(int firstVisibleItemPosition, int lastVisibleItemPosition) {
        Log.d("----> ", "mPositionInList: " + mPositionInList + " firstPosition: " + firstVisibleItemPosition + " lastPosition: " + lastVisibleItemPosition);
        if (mPositionInList >= 0 && (mPositionInList < firstVisibleItemPosition || mPositionInList > lastVisibleItemPosition)) {
            release();
            return true;
        }
        return false;
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

    public void setSurface(Surface surface) {
        if (mMediaPlayer == null) return;
        Log.d("------------------> ", "" + (surface != null ? ("isValid: " + surface.isValid()+" surface: "+surface.toString()) : "null"));
        if (surface != null && surface.isValid()) {
            mMediaPlayer.setSurface(surface);
        } else if (surface == null) {
            mMediaPlayer.setSurface(null);
        }
    }

    public DroidBasePlayerView getPlayerView() {
        return mBasePlayerView;
    }

    public void setPlayerView(DroidBasePlayerView mBasePlayerView) {
        this.mBasePlayerView = mBasePlayerView;
    }

    public boolean isFullScreen() {
        if (mBasePlayerView == null) return false;
        return mBasePlayerView.isFullScreen();
    }

    public boolean quitFullScreen() {
        if (isFullScreen()) {
            mBasePlayerView.quitFullScreen();
            return true;
        }
        return false;
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

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.mVideoUrl = videoUrl;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public int getPositionInList() {
        return mPositionInList;
    }

    public void setPositionInList(int mPositionInList) {
        this.mPositionInList = mPositionInList;
    }
}
