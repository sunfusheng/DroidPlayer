package com.sunfusheng.droidplayer.sample.DroidPlayer.delegate;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidBasePlayerView;

/**
 * Created by sunfusheng on 2017/3/29.
 */
public class DroidPlayerGestureDelegate extends GestureDetector.SimpleOnGestureListener {

    private DroidBasePlayerView basePlayerView;
    private int widthPixels;
    private int heightPixels;
    private boolean isDownMotion; // 是否按下动作
    private boolean isHorizontal; // 是否水平滑动
    private boolean isVolume; // 是否声音控制

    public DroidPlayerGestureDelegate(Context context, DroidBasePlayerView basePlayerView) {
        widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        this.basePlayerView = basePlayerView;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        isDownMotion = true;
        return super.onDown(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!basePlayerView.isLocked) {
            float oldX = e1.getX();
            float oldY = e1.getY();
            float deltaX = oldX - e2.getX();
            float deltaY = oldY - e2.getY();

            if (isDownMotion) {
                isDownMotion = false;
                isHorizontal = Math.abs(distanceX) >= Math.abs(distanceY);
                isVolume = oldX > (basePlayerView.isFullScreen() ? heightPixels : widthPixels) * 0.5f;
            }

            if (isHorizontal) {
                // 进度设置
                float percent = -deltaX / basePlayerView.getWidth();
                Log.d("------> ", "进度设置: " + percent);
            } else {
                float percent = deltaY / basePlayerView.getHeight();
                if (isVolume) {
                    // 声音设置
                    Log.d("------> ", "声音设置: " + percent);
                } else {
                    // 亮度设置
                    Log.d("------> ", "亮度设置: " + percent);
                }
            }
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        basePlayerView.onSingleTouch();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        basePlayerView.onDoubleTouch();
        return true;
    }
}
