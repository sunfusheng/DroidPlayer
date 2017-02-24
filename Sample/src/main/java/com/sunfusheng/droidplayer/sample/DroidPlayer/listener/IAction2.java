package com.sunfusheng.droidplayer.sample.DroidPlayer.listener;

import android.support.annotation.NonNull;

/**
 * Created by sunfusheng on 2017/2/23.
 */
public interface IAction2<T1, T2> {

    void call(@NonNull T1 t1, @NonNull T2 t2);

}