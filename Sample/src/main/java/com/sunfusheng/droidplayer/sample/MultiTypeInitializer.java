package com.sunfusheng.droidplayer.sample;

import com.sunfusheng.droidplayer.sample.model.VideoEntity;
import com.sunfusheng.droidplayer.sample.viewprovider.ItemVideoProvider;
import com.sunfusheng.droidplayer.sample.widget.MultiType.GlobalMultiTypePool;

/**
 * Created by sunfusheng on 2017/3/14.
 */
public class MultiTypeInitializer {

    static void init() {
        GlobalMultiTypePool.register(VideoEntity.class, new ItemVideoProvider());
    }
}
