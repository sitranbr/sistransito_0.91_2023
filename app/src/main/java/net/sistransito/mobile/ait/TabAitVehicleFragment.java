package net.sistransito.mobile.ait;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.rey.material.widget.CheckBox;

import net.sistransito.mobile.adapter.AnyArrayAdapter;
import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.AnyDialogFragment;
import net.sistransito.mobile.fragment.AnyDialogListener;
import net.sistransito.mobile.fragment.CallBackPlate;
import net.sistransito.mobile.network.NetworkConnection;
import net.sistransito.mobile.number.AsyncListernerNumber;
import net.sistransito.mobile.number.NumberHttpResultAsyncTask;

import net.sistransito.mobile.plate.data.DataFromPlate;
import net.sistransito.mobile.plate.data.PlateHttpResultAsyncTask;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

import java.util.Arrays;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class TabAitVehicleFragment extends DebugFragment implements
        AnyDialogListener {

    private PlateHttpResultAsyncTask httpResultAsyncTask;
    private View view;
    private MaterialSpinner spinner;
    private EditText editVehiclePlate, editVehicleBrand, editVehicleModel,
            editVehicleColor, editVehicleRenavan, editVehicleChassi;
    private TextView tvSearchPlate, tvIdAit, tvSaveData;
    private Spinner spinnerSpecies, spinnerCategory,
            spinnerCountry, spinnerPlateState;
    private List<String> listCategory, listSpecies, listCountry, listStatePlate;

    private AnyArrayAdapter<String> aaaCategory, aaaSpecies,
            aaaCountry, aaaStatePlate;

    private ArrayAdapter<String> aalistSpecies;

    private AitData aitData;
    private String sAitNumberRetained, searchType;
    private Bundle bundle;
    private AnyDialogFragment dialogFragment;
    private LinearLayout llVehicleState, llVehicleCountry;
    private CheckBox cbIfForeignVehicle, cbAitConfirm;


    public static TabAitVehicleFragment newInstance() {
        return new TabAitVehicleFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the state
        outState.putString("Ait_Number", aitData.getAitNumber());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lo_vehicle_fragment, null, false);

        if (savedInstanceState != null) {
            sAitNumberRetained = savedInstanceState.getString("Ait_Number");
        }

        initializedView();
        getAitObject();
        checkAitNumber();
        return view;
    }


    private void initializedView() {

        tvSaveData = (TextView) view.findViewById(R.id.ait_fab);
        tvIdAit = (TextView) view.findViewById(R.id.tv_ait_id);
        editVehiclePlate = (EditText) view.findViewById(R.id.edit_vehicle_plate);
        editVehicleChassi = (EditText) view.findViewById(R.id.edit_vehicle_chassi);
        editVehicleRenavan = (EditText) view.findViewById(R.id.edit_vehicle_renavan);
        editVehicleBrand = (EditText) view.findViewById(R.id.edit_vehicle_brand);
        editVehicleModel = (EditText) view
                .findViewById(R.id.edit_vehicle_model);
        editVehicleColor = (EditText) view
                .findViewById(R.id.edit_vehicle_color);

        listCategory = Arrays.asList(getResources().getStringArray(
                R.array.list_category));

        listCountry = Arrays.asList(getResources().getStringArray(
                R.array.filter_list_country));

        aaaCountry = new AnyArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, android.R.id.text1,
                listCountry);

        aaaCategory = new AnyArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, android.R.id.text1,
                listCategory);
        //Spinner Especie
        listSpecies = Arrays.asList(getResources().getStringArray(
                R.array.ait_species));
        aaaSpecies = new AnyArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, android.R.id.text1,
                listSpecies);
        spinnerSpecies = (Spinner) view.findViewById(R.id.spinner_auto_especie);
        spinnerSpecies.setAdapter(aaaSpecies);

        //Spinner UF da placaVeiculo
        listStatePlate = Arrays.asList(getResources().getStringArray(
                R.array.state_array));
        aaaStatePlate = new AnyArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, android.R.id.text1,
                listStatePlate);
        spinnerPlateState = (Spinner) view.findViewById(R.id.spinner_uf_veiculo);
        spinnerPlateState.setAdapter(aaaStatePlate);

        //
        spinner = (MaterialSpinner) view.findViewById(R.id.spinner_auto_especie);
        //aalistEspecie = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,listEspecie);
        aalistSpecies = new AnyArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, android.R.id.text1,
                listSpecies);
        aalistSpecies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aalistSpecies);

        //

        spinnerCountry = (Spinner) view.findViewById(R.id.spinner_auto_pais);
        spinnerCountry.setAdapter(aaaCountry);

        spinnerCategory = (Spinner) view.findViewById(R.id.spinner_auto_categoria);
        spinnerCategory.setAdapter(aaaCategory);

        llVehicleState = (LinearLayout) view.findViewById(R.id.ll_field_uf_veiculo);
        llVehicleCountry = (LinearLayout) view.findViewById(R.id.ll_field_pais_veiculo);
        cbIfForeignVehicle = (CheckBox) view.findViewById(R.id.cb_se_veiculo_estrangeiro);
        cbAitConfirm = (CheckBox) view.findViewById(R.id.cb_auto_confirmar);

        tvSearchPlate = (TextView) view.findViewById(R.id.tv_auto_search_plate);

    }

    private void addListener() {

        editVehicleRenavan.addTextChangedListener(new ChangeText(R.id.edit_vehicle_renavan));
        editVehicleChassi.addTextChangedListener(new ChangeText(
                R.id.edit_vehicle_chassi));

        spinnerCountry
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

        spinnerCategory
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int pos, long Id) {

                        aitData.setVehicleCategory((String) parent
                                .getItemAtPosition(pos));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        spinnerSpecies
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int pos, long Id) {
                        aitData.setVehicleSpecies((String) parent.getItemAtPosition(pos));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        spinnerPlateState
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int pos, long Id) {
                        aitData.setStateVehicle((String) parent.getItemAtPosition(pos));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        cbIfForeignVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbIfForeignVehicle.isChecked()){
                    llVehicleState.setVisibility(View.GONE);
                    llVehicleCountry.setVisibility(View.VISIBLE);
                }else{
                    llVehicleState.setVisibility(View.VISIBLE);
                    llVehicleCountry.setVisibility(View.GONE);
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

        tvSearchPlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchType = "plate";

                if (!editVehiclePlate.getText().toString().equals("")) {
                    if (NetworkConnection.isNetworkAvailable(getActivity())) {

                        httpResultAsyncTask = new PlateHttpResultAsyncTask(
                                new CallBackPlate() {
                                    @Override
                                    public void callBack(DataFromPlate DataFromPlate, boolean isOffline) {
                                        resultCallBack(DataFromPlate, isOffline);
                                    }
                                }, getActivity(), true, editVehiclePlate.getText().toString(), searchType,null);//gps.getLocation());
                        httpResultAsyncTask.execute("");

                    } else {
                        Routine.showAlert(getResources().getString(R.string.no_network_connection), getActivity());
                    }
                } else {
                    Routine.showAlert(getResources().getString(R.string.plate_search_screen_title), getActivity());
                }

            }
        });

        tvSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkInput()) {

                    if (!DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).insertAitData(aitData))
                        Routine.showAlert(getResources().getString(R.string.update_erro), getActivity());

                    ((AitActivity) getActivity()).setTabActual(1);

                }

            }
        });

    }

    private boolean checkInput(){

        if (editVehiclePlate.getText().toString().isEmpty()) {
            editVehiclePlate.setError(getResources().getString(
                    R.string.alert_insert_plate));
            editVehiclePlate.requestFocus();
            return false;
        } else if (editVehicleBrand.getText().toString().isEmpty()) {
            editVehicleBrand.setError(getResources().getString(
                    R.string.alert_insert_brand));
            editVehicleBrand.requestFocus();
            return false;
        } else if (editVehicleModel.getText().toString().isEmpty()) {
            editVehicleModel.setError(getResources().getString(
                    R.string.alert_insert_model));
            editVehicleModel.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    private void getAitObject() {
        aitData = AitObject.getAitData();
        if (aitData.isDataisNull()) {
            setViewEnable(true);
            addListener();
        } else if (aitData.isStoreFullData()) {
            setViewEnable(true);
            getRecommendUpdate();
            getOtherUpdate();
            addListener();
        } else {
            setViewEnable(true);
            getRecommendUpdate();
            getOtherUpdate();
            addListener();
        }
    }

    private void getRecommendUpdate() {

        editVehiclePlate.setText(aitData.getPlate());
        editVehicleBrand.setText(aitData.getVehicleBrand());
        editVehicleModel.setText(aitData.getVehicleModel());
        editVehicleColor.setText(aitData.getVehycleColor());
        editVehicleChassi.setText(aitData.getChassi());
        editVehicleRenavan.setText(aitData.getRenavam());

    }

    private void getOtherUpdate() {

        int selectionStateVehicle = 0,
                selectionCountry = 0,
                selectionVehicleSpecies = 0,
                selectionVehicleCategory = 0;

        selectionStateVehicle = listStatePlate.indexOf(aitData.getStateVehicle());
        selectionCountry = listCountry.indexOf(aitData.getCountry());
        selectionVehicleSpecies = listSpecies.indexOf(aitData.getVehicleSpecies());
        selectionVehicleCategory = listCategory.indexOf(aitData.getVehicleCategory());

        spinnerPlateState.setSelection(selectionStateVehicle + 1);
        spinnerCountry.setSelection(selectionCountry + 1);
        spinnerSpecies.setSelection(selectionVehicleSpecies + 1);
        spinnerCategory.setSelection(selectionVehicleCategory + 1);

    }

    private class ChangeText implements TextWatcher {
        private int id;

        public ChangeText(int id) {
            this.id = id;
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.toString() != null) {
                if (id == R.id.edit_vehicle_renavan) {
                    aitData.setRenavam(s.toString());
                } else if (id == R.id.edit_vehicle_chassi) {
                    aitData.setChassi(s.toString());
                } else if (id == R.id.edit_vehicle_plate) {
                    aitData.setPlate(s.toString());
                } else if (id == R.id.edit_vehicle_color) {
                    aitData.setVehycleColor(s.toString());
                } else if (id == R.id.edit_vehicle_brand) {
                    aitData.setVehicleBrand(s.toString());
                } else if (id == R.id.edit_vehicle_model) {
                    aitData.setVehicleModel(s.toString());
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
        if (isWork) {

            (new NumberHttpResultAsyncTask(new AsyncListernerNumber() {

                @Override
                public void asyncTaskComplete(boolean isComplete) {
                    if (isComplete) {
                        checkAitNumber();
                    }
                }
            }, getActivity(), AppConstants.AIT_NUMBER)).execute("");

        } else {
            getActivity().finish();
        }
    }

    private void setViewEnable(boolean isEnable) {

        editVehiclePlate.setEnabled(isEnable);
        editVehicleColor.setEnabled(isEnable);
        editVehicleBrand.setEnabled(isEnable);
        editVehicleModel.setEnabled(isEnable);

        if(isEnable){

            editVehiclePlate.addTextChangedListener(new ChangeText(R.id.edit_vehicle_plate));
            editVehicleColor.addTextChangedListener(new ChangeText(R.id.edit_vehicle_color));
            editVehicleBrand.addTextChangedListener(new ChangeText(R.id.edit_vehicle_brand));
            editVehicleModel.addTextChangedListener(new ChangeText(R.id.edit_vehicle_model));

        }

    }

    private void resultCallBack(DataFromPlate plateFormat, boolean offLine) {

        if (plateFormat == null) {
            Routine.showAlert(getResources().getString(R.string.no_result_returned), getActivity());
            return;
        }

        String state = plateFormat.getState().toUpperCase();
        String species = plateFormat.getSpecies().toUpperCase();
        String category = plateFormat.getCategory().toUpperCase();

        int selectionStatePlate = listStatePlate.indexOf(state);
        int selectionSpecies = listSpecies.indexOf(species);
        int selectionCategory = listCategory.indexOf(category);

        editVehicleRenavan.setText(plateFormat.getRenavam());
        editVehicleChassi.setText(plateFormat.getChassi());
        editVehicleBrand.setText(plateFormat.getBrand());
        editVehicleModel.setText(plateFormat.getModel());
        editVehicleColor.setText(plateFormat.getColor());

        spinnerPlateState.setSelection(selectionStatePlate + 1);
        spinnerSpecies.setSelection(selectionSpecies + 1);
        spinnerCategory.setSelection(selectionCategory + 1);

        Routine.closeKeyboard(editVehiclePlate, getActivity());
    }

    private void checkAitNumber() {

        String sAitNumber = (DatabaseCreator.getNumberDatabaseAdapter(getActivity()))
                .getAitNumber();

        if (sAitNumber == null) {
            dialogFragment = new AnyDialogFragment();
            dialogFragment.setTargetFragment(this, 0);
            bundle = new Bundle();
            bundle.putInt(AppConstants.DIALOG_TITLE_ID,
                    R.string.synchronization_screen_title);
            bundle.putInt(AppConstants.DIALOG_MGS_ID,
                    R.string.loading_ait);
            dialogFragment.setArguments(bundle);
            dialogFragment.setCancelable(false);
            dialogFragment
                    .show(getFragmentManager(), "dialog");
            //show(getActivity().getSupportFragmentManager(), null);
        } else {

            if(sAitNumberRetained != null){

                aitData.setAitNumber(sAitNumberRetained);
                tvIdAit.setText(getResources().getString(R.string.capital_number) + aitData.getAitNumber());

            } else {

                aitData.setAitNumber(sAitNumber);
                tvIdAit.setText(getResources().getString(R.string.capital_number) + aitData.getAitNumber());
                DatabaseCreator.getInfractionDatabaseAdapter(getActivity()).insertAitNumber(aitData);

            }

        }
    }

}