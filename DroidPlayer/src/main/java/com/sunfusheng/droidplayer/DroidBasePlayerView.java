package com.sunfusheng.droidplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sunfusheng.droidplayer.delegate.DroidPlayerGestureDelegate;
import com.sunfusheng.droidplayer.delegate.DroidPlayerMeasureDelegate;
import com.sunfusheng.droidplayer.delegate.DroidPlayerOrientationDelegate;
import com.sunfusheng.droidplayer.listener.IDroidMediaPlayer;
import com.sunfusheng.droidplayer.listener.IDroidMediaPlayerListener;
import com.sunfusheng.droidplayer.listener.IDroidOnPlayerViewListener;
import com.sunfusheng.droidplayer.util.PlayerUtil;

import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


/**
 * Created by sunfusheng on 2017/3/7.
 */
public class DroidBasePlayerView extends FrameLayout implements
        IDroidMediaPlayer,
        TextureView.SurfaceTextureListener,
        IDroidMediaPlayerListener {

    private static final String TAG = "----> BasePlayerView";

    private FrameLayout flRootView;
    private RelativeLayout playerContainer;
    private ImageView coverImage;
    private RelativeLayout rlCoverImage;
    protected RelativeLayout decorationContainer;
    private DroidTextureView droidTextureView;
    private DroidImageView droidImageView;
    private Surface mSurface;

    public static final int TIME_DELAY = 1000; // 进度更新的时间延时
    public static final int TIME_INTERVAL = 1000; // 进度更新的时间间隔
    public static final int TYPE_HIDE_TOP_BOTTOM_LAYOUT = 10000;
    public static final int TIME_HIDE_TOP_BOTTOM_LAYOUT = 3000; // 3s

    private DroidPlayerMeasureDelegate mMeasureDelegate;
    private DroidPlayerOrientationDelegate mOrientationDelegate;
    private DroidPlayerGestureDelegate mGestureDelegate;
    private IDroidOnPlayerViewListener mOnPlayerViewListener;

    protected int state; // 播放器状态

    protected String mTitle; // 名称
    protected String mVideoUrl; // 视频地址
    protected String mImageUrl; // 图片地址
    protected int mVideoWidth; // 宽度
    protected int mVideoHeight; // 高度
    protected long mDuration; // 时长，毫秒
    protected long mCurrentPosition; // 当前播放位置，毫秒

    private Bitmap mCaptureBitmap; // 暂停时抓拍的Bitmap
    private long mCapturePosition; // 暂停抓拍时的播放位置
    private int mPositionInList = -1; // 视频在List或RecyclerView中的位置

    private Timer mTimer;
    private ProgressTimerTask mTimerTask;

    public boolean isLocked; // 是否是锁屏

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
        setBackgroundColor(getResources().getColor(R.color.player_white_color));
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_player_base_view, this);

        flRootView = (FrameLayout) view.findViewById(R.id.fl_root_view);
        playerContainer = (RelativeLayout) view.findViewById(R.id.player_container);
        rlCoverImage = (RelativeLayout) view.findViewById(R.id.rl_cover_image);
        decorationContainer = (RelativeLayout) view.findViewById(R.id.decoration_container);

        mMeasureDelegate = new DroidPlayerMeasureDelegate(this, 16, 9);
        mOrientationDelegate = new DroidPlayerOrientationDelegate(this);
        mGestureDelegate = new DroidPlayerGestureDelegate(getContext(), this);

        GestureDetector gestureDetector = new GestureDetector(getContext(), mGestureDelegate);
        flRootView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mGestureDelegate.onUp(event);
            }
            gestureDetector.onTouchEvent(event);
            return true;
        });

        coverImage = new ImageView(getContext());
        rlCoverImage.addView(coverImage);
        coverImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams layoutParams = coverImage.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        coverImage.setLayoutParams(layoutParams);
    }

    // 设置视频标题
    public void setVideoTitle(String title) {
        mTitle = title;
    }

    // 设置视频地址
    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    // 设置封面图片
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
        loadingCoverImage(imageUrl);
    }

    // 设置封面图片
    public void setCoverImageDrawable(Drawable drawable) {
        coverImage.setImageDrawable(drawable);
    }

    // 设置宽高比
    public void setRatio(int widthRatio, int heightRatio) {
        mMeasureDelegate.setRatio(widthRatio, heightRatio);
    }

    public void play() {
        play(mVideoUrl);
    }

    @Override
    public void play(String url) {
        if (!checkVideoUrl(url)) return;
        DroidMediaPlayer.getInstance().setPositionInList(mPositionInList);
        if (droidImageView != null && droidImageView.isShown()) {
            droidImageView.setVisibility(GONE);
        }
        if (url.equals(DroidMediaPlayer.getInstance().getVideoUrl())) {
            if (isPlaying()) {
                pause();
                return;
            } else if (isPause()) {
                start();
                return;
            }
        }
        DroidMediaPlayer.getInstance().release();
        mVideoUrl = url;
        setState(DroidPlayerState.LOADING);
        addTextureView();
        DroidMediaPlayer.getInstance().play(url);
        DroidMediaPlayer.getInstance().setMediaPlayerListener(this);
        DroidMediaPlayer.getInstance().setPlayerView(this);
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

    // 滑动控制进度
    public void onScrollProgress(float stepPercent, boolean isUp) {
    }

    // 滑动控制声音
    public void onScrollVolume(float stepPercent, boolean isUp) {
    }

    // 滑动控制亮度
    public void onScrollBrightness(float stepPercent, boolean isUp) {
    }

    // 单击播放器
    public void onSingleTouch() {
    }

    // 双击播放器
    public void onDoubleTouch() {
    }

    // 添加视频显示层
    public void addTextureView() {
        if (playerContainer.getChildCount() > 0) {
            playerContainer.removeAllViews();
        }
        setBackgroundColor(getResources().getColor(R.color.player_black_color));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        droidTextureView = new DroidTextureView(getContext());
        droidTextureView.setSurfaceTextureListener(this);
        playerContainer.addView(droidTextureView, layoutParams);

        droidImageView = new DroidImageView(getContext());
        droidImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        playerContainer.addView(droidImageView, layoutParams);
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

    public IjkMediaPlayer getMediaPlayer() {
        return DroidMediaPlayer.getInstance().getMediaPlayer();
    }

    public boolean isPlaying() {
        return getMediaPlayer() != null && state == DroidPlayerState.PLAYING;
    }

    public boolean isPause() {
        return state == DroidPlayerState.PAUSE;
    }

    public void addScreenOnFlag() {
        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void clearScreenOnFlag() {
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void setState(@DroidPlayerState int state) {
        this.state = state;
        DroidMediaPlayer.getInstance().setState(state);
        switch (state) {
            case DroidPlayerState.IDLE:
                Log.d(TAG, "STATE IDLE");
                rlCoverImage.setVisibility(VISIBLE);
                break;
            case DroidPlayerState.LOADING:
                Log.d(TAG, "STATE LOADING");
                rlCoverImage.setVisibility(mCurrentPosition == 0 ? VISIBLE : GONE);
                break;
            case DroidPlayerState.PLAYING:
                Log.d(TAG, "STATE PLAYING");
                if (droidImageView != null && droidImageView.isShown()) {
                    droidImageView.setVisibility(GONE);
                }
                rlCoverImage.setVisibility(GONE);
                break;
            case DroidPlayerState.PAUSE:
                Log.d(TAG, "STATE PAUSE");
                rlCoverImage.setVisibility(GONE);
                break;
            case DroidPlayerState.COMPLETE:
                Log.d(TAG, "STATE COMPLETE");
                rlCoverImage.setVisibility(VISIBLE);
                break;
            case DroidPlayerState.ERROR:
                Log.d(TAG, "STATE ERROR");
                rlCoverImage.setVisibility(VISIBLE);
                break;
        }
        if (mOnPlayerViewListener != null) {
            mOnPlayerViewListener.onStateChange(state);
        }
    }

    // 添加自己装饰的视图
    public void addDecorationView(View view) {
        if (decorationContainer.getChildCount() > 0) {
            decorationContainer.removeAllViews();
        }
        decorationContainer.addView(view);
    }

    public void setOnPlayerViewListener(IDroidOnPlayerViewListener onPlayerViewListener) {
        this.mOnPlayerViewListener = onPlayerViewListener;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        DroidMediaPlayer.getInstance().setSurface(mSurface);
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
        startTimer();
        rlCoverImage.setVisibility(GONE);
        setState(DroidPlayerState.PLAYING);
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        mDuration = mp.getDuration();
        mCurrentPosition = mp.getCurrentPosition();
        Log.d(TAG, "onInfo() duration: " + mDuration + " CurrentPosition: " + mCurrentPosition + " what: " + what + " extra: " + extra);
        if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            setState(DroidPlayerState.LOADING);
        } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            setState(DroidPlayerState.PLAYING);
        }
        if (mOnPlayerViewListener != null) {
            mOnPlayerViewListener.onInfoCallback(mp, what, extra);
        }
        return true;
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int sar_num, int sar_den) {
        Log.d(TAG, "onVideoSizeChanged() width: " + width + " height: " + height);
        mVideoWidth = width;
        mVideoHeight = height;
        if (droidTextureView != null) {
            droidTextureView.setVideoSize(width, height);
        }
        if (droidImageView != null) {
            droidImageView.setVideoSize(width, height);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (mOnPlayerViewListener != null) {
            mOnPlayerViewListener.onCacheChange(percent);
        }
    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onCompletion() {
        Log.d(TAG, "onCompletion()");
        setState(DroidPlayerState.COMPLETE);
        clearScreenOnFlag();
    }

    @Override
    public boolean onError(int what, int extra) {
        Log.e(TAG, "onError() what: " + what + " extra: " + extra);
        setState(DroidPlayerState.ERROR);
        clearScreenOnFlag();
        return true;
    }

    @Override
    public void onVideoStart() {
        Log.d(TAG, "onVideoStart()");
        if (isPause()) {
            setState(DroidPlayerState.PLAYING);
        }
    }

    @Override
    public void onVideoResume() {
        Log.d(TAG, "onVideoResume()");
        if (DroidMediaPlayer.getInstance().isPausedWhenPlaying()) {
            setState(DroidPlayerState.PLAYING);
        } else if (isPause()) {
            showCaptureImage();
        }
    }

    @Override
    public void onVideoPause() {
        Log.d(TAG, "onVideoPause()");
        setState(DroidPlayerState.PAUSE);
        if (droidTextureView != null && DroidMediaPlayer.getInstance().isPausedWhenPlaying()) {
            Bitmap bitmap = droidTextureView.getBitmap();
            if (bitmap != null && mCurrentPosition != mCapturePosition) {
                mCapturePosition = mCurrentPosition;
                this.mCaptureBitmap = bitmap;
            }
        }
    }

    private void resetData() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mDuration = 0;
        mCaptureBitmap = null;
        mCurrentPosition = 0;
        mCapturePosition = 0L;
    }

    @Override
    public void onVideoRelease() {
        Log.d(TAG, "onVideoRelease()");
        resetData();
        stopTimer();
        if (playerContainer.getChildCount() > 0) {
            playerContainer.removeAllViews();
        }
        setBackgroundColor(getResources().getColor(R.color.player_white_color));
        DroidMediaPlayer.getInstance().setMediaPlayerListener(null);
        DroidMediaPlayer.getInstance().setPlayerView(null);
        setState(DroidPlayerState.IDLE);
        clearScreenOnFlag();
    }

    protected void showCaptureImage() {
        if (droidImageView != null && mCaptureBitmap != null) {
            droidImageView.setVisibility(VISIBLE);
            droidImageView.setVideoSize(mVideoWidth, mVideoHeight);
            droidImageView.setImageBitmap(mCaptureBitmap);
        }
    }

    // 显示封面图片
    public void loadingCoverImage(String imageUrl) {
        Glide.with(getContext())
                .load(imageUrl)
                .crossFade()
                .fallback(R.color.player_transparent)
                .error(R.color.player_transparent)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(coverImage);
    }

    // 启动定时器
    public void startTimer() {
        stopTimer();
        mTimer = new Timer();
        mTimerTask = new ProgressTimerTask();
        mTimer.schedule(mTimerTask, TIME_DELAY, TIME_INTERVAL);
    }

    // 停止定时器
    public void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    // 定时器任务
    public class ProgressTimerTask extends TimerTask {
        @Override
        public void run() {
            if (isPlaying()) {
                DroidMediaPlayer.getInstance().getMainThreadHandler().post(() -> {
                    mCurrentPosition += TIME_INTERVAL;
                    if (mOnPlayerViewListener != null) {
                        mOnPlayerViewListener.onPositionChange(mCurrentPosition);
                    }
                });
            }
        }
    }

    public void setVisible(boolean isVisible, View... views) {
        if (views == null || views.length == 0) return;
        for (View view : views) {
            if (view == null) continue;
            view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setGone(boolean isGone, View... views) {
        if (views == null || views.length == 0) return;
        for (View view : views) {
            if (view == null) continue;
            view.setVisibility(isGone ? View.GONE : View.VISIBLE);
        }
    }

    public void setText(TextView tv, @StringRes int id) {
        if (tv == null) return;
        tv.setText(id);
    }

    public void setText(TextView tv, String str) {
        if (tv == null || TextUtils.isEmpty(str)) return;
        tv.setText(str);
    }

    public void setImageResource(ImageView iv, @DrawableRes int id) {
        if (iv == null) return;
        iv.setImageResource(id);
    }

    public boolean isFullScreen() {
        return mOrientationDelegate.isFullScreen();
    }

    // 进入全屏
    public void enterFullScreen() {
        mOrientationDelegate.enterFullScreen();
        if (!isPlaying()) {
            if (isPause()) {
                showCaptureImage();
            } else if (!TextUtils.isEmpty(mImageUrl)) {
                rlCoverImage.setVisibility(VISIBLE);
                loadingCoverImage(mImageUrl);
            }
        }
    }

    // 退出全屏
    public void quitFullScreen() {
        mOrientationDelegate.quitFullScreen();
        if (!isPlaying()) {
            if (isPause()) {
                showCaptureImage();
            } else if (!TextUtils.isEmpty(mImageUrl)) {
                rlCoverImage.setVisibility(VISIBLE);
                loadingCoverImage(mImageUrl);
            }
        }
    }

    public int getPositionInList() {
        return mPositionInList;
    }

    public void setPositionInList(int mPositionInList) {
        this.mPositionInList = mPositionInList;
    }

}
