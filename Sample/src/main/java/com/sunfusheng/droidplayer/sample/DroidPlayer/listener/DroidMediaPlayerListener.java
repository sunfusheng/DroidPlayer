package com.sunfusheng.droidplayer.sample.DroidPlayer.listener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by sunfusheng on 2017/2/21.
 */
public interface DroidMediaPlayerListener {

    void onPrepared();

    boolean onInfo(IMediaPlayer mp, int what, int extra);

    void onVideoSizeChanged(int width, int height, int sar_num, int sar_den);

    void onBufferingUpdate(int percent);

    void onSeekComplete();

    void onCompletion();

    boolean onError(int what, int extra);

    void onVideoStart();

    void onVideoResume();

    void onVideoPause();

    void onVideoRelease();

}
