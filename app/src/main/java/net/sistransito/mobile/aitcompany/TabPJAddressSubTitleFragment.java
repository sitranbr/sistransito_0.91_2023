package net.sistransito.mobile.aitcompany;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sistransito.R;

public class TabPJAddressSubTitleFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ait_pj_address_fragment_subtitle, null, false);
        return view;
    }
    public static TabPJAddressSubTitleFragment newInstance() {
        return new TabPJAddressSubTitleFragment();
    }

}
