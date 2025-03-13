package net.sistransito.mobile.ait;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rey.material.widget.CheckBox;
import net.sistransito.mobile.fragment.BasePickerFragment;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.PrepopulatedDBOpenHelper;
import net.sistransito.mobile.database.SetttingDatabaseHelper;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

import java.util.ArrayList;
import java.util.List;

public class TabAitAddressFragment extends BasePickerFragment implements View.OnClickListener {

	private View view;
	private AutoCompleteTextView cityAutoComplete;
	private AitData aitData;
	private EditText etAddressInfraction, etCityCode,
			btnAitDate, btnAitTime, etState;
	private LinearLayout llCityState;
	private TextView tvClearData, tvSaveData;
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

		addPicker(R.id.btn_ait_pj_date, "date");
		addPicker(R.id.btn_ait_pj_time, "time");

		btnAitDate.setOnClickListener(this);
		btnAitTime.setOnClickListener(this);

		return view;
	}

	private void initializedView() {

		llCityState = (LinearLayout) view.findViewById(R.id.ll_auto_state);

		cityAutoComplete = (AutoCompleteTextView) view
				.findViewById(R.id.auto_complete_city);

		etCityCode = (EditText) view
				.findViewById(R.id.et_auto_city_code);
		etState = (EditText) view.findViewById(R.id.et_auto_address_state);

		etAddressInfraction = (EditText) view.findViewById(R.id.et_ait_local);
		btnAitDate = (EditText) view
				.findViewById(R.id.btn_ait_pj_date);
		btnAitTime = (EditText) view
				.findViewById(R.id.btn_ait_pj_time);

		tvSaveData = (TextView) view.findViewById(R.id.ait_fab);
		cbConfirm = (CheckBox) view.findViewById(R.id.cb_ait_confirm);

		tvClearData = (TextView) view.findViewById(R.id.tv_clear_data);

		llCityState.setVisibility(LinearLayout.GONE);
		setCityAutoComplete();

		tvClearData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				cityAutoComplete.setText("");
				cityAutoComplete.requestFocus();
				setCityStateVisibility(false);
			}
		});

	}

	private void setCityStateVisibility(boolean show) {
		int visibility = show ? View.VISIBLE : View.GONE;
		llCityState.setVisibility(visibility);
		llCityState.setVisibility(LinearLayout.GONE);

		if (show) {
			Routine.closeKeyboard(cityAutoComplete, getActivity());
			cityAutoComplete.setText("");
			cityAutoComplete.requestFocus();
			etCityCode.setEnabled(true);
			etState.setEnabled(true);

		} else {
			etCityCode.setEnabled(false);
			etState.setEnabled(false);
		}
	}

	public void setCityAutoComplete() {

		cityArray = new ArrayList<>();
		codeArray = new ArrayList<>();
		stateArray = new ArrayList<>();
		Cursor myCursor;

		Cursor cursor = (DatabaseCreator.getSettingDatabaseAdapter(getActivity())).getSettingCursor();

		int stateColumnIndex = cursor.getColumnIndex(SetttingDatabaseHelper.SETTING_STATE);
		String stateCity = (stateColumnIndex != -1) ? cursor.getString(stateColumnIndex) : null;

		if (stateCity != null) {
			myCursor = (DatabaseCreator.getPrepopulatedDBOpenHelper(getActivity())).getInitialsStateCursor(stateCity);
		} else {
			myCursor = (DatabaseCreator.getPrepopulatedDBOpenHelper(getActivity())).getCityCursor();
		}

		if (myCursor != null && myCursor.moveToFirst()) {
			int cityNameIndex = myCursor.getColumnIndex(PrepopulatedDBOpenHelper.CITY_NAME);
			int stateIndex = myCursor.getColumnIndex(PrepopulatedDBOpenHelper.STATE);
			int cityCodeIndex = myCursor.getColumnIndex(PrepopulatedDBOpenHelper.CITY_CODE);

			do {
				if (cityNameIndex != -1) {
					cityArray.add(myCursor.getString(cityNameIndex));
				}
				if (stateIndex != -1) {
					stateArray.add(myCursor.getString(stateIndex));
				}
				if (cityCodeIndex != -1) {
					codeArray.add(myCursor.getString(cityCodeIndex));
				}
			} while (myCursor.moveToNext());
			myCursor.close();
		}

		cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_autocompletar, R.id.autoCompleteItem, cityArray);
		cityAutoComplete.setAdapter(cityAdapter);

		cityAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
			aitData.setCity((String) parent.getItemAtPosition(position));
			int real_position = cityArray.indexOf(parent.getItemAtPosition(position));

			aitData.setCityCode(codeArray.get(real_position));
			etCityCode.setText(aitData.getCityCode());

			aitData.setState(stateArray.get(real_position));
			etState.setText(aitData.getState());

			setCityStateVisibility(true);
		});
	}

	private void addListener() {

		etAddressInfraction.addTextChangedListener(new ChangeText(
				R.id.et_ait_local));

		btnAitDate.setOnClickListener(this);
		btnAitTime.setOnClickListener(this);

		setCityStateVisibility(true);

		cbConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tvSaveData.setVisibility(cbConfirm.isChecked() ? View.VISIBLE : View.GONE);
				if(cbConfirm.isChecked()){
					Routine.closeKeyboard(tvSaveData, getActivity());
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

	private boolean checkInput() {
		if (etAddressInfraction.getText().toString().isEmpty()) {
			etAddressInfraction.setError(getResources().getString(R.string.alert_insert_address));
			etAddressInfraction.requestFocus();
			return false;
		}

		if (cityAutoComplete.getText().toString().isEmpty()) {
			cityAutoComplete.setError(getResources().getString(R.string.alert_insert_city_name));
			cityAutoComplete.requestFocus();
			return false;
		}

		if (btnAitDate.getText().toString().equals("Data")) {
			Routine.showAlert(getActivity().getString(R.string.alert_insert_date), getActivity());
			btnAitDate.requestFocus();
			return false;
		}

		if (btnAitTime.getText().toString().equals("Hora")) {
			Routine.showAlert(getActivity().getString(R.string.alert_insert_time), getActivity());
			btnAitTime.requestFocus();
			return false;
		}

		return true;
	}

	private void getAitObject() {
		aitData = AitObject.getAitData();

		if (aitData.isStoreFullData()) {
			getRecomandedUpdate();
			setNewAitView();
		} else {
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

		etCityCode.setText(aitData.getCityCode());
		etState.setText(aitData.getState());
		etAddressInfraction.setText(aitData.getAddress());

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
				aitData.setAddress(s.toString());
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
	protected void handlePickerResult(String selectedValue, int viewId) {
		if (viewId == R.id.btn_ait_pj_date) {
			aitData.setAitDate(selectedValue);
			btnAitDate.setText(aitData.getAitDate());
		} else if (viewId == R.id.btn_ait_pj_time) {
			aitData.setAitTime(selectedValue);
			btnAitTime.setText(aitData.getAitTime());
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
