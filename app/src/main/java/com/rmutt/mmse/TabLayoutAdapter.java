package com.rmutt.mmse;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabLayoutAdapter extends FragmentPagerAdapter{

    private int numOfTabs;

    public TabLayoutAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, numOfTabs);

        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new Patient_Data();
            case 1:
                return new Manual();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}
