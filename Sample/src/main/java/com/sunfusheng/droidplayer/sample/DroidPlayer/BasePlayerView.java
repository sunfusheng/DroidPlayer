package com.sunfusheng.droidplayer.sample.DroidPlayer;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.sunfusheng.droidplayer.sample.DroidPlayer.util.ToastUtil;
import com.sunfusheng.droidplayer.sample.R;

/**
 * Created by sunfusheng on 2017/2/20.
 */
public class BasePlayerView extends RelativeLayout {

    protected Context mContext;

    protected String mUrl;

    protected long mDuration;
    protected long mCurrentPosition;

    protected Handler mHandler = new Handler();

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
        this.mDuration = 0;
        this.mCurrentPosition = 0;
    }

    protected boolean checkVideoUrl(String url) {
        this.mUrl = url;
        if (TextUtils.isEmpty(url)) {
            ToastUtil.show(mContext, R.string.player_invalid_url_tip);
            return false;
        }
        return true;
    }

}
