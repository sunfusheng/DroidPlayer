package com.sunfusheng.droidplayer.sample.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sunfusheng.droidplayer.sample.DroidPlayer.DroidMediaPlayer;
import com.sunfusheng.droidplayer.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Fragment[] fragments = {
            new HomeFragment(),
            new SampleFragment(),
            new ListFragment(),
            new RecyclerViewFragment(),
            new AboutFragment()
    };

    private static final String[] TAGS = {
            "home",
            "sample",
            "list",
            "recycler_view",
            "about"
    };

    private static int lastPosition = 0;
    private MenuItem lastMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar(toolbar, R.string.app_name, false);
        showFragment(lastPosition, fragments[lastPosition]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_more, menu);
        lastMenuItem = menu.getItem(lastPosition);
        if (lastMenuItem != null) {
            lastMenuItem.setCheckable(true);
            lastMenuItem.setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DroidMediaPlayer.getInstance().release();
        if (lastMenuItem != null && lastMenuItem != item) {
            lastMenuItem.setCheckable(false);
            lastMenuItem.setChecked(false);
        }
        lastMenuItem = item;
        item.setCheckable(true);
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.item_main:
                showFragment(0, fragments[0]);
                break;
            case R.id.item_sample:
                showFragment(1, fragments[1]);
                break;
            case R.id.item_list:
                showFragment(2, fragments[2]);
                break;
            case R.id.item_recycler_view:
                showFragment(3, fragments[3]);
                break;
            case R.id.item_about:
                showFragment(4, fragments[4]);
                break;
        }
        return true;
    }

    private void showFragment(int position, Fragment fragment) {
        setBackPressedFragment(fragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment oldFragment = fragmentManager.findFragmentByTag(TAGS[lastPosition]);
        if (oldFragment != null) {
            fragmentTransaction.hide(oldFragment);
        }
        lastPosition = position;
        Fragment curFragment = fragmentManager.findFragmentByTag(TAGS[position]);
        if (curFragment != null) {
            fragmentTransaction.show(curFragment);
        } else {
            fragmentTransaction.add(R.id.fl_container, fragment, TAGS[position]);
        }
        fragmentTransaction.commit();
    }

}
