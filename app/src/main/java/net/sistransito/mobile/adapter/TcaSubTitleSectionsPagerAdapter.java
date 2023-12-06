package net.sistransito.mobile.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import net.sistransito.mobile.tca.SubTitleTcaConductorFragment;
import net.sistransito.mobile.tca.SubTitleTcaQuizFragment;

public class TcaSubTitleSectionsPagerAdapter extends FragmentPagerAdapter {

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
	}

	@Override
	public void startUpdate(ViewGroup container) {
		super.startUpdate(container);
	}

	public TcaSubTitleSectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {

		case 0:
			return SubTitleTcaConductorFragment.newInstance();
		case 1:
			return SubTitleTcaQuizFragment.newInstance();
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}
}