package com.sunfusheng.droidplayer.sample.DroidPlayer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sunfusheng.droidplayer.sample.R;

/**
 * Created by sunfusheng on 2017/7/12.
 */
public class DroidVolumeDialog {

    private Context mContext;
    private Dialog mDialog;
    private ImageView imageView;
    private ProgressBar progressBar;
    private AudioManager audioManager;
    private int maxVolume;
    private int curVolume;

    public DroidVolumeDialog(Context context) {
        this.mContext = context;
        createDialog();
    }

    private void createDialog() {
        mDialog = new Dialog(mContext, R.style.DialogStyle);

        View view = LayoutInflater.from(mContext).inflate(R.layout.droid_volume_dialog_layout, null);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mDialog.setContentView(view);

        if (audioManager == null) {
            audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            progressBar.setMax(maxVolume);
            curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            imageView.setImageResource(curVolume == 0 ? R.mipmap.player_no_volume_icon : R.mipmap.player_volume_icon);
            progressBar.setProgress(curVolume);
        }
    }

    private void setValues() {
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        progressBar.setProgress(curVolume);
    }

    public void show(int endVolume) {
        if (mDialog == null) {
            createDialog();
        }

        if (endVolume < 0) endVolume = 0;
        if (endVolume > maxVolume) endVolume = maxVolume;
        this.curVolume = endVolume;

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, endVolume, 0);
        imageView.setImageResource(endVolume == 0 ? R.mipmap.player_no_volume_icon : R.mipmap.player_volume_icon);
        progressBar.setProgress(endVolume);

        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    public int getCurVolume() {
        return curVolume;
    }
}
