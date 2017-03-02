package com.sunfusheng.droidplayer.sample.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sunfusheng.droidplayer.sample.R;
import com.sunfusheng.droidplayer.sample.model.VideoModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 2017/3/2.
 */
public class VideoAdapter extends BaseListAdapter<VideoModel> {

    public VideoAdapter(Context context, List<VideoModel> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_video, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoModel model = getItem(position);
        holder.tvTitle.setText(model.title);
        holder.tvSize.setText(model.size);
        holder.tvDuration.setText(model.duration);

        Glide.with(mContext)
                .load(model.image_url)
                .crossFade()
                .fallback(R.color.player_transparent)
                .error(R.color.player_transparent)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivImage);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_duration)
        TextView tvDuration;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
