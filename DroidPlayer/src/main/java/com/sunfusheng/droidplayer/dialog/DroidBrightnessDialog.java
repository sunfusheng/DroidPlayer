package com.sunfusheng.droidplayer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.sunfusheng.droidplayer.R;

/**
 * Created by sunfusheng on 2017/7/12.
 */
public class DroidBrightnessDialog {

    private Context mContext;
    private Dialog mDialog;

    private ProgressBar progressBar;

    public DroidBrightnessDialog(Context context) {
        this.mContext = context;
        createDialog();
    }

    private void createDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.player_dialog_brightness_layout, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mDialog = new Dialog(mContext, R.style.DialogStyle);
        mDialog.setContentView(view);
    }

    public void show(int progress) {
        if (mDialog == null) {
            createDialog();
        }

        progressBar.setProgress(progress);

        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

}
