package net.sistransito.mobile.aitcompany;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.widget.CheckBox;

import net.sistransito.library.datepicker.DateListener;
import net.sistransito.library.datepicker.MyDatePicker;
import net.sistransito.library.datepicker.MyTimePicker;
import net.sistransito.library.datepicker.TimeListener;
import net.sistransito.mobile.ait.AitActivity;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.PrepopulatedDBOpenHelper;
import net.sistransito.mobile.database.SetttingDatabaseHelper;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

import java.util.ArrayList;
import java.util.List;

public class TabPJAddressFragment extends
        Fragment implements
        View.OnClickListener, TimeListener, DateListener {

    private View view;
    private AutoCompleteTextView aitCompleteCities;
    private pjData pjData;
    private EditText etAitInfractionAddress, etAitInfractionState;
    private Button btnInfractionDate, btnInfractionTime;
    private LinearLayout llAitState;
    private TextView tvSaveData;
    private CheckBox cbAitConfirm;

    private List<String> cityArray, codeArray, stateArray;
    private ArrayAdapter<String> cityAdapter;

    private Bundle bundle;

    public static TabPJAddressFragment newInstance() {
        return new TabPJAddressFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ait_pj_address_fragment, null, false);
        initializedView();
        getAitPjObject();
        return view;
    }

    private void initializedView() {

        llAitState = (LinearLayout) view.findViewById(R.id.ll_auto_municipio_uf);

        aitCompleteCities = (AutoCompleteTextView) view
                .findViewById(R.id.auto_auto_municipio);

        etAitInfractionState = (EditText) view.findViewById(R.id.et_auto_end_uf);

        etAitInfractionAddress = (EditText) view.findViewById(R.id.et_ait_local);
        btnInfractionDate = (Button) view
                .findViewById(R.id.btn_ait_pj_date);
        btnInfractionTime = (Button) view
                .findViewById(R.id.btn_ait_pj_time);

        tvSaveData = (TextView) view.findViewById(R.id.ait_fab);
        cbAitConfirm = (CheckBox) view.findViewById(R.id.cb_auto_confirmar);

        etAitInfractionState.setEnabled(false);

        setAutoCompleteCity();
        hideComponents();

    }

    private void hideComponents(){

        llAitState.setVisibility(LinearLayout.GONE);

    }

    private void  showComponents(){

        llAitState.setVisibility(LinearLayout.VISIBLE);
        Routine.closeKeyboard(aitCompleteCities, getActivity());

    }

    @SuppressLint("Range")
    public void setAutoCompleteCity() {

        cityArray = new ArrayList<String>();
        codeArray = new ArrayList<String>();
        stateArray = new ArrayList<String>();

        Cursor myCursor;

        Cursor cursor = (DatabaseCreator
                .getSettingDatabaseAdapter(getActivity()))
                .getSettingCursor();

        String stateCode = cursor.getString(cursor.getColumnIndex(SetttingDatabaseHelper.SETTING_STATE));

        //Log.d("address", stateCode);

        myCursor = ((DatabaseCreator
                .getPrepopulatedDBOpenHelper(getActivity()))
                .getInitialsStateCursor(stateCode));

        if (myCursor.getCount() <= 0) {
            myCursor = ((DatabaseCreator
                    .getPrepopulatedDBOpenHelper(getActivity()))
                    .getCityCursor());
        }

        do {
            cityArray.add(myCursor.getString(myCursor.getColumnIndex(PrepopulatedDBOpenHelper.CITY_NAME)));
            stateArray.add(myCursor.getString(myCursor.getColumnIndex(PrepopulatedDBOpenHelper.STATE)));
            codeArray.add(myCursor.getString(myCursor.getColumnIndex(PrepopulatedDBOpenHelper.CITY_CODE)));
        } while (myCursor.moveToNext());
        myCursor.close();

        cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_autocompletar, R.id.autoCompleteItem, cityArray);
        aitCompleteCities.setAdapter(cityAdapter);

        aitCompleteCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int pos, long arg3) {

                pjData.setCity((String) parent.getItemAtPosition(pos));
                int real_position = cityArray.indexOf(parent.getItemAtPosition(pos));

                pjData.setState(stateArray.get(real_position));
                etAitInfractionState.setText(pjData.getState());

                showComponents();

            }
        });
    }

    private void addListener() {

        etAitInfractionAddress.addTextChangedListener(new TabPJAddressFragment.ChangeText(
                R.id.et_ait_local));

        btnInfractionDate.setOnClickListener(this);
        btnInfractionTime.setOnClickListener(this);

        hideComponents();

        cbAitConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbAitConfirm.isChecked()) {
                    tvSaveData.setVisibility(view.VISIBLE);
                    Routine.closeKeyboard(tvSaveData, getActivity());
                }else{
                    tvSaveData.setVisibility(view.GONE);
                }
            }
        });

        tvSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkInput()) {

                    if (!DatabaseCreator.getAitPJDatabaseAdapter(getActivity()).setDadosPJEnd(pjData))
                        Routine.showAlert(getResources().getString(R.string.update_erro), getActivity());

                    ((AitActivity) getActivity()).setTabActual(3);

                }

            }
        });

    }

    private boolean checkInput(){

        if (etAitInfractionAddress.getText().toString().isEmpty()) {
            etAitInfractionAddress.setError(getResources().getString(
                    R.string.alert_insert_address));
            etAitInfractionAddress.requestFocus();
            return false;
        } else if (aitCompleteCities.getText().toString().isEmpty()) {
            aitCompleteCities.setError(getResources().getString(
                    R.string.alert_insert_city_name));
            aitCompleteCities.requestFocus();
            return false;
        } else if (btnInfractionDate.getText().toString().equals("Data")) {
            Routine.showAlert(getActivity().getString(R.string.alert_insert_date), getActivity());
            btnInfractionDate.requestFocus();
            return false;
        } else if (btnInfractionTime.getText().toString().equals("Hora")) {
            Routine.showAlert(getActivity().getString(R.string.alert_insert_time), getActivity());
            btnInfractionTime.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    private void getAitPjObject() {
        pjData = ObjectAutoPJ.getDadosAuto();
        if (pjData.isDataisPJNull()) {
            addListener();
        } else if (pjData.isStorePJFullData()) {
            getRecomandedUpdate();
            addListener();
            setViewNovoAuto();
        } else {
            addListener();
            initializedSelectetItems();
        }
        addListener();
    }

    private void initializedSelectetItems() {
		/*
		editAutoLocalInfracao.setText("");
		editAutoCodigoMunicipio.setText("");
		editAutoUfLocal.setText("");
		btnDataInfracao.setText("");
		btnHoraInfracao.setText("");
		spinner_auto_descricao.setSelection(0);
        */
    }

    private void getRecomandedUpdate() {

        etAitInfractionState.setText(pjData.getState());
        etAitInfractionAddress.setText(pjData.getPlace());

    }

    private class ChangeText implements TextWatcher {
        private int id;

        public ChangeText(int id) {
            this.id = id;
        }

        @Override
        public void afterTextChanged(Editable edit) {
            String s = (edit.toString()).trim();
            if (id == R.id.et_ait_local) {
                pjData.setPlace(s.toString());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ait_pj_date) {
            MyDatePicker pickerdate = new MyDatePicker();
            bundle = new Bundle();
            bundle.putInt(MyDatePicker.MY_DATE_PICKER_ID,
                    R.id.btn_ait_pj_date);
            pickerdate.setArguments(bundle);
            pickerdate.setTargetFragment(this, 0);
            pickerdate.show(getActivity().getSupportFragmentManager(), "date");
        } else if (id == R.id.btn_ait_pj_time) {
            MyTimePicker picker = new MyTimePicker();
            bundle = new Bundle();
            bundle.putInt(MyTimePicker.MY_TIME_PICKER_ID,
                    R.id.btn_ait_pj_time);
            picker.setArguments(bundle);
            picker.setTargetFragment(this, 0);
            picker.show(getActivity().getSupportFragmentManager(), "time");
        }

    }

    @Override
    public void time(String time, int view_id) {
        if (time != null) {
            pjData.setAitTime(time);
            btnInfractionTime.setText(pjData.getAitTime());
        }

    }

    @Override
    public void date(String date, int view_id) {
        if (date != null) {
            pjData.setAitDate(date);
            btnInfractionDate.setText(pjData.getAitDate());
        }

    }

    private void setViewNovoAuto() {

        aitCompleteCities.setVisibility(AutoCompleteTextView.GONE);
        etAitInfractionState.setVisibility(EditText.GONE);
        btnInfractionDate.setVisibility(Button.GONE);
        btnInfractionTime.setVisibility(Button.GONE);

    }

}
