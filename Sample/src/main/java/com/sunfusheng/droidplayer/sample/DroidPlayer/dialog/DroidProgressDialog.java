package com.sunfusheng.droidplayer.sample.DroidPlayer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.util.PlayerUtil;
import com.sunfusheng.droidplayer.sample.R;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by sunfusheng on 2017/7/10.
 */
public class DroidProgressDialog {

    private Context mContext;
    private Dialog mDialog;

    private ImageView imageView;
    private TextView textView;
    private ProgressBar progressBar;

    public DroidProgressDialog(Context context) {
        this.mContext = context;
        createDialog();
    }

    private void createDialog() {
        mDialog = new Dialog(mContext, R.style.DialogStyle);

        View view = LayoutInflater.from(mContext).inflate(R.layout.droid_progress_dialog_layout, null);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        textView = (TextView) view.findViewById(R.id.textView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mDialog.setContentView(view);
    }

    public void show(IjkMediaPlayer ijkMediaPlayer, long endPosition, float endPercent) {
        if (mDialog == null) {
            createDialog();
        }

        long curPosition = ijkMediaPlayer.getCurrentPosition();
        long duration = ijkMediaPlayer.getDuration();

        imageView.setImageResource(endPosition >= curPosition ? R.mipmap.player_forward_icon : R.mipmap.player_backward_icon);
        textView.setText(PlayerUtil.getTimeString(endPosition) + "/" + PlayerUtil.getTimeString(duration));
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
