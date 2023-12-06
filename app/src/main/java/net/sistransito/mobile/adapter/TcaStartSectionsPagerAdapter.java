package net.sistransito.mobile.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import net.sistransito.mobile.tca.TcaConductorFragment;
import net.sistransito.mobile.tca.TcaQuizFragment;

public class TcaStartSectionsPagerAdapter extends FragmentPagerAdapter {

	public TcaStartSectionsPagerAdapter(FragmentManager fm) {
		super(fm);

	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			return  TcaConductorFragment.newInstance();
		case 1:
			return  TcaQuizFragment.newInstance();
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}