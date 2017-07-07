package com.sunfusheng.droidplayer.sample.DroidPlayer;

import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
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

    public static final int WHAT_INIT = 0;
    public static final int WHAT_SURFACE = 1;
    public static final int WHAT_RELEASE = 2;

    private IjkMediaPlayer mMediaPlayer;
    private DroidMediaPlayerListener mMediaPlayerListener;
    private DroidMediaPlayerHandler mMediaPlayerHandler;
    private Handler mMainThreadHandler;
    private DroidBasePlayerView mBasePlayerView;

    private int mState; // 播放器状态
    private boolean isPlaying; // 是否在播放中
    private boolean isPausedWhenPlaying; // 当播放中是否被暂停
    private String mVideoUrl; // 视频地址
    private int mPositionInList = -1; // 视频在List或RecyclerView中的位置


    public class DroidMediaPlayerHandler extends Handler {

        public DroidMediaPlayerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_INIT:
                    initPlayer((String) msg.obj);
                    break;
                case WHAT_SURFACE:
                    Surface surface = (Surface) msg.obj;
                    if (mMediaPlayer != null) {
                        if (surface == null) {
                            mMediaPlayer.setSurface(null);
                        } else if (surface.isValid()) {
                            mMediaPlayer.setSurface(surface);
                        }
                    }
                    break;
                case WHAT_RELEASE:
                    initData();
                    if (mMediaPlayer != null) {
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                    break;
            }
        }
    }

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
        HandlerThread handlerThread = new HandlerThread(getClass().getSimpleName());
        handlerThread.start();
        mMediaPlayerHandler = new DroidMediaPlayerHandler(handlerThread.getLooper());
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    private void initData() {
        this.mState = DroidPlayerState.IDLE;
        this.isPlaying = false;
        this.isPausedWhenPlaying = false;
        this.mVideoUrl = null;
    }

    private void initPlayer(String url) {
        try {
            this.mVideoUrl = url;
            mMediaPlayer = new IjkMediaPlayer();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            initListener();
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Message msg = new Message();
        msg.what = WHAT_INIT;
        msg.obj = url;
        mMediaPlayerHandler.sendMessage(msg);
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
        mMediaPlayerHandler.sendEmptyMessage(WHAT_RELEASE);
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
        mMainThreadHandler.post(() -> {
            if (mMediaPlayer != null) {
                mMediaPlayer.seekTo(time);
            }
        });
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        mMainThreadHandler.post(() -> {
            if (mMediaPlayerListener != null) {
                mMediaPlayerListener.onPrepared();
            }
        });
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        mMainThreadHandler.post(() -> {
            if (mMediaPlayerListener != null) {
                mMediaPlayerListener.onInfo(iMediaPlayer, what, extra);
            }
        });
        return false;
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int sar_num, int sar_den) {
        mMainThreadHandler.post(() -> {
            if (mMediaPlayerListener != null) {
                mMediaPlayerListener.onVideoSizeChanged(width, height, sar_num, sar_den);
            }
        });
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
        mMainThreadHandler.post(() -> {
            if (mMediaPlayerListener != null) {
                mMediaPlayerListener.onBufferingUpdate(percent);
            }
        });
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        mMainThreadHandler.post(() -> {
            if (mMediaPlayerListener != null) {
                mMediaPlayerListener.onSeekComplete();
            }
        });
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        mMainThreadHandler.post(() -> {
            mMediaPlayerHandler.sendEmptyMessage(WHAT_RELEASE);
            if (mMediaPlayerListener != null) {
                mMediaPlayerListener.onCompletion();
            }
        });
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        mMainThreadHandler.post(() -> {
            mMediaPlayerHandler.sendEmptyMessage(WHAT_RELEASE);
            if (mMediaPlayerListener != null) {
                mMediaPlayerListener.onError(what, extra);
            }
        });
        return true;
    }

    public boolean onBackPressed() {
        if (isFullScreen()) {
            quitFullScreen();
            return true;
        }
        release();
        return false;
    }

    public boolean releaseOnScroll(int firstVisibleItemPosition, int lastVisibleItemPosition) {
        if (mPositionInList >= 0 && (mPositionInList < firstVisibleItemPosition || mPositionInList > lastVisibleItemPosition)) {
            this.mPositionInList = -1;
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
        Message msg = new Message();
        msg.what = WHAT_SURFACE;
        msg.obj = surface;
        mMediaPlayerHandler.sendMessage(msg);
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
        return mMediaPlayer != null && isPlaying;
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

    public Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public int getPositionInList() {
        return mPositionInList;
    }

    public void setPositionInList(int mPositionInList) {
        this.mPositionInList = mPositionInList;
    }
}
