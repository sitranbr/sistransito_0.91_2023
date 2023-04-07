package net.sistransito.mobile.ait;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.PrepopulatedDBOpenHelper;
import net.sistransito.mobile.database.SetttingDatabaseHelper;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

import java.util.ArrayList;
import java.util.List;

public class TabAitAddressFragment extends
		Fragment implements
		View.OnClickListener, TimeListener, DateListener {

	private View view;
	private AutoCompleteTextView cityAutoComplete;
	private AitData aitData;
	private EditText etAddressInfration, etCityCode, etState;
	private Button btnAitDate, btnAitTime;
	private LinearLayout llCityState;
	private TextView tvSaveData;
	private CheckBox cbConfirm;

	private List<String> cityArray, codeArray, stateArray;
	private ArrayAdapter<String> cityAdapter;

	private Bundle bundle;

	public static TabAitAddressFragment newInstance() {
		return new TabAitAddressFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.ait_address_fragment, null, false);
		initializedView();
		getAitObject();
		return view;
	}

	private void initializedView() {

		llCityState = (LinearLayout) view.findViewById(R.id.ll_auto_municipio_uf);

		cityAutoComplete = (AutoCompleteTextView) view
				.findViewById(R.id.auto_auto_municipio);

		etCityCode = (EditText) view
				.findViewById(R.id.et_auto_end_codigo_municipio);
		etState = (EditText) view.findViewById(R.id.et_auto_end_uf);

		etAddressInfration = (EditText) view.findViewById(R.id.et_ait_local);
		btnAitDate = (Button) view
				.findViewById(R.id.btn_ait_pj_date);
		btnAitTime = (Button) view
				.findViewById(R.id.btn_ait_pj_time);

		tvSaveData = (TextView) view.findViewById(R.id.ait_fab);
		cbConfirm = (CheckBox) view.findViewById(R.id.cb_auto_confirmar);

		etCityCode.setEnabled(false);
		etState.setEnabled(false);

		setCityAutoComplete();
		hideComponents();

	}

	private void hideComponents(){

		llCityState.setVisibility(LinearLayout.GONE);

	}

	private void  showComponents(){

		llCityState.setVisibility(LinearLayout.VISIBLE);
		Routine.closeKeyboard(cityAutoComplete, getActivity());

	}

	public void setCityAutoComplete() {

		cityArray = new ArrayList<String>();
		codeArray = new ArrayList<String>();
		stateArray = new ArrayList<String>();
		Cursor myCursor;

		Cursor cursor = (DatabaseCreator
				.getSettingDatabaseAdapter(getActivity()))
				.getSettingCursor();

		String stateCity = cursor.getString(cursor.getColumnIndex(SetttingDatabaseHelper.SETTING_STATE));

		//Log.d("UfLocal", stateCity);

		myCursor = ((DatabaseCreator
				.getPrepopulatedDBOpenHelper(getActivity()))
				.getInitialsStateCursor(stateCity));

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
		cityAutoComplete.setAdapter(cityAdapter);

		cityAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int pos, long arg3) {

				aitData.setCity((String) parent.getItemAtPosition(pos));
				int real_position = cityArray.indexOf(parent.getItemAtPosition(pos));

				aitData.setCityCode(codeArray.get(real_position));
				etCityCode.setText(aitData.getCityCode());

				aitData.setState(stateArray.get(real_position));
				etState.setText(aitData.getState());

				showComponents();

			}
		});
	}

	private void addListener() {

		etAddressInfration.addTextChangedListener(new ChangeText(
				R.id.et_ait_local));

		btnAitDate.setOnClickListener(this);
		btnAitTime.setOnClickListener(this);

		hideComponents();

		cbConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cbConfirm.isChecked()) {
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

					if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).updateAitDataAddress(aitData))
						Routine.showAlert(getResources().getString(R.string.update_erro), getActivity());

					((AitActivity) getActivity()).setTabActual(3);

				}

			}
		});

	}

	private boolean checkInput(){

		//Log.d("TextoData", btnDataInfracao.getText().toString());
		//Log.d("TextoHora", btnHoraInfracao.getText().toString());

		if (etAddressInfration.getText().toString().isEmpty()) {
			etAddressInfration.setError(getResources().getString(
					R.string.alert_insert_address));
			etAddressInfration.requestFocus();
			return false;
		} else if (cityAutoComplete.getText().toString().isEmpty()) {
			cityAutoComplete.setError(getResources().getString(
					R.string.alert_insert_city_name));
			cityAutoComplete.requestFocus();
			return false;
		} else if (btnAitDate.getText().toString().equals("Data")) {
			Routine.showAlert(getActivity().getString(R.string.alert_insert_date), getActivity());
			btnAitDate.requestFocus();
			return false;
		} else if (btnAitTime.getText().toString().equals("Hora")) {
				Routine.showAlert(getActivity().getString(R.string.alert_insert_time), getActivity());
				btnAitTime.requestFocus();
				return false;
		} else {
			return true;
		}

	}

	private void getAitObject() {
		aitData = AitObject.getAitData();

		if (aitData.isDataisNull()) {
			addListener();
		} else if (aitData.isStoreFullData()) {
			getRecomandedUpdate();
			addListener();
			setNewAitView();
		} else {
			addListener();
			initializedSelectetItems();
		}

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

		etCityCode.setText(aitData.getCityCode());
		etState.setText(aitData.getState());
		etAddressInfration.setText(aitData.getAddress());

	}

	private class ChangeText implements TextWatcher {
		private int id;

		public ChangeText(int id) {
			this.id = id;
		}

		@Override
		public void afterTextChanged(Editable edit) {
			String s = (edit.toString()).trim();
			switch (id) {
				case R.id.et_ait_local:
					aitData.setAddress(s.toString());
					break;
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
		switch (v.getId()) {
			case R.id.btn_ait_pj_date:
				MyDatePicker pickerdate = new MyDatePicker();
				bundle = new Bundle();
				bundle.putInt(MyDatePicker.MY_DATE_PICKER_ID,
						R.id.btn_ait_pj_date);
				pickerdate.setArguments(bundle);
				pickerdate.setTargetFragment(this, 0);
				pickerdate.show(getActivity().getSupportFragmentManager(), "date");
				break;

			case R.id.btn_ait_pj_time:
				MyTimePicker picker = new MyTimePicker();
				bundle = new Bundle();
				bundle.putInt(MyTimePicker.MY_TIME_PICKER_ID,
						R.id.btn_ait_pj_time);
				picker.setArguments(bundle);
				picker.setTargetFragment(this, 0);
				picker.show(getActivity().getSupportFragmentManager(), "time");
				break;
		}

	}

	@Override
	public void time(String time, int view_id) {
		if (time != null) {
			aitData.setAitTime(time);
			btnAitTime.setText(aitData.getAitTime());
		}

	}

	@Override
	public void date(String date, int view_id) {
		if (date != null) {
			aitData.setAitDate(date);
			btnAitDate.setText(aitData.getAitDate());
		}

	}

	private void setNewAitView() {

		cityAutoComplete.setVisibility(AutoCompleteTextView.GONE);
		etCityCode.setVisibility(EditText.GONE);
		etState.setVisibility(EditText.GONE);
		btnAitDate.setVisibility(Button.GONE);
		btnAitTime.setVisibility(Button.GONE);

	}

}
