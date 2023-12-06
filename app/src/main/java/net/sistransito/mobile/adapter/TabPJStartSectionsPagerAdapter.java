package net.sistransito.mobile.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import net.sistransito.mobile.aitcompany.TabPJAddressFragment;
import net.sistransito.mobile.aitcompany.TabPJInfracaoFragment;
import net.sistransito.mobile.aitcompany.TabPJConductorFragment;

public class TabPJStartSectionsPagerAdapter extends FragmentPagerAdapter {

    public TabPJStartSectionsPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return TabPJConductorFragment.newInstance();
            case 1:
                return TabPJAddressFragment.newInstance();
            case 2:
                return TabPJInfracaoFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}