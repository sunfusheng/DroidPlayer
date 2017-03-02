package com.sunfusheng.droidplayer.sample.DroidPlayer;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sunfusheng.droidplayer.sample.DroidPlayer.delegate.DroidPlayerViewStateDelegate;
import com.sunfusheng.droidplayer.sample.DroidPlayer.util.ToastUtil;
import com.sunfusheng.droidplayer.sample.R;

/**
 * Created by sunfusheng on 2017/2/20.
 */
public class BasePlayerView extends RelativeLayout {

    protected Context mContext;

    public BasePlayerView(@NonNull Context context) {
        this(context, null);
    }

    public BasePlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasePlayerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        initData();
    }

    private void initData() {

    }

    protected boolean checkVideoUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            ToastUtil.show(mContext, R.string.player_invalid_url_tip);
            return false;
        }
        return true;
    }

    public void loadNetImage(ImageView imageView, String url) {
        if (imageView == null || TextUtils.isEmpty(url)) return;
        Glide.with(getContext())
                .load(url)
                .crossFade()
                .fallback(R.color.player_transparent)
                .error(R.color.player_transparent)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
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

}
