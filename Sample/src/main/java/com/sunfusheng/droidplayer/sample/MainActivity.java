package com.sunfusheng.droidplayer.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static final String TAG_MAIN = "main";
    public static final String TAG_SAMPLE = "sample";
    public static final String TAG_LIST = "list";
    public static final String TAG_RECYCLER_VIEW = "recycler_view";
    public static final String TAG_ABOUT = "about";
    private String curTag;
    private MenuItem lastItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar(toolbar, R.string.app_name, false);
        showFragment(TAG_MAIN, new MainFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_more, menu);
        lastItem = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DroidMediaPlayer.getInstance().pause();
        item.setCheckable(true);
        item.setChecked(true);
        if (lastItem != null && lastItem != item) {
            lastItem.setCheckable(false);
            lastItem.setChecked(false);
        }
        lastItem = item;
        switch (item.getItemId()) {
            case R.id.item_main:
                showFragment(TAG_MAIN, new MainFragment());
                break;
            case R.id.item_sample:
                showFragment(TAG_SAMPLE, new SampleFragment());
                break;
            case R.id.item_list:
                showFragment(TAG_LIST, new ListFragment());
                break;
            case R.id.item_recycler_view:
                showFragment(TAG_RECYCLER_VIEW, new RecyclerViewFragment());
                break;
            case R.id.item_about:
                showFragment(TAG_ABOUT, new AboutFragment());
                break;
        }
        return true;
    }

    private void showFragment(String tag, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment oldFragment = fragmentManager.findFragmentByTag(curTag);
        if (oldFragment != null) {
            fragmentTransaction.hide(oldFragment);
        }
        curTag = tag;
        Fragment curFragment = fragmentManager.findFragmentByTag(tag);
        if (curFragment != null) {
            fragmentTransaction.show(curFragment);
        } else {
            fragmentTransaction.add(R.id.fl_container, fragment, tag);
        }
        fragmentTransaction.commit();
    }

}
