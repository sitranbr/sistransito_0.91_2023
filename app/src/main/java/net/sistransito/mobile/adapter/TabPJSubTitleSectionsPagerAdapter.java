package net.sistransito.mobile.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import net.sistransito.mobile.aitcompany.TabPJConductorSubTitleFragment;
import net.sistransito.mobile.aitcompany.TabPJAddressSubTitleFragment;
import net.sistransito.mobile.aitcompany.TabPJInfractionSubTitleFragment;

public class TabPJSubTitleSectionsPagerAdapter extends FragmentPagerAdapter

    {

        @Override
        public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

        @Override
        public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

	public TabPJSubTitleSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

        @Override
        public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return TabPJConductorSubTitleFragment.newInstance();
            case 1:
                return TabPJAddressSubTitleFragment.newInstance();
            case 2:
                return TabPJInfractionSubTitleFragment.newInstance();
        }
        return null;
    }

        @Override
        public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
