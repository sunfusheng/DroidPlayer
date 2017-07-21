package com.sunfusheng.droidplayer.listener;

/**
 * Created by sunfusheng on 2017/2/20.
 */
public interface IDroidMediaPlayer {

    void start();

    void play(String url);

    void resume();

    void pause();

    void release();

    void reset();

    void seekTo(long time);

}
