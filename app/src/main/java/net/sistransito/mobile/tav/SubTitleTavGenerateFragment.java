package net.sistransito.mobile.tav;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sistransito.R;


public class SubTitleTavGenerateFragment extends Fragment {
	private View view;

	public static SubTitleTavGenerateFragment newInstance() {
		return new SubTitleTavGenerateFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tav_generation_fragment_subtitle, null, false);
		return view;
	}

}
