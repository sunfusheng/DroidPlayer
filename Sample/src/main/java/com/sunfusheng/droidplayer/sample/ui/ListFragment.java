package com.sunfusheng.droidplayer.sample.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.R;
import com.sunfusheng.droidplayer.sample.adapter.ListVideoAdapter;
import com.sunfusheng.droidplayer.sample.http.Api;
import com.sunfusheng.droidplayer.sample.http.ApiService;
import com.sunfusheng.droidplayer.sample.util.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.schedulers.Schedulers;

/**
 * Created by sunfusheng on 2017/3/3.
 */
public class ListFragment extends BaseFragment {

    @BindView(R.id.listView)
    ListView listView;

    ListVideoAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        ButterKnife.bind(this, view);
        initData();
        initView();
        initListener();
        return view;
    }

    private void initData() {
        getVideoList();
    }

    private void initView() {

    }

    private void initListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            int firstVisibleItem;
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.firstVisibleItem = firstVisibleItem;
                this.lastVisibleItem = firstVisibleItem + visibleItemCount;
                DroidMediaPlayer.getInstance().releaseOnScroll(firstVisibleItem, lastVisibleItem);
            }
        });
    }

    // 获取视频数据
    protected void getVideoList() {
        Api.getInstance().getApiService().getVideoList(0)
                .subscribeOn(Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .filter(it -> it != null && it.containsKey(ApiService.ID))
                .map(it -> it.get(ApiService.ID))
                .compose(bindToLifecycle())
                .subscribe(list -> {
                    if (AppUtil.notEmpty(list)) {
                        mAdapter = new ListVideoAdapter(getContext(), list);
                        listView.setAdapter(mAdapter);
                    }
                }, Throwable::printStackTrace);
    }

}
