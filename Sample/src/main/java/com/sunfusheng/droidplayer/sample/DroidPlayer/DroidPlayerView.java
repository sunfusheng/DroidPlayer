package com.sunfusheng.droidplayer.sample.DroidPlayer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.delegate.DroidPlayerViewMeasureDelegate;
import com.sunfusheng.droidplayer.sample.DroidPlayer.delegate.DroidPlayerViewStateDelegate;
import com.sunfusheng.droidplayer.sample.DroidPlayer.listener.DroidMediaMediaPlayerListener;
import com.sunfusheng.droidplayer.sample.DroidPlayer.listener.IDroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.R;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by sunfusheng on 2017/2/20.
 */
public class DroidPlayerView extends BasePlayerView implements View.OnClickListener,
        TextureView.SurfaceTextureListener,
        IDroidMediaPlayer,
        DroidMediaMediaPlayerListener {

    private static final String TAG = "----> PlayerView";

    private RelativeLayout textureViewContainer;
    private DroidTextureView textureView;
    private View fullScreenTransparentBg;
    private ProgressBar loadingView;
    private ProgressBar bottomProgressBar;
    private ImageView ivCenterPlay;
    public ImageView ivReplay;
    public TextView tvTipUp;
    public TextView tvTipDown;
    public LinearLayout llBottomLayout;
    public ImageView ivPlay;
    public TextView tvCurrentPosition;
    public SeekBar sbCurrentProgress;
    public TextView tvDuration;

    private DroidPlayerViewMeasureDelegate mMeasureDelegate;
    private DroidPlayerViewStateDelegate mStateDelegate;

    public DroidPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public DroidPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DroidPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_video_base, this);
        textureViewContainer = (RelativeLayout) view.findViewById(R.id.texture_view_container);
        fullScreenTransparentBg = view.findViewById(R.id.full_screen_transparent_bg);
        ivCenterPlay = (ImageView) view.findViewById(R.id.iv_center_play);
        loadingView = (ProgressBar) view.findViewById(R.id.loading_view);
        bottomProgressBar = (ProgressBar) view.findViewById(R.id.bottom_progress_bar);
        ivReplay = (ImageView) view.findViewById(R.id.iv_replay);
        tvTipUp = (TextView) view.findViewById(R.id.tv_tip_up);
        tvTipDown = (TextView) view.findViewById(R.id.tv_tip_down);
        llBottomLayout = (LinearLayout) view.findViewById(R.id.ll_bottom_layout);
        ivPlay = (ImageView) view.findViewById(R.id.iv_play);
        tvCurrentPosition = (TextView) view.findViewById(R.id.tv_current_position);
        sbCurrentProgress = (SeekBar) view.findViewById(R.id.sb_current_progress);
        tvDuration = (TextView) view.findViewById(R.id.tv_duration);

        initStateDelegate();
    }

    private void initStateDelegate() {
        mStateDelegate = new DroidPlayerViewStateDelegate(this);
        mStateDelegate.fullScreenTransparentBg = fullScreenTransparentBg;
        mStateDelegate.ivCenterPlay = ivCenterPlay;
        mStateDelegate.loadingView = loadingView;
        mStateDelegate.setBottomProgressBar(bottomProgressBar);
        mStateDelegate.ivReplay = ivReplay;
        mStateDelegate.tvTipUp = tvTipUp;
        mStateDelegate.tvTipDown = tvTipDown;
        mStateDelegate.setLlBottomLayout(llBottomLayout);

        mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.IDLE);
        mStateDelegate.init();
    }

    private void initData() {
        mMeasureDelegate = new DroidPlayerViewMeasureDelegate(this, 16, 9);

    }

    private void initListener() {
        ivCenterPlay.setOnClickListener(this);
        ivReplay.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
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
        }
    }

    // 设置播放资源
    public boolean setDataSource(String url) {
        return checkVideoUrl(url);
    }

    public boolean play() {
        return play(mUrl);
    }

    @Override
    public boolean play(String url) {
        if (!checkVideoUrl(url)) return false;
        if (isPlaying()) {
            pause();
            return true;
        } else if (isPause()) {
            start();
            return true;
        }
        mStateDelegate.init();
        mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.LOADING);
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

    // 添加视频显示层
    public void addTextureView() {
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        textureView = new DroidTextureView(mContext);
        textureView.setSurfaceTextureListener(this);
        textureView.setOnClickListener(v -> {
            mStateDelegate.showLlBottomLayout();
        });

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textureViewContainer.addView(textureView, layoutParams);
        mStateDelegate.textureView = textureView;
    }

    // 设置宽高比
    public void setRatio(int widthRatio, int heightRatio) {
        mMeasureDelegate.setRatio(widthRatio, heightRatio);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMeasureDelegate.measure(widthMeasureSpec, heightMeasureSpec)) {
            setMeasuredDimension(mMeasureDelegate.getPlayerWidth(), mMeasureDelegate.getPlayerHeight());
            mMeasureDelegate.measureChild();
        }
    }

    public boolean isPlaying() {
        return DroidMediaPlayer.getInstance().isPlaying();
    }

    public boolean isPause() {
        return DroidMediaPlayer.getInstance().getState() == DroidPlayerViewStateDelegate.STATE.PAUSE;
    }

    //*******************************************************
    // 下面方法不适合在你的程序中调用，主要处理界面显示
    //*******************************************************

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        DroidMediaPlayer.getInstance().setSurface(new Surface(surface));
        Log.d(TAG, "onSurfaceTextureAvailable() width: " + width + " height: " + height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "onSurfaceTextureSizeChanged() width: " + width + " height: " + height);
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
        mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.PLAYING);
    }

    @Override
    public boolean onInfo(IMediaPlayer mediaPlayer, int what, int extra) {
        mDuration = mediaPlayer.getDuration();
        mCurrentPosition = mediaPlayer.getCurrentPosition();
        mStateDelegate.setDuration(mDuration);
        if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.LOADING);
        } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.PLAYING);
        }
        Log.d(TAG, "onInfo() duration: " + mDuration + " what: " + what + " extra: " + extra);
        return true;
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int sar_num, int sar_den) {
        Log.d(TAG, "onVideoSizeChanged() width: " + width + " height: " + height);
        if (textureView != null) {
            textureView.setVideoSize(width, height);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        mStateDelegate.setBufferingProgress(percent);
    }

    @Override
    public void onSeekComplete() {
        Log.d(TAG, "onSeekComplete()");
    }

    @Override
    public void onCompletion() {
        Log.d(TAG, "onCompletion()");
        mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.COMPLETE);
    }

    @Override
    public boolean onError(int what, int extra) {
        Log.e(TAG, "onError() what: " + what + " extra: " + extra);
        mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.ERROR);
        return false;
    }

    @Override
    public void onVideoStart() {
        Log.d(TAG, "onVideoStart()");
        if (isPlaying() || isPause()) {
            mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.PLAYING);
        }
    }

    @Override
    public void onVideoResume() {
        Log.d(TAG, "onVideoResume()");
        if (isPause()) {
            mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.PLAYING);
        }
    }

    @Override
    public void onVideoPause() {
        Log.d(TAG, "onVideoPause()");
        mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.PAUSE);
    }

    @Override
    public void onVideoRelease() {
        Log.d(TAG, "onVideoRelease()");
        mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.IDLE);
        mStateDelegate.unInit();
    }

}
