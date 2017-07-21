package com.sunfusheng.droidplayer.sample.viewprovider;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.droidplayer.DroidPlayerView;
import com.sunfusheng.droidplayer.sample.R;
import com.sunfusheng.droidplayer.sample.model.VideoEntity;
import com.sunfusheng.droidplayer.sample.util.AppUtil;
import com.sunfusheng.droidplayer.sample.widget.MultiType.ItemViewProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 2017/3/14.
 */
public class ItemVideoProvider extends ItemViewProvider<VideoEntity, ItemVideoProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_recyclerview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull VideoEntity entity) {
        holder.itemRecyclerViewTitle.setText(entity.getTitle());
        holder.itemRecyclerViewTime.setText(AppUtil.getTimeString(entity.getLength()));

        holder.itemRecyclerViewPlayerView.setImageUrl(entity.getCover());
        holder.itemRecyclerViewPlayerView.setVideoUrl(TextUtils.isEmpty(entity.getMp4Hd_url()) ? entity.getMp4_url() : entity.getMp4Hd_url());
        holder.itemRecyclerViewPlayerView.setPositionInList(holder.getAdapterPosition());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemRecyclerViewPlayerView)
        DroidPlayerView itemRecyclerViewPlayerView;
        @BindView(R.id.itemRecyclerViewTitle)
        TextView itemRecyclerViewTitle;
        @BindView(R.id.itemRecyclerViewTime)
        TextView itemRecyclerViewTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
