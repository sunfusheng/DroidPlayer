package com.sunfusheng.droidplayer.sample.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sunfusheng.droidplayer.sample.DroidPlayerApp;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by sunfusheng on 2017/3/14.
 */
public class AppUtil {

    // 判断网络是否可用
    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) DroidPlayerApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static String getTimeString(int totalSeconds) {
        if (totalSeconds <= 0) return "00:00";
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if (hours > 0) minutes += hours * 60;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder, Locale.getDefault());
        return formatter.format("%02d:%02d", minutes, seconds).toString();
    }
}
