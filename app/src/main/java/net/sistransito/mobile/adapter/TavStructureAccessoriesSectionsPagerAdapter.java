package net.sistransito.mobile.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import net.sistransito.mobile.tav.TavAccessoriesFragment;
import net.sistransito.mobile.tav.TavStructureFragment;

public class TavStructureAccessoriesSectionsPagerAdapter extends
		FragmentPagerAdapter {

	public TavStructureAccessoriesSectionsPagerAdapter(FragmentManager fm) {
		super(fm);

	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return new TavStructureFragment();
		case 1:
			return new TavAccessoriesFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}