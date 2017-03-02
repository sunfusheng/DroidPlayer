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
        DroidMediaMediaPlayerListener,
        SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "----> PlayerView";

    private RelativeLayout textureViewContainer;
    private DroidTextureView textureView;
    private ImageView ivCoverImage;
    private ProgressBar loadingView;
    private ProgressBar bottomProgressBar;
    private ImageView ivCenterPlay;
    public ImageView ivReplay;
    public TextView tvTitle;
    public TextView tvTip;
    public LinearLayout llBottomLayout;
    public ImageView ivPlay;
    public TextView tvCurrentPosition;
    public SeekBar sbCurrentProgress;
    public TextView tvDuration;

    private DroidPlayerViewMeasureDelegate mMeasureDelegate;
    private DroidPlayerViewStateDelegate mStateDelegate;

    private int preState; // 滑动SeekBar前，播放器状态

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
        ivCoverImage = (ImageView) view.findViewById(R.id.iv_cover_image);
        ivCenterPlay = (ImageView) view.findViewById(R.id.iv_center_play);
        loadingView = (ProgressBar) view.findViewById(R.id.loading_view);
        bottomProgressBar = (ProgressBar) view.findViewById(R.id.bottom_progress_bar);
        ivReplay = (ImageView) view.findViewById(R.id.iv_replay);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTip = (TextView) view.findViewById(R.id.tv_tip);
        llBottomLayout = (LinearLayout) view.findViewById(R.id.ll_bottom_layout);
        ivPlay = (ImageView) view.findViewById(R.id.iv_play);
        tvCurrentPosition = (TextView) view.findViewById(R.id.tv_current_position);
        sbCurrentProgress = (SeekBar) view.findViewById(R.id.sb_current_progress);
        tvDuration = (TextView) view.findViewById(R.id.tv_duration);

        initStateDelegate();
    }

    private void initStateDelegate() {
        mStateDelegate = new DroidPlayerViewStateDelegate(this);
        mStateDelegate.ivCoverImage = ivCoverImage;
        mStateDelegate.ivCenterPlay = ivCenterPlay;
        mStateDelegate.loadingView = loadingView;
        mStateDelegate.setBottomProgressBar(bottomProgressBar);
        mStateDelegate.ivReplay = ivReplay;
        mStateDelegate.tvTitle = tvTitle;
        mStateDelegate.tvTip = tvTip;
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
        sbCurrentProgress.setOnSeekBarChangeListener(this);
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

    // 设置视频标题
    public void setVideoTitle(String title) {
        mStateDelegate.setTitle(title);
    }

    // 设置视频地址
    public void setVideoUrl(String url) {
        if (checkVideoUrl(url)) {
            DroidMediaPlayer.getInstance().setUrl(url);
        }
    }

    // 设置封面图片
    public void setImageUrl(String url) {
        loadNetImage(ivCoverImage, url);
    }

    // 设置宽高比
    public void setRatio(int widthRatio, int heightRatio) {
        mMeasureDelegate.setRatio(widthRatio, heightRatio);
    }

    public boolean play() {
        return play(DroidMediaPlayer.getInstance().getUrl());
    }

    @Override
    public boolean play(String url) {
        if (!checkVideoUrl(url)) return false;
        DroidMediaPlayer.getInstance().setUrl(url);
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

    @Override
    public void seekTo(long time) {
        DroidMediaPlayer.getInstance().seekTo(time);
    }

    // 添加视频显示层
    public void addTextureView() {
        if (textureViewContainer.getChildCount() > 0) {
            textureViewContainer.removeAllViews();
        }
        textureView = new DroidTextureView(mContext);
        textureView.setSurfaceTextureListener(this);
        textureView.setOnClickListener(v -> {
            mStateDelegate.showBottomLayout();
        });

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textureViewContainer.addView(textureView, layoutParams);
        mStateDelegate.textureView = textureView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMeasureDelegate.measure(widthMeasureSpec, heightMeasureSpec)) {
            setMeasuredDimension(mMeasureDelegate.getPlayerWidth(), mMeasureDelegate.getPlayerHeight());
            mMeasureDelegate.measureChild();
        }
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
        addScreenOnFlag();
        mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.PLAYING);
    }

    @Override
    public boolean onInfo(IMediaPlayer mediaPlayer, int what, int extra) {
        mStateDelegate.setDuration(mediaPlayer.getDuration());
        mStateDelegate.setCurrentPosition(mediaPlayer.getCurrentPosition());

        if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.LOADING);
        } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            mStateDelegate.setState(preState);
        }
        Log.d(TAG, "onInfo() duration: " + mediaPlayer.getDuration() + " CurrentPosition: " + mediaPlayer.getCurrentPosition() + " what: " + what + " extra: " + extra);
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
        clearScreenOnFlag();
        mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.COMPLETE);
    }

    @Override
    public boolean onError(int what, int extra) {
        Log.e(TAG, "onError() what: " + what + " extra: " + extra);
        clearScreenOnFlag();
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
        clearScreenOnFlag();
        mStateDelegate.setState(DroidPlayerViewStateDelegate.STATE.IDLE);
        mStateDelegate.unInit();
        DroidMediaPlayer.getInstance().setMediaPlayerListener(null);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mStateDelegate.removeBottomLayoutMessage();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mStateDelegate.state != DroidPlayerViewStateDelegate.STATE.IDLE) {
            preState = mStateDelegate.state;
            int time = (int) (seekBar.getProgress() * DroidMediaPlayer.getInstance().getDuration() / 100);
            mStateDelegate.setCurrentPosition(time);
            mStateDelegate.showBottomLayout();
            seekTo(time);
        }
    }
}
