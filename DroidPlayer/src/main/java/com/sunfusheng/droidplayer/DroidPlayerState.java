package com.sunfusheng.droidplayer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by sunfusheng on 2017/3/8.
 */
@IntDef({DroidPlayerState.IDLE, DroidPlayerState.LOADING, DroidPlayerState.PLAYING,
        DroidPlayerState.PAUSE, DroidPlayerState.COMPLETE, DroidPlayerState.ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface DroidPlayerState {
    int IDLE = 0;
    int LOADING = 1;
    int PLAYING = 2;
    int PAUSE = 3;
    int COMPLETE = 4;
    int ERROR = 5;
}