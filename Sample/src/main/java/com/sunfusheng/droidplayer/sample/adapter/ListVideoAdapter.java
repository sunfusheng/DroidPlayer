package com.sunfusheng.droidplayer.sample.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.droidplayer.DroidPlayerView;
import com.sunfusheng.droidplayer.sample.R;
import com.sunfusheng.droidplayer.sample.model.VideoEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 2017/3/21.
 */
public class ListVideoAdapter extends BaseListAdapter<VideoEntity> {

    public ListVideoAdapter(Context context, List<VideoEntity> list) {
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

        VideoEntity entity = getItem(position);
        holder.playerView.setVideoTitle(entity.getTitle());
        holder.playerView.setImageUrl(entity.getCover());
        holder.playerView.setVideoUrl(TextUtils.isEmpty(entity.getMp4Hd_url())? entity.getMp4_url():entity.getMp4Hd_url());
        holder.playerView.setPositionInList(position);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.playerView)
        DroidPlayerView playerView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
