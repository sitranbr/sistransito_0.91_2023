package net.sistransito.mobile.plate.search;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import net.sistransito.mobile.plate.data.PlateHttpResultAsyncTask;
import net.sistransito.mobile.plate.data.DataFromPlate;
import net.sistransito.mobile.plate.data.PlateViewFormat;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

public class ConsultPlateFragment extends Fragment implements
        OnClickListener {

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
    private String plateOrChassis, emptyField, typeSearch;
    private final int LENGTH_PLATE_CHARACTER = 3;
    private final int LENGTH_PLATE_NUMBER = 4;
    private final int LENGTH_CHASSI = 17;
    private final int LENGTH_PLATE_MERCOSUL = 7;
    private final int VIS_NUMBER = 8;
    private PlateHttpResultAsyncTask httpResultAnysTask;
    private EditTextChange etCharacter, etNumber;
    private Boolean textChangeState = true;
    private DataFromPlate dataFromPlate;

    //private GPSTracker gps;

    public static ConsultPlateFragment newInstance() {
        return new ConsultPlateFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.plate_search_fragment, null, false);
        //checkAitCancel();
        initializeView();
        return view;
    }

    private void checkAitCancel() {

        String idAuto = (DatabaseCreator.getNumberDatabaseAdapter(getActivity()))
                .getAitNumber();

        if(idAuto != null) {

            startActivity(new Intent(getActivity(), AitLister.class));
            getActivity().finish();

        }

    }

    private void initializeView() {
        llPlateSearch = view.findViewById(R.id.ll_plate_search);
        llVis = view.findViewById(R.id.ll_radio_two);
        llParentResultView = view.findViewById(R.id.ll_parent_result_view);
        llChildResultView = view.findViewById(R.id.ll_child_result_view);
        tvResultShow = view.findViewById(R.id.tv_result_plate_show);
        tvResultShow.setOnClickListener(this);
        btnCreateAit = view.findViewById(R.id.btn_create_ait);
        btnCreateAit.setOnClickListener(this);
        btnPlateSearch = view.findViewById(R.id.btn_plate_search);
        btnPlateSearch.setOnClickListener(this);
        etPlateLetters = view.findViewById(R.id.et_license_plate_letters);
        etPlateNumbers = view.findViewById(R.id.et_license_plate_numbers);
        etChassi = view.findViewById(R.id.et_chassi_input);
        etPlateMercosul = view.findViewById(R.id.et_mercosul_input);
        etVisNumber = view.findViewById(R.id.et_vis_input);
        etSealNumber = view.findViewById(R.id.et_seal_input);
        etEngineNumber = view.findViewById(R.id.et_engine_input);
        cbOfflineSearch = view.findViewById(R.id.cb_offline_search);
        rgTypeSearch = view.findViewById(R.id.rg_type_search);
        rgVisTypeSearch = view.findViewById(R.id.rg_type_search_two);
        rgSealTypeSearch = view.findViewById(R.id.rg_type_search_seal);
        rbPlateSearch = view.findViewById(R.id.rb_plate_search);
        rbChassiSearch = view.findViewById(R.id.rb_chassi_search);
        rbMercosulPlateSearch = view.findViewById(R.id.rb_mercosul_search);
        rbVisNumber = view.findViewById(R.id.rb_vis_search);
        rbSealNumber = view.findViewById(R.id.rb_search_seal);
        rbEngineNumber = view.findViewById(R.id.rb_engine_search);

        etPlateLetters.addTextChangedListener(new EditTextChange(R.id.et_license_plate_letters));
        etPlateNumbers.addTextChangedListener(new EditTextChange(R.id.et_license_plate_numbers));
        etChassi.addTextChangedListener(new EditTextChange(R.id.et_chassi_input));
        etPlateMercosul.addTextChangedListener(new EditTextChange(R.id.et_mercosul_input));
        etVisNumber.addTextChangedListener(new EditTextChange(R.id.et_vis_input));

        cbOfflineSearch.setOnClickListener(v -> {
            Routine.openKeyboard(cbOfflineSearch, requireActivity());
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
        Routine.openKeyboard(requireActivity().getCurrentFocus(), requireActivity());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_plate_search) {
            if (iSVehicleSearch()) {
                getVehicleResult();
                //Log.d("Latitude", gps.getLocation().getLatitude() + " ");
                //Log.d("Longitude", gps.getLocation().getLongitude() + " ");
            }
        } else if (id == R.id.tv_show_result) {
            llParentResultView.setVisibility(View.GONE);
            enableSearch();
            Routine.closeKeyboard(tvResultShow, getActivity());
        } else if (id == R.id.btn_create_ait) {
            if (dataFromPlate != null) {

                if ((DatabaseCreator
                        .getInfractionDatabaseAdapter(getActivity()))
                        .isSamePlateExist(dataFromPlate.getPlate())) {

                    openAitFragment(DatabaseCreator.getInfractionDatabaseAdapter(getActivity())
                            .getAitDataFromPlate(dataFromPlate.getPlate()));

                } else {
                    AitData aitData = createAitDataFromPlate(dataFromPlate); // Chama o novo método
                    openAitFragment(aitData);
                }
            }
        }
    }

    private AitData createAitDataFromPlate(DataFromPlate dataFromPlate) {
        AitData aitData = new AitData();
        aitData.setPlate(dataFromPlate.getPlate());
        aitData.setRenavam(dataFromPlate.getRenavam());
        aitData.setChassi(dataFromPlate.getChassi());
        aitData.setStateVehicle(dataFromPlate.getState() != null ? dataFromPlate.getState().toUpperCase() : "");
        aitData.setVehicleModel(dataFromPlate.getModel() != null ? dataFromPlate.getModel() : "");
        aitData.setVehicleBrand(dataFromPlate.getBrand() != null ? dataFromPlate.getBrand() : "");
        aitData.setVehycleColor(dataFromPlate.getColor() != null ? dataFromPlate.getColor() : "");
        aitData.setVehicleSpecies(dataFromPlate.getSpecies() != null ? dataFromPlate.getSpecies().toUpperCase() : "");
        aitData.setVehicleCategory(dataFromPlate.getCategory() != null ? dataFromPlate.getCategory().toUpperCase() : "");
        aitData.setStoreFullData(false);
        return aitData;
    }

    private void getVehicleResult() {
        httpResultAnysTask = new PlateHttpResultAsyncTask(
                new CallBackPlate() {
                    @Override
                    public void callBack(DataFromPlate dataPlate, boolean isOffline) {
                        resultCallBack(dataPlate, isOffline);
                    }
                }, getActivity(), isOfflineSearch(), plateOrChassis, typeSearch, null);//gps.getLocation());
        httpResultAnysTask.execute("");
    }

    private void addResultView() {
        llParentResultView.setVisibility(View.VISIBLE);
    }

    private void removeResultView() {

        llParentResultView.setVisibility(View.GONE);
        tvResultShow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llParentResultView.setVisibility(View.GONE);
                enableSearch();
            }
        });

    }

    public boolean iSVehicleSearch() {

        if (true){//gps.canGetLocation()) {

            if (checkInput()) {
                if (isOfflineSearch()) {
                    return true;
                } else {
                    if (isInternetConnected()) {
                        return true;
                    } else {
                        AnyAlertDialog.dialogShow(
                                getResources().getString(R.string.no_network_connection),
                                getActivity(), "info");
                        return false;
                    }
                }
            } else {
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
        //     gps.stopUsingGPS();

        //Log.d("resultCallBack", String.valueOf(dataPlate));

        if (dataPlate != null) {

            this.dataFromPlate = dataPlate;
            PlateViewFormat plateViewFormat = new PlateViewFormat(
                    dataPlate, getActivity());
            addResultView();

            tvResultShow.setText(plateViewFormat.getResultViewData(),
                    BufferType.SPANNABLE);
            plateViewFormat.setWarning();
            addResultView();
            disableSearch();
            btnCreateAit.setEnabled(true);

        } else {
            tvResultShow.setText(getResources().getString(
                    R.string.no_result_returned));
            btnCreateAit.setEnabled(false);
            addResultView();
            disableSearch();
        }
    }

    // after result enable all search helper view ;
    private void enableSearch() {
        /*
        btnConsultarPlaca.setEnabled(true);
        etLetrasPlaca.setEnabled(true);
        etNumerosPlaca.setEnabled(true);
        cbPesquisaOffline.setEnabled(true);
        */
        llPlateSearch.setVisibility(View.VISIBLE);
        rgTypeSearch.setVisibility(View.VISIBLE);
        rgSealTypeSearch.setVisibility(View.VISIBLE);
        llVis.setVisibility(View.VISIBLE);
        etPlateLetters.requestFocus();
        Routine.openKeyboard(llParentResultView, getActivity());
    }

    // after show search result disable all search helper view ;
    private void disableSearch() {
        /*
        btnConsultarPlaca.setEnabled(false);
        etLetrasPlaca.setEnabled(false);
        etNumerosPlaca.setEnabled(false);
        cbPesquisaOffline.setEnabled(false);
        textChangeState = false;
        etNumerosPlaca.setText("");
        etLetrasPlaca.setText("");
        textChangeState = true;
        */
        textChangeState = false;
        etPlateNumbers.setText("");
        etPlateLetters.setText("");
        etChassi.setText("");
        etPlateMercosul.setText("");
        etVisNumber.setText("");
        llPlateSearch.setVisibility(View.GONE);
        llVis.setVisibility(View.GONE);
        rgTypeSearch.setVisibility(View.GONE);
        rgSealTypeSearch.setVisibility(View.GONE);
        textChangeState = true;
    }

    private void clearAllFields(){
        etPlateLetters.setText("");
        etPlateNumbers.setText("");
        etChassi.setText("");
        etPlateMercosul.setText("");
        etVisNumber.setText("");
    }

    private boolean checkInput() {
        String character = etPlateLetters.getText().toString().trim();
        String number = etPlateNumbers.getText().toString().trim();
        String chassi = etChassi.getText().toString().trim();
        String mercosulPlate = etPlateMercosul.getText().toString().trim();
        String visNumber = etVisNumber.getText().toString().trim();
        String sealNumber = etSealNumber.getText().toString().trim();
        String engineNumber = etEngineNumber.getText().toString().trim();

        if (rbPlateSearch.isChecked()){
            plateOrChassis = character + number;
            typeSearch = "plate";
            if (character.length() != LENGTH_PLATE_CHARACTER) {
                etPlateLetters.setError(getResources().getString(
                        R.string.license_plate_letters));
                etPlateLetters.requestFocus();
                return false;
            } else if (number.length() != LENGTH_PLATE_NUMBER) {
                etPlateNumbers.setError(getResources().getString(
                        R.string.plate_numbers));
                etPlateNumbers.requestFocus();
                return false;
            } else {
                return true;
            }
        } else if (rbChassiSearch.isChecked()){
            plateOrChassis = chassi;
            typeSearch = "chassi";
            if (chassi.length() != LENGTH_CHASSI) {
                etChassi.setError(getResources().getString(
                        R.string.chassi_field));
                etChassi.requestFocus();
                return false;
            } else {
                return true;
            }
        } else if (rbMercosulPlateSearch.isChecked()){
            plateOrChassis = mercosulPlate;
            typeSearch = "plate";
            if (mercosulPlate.length() != LENGTH_PLATE_MERCOSUL) {
                etPlateMercosul.setError(getResources().getString(
                        R.string.plate_mercosul_field));
                etPlateMercosul.requestFocus();
                return false;
            } else {
                return true;
            }
        } else if (rbVisNumber.isChecked()){
            plateOrChassis = visNumber;
            typeSearch = "vis";
            if (visNumber.length() != VIS_NUMBER) {
                etVisNumber.setError(getResources().getString(
                        R.string.vis_field));
                etVisNumber.requestFocus();
                return false;
            } else {
                return true;
            }
        }else if (rbSealNumber.isChecked()){
            plateOrChassis = sealNumber;
            typeSearch = "seal";
            if (sealNumber == null) {
                etSealNumber.setError(getResources().getString(
                        R.string.seal_field));
                etSealNumber.requestFocus();
                return false;
            } else {
                return true;
            }
        }else if (rbEngineNumber.isChecked()){
            plateOrChassis = engineNumber;
            typeSearch = "engine";
            if (engineNumber == null) {
                etEngineNumber.setError(getResources().getString(
                        R.string.engine_field));
                etEngineNumber.requestFocus();
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }

    }

    private class EditTextChange implements TextWatcher {
        private int id;

        public EditTextChange(int id) {
            this.id = id;
        }

        @Override
        public void afterTextChanged(Editable text) {
            if (textChangeState) {

                if (id == R.id.et_license_plate_letters) {
                    etPlateLetters.setError(null);
                    if (text.toString().length() == LENGTH_PLATE_CHARACTER) {
                        if (etPlateNumbers.getText().toString().length() != LENGTH_PLATE_NUMBER) {
                            etPlateNumbers.requestFocus();
                        } else {
                            Routine.openKeyboard(btnPlateSearch, getActivity());
                            btnPlateSearch.setFocusableInTouchMode(true);
                            btnPlateSearch.requestFocus();
                        }
                    }
                } else if (id == R.id.et_license_plate_numbers) {
                    etPlateNumbers.setError(null);
                    if (text.toString().length() == LENGTH_PLATE_NUMBER) {
                        if (etPlateLetters.getText().toString().length() != LENGTH_PLATE_CHARACTER) {
                            etPlateLetters.requestFocus();
                        } else {
                            //btnConsultarPlaca.setFocusableInTouchMode(true);
                            //btnConsultarPlaca.requestFocus();
                            if (iSVehicleSearch()) {
                                getVehicleResult();
                                //       Log.d("Latitude", gps.getLocation().getLatitude() + " ");
                                //    Log.d("Longitude", gps.getLocation().getLongitude() + " ");
                            }
                        }
                    }
                } else if (id == R.id.et_chassi_input) {
                    etChassi.setError(null);
                    if (text.toString().length() == LENGTH_CHASSI) {
                        if (etChassi.getText().toString().length() != LENGTH_CHASSI) {
                            etChassi.requestFocus();
                        } else {
                            if (iSVehicleSearch()) {
                                getVehicleResult();
                            }
                        }
                    }
                } else if (id == R.id.et_mercosul_input) {
                    etPlateMercosul.setError(null);
                    if (text.toString().length() == LENGTH_PLATE_MERCOSUL) {
                        if (etPlateMercosul.getText().toString().length() != LENGTH_PLATE_MERCOSUL) {
                            etPlateMercosul.requestFocus();
                        } else {
                            if (iSVehicleSearch()) {
                                getVehicleResult();
                            }
                        }
                    }
                } else if (id == R.id.et_vis_input) {
                    etVisNumber.setError(null);
                    if (text.toString().length() == VIS_NUMBER) {
                        if (etVisNumber.getText().toString().length() != VIS_NUMBER) {
                            etVisNumber.requestFocus();
                        } else {
                            if (iSVehicleSearch()) {
                                getVehicleResult();
                            }
                        }
                    }
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

    private void openAitFragment(AitData aitData) {
        Intent intent = new Intent(getActivity(), AitActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AitData.getAitID(), aitData);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
