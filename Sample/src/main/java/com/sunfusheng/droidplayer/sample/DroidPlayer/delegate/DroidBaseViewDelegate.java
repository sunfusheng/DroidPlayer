package com.sunfusheng.droidplayer.sample.DroidPlayer.delegate;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sunfusheng on 2017/2/25.
 */
public class DroidBaseViewDelegate {

    public void setVisible(boolean isVisible, View... views) {
        if (views == null || views.length == 0) return;
        for (View view : views) {
            if (view == null) continue;
            view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void setGone(boolean isGone, View... views) {
        if (views == null || views.length == 0) return;
        for (View view : views) {
            if (view == null) continue;
            view.setVisibility(isGone ? View.GONE : View.VISIBLE);
        }
    }

    public void setText(TextView tv, @StringRes int id) {
        if (tv == null) return;
        tv.setText(id);
    }

    public void setText(TextView tv, String str) {
        if (tv == null || TextUtils.isEmpty(str)) return;
        tv.setText(str);
    }

    public void setImageResource(ImageView iv, @DrawableRes int id) {
        if (iv == null) return;
        iv.setImageResource(id);
    }
}
