package net.sistransito.mobile.aitcompany;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sistransito.R;

public class TabPJInfractionSubTitleFragment extends Fragment {
    private View view;

    public static TabPJInfractionSubTitleFragment newInstance() {
        return new TabPJInfractionSubTitleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ait_pj_infraction_fragment_subtitle, null, false);
        return view;
    }

}
