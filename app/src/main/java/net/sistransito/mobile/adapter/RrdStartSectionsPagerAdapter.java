package net.sistransito.mobile.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import net.sistransito.mobile.rrd.TabRrdDocumentFragment;
import net.sistransito.mobile.rrd.TabRrdInformationFragment;


public class RrdStartSectionsPagerAdapter extends FragmentPagerAdapter {

	public RrdStartSectionsPagerAdapter(FragmentManager fm) {
		super(fm);

	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return TabRrdDocumentFragment.newInstance();
		case 1:
			return  TabRrdInformationFragment.newInstance();

		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}