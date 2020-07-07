package com.g15.demo.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.g15.demo.R;
import com.g15.demo.ui.main.Fragment1;
import com.g15.demo.ui.main.Fragment2;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    //private String[] mTitles = new String[]{"Settings", "Context"};
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 1) {
            return new Fragment2();
        } else {
            return new Fragment1();
        }
        // return Fragment1.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        //return 2;
        //return mTitles.length;
        return TAB_TITLES.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
        //return mTitles[position];
    }
}