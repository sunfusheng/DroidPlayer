package com.sunfusheng.droidplayer.sample.DroidPlayer.listener;

/**
 * Created by sunfusheng on 2017/2/20.
 */
public interface IDroidMediaPlayer {

    void start();

    boolean play(String url);

    void resume();

    void pause();

    void release();

    void reset();

    void seekTo(long time);

}
