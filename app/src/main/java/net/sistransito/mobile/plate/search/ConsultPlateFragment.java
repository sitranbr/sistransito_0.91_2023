package net.sistransito.mobile.plate.search;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.rey.material.widget.CheckBox;

import net.sistransito.mobile.ait.AitActivity;
import net.sistransito.mobile.ait.AitData;
import net.sistransito.mobile.ait.lister.AitLister;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.AnyAlertDialog;
import net.sistransito.mobile.fragment.CallBackPlate;
import net.sistransito.mobile.network.NetworkConnection;
import net.sistransito.mobile.plate.data.PlateHttpResultRetrofit;
import net.sistransito.mobile.plate.data.DataFromPlate;
import net.sistransito.mobile.plate.data.PlateViewFormat;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

public class ConsultPlateFragment extends Fragment implements OnClickListener {

    private View view;
    private LinearLayout llParentResultView, llChildResultView, llPlateSearch, llVis;
    private TextView tvResultShow;
    private Button btnCreateAit, btnPlateSearch;
    private EditText etPlateLetters, etPlateNumbers, etChassi, etPlateMercosul, etVisNumber,
            etSealNumber, etEngineNumber;
    private CheckBox cbOfflineSearch;
    private RadioButton rbPlateSearch, rbChassiSearch, rbMercosulPlateSearch, rbVisNumber,
            rbSealNumber, rbEngineNumber;
    private RadioGroup rgTypeSearch, rgVisTypeSearch, rgSealTypeSearch;
    private String plateOrChassis, typeSearch;
    private final int LENGTH_PLATE_CHARACTER = 3;
    private final int LENGTH_PLATE_NUMBER = 4;
    private final int LENGTH_CHASSI = 17;
    private final int LENGTH_PLATE_MERCOSUL = 7;
    private final int VIS_NUMBER = 8;
    private Boolean textChangeState = true;
    private DataFromPlate dataFromPlate;

    public static ConsultPlateFragment newInstance() {
        return new ConsultPlateFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.plate_search_fragment, container, false);
        initializeView();
        return view;
    }

    private void initializeView() {
        llPlateSearch = view.findViewById(R.id.ll_placa_search);
        llVis = view.findViewById(R.id.ll_radio_two);
        llParentResultView = view.findViewById(R.id.ll_parent_result_view);
        llChildResultView = view.findViewById(R.id.ll_child_result_view);
        tvResultShow = view.findViewById(R.id.tv_result_placa_show);
        tvResultShow.setOnClickListener(this);
        btnCreateAit = view.findViewById(R.id.btn_create_ait);
        btnCreateAit.setOnClickListener(this);
        btnPlateSearch = view.findViewById(R.id.btn_search_plate);
        btnPlateSearch.setOnClickListener(this);
        etPlateLetters = view.findViewById(R.id.et_letras_da_placa);
        etPlateNumbers = view.findViewById(R.id.et_numeros_da_placa);
        etChassi = view.findViewById(R.id.et_chassi_input);
        etPlateMercosul = view.findViewById(R.id.et_mercosul_input);
        etVisNumber = view.findViewById(R.id.et_vis_input);
        etSealNumber = view.findViewById(R.id.et_lacre_input);
        etEngineNumber = view.findViewById(R.id.et_motor_input);
        cbOfflineSearch = view.findViewById(R.id.cb_pesquisa_offline);
        rgTypeSearch = view.findViewById(R.id.rg_type_search);
        rgVisTypeSearch = view.findViewById(R.id.rg_type_search_two);
        rgSealTypeSearch = view.findViewById(R.id.rg_type_search_lacre);
        rbPlateSearch = view.findViewById(R.id.rb_search_placa);
        rbChassiSearch = view.findViewById(R.id.rb_search_chassi);
        rbMercosulPlateSearch = view.findViewById(R.id.rb_search_mercosul);
        rbVisNumber = view.findViewById(R.id.rb_search_vis);
        rbSealNumber = view.findViewById(R.id.rb_search_lacre);
        rbEngineNumber = view.findViewById(R.id.rb_search_motor);

        etPlateLetters.addTextChangedListener(new EditTextChange(R.id.et_letras_da_placa));
        etPlateNumbers.addTextChangedListener(new EditTextChange(R.id.et_numeros_da_placa));
        etChassi.addTextChangedListener(new EditTextChange(R.id.et_chassi_input));
        etPlateMercosul.addTextChangedListener(new EditTextChange(R.id.et_mercosul_input));
        etVisNumber.addTextChangedListener(new EditTextChange(R.id.et_vis_input));

        cbOfflineSearch.setOnClickListener(v -> {
            Routine.openKeyboard(cbOfflineSearch, getActivity());
            etPlateLetters.requestFocus();
        });

        rbPlateSearch.setOnClickListener(v -> setupSearchView(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE));
        rbChassiSearch.setOnClickListener(v -> setupSearchView(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE));
        rbMercosulPlateSearch.setOnClickListener(v -> setupSearchView(View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE, View.GONE));
        rbVisNumber.setOnClickListener(v -> setupSearchView(View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE));
        rbSealNumber.setOnClickListener(v -> setupSearchView(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE));
        rbEngineNumber.setOnClickListener(v -> setupSearchView(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE));

        removeResultView();
    }

    private void setupSearchView(int plateLettersVisibility, int plateNumbersVisibility, int chassiVisibility, int plateMercosulVisibility, int visNumberVisibility, int sealNumberVisibility, int engineNumberVisibility) {
        etPlateLetters.setVisibility(plateLettersVisibility);
        etPlateNumbers.setVisibility(plateNumbersVisibility);
        etChassi.setVisibility(chassiVisibility);
        etPlateMercosul.setVisibility(plateMercosulVisibility);
        etVisNumber.setVisibility(visNumberVisibility);
        etSealNumber.setVisibility(sealNumberVisibility);
        etEngineNumber.setVisibility(engineNumberVisibility);

        rgVisTypeSearch.clearCheck();
        rgSealTypeSearch.clearCheck();

        clearAllFields();
        Routine.openKeyboard(getActivity().getCurrentFocus(), getActivity());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_search_plate) {
            if (isVehicleSearchValid()) {
                getVehicleResult();
            }
        } else if (id == R.id.tv_result_placa_show) {
            llParentResultView.setVisibility(View.GONE);
            enableSearch();
            Routine.closeKeyboard(tvResultShow, getActivity());
        } else if (id == R.id.btn_create_ait) {
            if (dataFromPlate != null) {
                createAit();
            }
        }
    }

    private void getVehicleResult() {
        Location location = getLocation();
        PlateHttpResultRetrofit httpResult = new PlateHttpResultRetrofit(
                new CallBackPlate() {
                    @Override
                    public void callBack(DataFromPlate dataPlate, boolean isOffline) {
                        resultCallBack(dataPlate, isOffline);
                    }
                }, getActivity(), isOfflineSearch(), plateOrChassis, typeSearch, location);
        httpResult.execute("");
    }

    private Location getLocation() {
        // Lógica para obter a localização do dispositivo
        // Retorne uma localização fictícia se a real não estiver disponível para testes
        Location location = new Location("dummyprovider");
        location.setLatitude(0.0);
        location.setLongitude(0.0);
        return location;
    }

    private void addResultView() {
        llParentResultView.setVisibility(View.VISIBLE);
    }

    private void removeResultView() {
        llParentResultView.setVisibility(View.GONE);
        tvResultShow.setOnClickListener(v -> {
            llParentResultView.setVisibility(View.GONE);
            enableSearch();
        });
    }

    public boolean isVehicleSearchValid() {
        if (checkInput()) {
            if (isOfflineSearch()) {
                return true;
            } else if (isInternetConnected()) {
                return true;
            } else {
                AnyAlertDialog.dialogShow(getResources().getString(R.string.no_network_connection), getActivity(), "info");
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isOfflineSearch() {
        return cbOfflineSearch.isChecked();
    }

    private boolean isInternetConnected() {
        return NetworkConnection.isNetworkAvailable(getActivity());
    }

    public void resultCallBack(DataFromPlate dataPlate, boolean isOffline) {
        this.dataFromPlate = null;
        if (dataPlate != null) {
            this.dataFromPlate = dataPlate;
            PlateViewFormat plateViewFormat = new PlateViewFormat(dataPlate, getActivity());
            addResultView();
            tvResultShow.setText(plateViewFormat.getResultViewData(), TextView.BufferType.SPANNABLE);
            plateViewFormat.setWarning();
            addResultView();
            disableSearch();
            btnCreateAit.setEnabled(true);
        } else {
            tvResultShow.setText(getResources().getString(R.string.no_result_returned));
            btnCreateAit.setEnabled(false);
            addResultView();
            disableSearch();
        }
    }

    private void enableSearch() {
        llPlateSearch.setVisibility(View.VISIBLE);
        rgTypeSearch.setVisibility(View.VISIBLE);
        rgSealTypeSearch.setVisibility(View.VISIBLE);
        llVis.setVisibility(View.VISIBLE);
        etPlateLetters.requestFocus();
        Routine.openKeyboard(llParentResultView, getActivity());
    }

    private void disableSearch() {
        textChangeState = false;
        clearAllFields();
        llPlateSearch.setVisibility(View.GONE);
        llVis.setVisibility(View.GONE);
        rgTypeSearch.setVisibility(View.GONE);
        rgSealTypeSearch.setVisibility(View.GONE);
        textChangeState = true;
    }

    private void clearAllFields() {
        etPlateLetters.setText("");
        etPlateNumbers.setText("");
        etChassi.setText("");
        etPlateMercosul.setText("");
        etVisNumber.setText("");
        etSealNumber.setText("");
        etEngineNumber.setText("");
    }

    private boolean checkInput() {
        String character = etPlateLetters.getText().toString().trim();
        String number = etPlateNumbers.getText().toString().trim();
        String chassi = etChassi.getText().toString().trim();
        String mercosulPlate = etPlateMercosul.getText().toString().trim();
        String visNumber = etVisNumber.getText().toString().trim();
        String sealNumber = etSealNumber.getText().toString().trim();
        String engineNumber = etEngineNumber.getText().toString().trim();

        if (rbPlateSearch.isChecked()) {
            plateOrChassis = character + number;
            typeSearch = "plate";
            if (character.length() != LENGTH_PLATE_CHARACTER) {
                etPlateLetters.setError(getResources().getString(R.string.license_plate_letters));
                etPlateLetters.requestFocus();
                return false;
            } else if (number.length() != LENGTH_PLATE_NUMBER) {
                etPlateNumbers.setError(getResources().getString(R.string.plate_numbers));
                etPlateNumbers.requestFocus();
                return false;
            } else {
                return true;
            }
        } else if (rbChassiSearch.isChecked()) {
            plateOrChassis = chassi;
            typeSearch = "chassi";
            if (chassi.length() != LENGTH_CHASSI) {
                etChassi.setError(getResources().getString(R.string.chassi_field));
                etChassi.requestFocus();
                return false;
            } else {
                return true;
            }
        } else if (rbMercosulPlateSearch.isChecked()) {
            plateOrChassis = mercosulPlate;
            typeSearch = "plate";
            if (mercosulPlate.length() != LENGTH_PLATE_MERCOSUL) {
                etPlateMercosul.setError(getResources().getString(R.string.plate_mercosul_field));
                etPlateMercosul.requestFocus();
                return false;
            } else {
                return true;
            }
        } else if (rbVisNumber.isChecked()) {
            plateOrChassis = visNumber;
            typeSearch = "vis";
            if (visNumber.length() != VIS_NUMBER) {
                etVisNumber.setError(getResources().getString(R.string.vis_field));
                etVisNumber.requestFocus();
                return false;
            } else {
                return true;
            }
        } else if (rbSealNumber.isChecked()) {
            plateOrChassis = sealNumber;
            typeSearch = "seal";
            if (sealNumber.isEmpty()) {
                etSealNumber.setError(getResources().getString(R.string.seal_field));
                etSealNumber.requestFocus();
                return false;
            } else {
                return true;
            }
        } else if (rbEngineNumber.isChecked()) {
            plateOrChassis = engineNumber;
            typeSearch = "engine";
            if (engineNumber.isEmpty()) {
                etEngineNumber.setError(getResources().getString(R.string.engine_field));
                etEngineNumber.requestFocus();
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private void createAit() {
        if (DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).isSamePlateExist(dataFromPlate.getPlate())) {
            openAitFragment(DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).getAitDataFromPlate(dataFromPlate.getPlate()));
        } else {
            AitData aitData = new AitData();
            aitData.setPlate(dataFromPlate.getPlate());
            aitData.setRenavam(dataFromPlate.getRenavam());
            aitData.setChassi(dataFromPlate.getChassi());
            aitData.setStateVehicle(dataFromPlate.getState());
            aitData.setVehicleModel(dataFromPlate.getModel());
            aitData.setVehicleBrand(dataFromPlate.getBrand());
            aitData.setVehycleColor(dataFromPlate.getColor());
            aitData.setVehicleSpecies(dataFromPlate.getSpecies().toUpperCase());
            aitData.setVehicleCategory(dataFromPlate.getCategory().toUpperCase());
            aitData.setStoreFullData(false);
            openAitFragment(aitData);
        }
    }

    private void openAitFragment(AitData aitData) {
        Intent intent = new Intent(getActivity(), AitActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AitData.getAitID(), aitData);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class EditTextChange implements TextWatcher {
        private int id;

        public EditTextChange(int id) {
            this.id = id;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (textChangeState) {
                if (id == R.id.et_letras_da_placa) {
                    etPlateLetters.setError(null);
                    if (s.toString().length() == LENGTH_PLATE_CHARACTER) {
                        if (etPlateNumbers.getText().toString().length() != LENGTH_PLATE_NUMBER) {
                            etPlateNumbers.requestFocus();
                        } else {
                            Routine.openKeyboard(btnPlateSearch, getActivity());
                            btnPlateSearch.setFocusableInTouchMode(true);
                            btnPlateSearch.requestFocus();
                        }
                    }
                } else if (id == R.id.et_numeros_da_placa) {
                    etPlateNumbers.setError(null);
                    if (s.toString().length() == LENGTH_PLATE_NUMBER) {
                        if (etPlateLetters.getText().toString().length() != LENGTH_PLATE_CHARACTER) {
                            etPlateLetters.requestFocus();
                        } else {
                            if (isVehicleSearchValid()) {
                                getVehicleResult();
                            }
                        }
                    }
                } else if (id == R.id.et_chassi_input) {
                    etChassi.setError(null);
                    if (s.toString().length() == LENGTH_CHASSI) {
                        if (etChassi.getText().toString().length() != LENGTH_CHASSI) {
                            etChassi.requestFocus();
                        } else {
                            if (isVehicleSearchValid()) {
                                getVehicleResult();
                            }
                        }
                    }
                } else if (id == R.id.et_mercosul_input) {
                    etPlateMercosul.setError(null);
                    if (s.toString().length() == LENGTH_PLATE_MERCOSUL) {
                        if (etPlateMercosul.getText().toString().length() != LENGTH_PLATE_MERCOSUL) {
                            etPlateMercosul.requestFocus();
                        } else {
                            if (isVehicleSearchValid()) {
                                getVehicleResult();
                            }
                        }
                    }
                } else if (id == R.id.et_vis_input) {
                    etVisNumber.setError(null);
                    if (s.toString().length() == VIS_NUMBER) {
                        if (etVisNumber.getText().toString().length() != VIS_NUMBER) {
                            etVisNumber.requestFocus();
                        } else {
                            if (isVehicleSearchValid()) {
                                getVehicleResult();
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }
}
