package net.sistransito.mobile.rrd;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.appcompat.widget.AppCompatButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.sistransito.mobile.fragment.BasePickerFragment;
import net.sistransito.mobile.adapter.AnyArrayAdapter;
import net.sistransito.mobile.adapter.CustomSpinnerAdapter;
import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.PrepopulatedDBOpenHelper;
import net.sistransito.mobile.database.SetttingDatabaseHelper;
import net.sistransito.mobile.fragment.AnyDialogFragment;
import net.sistransito.mobile.fragment.AnyDialogListener;
import net.sistransito.mobile.number.AsyncListernerNumber;
import net.sistransito.mobile.number.NumberHttpResultAsyncTask;
import net.sistransito.mobile.utility.Routine;
import net.sistransito.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabRrdDocumentFragment extends BasePickerFragment implements
		AnyDialogListener, OnClickListener  {
	private View view;
	private AutoCompleteTextView autoCompleteCity, spinnerDocument;
    private TextView tvAitNumber, tvRrdTitle;
	private EditText etDriverName, btnValidity,
			etCrlvNumber, etPlate, etState, etRegisterNumber, etRegisterState;
	private RrdData rrdData;
	private List<String> listDocument;
	private AnyArrayAdapter<String> aaaAdapter;
	private AnyDialogFragment anyDialogFragment;
	private Bundle bundle;
	private Button btnDate, btnTime;
	private AppCompatButton btnValidity2;

	private List<String> cityArray, codeArray, stateArray;
	private ArrayAdapter<String> aaCity;

	private LinearLayout llValidityRegister, llPlateState, llDateAndTime;

	public static TabRrdDocumentFragment newInstance() {
		return new TabRrdDocumentFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.rrd_document_fragment, null, false);
		initializedView();
		getAitObject();
		checkAitNumber();

		addPicker(R.id.et_rrd_validity, "date");

		btnValidity.setOnClickListener(this);

		return view;
	}

	private void initializedView() {

		btnDate = (Button) view.findViewById(R.id.btn_rrd_data);
		btnTime = (Button) view.findViewById(R.id.btn_rrd_time);

		autoCompleteCity = (AutoCompleteTextView) view
				.findViewById(R.id.actv_rrd_city);

		tvRrdTitle = (TextView) view.findViewById(R.id.tv_rrd_titulo);
		llPlateState = (LinearLayout) view
				.findViewById(R.id.layout_hide_plate_state);
		llValidityRegister = (LinearLayout) view
				.findViewById(R.id.layout_hide_cnh_validity);

		llDateAndTime = (LinearLayout) view
				.findViewById(R.id.ll_rrd_date_time);

		tvAitNumber = (TextView) view.findViewById(R.id.text_rrd_ait_number);

		etDriverName = (EditText) view
				.findViewById(R.id.et_rrd_driver_name);

		//etRrdNomeCondutor.setEnabled(false);

		etCrlvNumber = (EditText) view.findViewById(R.id.et_rrd_crlv_number);
		etPlate = (EditText) view.findViewById(R.id.et_rrd_plate);

		etState = (EditText) view.findViewById(R.id.et_rrd_plate_state);

		etRegisterNumber = (EditText) view
				.findViewById(R.id.et_rrd_register_number);

		etRegisterState = (EditText) view
				.findViewById(R.id.et_rrd_register_state);

		btnValidity = (EditText) view.findViewById(R.id.et_rrd_validity);

		listDocument = Arrays.asList(getResources().getStringArray(
				R.array.rrd_document));

		CustomSpinnerAdapter aaaAdapter = CustomSpinnerAdapter.createStateAdapter(getActivity(), listDocument);
		spinnerDocument = view.findViewById(R.id.spinner_rrd_document);
		spinnerDocument.setAdapter(aaaAdapter);

		llPlateState.removeView(llPlateState);

		spinnerDocument.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String selectedItem = (String) parent.getItemAtPosition(position);
				rrdData.setDocumentType(selectedItem);
				Log.d("Escolha", String.valueOf(position));

				if (position == 0) {
					showLayoutViewPlate();
					hideLayoutRegisterView();
				} else {
					showLayoutRegisterView();
					hideLayoutPlateView();
				}
			}
		});

	}

	private void addListener() {

		etDriverName.addTextChangedListener(new ChangeText(R.id.et_rrd_driver_name));
		etCrlvNumber.addTextChangedListener(new ChangeText(R.id.et_rrd_crlv_number));
		etState.addTextChangedListener(new ChangeText(R.id.et_rrd_plate_state));
		etRegisterNumber.addTextChangedListener(new ChangeText(R.id.et_rrd_register_number));
		etRegisterState.addTextChangedListener(new ChangeText(R.id.et_rrd_register_state));

		btnDate.addTextChangedListener(new ChangeText(R.id.btn_rrd_data));
		btnTime.addTextChangedListener(new ChangeText(R.id.btn_rrd_time));

		btnDate.setOnClickListener(this);
		btnTime.setOnClickListener(this);
		btnValidity.setOnClickListener(this);

		hideLayoutRegisterView();

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

		String ufMunicipio = cursor.getString(cursor.getColumnIndex(SetttingDatabaseHelper.SETTING_STATE));

		//Log.d("UfLocal", ufMunicipio);

		myCursor = ((DatabaseCreator
				.getPrepopulatedDBOpenHelper(getActivity()))
				.getInitialsStateCursor(ufMunicipio));

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

		aaCity = new ArrayAdapter<String>(getActivity(), R.layout.custom_autocompletar, R.id.autoCompleteItem, cityArray);
		autoCompleteCity.setAdapter(aaCity);

		autoCompleteCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int pos, long arg3) {

				rrdData.setCityCollected((String) parent.getItemAtPosition(pos));
				int real_position = cityArray.indexOf(parent.getItemAtPosition(pos));

				rrdData.setStateCollected(stateArray.get(real_position));

				Routine.closeKeyboard(autoCompleteCity, getActivity());

			}
		});
	}

	private void checkAitNumber() {
		String value = (DatabaseCreator.getNumberDatabaseAdapter(getActivity()))
				.getRrdNumber();
		if (value == null) {
			anyDialogFragment = new AnyDialogFragment();
			anyDialogFragment.setTargetFragment(this, 0);
			bundle = new Bundle();
			bundle.putInt(AppConstants.DIALOG_TITLE_ID, R.string.synchronization_screen_title);
			bundle.putInt(AppConstants.DIALOG_MGS_ID, R.string.message_need_synchronize_ait_number);
			anyDialogFragment.setArguments(bundle);
			anyDialogFragment.setCancelable(false);
			anyDialogFragment
					.show(getChildFragmentManager(), "dialog");
		} else {
			rrdData.setRrdNumber(value);

		}
	}

	private void getAitObject() {
		rrdData = RrdObject.getRRDOject();
		getRecomandedUpdate();
		addListener();
		initializedSelectetItems();
	}

	private void initializedSelectetItems() {
		// spinnerRrdDocumento.setSelection(0);
		// etRrdNomeCondutor.setText("");
		// etRrdNumeroCrlv.setText("");
		// etRrdUf.setText("");
	}

	private void getRecomandedUpdate() {
		Log.d("getRrdType", rrdData.getRrdType());
		if(rrdData.getRrdType().equals("avulso")) {

			tvAitNumber.setVisibility(View.GONE);
			llDateAndTime.setVisibility(View.VISIBLE);
			autoCompleteCity.setVisibility(View.VISIBLE);
			setAutoCompleteCity();

		}else {
			tvAitNumber.setText("Referente ao AIT: " + rrdData.getAitNumber());
			etPlate.setText(rrdData.getPlate());
			etPlate.setEnabled(false);
			etDriverName.setText(rrdData.getDriverName());
			etState.setText(rrdData.getPlateState());
			etRegisterNumber.setText(rrdData.getRegistrationNumber());
			etRegisterState.setText(rrdData.getRegistrationState());
			llDateAndTime.setVisibility(View.GONE);
			autoCompleteCity.setVisibility(View.GONE);
			etState.setEnabled(false);
		}

	}

	private class ChangeText implements TextWatcher {
		private int id;

		public ChangeText(int id) {
			this.id = id;
		}

		@Override
		public void afterTextChanged(Editable value) {
			String s = (value.toString()).trim();

			if (id == R.id.et_rrd_driver_name) {
				rrdData.setDriverName(s);
			} else if (id == R.id.et_rrd_crlv_number) {
				rrdData.setCrlvNumber(s);
			} else if (id == R.id.et_rrd_plate_state) {
				rrdData.setPlateState(s);
			} else if (id == R.id.et_rrd_register_number) {
				rrdData.setRegistrationNumber(s);
			} else if (id == R.id.et_rrd_register_state) {
				rrdData.setRegistrationState(s);
			} else if (id == R.id.btn_rrd_data) {
				rrdData.setDateCollected(s);
			} else if (id == R.id.btn_rrd_time) {
				rrdData.setTimeCollected(s);
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
	public void onDialogTaskWork(boolean isWork) {
		if (isWork) {

			(new NumberHttpResultAsyncTask(new AsyncListernerNumber() {

				@Override
				public void asyncTaskComplete(boolean isComplete) {
					if (isComplete) {
						checkAitNumber();
					}

				}
			}, getActivity(), AppConstants.RRD_NUMBER)).execute("");

		} else {
			getActivity().finish();
		}

	}

	private void showLayoutViewPlate() {

		//if (layoutPlacaUf.getParent() == null) {
			//layoutPlacaUf.addView(layoutPlacaUf);
			llPlateState.setVisibility(View.VISIBLE);
		//}

		hideLayoutRegisterView();

	}

	private void showLayoutRegisterView() {

	    llValidityRegister.setVisibility(View.VISIBLE);
        btnValidity.setVisibility(View.VISIBLE);
	    btnValidity.setText(R.string.rrd_validity);
		hideLayoutPlateView();

	}

	private void hideLayoutPlateView() {
	    etCrlvNumber.setText("");
	    llPlateState.setVisibility(View.GONE);
	}

	private void hideLayoutRegisterView() {
        llValidityRegister.setVisibility(View.GONE);
        btnValidity.setVisibility(View.GONE);
        btnValidity.setText(R.string.rrd_validity);
        rrdData.setValidity("");
	}

	@Override
	protected void handlePickerResult(String selectedValue, int viewId) {
		if (viewId == R.id.et_rrd_validity) {
			rrdData.setValidity(selectedValue);
			btnValidity.setText(rrdData.getValidity());
		}
	}

}