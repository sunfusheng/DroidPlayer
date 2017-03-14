package com.sunfusheng.droidplayer.sample;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.Logger;

/**
 * Created by sunfusheng on 2017/3/14.
 */
public class DroidPlayerApp extends Application {

    private static Context context;
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        application = this;
        Logger.init("DroidPlayer");
        MultiTypeInitializer.init();
    }

    public static Context getContext() {
        return context;
    }

    public static Application getApplication() {
        return application;
    }
}
