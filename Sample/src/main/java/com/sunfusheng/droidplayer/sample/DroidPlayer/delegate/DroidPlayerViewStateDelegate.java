package com.sunfusheng.droidplayer.sample.DroidPlayer.delegate;

import android.support.annotation.IntDef;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidPlayerView;
import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidTextureView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by sunfusheng on 2017/1/16.
 */
public class DroidPlayerViewStateDelegate {

    @IntDef({STATE.IDLE, STATE.LOADING, STATE.PLAYING, STATE.PAUSE, STATE.COMPLETE, STATE.ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATE {
        int IDLE = 0;
        int LOADING = 1;
        int PLAYING = 2;
        int PAUSE = 3;
        int COMPLETE = 4;
        int ERROR = 5;
    }

    public int mState; // 当前视频状态

    public DroidPlayerView playView;
    public DroidTextureView textureView;
    public ImageView centerPlay;
    public ProgressBar loadingView;

    public DroidPlayerViewStateDelegate(DroidPlayerView playView) {
        this.mState = STATE.IDLE;
        this.playView = playView;
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

    public void setState(int state) {
        this.mState = state;
        switch (state) {
            case STATE.IDLE:
                setVisible(true, centerPlay);
                setVisible(false, loadingView);
                break;
            case STATE.LOADING:
                setVisible(true, loadingView);
                setVisible(false, centerPlay);
                break;
            case STATE.PLAYING:
                setVisible(true);
                setVisible(false, centerPlay, loadingView);
                break;
            case STATE.PAUSE:
                setVisible(true, centerPlay);
                setVisible(false, loadingView);
                break;
            case STATE.COMPLETE:
                setVisible(true, centerPlay);
                setVisible(false, loadingView);
                break;
            case STATE.ERROR:
                setVisible(true);
                setVisible(false, centerPlay, loadingView);
                break;
        }
    }

}
