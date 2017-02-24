package com.sunfusheng.droidplayer.sample.DroidPlayer.util;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by sunfusheng on 2017/2/25.
 */
public class TimeUtil {

    public static String getTimeString(long ms) {
        if (ms <= 0) return "00:00";
        int totalSeconds = (int) (ms / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if (hours > 0) minutes += hours * 60;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder, Locale.getDefault());
        return formatter.format("%02d:%02d", minutes, seconds).toString();
    }
}
