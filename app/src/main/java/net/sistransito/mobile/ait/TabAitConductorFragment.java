package net.sistransito.mobile.ait;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.widget.CheckBox;

import net.sistransito.mobile.adapter.AnyArrayAdapter;
import net.sistransito.mobile.adapter.CustomSpinnerAdapter;
import net.sistransito.mobile.cnh.dados.CnhHttpResultAsyncTask;
import net.sistransito.mobile.cnh.dados.DataFromCnh;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.AnyDialogListener;
import net.sistransito.mobile.fragment.CallBackCnh;
import net.sistransito.mobile.network.NetworkConnection;
import net.sistransito.mobile.utility.Routine;
import net.sistransito.R;

import java.util.Arrays;
import java.util.List;

public class TabAitConductorFragment extends Fragment implements
		AnyDialogListener {
	private CnhHttpResultAsyncTask httpResultAnysTask;
	private View view;
	private AitData aitData;
	private EditText editDriverName, editDriverDocument, editDriverDocumentNumber;
	private AutoCompleteTextView spinnerStateDriver, spinnerCountryDriver, spinnerDocumentType;
	private List<String> listStateDriver, listCountryDriver, listDocumentType;
	private AnyArrayAdapter<String> aaaStateDriver, aaaCountryDriver, aaaDocumentType;
	private LinearLayout llHideAllLayout, llDriverDocument, llSpinnerStateDriver, llDocumentType,
			llspinnerCountryDriver;
	private CheckBox cbIfDriverWasSpproached, cbIfDriverForeign,
			cbUnqualifiedDriver, cbAitConfirm;
	private TextView tvSearchCNH, tvSaveData;
	private String searchCnh = "cnh";

	public static TabAitConductorFragment newInstance() {
		return new TabAitConductorFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.ait_conductor_fragment, null, false);
		initializedView();
		getAitObject();
		return view;
	}

	private void initializedView() {

		editDriverName = (EditText) view
				.findViewById(R.id.et_ait_driver_name);

		editDriverDocument = (EditText) view.findViewById(R.id.et_register_cnh);

		listStateDriver = Arrays.asList(getResources().getStringArray(
				R.array.state_array));

		listCountryDriver = Arrays.asList(getResources().getStringArray(
				R.array.filter_list_country));

		aaaCountryDriver = new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				listCountryDriver);

		aaaStateDriver = new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				listStateDriver);

		listDocumentType = Arrays.asList(getResources().getStringArray(
				R.array.other_document_type));

		CustomSpinnerAdapter aaaCountryDriver = CustomSpinnerAdapter.createStateAdapter(getActivity(), listCountryDriver);
		spinnerCountryDriver = (AutoCompleteTextView) view.findViewById(R.id.spinner_driver_country);
		spinnerCountryDriver.setAdapter(aaaCountryDriver);

		CustomSpinnerAdapter stateAdapter = CustomSpinnerAdapter.createStateAdapter(getActivity(), listStateDriver);
		spinnerStateDriver = (AutoCompleteTextView) view.findViewById(R.id.spinner_state_driver);
		spinnerStateDriver.setAdapter(stateAdapter);
		spinnerStateDriver.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String selectedState = (String) parent.getItemAtPosition(position);
				aitData.setCnhState(selectedState);
				Routine.closeKeyboard(spinnerStateDriver, getActivity());
			}
		});

		CustomSpinnerAdapter aaaDocumentType = CustomSpinnerAdapter.createStateAdapter(getActivity(), listDocumentType);
		spinnerDocumentType = (AutoCompleteTextView) view.findViewById(R.id.spinner_another_document);
		spinnerDocumentType.setAdapter(aaaDocumentType);

		editDriverDocumentNumber = (EditText) view
				.findViewById(R.id.et_document_number);

		llDriverDocument = (LinearLayout) view.findViewById(R.id.ll_register);
		llSpinnerStateDriver = (LinearLayout) view.findViewById(R.id.ll_spinner_state_register);
		llHideAllLayout = (LinearLayout) view.findViewById(R.id.ll_hide_all);
		llDocumentType = (LinearLayout) view.findViewById(R.id.ll_driver_document);
		llspinnerCountryDriver = (LinearLayout) view.findViewById(R.id.ll_spinner_driver_country);
		cbIfDriverWasSpproached = (CheckBox) view.findViewById(R.id.cb_se_condutor_abordado);
		cbUnqualifiedDriver = (CheckBox) view.findViewById(R.id.cb_if_no_have_cnh);
		cbIfDriverForeign = (CheckBox) view.findViewById(R.id.cb_if_a_foreign_driver);
		cbAitConfirm = (CheckBox) view.findViewById(R.id.cb_ait_confirm);
		tvSearchCNH = (TextView) view.findViewById(R.id.tv_auto_search_cnh);
		tvSaveData = (TextView) view.findViewById(R.id.ait_fab);

	}

	private void getAitObject() {
		aitData = AitObject.getAitData();
		if (aitData.isDataisNull()) {
			addListener();
		} else if (aitData.isStoreFullData()) {
			getRecomandedUpdate();
			getOtherUpdate();
			addListener();
		} else {
			getRecomandedUpdate();
			addListener();

		}
	}

	private void addListener() {

		editDriverName.addTextChangedListener(new ChangeText(
				R.id.et_ait_driver_name));

		editDriverDocument.addTextChangedListener(new ChangeText(
				R.id.et_register_cnh));

		editDriverDocumentNumber.addTextChangedListener(new ChangeText(
				R.id.et_document_number));

		spinnerCountryDriver
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
											   View view, int pos, long Id) {

						aitData.setCountry((String) parent.getItemAtPosition(pos));

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		spinnerDocumentType
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
											   View view, int pos, long Id) {

						aitData.setDocumentType((String) parent
								.getItemAtPosition(pos));

						int realPosition = listDocumentType.indexOf(aitData.getDocumentType());

						if (realPosition == 5) {
							editDriverDocumentNumber.setVisibility(View.GONE);
						} else {
							editDriverDocumentNumber.setVisibility(View.VISIBLE);
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		cbIfDriverForeign.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cbIfDriverForeign.isChecked()){
					llspinnerCountryDriver.setVisibility(View.VISIBLE);
					llDriverDocument.setVisibility(View.GONE);
					llSpinnerStateDriver.setVisibility(View.GONE);
					aitData.setForeignDriver("1");
				}else{
					llspinnerCountryDriver.setVisibility(View.GONE);
					llDriverDocument.setVisibility(View.VISIBLE);
					llSpinnerStateDriver.setVisibility(View.VISIBLE);
					aitData.setForeignDriver("0");
				}
			}
		});

		cbIfDriverWasSpproached.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cbIfDriverWasSpproached.isChecked()){
					llHideAllLayout.setVisibility(View.GONE);
				}else{
					llHideAllLayout.setVisibility(View.VISIBLE);
				}
			}
		});

		cbUnqualifiedDriver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cbUnqualifiedDriver.isChecked()){
					llDriverDocument.setVisibility(View.GONE);
					llSpinnerStateDriver.setVisibility(View.GONE);
					llDocumentType.setVisibility(View.VISIBLE);
					aitData.setQualifiedDriver("0");
				}else{
					llDriverDocument.setVisibility(View.VISIBLE);
					llSpinnerStateDriver.setVisibility(View.VISIBLE);
					llDocumentType.setVisibility(View.GONE);
					aitData.setQualifiedDriver("1");
				}
			}
		});

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

		tvSearchCNH.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!editDriverDocument.getText().toString().equals("")) {

					if (NetworkConnection.isNetworkAvailable(getActivity())) {

						httpResultAnysTask = new CnhHttpResultAsyncTask(
								new CallBackCnh() {
									@Override
									public void callBack(DataFromCnh fromCnh) {
										resultCallBack(fromCnh);
									}
								}, getActivity(), true, null, editDriverDocument.getText().toString(), searchCnh);//gps.getLocation());
						httpResultAnysTask.execute("");

					} else {
						Routine.showAlert(getResources().getString(R.string.no_network_connection), getActivity());
					}

				} else {
					Routine.showAlert(getResources().getString(R.string.cnh_search_screen_title), getActivity());
				}

				Routine.closeKeyboard(editDriverDocument, getActivity());

			}
		});

		tvSaveData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(cbIfDriverWasSpproached.isChecked()){
					aitData.setApproach("0");
				}else{
					aitData.setApproach("1");
				}

				//Log.i("Abordagem: ", dadosAuto.getAbordagem());

				if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).updateAitDataConductor(aitData))
					Routine.showAlert(getResources().getString(R.string.update_erro), getActivity());

				((AitActivity) getActivity()).setTabActual(2);

			}
		});

	}

	private void resultCallBack(DataFromCnh fromCNH) {

		if (fromCNH != null) {

			int selection_1 = 0;

			selection_1 = listStateDriver.indexOf(fromCNH.getState().toUpperCase());

			editDriverName.setText(fromCNH.getName());
			editDriverDocument.setText(fromCNH.getRegister());

			spinnerStateDriver.setText(fromCNH.getState());

		} else {
			Routine.showAlert(getResources().getString(R.string.no_result_returned), getActivity());
		}

	}

	private void getRecomandedUpdate() {
		editDriverName.setText(aitData.getConductorName());
		editDriverDocument.setText(aitData.getCnhPpd());
		editDriverDocumentNumber.setText(aitData.getDocumentNumber());
	}

	private void getOtherUpdate() {

		int selection_1 = 0, selection_2 = 0, selection_3 = 0;

		selection_1 = listCountryDriver.indexOf(aitData.getCountry());
		selection_2 = listStateDriver.indexOf(aitData.getCnhState());
		selection_3 = listDocumentType.indexOf(aitData.getDocumentType());

		spinnerCountryDriver.setSelection(selection_1 + 1);
		spinnerStateDriver.setText(aitData.getCnhState());
		spinnerDocumentType.setSelection(selection_3 + 1);

		editDriverName.setText(aitData.getConductorName());
		editDriverDocument.setText(aitData.getCnhPpd());
		editDriverDocumentNumber.setText(aitData.getDocumentNumber());

	}

	private class ChangeText implements TextWatcher {
		private int id;

		public ChangeText(int id) {
			this.id = id;
		}

		@Override
		public void afterTextChanged(Editable s) {

			if (s.toString() != null) {
				if (id == R.id.et_ait_driver_name) {
					aitData.setConductorName(s.toString());
				} else if (id == R.id.et_register_cnh) {
					aitData.setCnhPpd(s.toString());
					//closeKeyboard(editRegistroCondutor);
				} else if (id == R.id.spinner_state_driver) {
					aitData.setCnhState(s.toString());
					Routine.openKeyboard(spinnerStateDriver, getActivity());
				} else if (id == R.id.et_document_number) {
					aitData.setDocumentNumber(s.toString());
				}
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
	}

}