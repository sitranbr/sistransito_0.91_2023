package net.sistransito.mobile.plate.lister;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import net.sistransito.mobile.appobject.AppObject;
import net.sistransito.R;

public class LogListFragment extends Fragment implements OnClickListener {
    private View view;
    private Button btnListPlate, btnListAit, btnListRrd,
            btnListTca, btnListTav;

    public static LogListFragment newInstance() {
        return new LogListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history_list_fragment, null, false);
        initializedView();
        return view;
    }

    private void initializedView() {
        btnListPlate = (Button) view.findViewById(R.id.btn_list_plate);
        btnListPlate.setOnClickListener(this);
        btnListAit = (Button) view.findViewById(R.id.btn_list_ait);
        btnListAit.setOnClickListener(this);
        btnListRrd = (Button) view.findViewById(R.id.btn_list_rrd);
        btnListRrd.setOnClickListener(this);
        btnListTca = (Button) view.findViewById(R.id.btn_list_tca);
        btnListTca.setOnClickListener(this);
        btnListTav = (Button) view.findViewById(R.id.btn_list_tav);
        btnListTav.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_list_plate) {
            startActivity(AppObject.getPlateListIntent(getActivity()));
        } else if (id == R.id.btn_list_ait) {
            startActivity(AppObject.getAitListerIntent(getActivity()));
        } else if (id == R.id.btn_list_rrd) {
            startActivity(AppObject.getRrdListerIntent(getActivity()));
        } else if (id == R.id.btn_list_tca) {
            startActivity(AppObject.getTcaListerIntent(getActivity()));
        } else if (id == R.id.btn_list_tav) {
            startActivity(AppObject.getTavListerIntent(getActivity()));
        }
    }
}
