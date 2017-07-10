package com.sunfusheng.droidplayer.sample.DroidPlayer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sunfusheng.droidplayer.sample.R;

/**
 * Created by sunfusheng on 2017/7/10.
 */
public class DroidProgressDialog {

    private Context mContext;
    private Dialog mDialog;

    private ImageView ivIndex;
    private TextView tvPosition;
    private ProgressBar progressBar;

    public DroidProgressDialog(Context context) {
        this.mContext = context;
        createDialog();
    }

    private void createDialog() {
        mDialog = new Dialog(mContext, R.style.DialogStyle);

        View view = LayoutInflater.from(mContext).inflate(R.layout.droid_progress_dialog_layout, null);
        ivIndex = (ImageView) view.findViewById(R.id.iv_index);
        tvPosition = (TextView) view.findViewById(R.id.tv_position);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mDialog.setContentView(view);
    }

    public void show(float startPercent, float endPercent, String endTime, String totalTime) {
        if (mDialog == null) {
            createDialog();
        }

        ivIndex.setImageResource(endPercent >= startPercent ? R.mipmap.player_forward_icon : R.mipmap.player_backward_icon);
        tvPosition.setText(endTime + "/" + totalTime);
        progressBar.setProgress((int) (endPercent * 100));

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
