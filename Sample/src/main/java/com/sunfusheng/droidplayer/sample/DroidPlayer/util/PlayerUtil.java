package com.sunfusheng.droidplayer.sample.DroidPlayer.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by sunfusheng on 2017/3/2.
 */
public class PlayerUtil {

    private static Toast mToast;

    public static void show(Context context, @StringRes int id) {
        if (context == null) return;
        show(context, context.getResources().getString(id));
    }

    public static void show(Context context, String msg) {
        if (context == null) return;
        if (TextUtils.isEmpty(msg)) return;
        int duration;
        if (msg.length() > 10) {
            duration = Toast.LENGTH_LONG;
        } else {
            duration = Toast.LENGTH_SHORT;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

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

    public static Activity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

}
