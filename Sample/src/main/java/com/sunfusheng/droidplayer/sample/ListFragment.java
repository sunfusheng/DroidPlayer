package com.sunfusheng.droidplayer.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.adapter.ListVideoAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 2017/3/3.
 */
public class ListFragment extends BaseFragment {

    @BindView(R.id.listView)
    ListView listView;

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

    }
    ListVideoAdapter mAdapter;
    private void initView() {
        mAdapter = new ListVideoAdapter(mContext, mList);
        listView.setAdapter(mAdapter);
    }

    private void initListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!DroidMediaPlayer.getInstance().isPlaying()) return;
                if (DroidMediaPlayer.getInstance().getPositionInList() < 0) return;
                int positionInList = DroidMediaPlayer.getInstance().getPositionInList();
                int lastVisibleItem = view.getLastVisiblePosition();

                Log.d("-----> ", "positionInList: "+positionInList+" firstVisibleItem: "+firstVisibleItem+" lastVisibleItem: "+lastVisibleItem);
                if (positionInList < firstVisibleItem || positionInList > lastVisibleItem) {
                    DroidMediaPlayer.getInstance().release();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected boolean onBackPressed() {
        if (DroidMediaPlayer.getInstance().onBackPressed()) {
            return true;
        }
        return false;
    }
}
