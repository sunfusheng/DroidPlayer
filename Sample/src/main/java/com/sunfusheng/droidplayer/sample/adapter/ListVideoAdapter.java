package com.sunfusheng.droidplayer.sample.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidPlayerView;
import com.sunfusheng.droidplayer.sample.R;
import com.sunfusheng.droidplayer.sample.model.VideoModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 2017/3/13.
 */
public class ListVideoAdapter extends BaseListAdapter<VideoModel> {

    public ListVideoAdapter(Context context, List<VideoModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoModel model = getItem(position);
        holder.tvVideoTitle.setText(model.title);
        holder.tvVideoSize.setText(model.size);
        holder.tvVideoDuration.setText(model.duration);

        holder.playerView.setImageUrl(model.image_url);
        holder.playerView.setVideoUrl(model.video_url);
        holder.playerView.setPositionInList(position);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.playerView)
        DroidPlayerView playerView;
        @BindView(R.id.tv_video_duration)
        TextView tvVideoDuration;
        @BindView(R.id.tv_video_title)
        TextView tvVideoTitle;
        @BindView(R.id.tv_video_size)
        TextView tvVideoSize;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}