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

    private DroidBasePlayerView playerView;
    private int widthPixels;
    private int heightPixels;
    private boolean isUpMotion; // 是否抬起动作
    private boolean isDownMotion; // 是否按下动作
    private boolean isHorizontal; // 是否水平滑动
    private boolean isVolume; // 是否声音控制

    private boolean hasScrolled;
    private float offsetX;
    private float offsetY;

    public DroidPlayerGestureDelegate(Context context, DroidBasePlayerView playerView) {
        widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        this.playerView = playerView;
    }

    public void onUp(MotionEvent e) {
        isUpMotion = true;
        isDownMotion = false;
        if (hasScrolled) {
            hasScrolled = false;
            handleSlideEvent(offsetX, offsetY);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        isUpMotion = false;
        isDownMotion = true;
        return super.onDown(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (isSlidingValid()) {
            hasScrolled = true;
            float oldX = e1.getX();
            float oldY = e1.getY();
            offsetX = oldX - e2.getX();
            offsetY = oldY - e2.getY();

            if (isDownMotion) {
                isDownMotion = false;
                isHorizontal = Math.abs(distanceX) >= Math.abs(distanceY);
                isVolume = oldX > (playerView.isFullScreen() ? heightPixels : widthPixels) * 0.5f;
            }

            handleSlideEvent(offsetX, offsetY);
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    private boolean isSlidingValid() {
        if (playerView == null) return false;
        if (playerView.isLocked) return false;
        if (!playerView.isFullScreen()) return false;
        return true;
    }

    private void handleSlideEvent(float offsetX, float offsetY) {
        if (isHorizontal) {
            // 进度设置
            float percent = -offsetX / playerView.getWidth();
            playerView.onScrollProgress(percent, isUpMotion);
        } else {
            float percent = offsetY / playerView.getHeight();
            if (isVolume) {
                // 声音设置
                playerView.onScrollVolume(percent, isUpMotion);
            } else {
                // 亮度设置
                playerView.onScrollBrightness(percent, isUpMotion);
            }
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        playerView.onSingleTouch();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        playerView.onDoubleTouch();
        return true;
    }
}
