package net.sistransito.mobile.ait;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sistransito.R;


public class TabAddressSubTitleFragment extends Fragment {
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.ait_address_fragment_subtitle, null, false);
		return view;
	}
	public static TabAddressSubTitleFragment newInstance() {
		return new TabAddressSubTitleFragment();
	}

}
