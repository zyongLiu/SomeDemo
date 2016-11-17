package com.example.administrator.testsurfaceview.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.administrator.testsurfaceview.fragment.PlaceholderFragment;

import java.util.Locale;

/**
 * Created by Liu on 2016/11/17.
 */
public class DummyAdapter extends FragmentPagerAdapter {

    public DummyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position%3 + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 30;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position%3) {
            case 0:
                return "PAGE 1";
            case 1:
                return "PAGE 2";
            case 2:
                return "PAGE 3";
        }
        return null;
    }

}
