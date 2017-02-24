package com.sunfusheng.droidplayer.sample.DroidPlayer.listener;

/**
 * Created by sunfusheng on 2017/2/20.
 */
public interface IDroidMediaPlayer {

    boolean play(String url);

    void resume();

    void pause();

    void stop();

    void release();

    void reset();

}
