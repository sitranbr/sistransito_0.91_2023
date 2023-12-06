package net.sistransito.mobile.ait;

import android.database.Cursor;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.rey.material.widget.CheckBox;

import net.sistransito.mobile.adapter.AnyArrayAdapter;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.PrepopulatedDBOpenHelper;
import net.sistransito.mobile.fragment.AnyAlertDialog;
import net.sistransito.mobile.fragment.UpdateFragment;
import net.sistransito.mobile.util.AlcoholCalculator;
import net.sistransito.mobile.util.Cnpj;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabAitInfractionFragment extends Fragment implements OnClickListener,
		UpdateFragment, OnCheckedChangeListener {

	private View view, separator;
	private AutoCompleteTextView autoCompleteInfraction;
	private AitData aitData;
	private RadioGroup rgProcedures, rgrRetreat, rgShipperIdentification, rgCarrierIdentification;
	private RadioButton rbAlcoholTestNumber, rbTca, rbCpfShipper, rbCnpjShipper,
			rbCpfCarrier, rbCnpjCarrier;
	private EditText etFramingCode, etCategorization, etArticle, etObservation,
			etEquipmentBrand, etEquipmentModel, etSerialNumber,
			etMeasurementPerformed, etRegulatedValue, etValueConsidered, etAlcoholSampleNumber, etTcaNumber,
			etShipperName, etShipperCpf, etShipperCnpj,
			etCarrierName, etCarrierCpf, etCarrierCnpj;
	private TextInputLayout tilAutoTca,tilCpfEmbarcador, tilCnpjEmbarcador, tilCpfTransportador, tilCnpjTransportador;
	private TextView tvClearSearch, tvClearObs, tvAitObservation, TvSaveData;
	private Spinner spinnerAitDescription, spinnerMeasuringUnit;
	private List<String> listDescription, listBrand, listModel, listMeasurement,
			listSerialNumber, listAitArray, listInfractionArray,
			listAitObservation, listAitUnfolding, listArticle, listAitFramingCode;
	private AnyArrayAdapter<String> aaaEquipmentDescription, aaaMeasuringUnit;

	private ArrayAdapter<String> infractionArrayAdapter;
	private CheckBox cbTca, cbEquipment, cbProcedures, cbCarrier, cbConfirm;

	private LinearLayout llFramingCode, llEquipment, llProcedures, llCarrier;

	private FrameLayout flObservation;

	private String unitMeasureCalculation;
	boolean ifCalculate = false;

	public static TabAitInfractionFragment newInstance() {
		return new TabAitInfractionFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.ait_infraction_fragment, null, false);
		initializedView();
		getAitObject();
		return view;
	}

	private void initializedView() {

		llFramingCode = (LinearLayout) view.findViewById(R.id.ll_enquadramento);
		llEquipment = (LinearLayout) view.findViewById(R.id.ll_auto_equipamento);
		llProcedures = (LinearLayout) view.findViewById(R.id.ll_auto_procedimentos);
		llCarrier = (LinearLayout) view.findViewById(R.id.ll_auto_transportador);

		flObservation = (FrameLayout) view.findViewById(R.id.fl_auto_obs);

		autoCompleteInfraction = (AutoCompleteTextView) view.findViewById(R.id.tv_autocomplete_infracao);
		cbTca = (CheckBox) view.findViewById(R.id.cb_auto_tca);
		cbEquipment = (CheckBox) view.findViewById(R.id.cb_equipamento_afericao);
		cbProcedures = (CheckBox) view.findViewById(R.id.cb_procedimentos);
		cbCarrier = (CheckBox) view.findViewById(R.id.cb_transportador);
		cbConfirm = (CheckBox) view.findViewById(R.id.cb_auto_confirmar);

		etTcaNumber = (EditText) view.findViewById(R.id.et_auto_numero_tca);
		etFramingCode = (EditText) view.findViewById(R.id.et_auto_enquadramento);
		etCategorization = (EditText) view.findViewById(R.id.et_auto_desdobramento);
		etArticle = (EditText) view.findViewById(R.id.et_auto_amparo_legal);
		etObservation = (EditText) view.findViewById(R.id.et_auto_observacao);

		etEquipmentBrand = (EditText) view.findViewById(R.id.et_auto_marca_equipamento);
		etEquipmentModel = (EditText) view
				.findViewById(R.id.et_auto_modelo_equipamento);

		etSerialNumber = (EditText) view
				.findViewById(R.id.et_auto_numero_de_serie);

		etMeasurementPerformed = (EditText) view
				.findViewById(R.id.et_ait_measurement_performed);

		etMeasurementPerformed.setKeyListener(DigitsKeyListener.getInstance("0123456789."));

		etRegulatedValue = (EditText) view.findViewById(R.id.et_auto_limite_regulamentado);
		etValueConsidered = (EditText) view.findViewById(R.id.et_auto_valor_medicao);
		//etAutoValor.setEnabled(false);
		etAlcoholSampleNumber = (EditText) view
				.findViewById(R.id.et_ait_sample_number);

		etShipperName = (EditText) view.findViewById(R.id.et_auto_nome_embarcador) ;
		etCarrierName = (EditText) view.findViewById(R.id.et_auto_nome_transportador);

		etShipperCpf = (EditText) view.findViewById(R.id.et_cpf_embarcador);
		etShipperCnpj = (EditText) view.findViewById(R.id.et_cnpj_embarcador);
		etCarrierCpf = (EditText) view.findViewById(R.id.et_cfp_transportador);
		etCarrierCnpj = (EditText) view.findViewById(R.id.et_cnpj_transportador);

		tilAutoTca = (TextInputLayout) view.findViewById(R.id.til_auto_tca);
		tilCpfEmbarcador = (TextInputLayout) view.findViewById(R.id.til_cpf_embarcador);
		tilCnpjEmbarcador = (TextInputLayout) view.findViewById(R.id.til_cnpj_embarcador);
		tilCpfTransportador = (TextInputLayout) view.findViewById(R.id.til_cpf_transportador);
		tilCnpjTransportador = (TextInputLayout) view.findViewById(R.id.til_cnpj_transportador);

		rgrRetreat = (RadioGroup) view.findViewById(R.id.rg_recolhimento);
		rgProcedures = (RadioGroup) view
				.findViewById(R.id.rg_procedimentos);
		rgShipperIdentification = (RadioGroup) view.findViewById(R.id.rg_tipo_documento_embarcador);
		rgCarrierIdentification = (RadioGroup) view.findViewById(R.id.rg_tipo_documento_transportador);
		//sampleRadio = (RadioButton) view.findViewById(R.id.sample_radio);
		//tcaRadio = (RadioButton) view.findViewById(R.id.tca_radio);
		rbCpfShipper = (RadioButton) view.findViewById(R.id.rg_cpf_embarcador);
		rbCnpjShipper = (RadioButton) view.findViewById(R.id.rg_cnpj_embarcador);
		rbCpfCarrier = (RadioButton) view.findViewById(R.id.rg_cpf_transportador);
		rbCnpjCarrier = (RadioButton) view.findViewById(R.id.rg_cnpj_transportador);
		separator = (View) view.findViewById(R.id.separador);

		etFramingCode.setEnabled(false);
		etCategorization.setEnabled(false);
		etArticle.setEnabled(false);

		tvClearSearch = (TextView) view.findViewById(R.id.tv_clear_search);
		tvClearObs = (TextView) view.findViewById(R.id.tv_clear_obs);
		tvAitObservation = (TextView) view.findViewById(R.id.tv_ait_observation);
		TvSaveData = (TextView) view.findViewById(R.id.ait_fab);

		etEquipmentBrand.setEnabled(false);
		etEquipmentModel.setEnabled(false);
		etSerialNumber.setEnabled(false);

		etRegulatedValue.setEnabled(false);
		etValueConsidered.setEnabled(false);

		flObservation.setVisibility(view.GONE);
		tilAutoTca.setVisibility(view.GONE);
		tilCnpjEmbarcador.setVisibility(view.GONE);
		tilCnpjTransportador.setVisibility(view.GONE);

		setAutoCompleteInfraction();
		setSpinnerDescription();
		setSpinnerUnit();

	}

	private void setSpinnerUnit(){

		listMeasurement = Arrays.asList(getResources().getStringArray(
				R.array.array_measurement_units));

		aaaMeasuringUnit = new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				listMeasurement);

		spinnerMeasuringUnit = (Spinner) view.findViewById(R.id.spinner_medicao_unidade);
		spinnerMeasuringUnit.setAdapter(aaaMeasuringUnit);

		spinnerMeasuringUnit
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
											   View view, int pos, long Id) {

						unitMeasureCalculation = ((String) parent.getItemAtPosition(pos));

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		spinnerMeasuringUnit.setSelection(3);

	}

	private void hideComponents(){

		tvClearSearch.setVisibility(TextView.GONE);
		tvClearObs.setVisibility(TextView.GONE);
		llFramingCode.setVisibility(EditText.GONE);
		flObservation.setVisibility(view.GONE);

	}

	private void  showComponents(){

		tvClearSearch.setVisibility(TextView.VISIBLE);
		tvClearObs.setVisibility(TextView.VISIBLE);
		llFramingCode.setVisibility(EditText.VISIBLE);
		flObservation.setVisibility(view.VISIBLE);

	}

	private void setSpinnerDescription() {

		listDescription = new ArrayList<String>();
		listBrand = new ArrayList<String>();
		listModel = new ArrayList<String>();
		listSerialNumber = new ArrayList<String>();

		Cursor cursor = ((DatabaseCreator
				.getPrepopulatedDBOpenHelper(getActivity()))
				.getEquipmentCursor());
		cursor.moveToFirst();

		for (int i = 0; i < cursor.getCount(); i++) {

			listDescription.add(
					cursor.getString(cursor
							.getColumnIndex(PrepopulatedDBOpenHelper.EQUIPMENT_DESCRIPTION)));

			listBrand
					.add(cursor.getString(cursor
							.getColumnIndex(PrepopulatedDBOpenHelper.EQUIPMENT_BRAND)));

			listModel
					.add(cursor.getString(cursor
							.getColumnIndex(PrepopulatedDBOpenHelper.EQUIPMENT_MODEL)));

			listSerialNumber
					.add(cursor.getString(cursor
							.getColumnIndex(PrepopulatedDBOpenHelper.EQUIPMENT_SERIAL)));

			cursor.moveToNext();
		}
		cursor.close();

		aaaEquipmentDescription = new AnyArrayAdapter<String>(getActivity(),
				R.layout.spinner_custom_item, android.R.id.text1,
				listDescription);

		spinnerAitDescription = (Spinner) view.findViewById(R.id.spinner_auto_descricao_equipamento);
		spinnerAitDescription.setAdapter(aaaEquipmentDescription);

		spinnerAitDescription
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {

						aitData.setDescription((String) parent.getItemAtPosition(pos));

						int realPosition = listDescription.indexOf(aitData.getDescription());

						if (realPosition == 0){
							ifCalculate = true;
							spinnerMeasuringUnit.setSelection(4);
							spinnerMeasuringUnit.setEnabled(false);
							etRegulatedValue.setEnabled(false);
							etValueConsidered.setEnabled(false);
							etMeasurementPerformed.setText("");
							etMeasurementPerformed.requestFocus();
							etValueConsidered.setText("");
							etRegulatedValue.setText("");
							etRegulatedValue.setText("0,00 Mg/L");
							aitData.setRegulatedValue("0,00 Mg/L");
						} else {
							ifCalculate = false;
							spinnerMeasuringUnit.setSelection(3);
							spinnerMeasuringUnit.setEnabled(true);
							etRegulatedValue.setEnabled(true);
							etValueConsidered.setEnabled(true);
							etMeasurementPerformed.setText("");
							etMeasurementPerformed.requestFocus();
							etValueConsidered.setText("");
							etRegulatedValue.setText("");
							aitData.setRegulatedValue(null);
						}

						//Toast.makeText(getActivity(), "Posição " + realPosition, Toast.LENGTH_SHORT).show();

						if (realPosition == -1) {
							clearAllEquipamentFields();
						} else {
							aitData.setEquipmentBrand(listBrand.get(realPosition));
							aitData.setEquipmentModel(listModel.get(realPosition));
							aitData.setSerialNumber(listSerialNumber.get(realPosition));

							etEquipmentBrand.setText(aitData.getEquipmentBrand());
							etEquipmentModel.setText(aitData.getEquipmentModel());
							etSerialNumber.setText(aitData.getSerialNumber());

						}

						//Toast.makeText(getActivity(), "Posição atual" + realPosition, Toast.LENGTH_SHORT).show();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

	}

	private void setAutoCompleteInfraction() {

		Cursor cursor = (DatabaseCreator
				.getPrepopulatedDBOpenHelper(getActivity()))
				.getInfrationCursor();

		listInfractionArray = new ArrayList<String>();
		listAitArray = new ArrayList<String>();
		listAitUnfolding = new ArrayList<String>();
		listArticle = new ArrayList<String>();
		listAitFramingCode = new ArrayList<String>();
		listAitObservation = new ArrayList<String>();

		for (int i = 0; i < cursor.getCount(); i++) {

			listAitArray.add(
					cursor.getString(cursor.getColumnIndex(PrepopulatedDBOpenHelper.AIT_ARTICLE)) + " - " +
							cursor.getString(cursor.getColumnIndex(PrepopulatedDBOpenHelper.AIT_DESCRIPTION))
			);
			listInfractionArray
					.add(cursor.getString(cursor.getColumnIndex(PrepopulatedDBOpenHelper.AIT_DESCRIPTION))
					);
			listAitFramingCode
					.add(cursor.getString(cursor
							.getColumnIndex(PrepopulatedDBOpenHelper.AIT_FLAMING_CODE)));
			listAitUnfolding
					.add(cursor.getString(cursor
							.getColumnIndex(PrepopulatedDBOpenHelper.AIT_UNFOLDING)));
			listArticle.add(cursor.getString(cursor
					.getColumnIndex(PrepopulatedDBOpenHelper.AIT_ARTICLE)));

			listAitObservation
					.add(cursor.getString(cursor
							.getColumnIndex(PrepopulatedDBOpenHelper.AIT_OBSERVATION)));

			cursor.moveToNext();

		}
		cursor.close();

		infractionArrayAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.custom_autocompletar, R.id.autoCompleteItem,
				listAitArray);

		autoCompleteInfraction.setAdapter(infractionArrayAdapter);

		autoCompleteInfraction
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int pos, long id) {

						Routine.closeKeyboard(autoCompleteInfraction, getActivity());

						aitData.setInfraction((String) parent.getItemAtPosition(pos));
						int realPosition = listAitArray.indexOf(aitData.getInfraction());

						aitData.setInfraction(listInfractionArray.get(realPosition));
						//etAutoEnquadra.setText(dadosAuto.getCodigoInfracao());

						aitData.setFramingCode(listAitFramingCode.get(realPosition));
						etFramingCode.setText(aitData.getFramingCode());

						aitData.setUnfolding(listAitUnfolding.get(realPosition));
						etCategorization.setText(aitData.getUnfolding());

						aitData.setArticle(listArticle.get(realPosition));
						etArticle.setText(aitData.getArticle());

						aitData.setObservation(listAitObservation.get(realPosition));

						String obs = aitData.getObservation();

						etObservation.setText(replaceStringObs(obs));

						showComponents();

					}
				});

	}

	public String replaceStringObs(String obs){

		String idRRD = (DatabaseCreator.getNumberDatabaseAdapter(getActivity()))
				.getRrdNumber();

		obs = obs.replace("ARAA-0000","ARAA-0816");
		obs = obs.replace("CNH","CNH nº " + aitData.getCnhPpd());
		obs = obs.replace("cnh","CNH nº ...");
		obs = obs.replace("RRD","RRD nº " + idRRD);

		return obs;
	}

	private void addListener() {

		etObservation.addTextChangedListener(new ChangeText(R.id.et_auto_observacao));
		etEquipmentBrand.addTextChangedListener(new ChangeText(R.id.et_auto_marca_equipamento));
		etSerialNumber.addTextChangedListener(new ChangeText(R.id.et_auto_numero_de_serie));
		etEquipmentModel.addTextChangedListener(new ChangeText(R.id.et_auto_modelo_equipamento));
		etMeasurementPerformed.addTextChangedListener(new ChangeText(R.id.et_ait_measurement_performed));
		etRegulatedValue.addTextChangedListener(new ChangeText(R.id.et_auto_limite_regulamentado));
		etValueConsidered.addTextChangedListener(new ChangeText(R.id.et_auto_valor_medicao));
		etAlcoholSampleNumber.addTextChangedListener(new ChangeText(R.id.et_ait_sample_number));
		etShipperName.addTextChangedListener(new ChangeText(R.id.et_auto_nome_embarcador));
		etCarrierName.addTextChangedListener(new ChangeText(R.id.et_auto_nome_transportador));
		etShipperCpf.addTextChangedListener(new ChangeText(R.id.et_cpf_embarcador));
		etShipperCnpj.addTextChangedListener(new ChangeText(R.id.et_cnpj_embarcador));
		etCarrierCpf.addTextChangedListener(new ChangeText(R.id.et_cfp_transportador));
		etCarrierCnpj.addTextChangedListener(new ChangeText(R.id.et_cnpj_transportador));

		rgProcedures.setOnCheckedChangeListener(this);
		rgrRetreat.setOnCheckedChangeListener(this);

		tvClearSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				autoCompleteInfraction.setText(""); // clear your TextView
				etArticle.setText("");
				etFramingCode.setText("");
				etCategorization.setText("");
				etObservation.setText("");
				tvAitObservation.setText("");

				Routine.openKeyboard(autoCompleteInfraction, getActivity());

				hideComponents();
			}
		});

		tvClearObs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				tvAitObservation.setVisibility(View.VISIBLE);
				tvClearObs.setVisibility(View.GONE);
				tvAitObservation.setText(aitData.getObservation());
				etObservation.setText("");
				etObservation.requestFocus();

				Routine.openKeyboard(tvClearObs, getActivity());

			}
		});

		cbTca.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(cbTca.isChecked()){
					String idTCA = (DatabaseCreator.getNumberDatabaseAdapter(getActivity()))
							.getTcaNumber();
					etTcaNumber.setText(idTCA);
					aitData.setTcaNumber(idTCA);
					tilAutoTca.setVisibility(View.VISIBLE);
                    cbEquipment.setVisibility(View.GONE);
                    cbCarrier.setVisibility(View.GONE);
				}else{
					etTcaNumber.setText("");
					aitData.setTcaNumber(null);
					tilAutoTca.setVisibility(View.GONE);
                    cbEquipment.setVisibility(View.VISIBLE);
                    cbCarrier.setVisibility(View.VISIBLE);
				}
			}

		});

		cbProcedures.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(cbProcedures.isChecked()){
					llProcedures.setVisibility(View.VISIBLE);
					Routine.closeKeyboard(llProcedures, getActivity());
                    aitData.setProcedures("");
                    aitData.setRetreat("");
				}else{
					llProcedures.setVisibility(View.GONE);
					rgProcedures.clearCheck();
					rgrRetreat.clearCheck();
                    aitData.setProcedures("");
                    aitData.setRetreat("");
				}
			}
		});

		cbEquipment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cbEquipment.isChecked()){
					llEquipment.setVisibility(View.VISIBLE);
					Routine.closeKeyboard(llEquipment, getActivity());
				}else{
					llEquipment.setVisibility(View.GONE);
					spinnerAitDescription.setSelection(0);
					clearAllEquipamentFields();
				}
			}
		});

		cbCarrier.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (cbCarrier.isChecked()) {
					llCarrier.setVisibility(View.VISIBLE);
					separator.setVisibility(View.GONE);
					Routine.closeKeyboard(llCarrier, getActivity());
				}else{
					llCarrier.setVisibility(View.GONE);
					separator.setVisibility(View.VISIBLE);
					etCarrierName.setText("");
					etCarrierCpf.setText("");
					etCarrierCnpj.setText("");
					etShipperName.setText("");
					etShipperCpf.setText("");
					etShipperCnpj.setText("");
					rgShipperIdentification.clearCheck();
					rgCarrierIdentification.clearCheck();
				}
			}
		});

		rbCpfShipper.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rbCpfShipper.isChecked()){
					tilCpfEmbarcador.setVisibility(view.VISIBLE);
					tilCnpjEmbarcador.setVisibility(view.GONE);
					etShipperCnpj.setText("");
					etShipperCpf.requestFocus();
					Routine.openKeyboard(etShipperCpf, getActivity());
				}
			}
		});

		rbCnpjShipper.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rbCnpjShipper.isChecked()){
					tilCpfEmbarcador.setVisibility(view.GONE);
					tilCnpjEmbarcador.setVisibility(view.VISIBLE);
					etShipperCpf.setText("");
					etShipperCnpj.requestFocus();
					Routine.openKeyboard(etShipperCnpj, getActivity());
				}
			}
		});

		rbCpfCarrier.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rbCpfCarrier.isChecked()){
					tilCpfTransportador.setVisibility(view.VISIBLE);
					tilCnpjTransportador.setVisibility(view.GONE);
					etCarrierCnpj.setText("");
					etCarrierCpf.requestFocus();
					Routine.openKeyboard(etCarrierCpf, getActivity());
				}
			}
		});

		rbCnpjCarrier.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rbCnpjCarrier.isChecked()){
					tilCpfTransportador.setVisibility(view.GONE);
					tilCnpjTransportador.setVisibility(view.VISIBLE);
					etCarrierCpf.setText("");
					etCarrierCnpj.requestFocus();
					Routine.openKeyboard(etCarrierCnpj, getActivity());
				}
			}
		});

		cbConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cbConfirm.isChecked()) {
					TvSaveData.setVisibility(view.VISIBLE);
					Routine.closeKeyboard(TvSaveData, getActivity());
				}else{
					TvSaveData.setVisibility(view.GONE);
				}
			}
		});

		TvSaveData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (checkInput()) {

					if (aitData.isStoreFullData()) {
						//Log.d("AutoExiste", "linha 567");
						if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).insertAitData(aitData))
							Routine.showAlert(getResources().getString(R.string.update_vehicle), getActivity());

						if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).updateAitDataConductor(aitData))
							Routine.showAlert(getResources().getString(R.string.update_driver), getActivity());

						if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).updateAitDataAddress(aitData))
							Routine.showAlert(getResources().getString(R.string.update_address), getActivity());

						if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).updateAitDataInfraction(aitData))
							Routine.showAlert(getResources().getString(R.string.update_infraction), getActivity());

					}else {
						if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).updateAitDataInfraction(aitData))
							Routine.showAlert(getResources().getString(R.string.update_infraction), getActivity());
					}

					((AitActivity) getActivity()).setTabActual(4);

				}

			}
		});

	}

	private boolean checkInput(){

		if (autoCompleteInfraction.getText().toString().isEmpty()) {
			autoCompleteInfraction.setError(getResources().getString(
					R.string.insert_infraction));
			autoCompleteInfraction.requestFocus();
			return false;
		} else {
			return true;
		}

	}

	public void clearAllEquipamentFields(){
		etEquipmentBrand.setText("");
		etEquipmentModel.setText("");
		etSerialNumber.setText("");
		etMeasurementPerformed.setText("");
		etRegulatedValue.setText("");
		etValueConsidered.setText("");
		etAlcoholSampleNumber.setText("");
		spinnerMeasuringUnit.setSelection(0);
		spinnerAitDescription.requestFocus();
	}

	private boolean calculateAmountAlcoholInBlood(String value) {

		AlcoholCalculator calculator = new AlcoholCalculator("mg/L");
		return calculator.calculateAmountAlcoholInBlood(value, etValueConsidered, aitData);


		/*try {
			*//*double amountAlcoholTolerated = 0.00;
			double consideredValue = Double.valueOf(value);

			if (consideredValue < 0.05) {
				etValueConsidered.setText(String.valueOf("0,00"));
			} else if (consideredValue >= 0.05
					&& consideredValue <= 0.50) {
				amountAlcoholTolerated = consideredValue - 0.032;
			} else if (consideredValue > 0.50 && consideredValue <= 2) {
				amountAlcoholTolerated = consideredValue - (consideredValue * 8) / 100;
			} else {
				amountAlcoholTolerated = consideredValue - (consideredValue * 30) / 100;
			}*//*

			double amountAlcoholTolerated = 0.00;
			double consideredValue = Double.valueOf(value);

			switch (Double.compare(consideredValue, 0.05)) {
				case -1:
					etValueConsidered.setText(String.format("%.2f", 0.00));
					break;
				case 0:
				case 1:
					if (consideredValue <= 0.50) {
						amountAlcoholTolerated = consideredValue - 0.032;
					} else if (consideredValue <= 2) {
						amountAlcoholTolerated = consideredValue - (consideredValue * 8) / 100;
					} else {
						amountAlcoholTolerated = consideredValue - (consideredValue * 30) / 100;
					}
					etValueConsidered.setText(String.format("%.2f", amountAlcoholTolerated));
					break;
				default:
					break;
			}

			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			decimalFormat.setRoundingMode(RoundingMode.DOWN);
			String formatTwoDecimalPoints = decimalFormat.format(amountAlcoholTolerated);
			if(formatTwoDecimalPoints.length() < 4){
				formatTwoDecimalPoints = formatTwoDecimalPoints + "0";
			}

			etValueConsidered.setText(String.valueOf(formatTwoDecimalPoints) + " " + unitMeasureCalculation);
			aitData.setValueConsidered(String.valueOf(formatTwoDecimalPoints) + " " + unitMeasureCalculation);

			return true;

		} catch (NumberFormatException e) {
			return false;
		}*/

	}

	private void getAitObject() {
		aitData = AitObject.getAitData();

		addListener();

		if (aitData.isStoreFullData()) {
			getRecomandeUpdate();
		} else {
			initializeSelectedItems();
		}

	}

	private void initializeSelectedItems() {

		aitData.setProcedures("");
		aitData.setRetreat("");

		//Log.d("initialized: 734: ", String.valueOf(dadosAuto.isStoreFullData()));

	}

	private void getRecomandeUpdate() {

		aitData.setProcedures("");
		aitData.setRetreat("");

		//Log.d("initialized: 743: ", String.valueOf(dadosAuto.isStoreFullData()));

		//etAutoEnquadra.setText(dadosAuto.getEnquadra());
		//etAutoDesdob.setText(dadosAuto.getDesdob());
		//etAutoArt.setText(dadosAuto.getAmparoLegal());
		//etAutoObservacao.setText(dadosAuto.getObservacao());
		//etAutoMarcaEquipamento.setText(dadosAuto.getMarcaDoEquipamento());
		//etAutoModeloEquipamento.setText(dadosAuto.getModeloDoEquipamento());
		//etAutoNumeroSerie.setText(dadosAuto.getNumeroDeSerie());
		//etAutoMedicaoRealizada.setText(dadosAuto.getMedicaoRealizada());
		//etAutoValor.setText(dadosAuto.getValorConsiderada());
		//etAutoNumeroDaAmostra.setText(dadosAuto.getNumeroAmostra());
		//spinnerAutoDescricao.setSelection(0);

	}

	@Override
	public void Update() {

	}

	@Override
	public void onCheckedChanged(RadioGroup v, int check_id) {

		switch (v.getId()) {
			case R.id.rg_recolhimento:
				if (check_id != -1) {
					aitData.setRetreat(((RadioButton) view
							.findViewById(rgrRetreat.getCheckedRadioButtonId()))
							.getText().toString());
				}
				break;
			case R.id.rg_procedimentos:
				if (check_id != -1) {
					aitData.setProcedures(((RadioButton) view
							.findViewById(rgProcedures
									.getCheckedRadioButtonId())).getText()
							.toString());
				}
				break;
		}
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
				case R.id.et_auto_observacao:
					aitData.setObservation(s.toString());
					break;
				case R.id.et_auto_marca_equipamento:
					aitData.setEquipmentBrand(s.toString());
					break;
				case R.id.et_auto_numero_de_serie:
					aitData.setSerialNumber(s.toString());
					break;
				case R.id.et_auto_modelo_equipamento:
					aitData.setEquipmentModel(s.toString());
					break;
				case R.id.et_ait_measurement_performed:
					//Routine.formatEditText(etMeasurementPerformed);
					if (ifCalculate) {
						if (calculateAmountAlcoholInBlood(s.toString())) {
							aitData.setMeasurementPerformed(s.toString() + " " + unitMeasureCalculation);
						}
					}
					aitData.setMeasurementPerformed(s.toString() + " " + unitMeasureCalculation);
					break;
				case R.id.et_auto_valor_medicao:
					aitData.setValueConsidered(s.toString() + " " + unitMeasureCalculation);
				case R.id.et_ait_sample_number:
					aitData.setAlcoholTestNumber(s.toString());
					break;
				case R.id.et_auto_nome_transportador:
					aitData.setCarrierIdentification(s.toString());
					break;
				case R.id.et_auto_nome_embarcador:
					aitData.setCarrierIdentification(s.toString());
					break;
				case R.id.et_cfp_transportador:
					aitData.setCpfCarrier(s.toString());
					break;
				case R.id.et_cnpj_transportador:
					aitData.setCnpjCarrier(s.toString());
					break;
				case R.id.et_cpf_embarcador:
					aitData.setCpfShipper(s.toString());
					break;
				case R.id.et_cnpj_embarcador:
					aitData.setCnpShipper(s.toString());
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

			if (ifCalculate) {
				if (etMeasurementPerformed.getText().toString().length() == 4) {
				    etMeasurementPerformed.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
				    etRegulatedValue.setEnabled(false);
					etValueConsidered.setEnabled(false);
					//etAitSampleNumber.requestFocus();
				}
			}else{
				aitData.setRegulatedValue(etRegulatedValue.getText().toString() + " " + unitMeasureCalculation);
				etRegulatedValue.setEnabled(true);
				etValueConsidered.setEnabled(true);
			}

			if (etShipperCpf.getText().toString().length() == 11) {

				if(!Cnpj.isValidCpf(etShipperCpf.getText().toString()))
					AnyAlertDialog.dialogShow(getActivity().getResources()
									.getString(R.string.shipper_invalid_cpf), getActivity(),
							getActivity().getResources().getString(R.string.error_message));

			}

			if (etShipperCnpj.getText().toString().length() == 14) {

				if(!Cnpj.isValidCnpj(etShipperCnpj.getText().toString()))
					AnyAlertDialog.dialogShow(getActivity().getResources()
									.getString(R.string.shipper_invalid_cnpj), getActivity(),
							getActivity().getResources().getString(R.string.error_message));

			}

			if (etCarrierCpf.getText().toString().length() == 11) {

				if(!Cnpj.isValidCpf(etCarrierCpf.getText().toString()))
					AnyAlertDialog.dialogShow(getActivity().getResources()
									.getString(R.string.carrier_invalid_cpf), getActivity(),
							getActivity().getResources().getString(R.string.error_message));

			}

			if (etCarrierCnpj.getText().toString().length() == 14) {

				if(!Cnpj.isValidCnpj(etCarrierCnpj.getText().toString()))
					AnyAlertDialog.dialogShow(getActivity().getResources()
									.getString(R.string.carrier_invalid_cnpj), getActivity(),
							getActivity().getResources().getString(R.string.error_message));

			}

		}
	}

	@Override
	public void onClick(View v) {

	}

}
