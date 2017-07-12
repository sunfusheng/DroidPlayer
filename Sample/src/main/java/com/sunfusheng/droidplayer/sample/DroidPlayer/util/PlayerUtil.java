package com.sunfusheng.droidplayer.sample.DroidPlayer.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by sunfusheng on 2017/3/2.
 */
public class PlayerUtil {

    private static Toast mToast;

    public static void show(Context context, @StringRes int id) {
        show(context, context.getResources().getString(id));
    }

    public static void show(Context context, String msg) {
        if (context == null) return;
        if (TextUtils.isEmpty(msg)) return;
        int duration = Toast.LENGTH_SHORT;
        if (msg.length() > 10) {
            duration = Toast.LENGTH_LONG;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), msg, duration);
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

    // 获得当前Window亮度 brightness：0～255
    public static int getWindowBrightness(Context context) {
        Activity activity = getActivity(context);
        if (activity == null) return -1;
        Window window = activity.getWindow();
        if (window == null) return -1;
        if (window.getAttributes().screenBrightness == WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE)
            return getSystemBrightness(context);
        return (int) (window.getAttributes().screenBrightness * 255);
    }

    // 获得系统亮度 brightness：0～255
    public static int getSystemBrightness(Context context) {
        if (context == null) return -1;
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            return -1;
        }
    }

    // 修改当前Window亮度 brightness：0～255
    public static void changeWindowBrightness(Context context, int brightness) {
        Activity activity = getActivity(context);
        if (activity == null) return;
        Window window = activity.getWindow();
        if (window == null) return;
        if (brightness < 0) brightness = 0;
        if (brightness > 255) brightness = 255;
        WindowManager.LayoutParams params = window.getAttributes();
        params.screenBrightness = brightness * 1.0f / 255;
        window.setAttributes(params);
    }

}
