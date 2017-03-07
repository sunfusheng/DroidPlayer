package com.sunfusheng.droidplayer.sample.DroidPlayer.second;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidImageView;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidTextureView;
import com.sunfusheng.droidplayer.sample.DroidPlayer.delegate.DroidPlayerViewMeasureDelegate;
import com.sunfusheng.droidplayer.sample.DroidPlayer.delegate.DroidPlayerViewStateDelegate;
import com.sunfusheng.droidplayer.sample.DroidPlayer.listener.DroidMediaPlayerListener;
import com.sunfusheng.droidplayer.sample.DroidPlayer.listener.IDroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.DroidPlayer.util.PlayerUtil;
import com.sunfusheng.droidplayer.sample.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * Created by sunfusheng on 2017/3/7.
 */
public class DroidBasePlayerView extends RelativeLayout implements IDroidMediaPlayer, TextureView.SurfaceTextureListener, DroidMediaPlayerListener {

    private static final String TAG = "----> BasePlayerView";

    private RelativeLayout playerContainer;
    private RelativeLayout decorationContainer;
    private DroidTextureView droidTextureView;
    private DroidImageView droidImageView;

    private DroidPlayerViewMeasureDelegate mMeasureDelegate;

    private Bitmap mCaptureBitmap; // 暂停时抓拍的Bitmap
    private long mCapturePosition; // 暂停抓拍时的播放位置

    public DroidBasePlayerView(@NonNull Context context) {
        this(context, null);
    }

    public DroidBasePlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DroidBasePlayerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.droid_base_player_layout, this);
        playerContainer = (RelativeLayout) view.findViewById(R.id.player_container);
        decorationContainer = (RelativeLayout) view.findViewById(R.id.decoration_container);
        mMeasureDelegate = new DroidPlayerViewMeasureDelegate(this, 16, 9);
    }

    // 设置视频地址
    public void setVideoUrl(String url) {
        if (checkVideoUrl(url)) {
            DroidMediaPlayer.getInstance().setVideoUrl(url);
        }
    }

    // 设置宽高比
    public void setRatio(int widthRatio, int heightRatio) {
        mMeasureDelegate.setRatio(widthRatio, heightRatio);
    }

    public boolean play() {
        return play(DroidMediaPlayer.getInstance().getVideoUrl());
    }

    @Override
    public boolean play(String url) {
        if (!checkVideoUrl(url)) return false;
        DroidMediaPlayer.getInstance().setVideoUrl(url);
        if (droidImageView != null) {
            droidImageView.setVisibility(GONE);
        }
        if (isPlaying()) {
            pause();
            return true;
        } else if (isPause()) {
            start();
            return true;
        }
        addTextureView();
        boolean isPlay = DroidMediaPlayer.getInstance().play(url);
        DroidMediaPlayer.getInstance().setMediaPlayerListener(this);
        return isPlay;
    }

    @Override
    public void start() {
        DroidMediaPlayer.getInstance().start();
    }

    @Override
    public void resume() {
        DroidMediaPlayer.getInstance().resume();
    }

    @Override
    public void pause() {
        DroidMediaPlayer.getInstance().pause();
    }

    @Override
    public void release() {
        DroidMediaPlayer.getInstance().release();
    }

    @Override
    public void reset() {
        DroidMediaPlayer.getInstance().reset();
    }

    @Override
    public void seekTo(long time) {
        DroidMediaPlayer.getInstance().seekTo(time);
    }

    // 添加视频显示层
    public void addTextureView() {
        droidTextureView = new DroidTextureView(getContext());
        droidTextureView.setSurfaceTextureListener(this);
        droidTextureView.setOnClickListener(v -> {

        });
        playerContainer.addView(droidTextureView);

        droidImageView = new DroidImageView(getContext());
        playerContainer.addView(droidImageView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMeasureDelegate.measure(widthMeasureSpec, heightMeasureSpec)) {
            setMeasuredDimension(mMeasureDelegate.getPlayerWidth(), mMeasureDelegate.getPlayerHeight());
            mMeasureDelegate.measureChild();
        }
    }

    public boolean checkVideoUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            PlayerUtil.show(getContext(), R.string.player_invalid_url_tip);
            return false;
        }
        return true;
    }

    public boolean isPlaying() {
        return DroidMediaPlayer.getInstance().isPlaying();
    }

    public boolean isPause() {
        return DroidMediaPlayer.getInstance().getState() == DroidPlayerViewStateDelegate.STATE.PAUSE;
    }

    public void addScreenOnFlag() {
        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void clearScreenOnFlag() {
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        DroidMediaPlayer.getInstance().setSurface(new Surface(surface));
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        DroidMediaPlayer.getInstance().setSurface(null);
        surface.release();
        return true;
    }

    @Override
    public void onPrepared() {
        Log.d(TAG, "onPrepared()");
        addScreenOnFlag();
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        DroidMediaPlayer.getInstance().setDuration(mp.getDuration());
        DroidMediaPlayer.getInstance().setCurrentPosition(mp.getCurrentPosition());
        Log.d(TAG, "onInfo() duration: " + mp.getDuration() + " CurrentPosition: " + mp.getCurrentPosition() + " what: " + what + " extra: " + extra);
        return true;
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int sar_num, int sar_den) {
        Log.d(TAG, "onVideoSizeChanged() width: " + width + " height: " + height);
        if (droidTextureView != null) {
            droidTextureView.setVideoSize(width, height);
        }
        if (droidImageView != null) {
            droidImageView.setVideoSize(width, height);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onCompletion() {
        Log.d(TAG, "onCompletion()");
        clearScreenOnFlag();
    }

    @Override
    public boolean onError(int what, int extra) {
        Log.e(TAG, "onError() what: " + what + " extra: " + extra);
        clearScreenOnFlag();
        return true;
    }

    @Override
    public void onVideoStart() {
        Log.d(TAG, "onVideoStart()");
    }

    @Override
    public void onVideoResume() {
        Log.d(TAG, "onVideoResume()");
        if (isPause()) {
            if (droidImageView != null && mCaptureBitmap != null) {
                droidImageView.setVisibility(VISIBLE);
                droidImageView.setVideoSize(DroidMediaPlayer.getInstance().getVideoWidth(), DroidMediaPlayer.getInstance().getVideoHeight());
                droidImageView.setImageBitmap(mCaptureBitmap);
            }
        }
    }

    @Override
    public void onVideoPause() {
        Log.d(TAG, "onVideoPause()");
        if (droidTextureView != null && DroidMediaPlayer.getInstance().isPausedWhenPlaying()) {
            Bitmap bitmap = droidTextureView.getBitmap();
            if (bitmap != null && DroidMediaPlayer.getInstance().getCurrentPosition() != mCapturePosition) {
                mCapturePosition = DroidMediaPlayer.getInstance().getCurrentPosition();
                this.mCaptureBitmap = bitmap;
            }
        }
    }

    @Override
    public void onVideoRelease() {
        Log.d(TAG, "onVideoRelease()");
        DroidMediaPlayer.getInstance().setMediaPlayerListener(null);
        clearScreenOnFlag();
        if (droidImageView != null) {
            droidImageView.setVisibility(GONE);
        }
        mCaptureBitmap = null;
        mCapturePosition = 0L;
    }
}
