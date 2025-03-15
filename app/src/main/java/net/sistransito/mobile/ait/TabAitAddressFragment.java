package net.sistransito.mobile.ait;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.rey.material.widget.CheckBox;

import net.sistransito.mobile.fragment.BasePickerFragment;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.PrepopulatedDBOpenHelper;
import net.sistransito.mobile.database.SetttingDatabaseHelper;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class TabAitAddressFragment extends BasePickerFragment implements View.OnClickListener {

	private View view;
	private AutoCompleteTextView cityAutoComplete;
	private AitData aitData;
	private EditText etAddressInfraction, etCityCode,
			etAitDate, etAitTime, etState;
	private LinearLayout llCityState;
	private TextView tvLocationGps, tvClearAddress, tvClearData, tvSaveData;
	private CheckBox cbConfirm;

	private List<String> cityArray, codeArray, stateArray;
	private ArrayAdapter<String> cityAdapter;

	private Bundle bundle;

	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

	public static TabAitAddressFragment newInstance() {
		return new TabAitAddressFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.ait_address_fragment, null, false);
		initializedView();
		getAitObject();

		etAitDate.setOnClickListener(v -> addPicker(R.id.btn_ait_pj_date, "date"));
		etAitTime.setOnClickListener(v -> addPicker(R.id.btn_ait_pj_time, "time"));

		return view;
	}

	private void initializedView() {
		initializeViews();
		setInitialVisibility();
		setupListeners();
		setCityAutoComplete();
	}

	private void initializeViews() {
		llCityState = view.findViewById(R.id.ll_auto_state);
		cityAutoComplete = view.findViewById(R.id.auto_complete_city);
		etCityCode = view.findViewById(R.id.et_auto_city_code);
		etState = view.findViewById(R.id.et_auto_address_state);
		etAddressInfraction = view.findViewById(R.id.et_ait_address);
		etAitDate = view.findViewById(R.id.btn_ait_pj_date);
		etAitTime = view.findViewById(R.id.btn_ait_pj_time);
		tvSaveData = view.findViewById(R.id.ait_fab);
		cbConfirm = view.findViewById(R.id.cb_ait_confirm);
		tvLocationGps = view.findViewById(R.id.tv_location_gps);
		tvClearData = view.findViewById(R.id.tv_clear_data);
		tvClearAddress = view.findViewById(R.id.tv_clear_address);
	}

	private void setInitialVisibility() {
		llCityState.setVisibility(LinearLayout.GONE);
		tvClearAddress.setVisibility(View.GONE);
	}

	private void setupListeners2() {
		tvLocationGps.setOnClickListener(v -> getLocationAndSetAddress());
		tvClearAddress.setOnClickListener(v -> clearAddressFields());
		tvClearData.setOnClickListener(v -> clearCityFieldsAndVisibility());
	}

	private void setupListeners() {
		Map<View, View.OnClickListener> listeners = new HashMap<>();
		listeners.put(tvLocationGps, v -> getLocationAndSetAddress());
		listeners.put(tvClearAddress, v -> clearAddressFields());
		listeners.put(tvClearData, v -> clearCityFieldsAndVisibility());
		listeners.put(cbConfirm, v -> toggleSaveDataVisibility());
		listeners.put(tvSaveData, v -> saveDataIfValid());

		listeners.forEach(View::setOnClickListener);
	}

	/**
	 * Alterna a visibilidade do botão de salvamento com base no estado do CheckBox.
	 */
	private void toggleSaveDataVisibility() {
		tvSaveData.setVisibility(cbConfirm.isChecked() ? View.VISIBLE : View.GONE);
		if (cbConfirm.isChecked()) {
			Routine.closeKeyboard(tvSaveData, getActivity());
		}
	}

	/**
	 * Valida os campos de entrada e salva os dados se válidos.
	 */
	private void saveDataIfValid() {
		if (checkInput()) {
			if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).updateAitDataAddress(aitData)) {
				Routine.showAlert(getResources().getString(R.string.update_erro), getActivity());
			} else {
				((AitActivity) getActivity()).setTabActual(3);
			}
		}
	}

	private void clearAddressFields() {
		clearEditText(etAddressInfraction);
		clearEditText(cityAutoComplete);
		tvClearAddress.setVisibility(View.GONE);
		tvLocationGps.setVisibility(View.VISIBLE);
		clearEditTextError(etAddressInfraction);
		setCityStateVisibility(false);
	}

	private void clearCityFieldsAndVisibility() {
		clearEditText(cityAutoComplete);
		setCityStateVisibility(false);
	}

	private void clearEditTextError(EditText editText) {
		if (editText != null) {
			editText.setError(null);
		}
	}

	private void getLocationAndSetAddress() {
		if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(),
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
					LOCATION_PERMISSION_REQUEST_CODE);
			return;
		}

		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!isGpsEnabled) {
			Toast.makeText(getContext(), "GPS desativado", Toast.LENGTH_SHORT).show();
			return;
		}

		FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
		fusedLocationClient.getLastLocation()
				.addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
					@Override
					public void onSuccess(Location location) {
						if (location != null) {
							setAddressFromLocation(location);
						} else {
							Toast.makeText(getContext(), "Localização não disponível", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void setAddressFromLocation(Location location) {
		Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			if (addresses == null || addresses.isEmpty()) {
				showToast("Endereço não encontrado");
				return;
			}

			Address address = addresses.get(0);
			String cityName = determineCityName(address);
			String addressText = buildAddressText(address);

			updateAddressFields(addressText, cityName);
			updateCityFields(cityName);

			toggleLocationViewsVisibility();
		} catch (IOException e) {
			handleGeocoderError(e);
		}
	}

	/**
	 * Determina o nome da cidade a partir do endereço, com normalização e fallback.
	 */
	private String determineCityName(Address address) {
		String cityName = address.getLocality();
		if (cityName == null) {
			cityName = address.getSubAdminArea();
			if (cityName == null) {
				cityName = "CIDADE DESCONHECIDA";
			}
		}
		cityName = normalizeString(cityName).toUpperCase(Locale.getDefault());
		if (cityName.isEmpty()) {
			cityName = "CIDADE DESCONHECIDA";
		}
		logCityName(cityName);
		return cityName;
	}

	/**
	 * Constrói o texto do endereço (rua, número e bairro) sem cidade, CEP ou país.
	 */
	private String buildAddressText(Address address) {
		String thoroughfare = address.getThoroughfare(); // Rua
		String subThoroughfare = address.getSubThoroughfare(); // Número
		String subLocality = address.getSubLocality(); // Bairro
		String featureName = address.getFeatureName(); // Ponto de referência (fallback)

		StringBuilder addressText = new StringBuilder();

		// Adiciona a rua
		if (thoroughfare != null && !thoroughfare.isEmpty()) {
			addressText.append(thoroughfare.trim());
		} else if (featureName != null && !featureName.isEmpty()) {
			addressText.append(featureName.trim());
		} else {
			return "Endereço não identificado";
		}

		// Adiciona o número, se não estiver duplicado
		if (subThoroughfare != null && !subThoroughfare.isEmpty() && !thoroughfare.contains(subThoroughfare)) {
			addressText.append(", ").append(subThoroughfare.trim());
		}

		// Adiciona o bairro (subLocality ou featureName como fallback)
		String neighborhood = (subLocality != null && !subLocality.isEmpty()) ? subLocality.trim() :
				(featureName != null && !featureName.isEmpty() && !featureName.equals(subThoroughfare) ? featureName.trim() : null);
		if (neighborhood != null && !addressText.toString().contains(neighborhood)) {
			addressText.append(" - ").append(neighborhood);
		}

		String finalAddress = addressText.toString();
		Log.d("AddressText", "Endereço final: " + finalAddress);
		return finalAddress;
	}

	/**
	 * Atualiza os campos de endereço na UI.
	 */
	private void updateAddressFields(String addressText, String cityName) {
		etAddressInfraction.setText(addressText);
		cityAutoComplete.setText(cityName);
		cityAutoComplete.dismissDropDown(); // Evita abrir o spinner
		aitData.setCity(cityName);
	}

	/**
	 * Atualiza os campos relacionados à cidade (código e estado).
	 */
	private void updateCityFields(String cityName) {
		int position = cityArray.indexOf(cityName);
		if (position != -1) {
			aitData.setCityCode(codeArray.get(position));
			etCityCode.setText(aitData.getCityCode());
			aitData.setState(stateArray.get(position));
			etState.setText(aitData.getState());
			setCityStateVisibility(true);
		} else {
			Log.d("Cidade", "Cidade não encontrada na lista: " + cityName);
			showToast("Cidade não está na lista pré-definida");
			clearCityFields();
			setCityStateVisibility(false);
		}
	}

	/**
	 * Limpa os campos relacionados à cidade.
	 */
	private void clearCityFields() {
		etCityCode.setText("");
		etState.setText("");
		aitData.setCityCode("");
		aitData.setState("");
	}

	/**
	 * Alterna a visibilidade dos elementos de localização na UI.
	 */
	private void toggleLocationViewsVisibility() {
		tvLocationGps.setVisibility(LinearLayout.GONE);
		tvClearAddress.setVisibility(LinearLayout.VISIBLE);
	}

	/**
	 * Registra logs para depuração do nome da cidade.
	 */
	private void logCityName(String cityName) {
		Log.d("CidadeRaw", "Locality: " + cityName);
		Log.d("CidadeNormalized", "Após normalização: " + cityName);
		Log.d("CidadeFinal", "Após maiúsculas: " + cityName);
		if (cityName.isEmpty()) {
			Log.e("CidadeErro", "cityName está vazio após processamento");
		}
	}

	/**
	 * Exibe uma mensagem de erro para o usuário.
	 */
	private void showToast(String message) {
		Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Trata erros do Geocoder, exibindo mensagem e logando detalhes.
	 */
	private void handleGeocoderError(IOException e) {
		e.printStackTrace();
		showToast("Erro ao obter endereço: " + e.getMessage());
	}

	/**
	 * Normaliza uma string removendo acentos.
	 */
	private String normalizeString(String input) {
		if (input == null || input.trim().isEmpty()) {
			return "";
		}
		String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
		return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				getLocationAndSetAddress();
			} else {
				Toast.makeText(getContext(), "Permissão de localização negada", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void clearEditText(EditText editText) {
		editText.setText("");
		editText.requestFocus();
	}

	private void setCityStateVisibility(boolean show) {
		int visibility = show ? LinearLayout.VISIBLE : LinearLayout.GONE;
		llCityState.setVisibility(visibility);
		if (show) {
			Routine.closeKeyboard(cityAutoComplete, getActivity());
			cityAutoComplete.requestFocus();
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
				R.id.et_ait_address));

		etAitDate.setOnClickListener(this);
		etAitTime.setOnClickListener(this);

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
		if (!isEditTextValid(etAddressInfraction, R.string.alert_insert_address)) {
			return false;
		}

		if (!isEditTextValid(cityAutoComplete, R.string.alert_insert_city_name)) {
			return false;
		}

		if (!isEditTextValid(etAitDate, R.string.alert_insert_date)) {
			return false;
		}

		if (!isEditTextValid(etAitTime, R.string.alert_insert_time)) {
			return false;
		}

		return true;
	}

	private boolean isEditTextValid(EditText editText, int errorMessageResId) {
		if (editText == null) {
			return false;
		}

		String text = editText.getText().toString();
		if (TextUtils.isEmpty(text)) {
			String errorMessage = getResources().getString(errorMessageResId);
			editText.setError(errorMessage);
			editText.requestFocus();
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
			if (id == R.id.et_ait_address) {
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
			etAitDate.setText(aitData.getAitDate());
		} else if (viewId == R.id.btn_ait_pj_time) {
			aitData.setAitTime(selectedValue);
			etAitTime.setText(aitData.getAitTime());
		}
	}

	private void setNewAitView() {

		cityAutoComplete.setVisibility(AutoCompleteTextView.GONE);
		etCityCode.setVisibility(EditText.GONE);
		etState.setVisibility(EditText.GONE);
		etAitDate.setVisibility(Button.GONE);
		etAitTime.setVisibility(Button.GONE);

	}

}
