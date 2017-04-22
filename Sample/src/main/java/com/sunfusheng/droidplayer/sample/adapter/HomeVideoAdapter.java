package com.sunfusheng.droidplayer.sample.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sunfusheng.droidplayer.sample.R;
import com.sunfusheng.droidplayer.sample.model.VideoEntity;
import com.sunfusheng.droidplayer.sample.util.AppUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 2017/3/2.
 */
public class HomeVideoAdapter extends BaseListAdapter<VideoEntity> {

    public HomeVideoAdapter(Context context, List<VideoEntity> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_home_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoEntity entity = getItem(position);
        holder.tvTitle2.setText(entity.getTitle());
        holder.tvDuration2.setText(AppUtil.getTimeString(entity.getLength()));

        Glide.with(mContext)
                .load(entity.getCover())
                .crossFade()
                .fallback(R.color.player_white_color)
                .error(R.color.player_transparent)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivImage2);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_image2)
        ImageView ivImage2;
        @BindView(R.id.tv_duration2)
        TextView tvDuration2;
        @BindView(R.id.tv_title2)
        TextView tvTitle2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
