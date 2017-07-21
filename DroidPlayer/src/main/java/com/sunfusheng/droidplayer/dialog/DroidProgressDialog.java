package com.sunfusheng.droidplayer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sunfusheng.droidplayer.R;
import com.sunfusheng.droidplayer.util.PlayerUtil;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by sunfusheng on 2017/7/12.
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.player_dialog_progress_layout, null);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        textView = (TextView) view.findViewById(R.id.textView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mDialog = new Dialog(mContext, R.style.DialogStyle);
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
