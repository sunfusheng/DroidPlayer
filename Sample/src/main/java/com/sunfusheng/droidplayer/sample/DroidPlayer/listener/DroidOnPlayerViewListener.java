package com.sunfusheng.droidplayer.sample.DroidPlayer.listener;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidPlayerState;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by sunfusheng on 2017/3/8.
 */
public interface DroidOnPlayerViewListener {

    void onInfoCallback(IMediaPlayer mp, int what, int extra);

    void onStateChange(@DroidPlayerState int state);

    void onCacheChange(int progress);

    void onPositionChange(long position);

}
