package net.sistransito.mobile.ait;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textview.MaterialTextView;

public class TabAitVehicleFragment extends DebugFragment implements
        AnyDialogListener {

    private PlateHttpResultAsyncTask httpResultAsyncTask;
    private View view;
    private MaterialAutoCompleteTextView spinner;
    private TextInputEditText editVehiclePlate, editVehicleBrand, editVehicleModel, editVehicleColor, editVehicleRenavan, editVehicleChassi;
    private TextView tvSearchPlate, tvIdAit;
    private FloatingActionButton tvSaveData; // Corrigido para FloatingActionButton
    private MaterialAutoCompleteTextView spinnerSpecies, spinnerCategory, spinnerCountry, spinnerPlateState;
    private List<String> listCategory, listSpecies, listCountry, listStatePlate;

    private AnyArrayAdapter<String> aaaCategory, aaaSpecies, aaaCountry, aaaStatePlate;

    private AitData aitData;
    private String sAitNumberRetained, searchType;
    private Bundle bundle;
    private AnyDialogFragment dialogFragment;
    private LinearLayout llVehicleState, llVehicleCountry;
    private MaterialCheckBox cbIfForeignVehicle, cbAitConfirm;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lo_vehicle_fragment, container, false);

        if (savedInstanceState != null) {
            sAitNumberRetained = savedInstanceState.getString("Ait_Number");
        }

        initializedView();
        getAitObject();
        checkAitNumber();
        return view;
    }

    private void initializedView() {
        tvSaveData = view.findViewById(R.id.ait_fab);
        tvIdAit = (TextView) view.findViewById(R.id.tv_ait_id);
        editVehiclePlate = (TextInputEditText) view.findViewById(R.id.edit_vehicle_plate);
        editVehicleChassi = (TextInputEditText) view.findViewById(R.id.edit_vehicle_chassi);
        editVehicleRenavan = (TextInputEditText) view.findViewById(R.id.edit_vehicle_renavan);
        editVehicleBrand = (TextInputEditText) view.findViewById(R.id.edit_vehicle_brand);
        editVehicleModel = (TextInputEditText) view.findViewById(R.id.edit_vehicle_model);
        editVehicleColor = (TextInputEditText) view.findViewById(R.id.edit_vehicle_color);

        // Verificar se as listas contêm dados
        listCategory = Arrays.asList(getResources().getStringArray(R.array.list_category));
        listCountry = Arrays.asList(getResources().getStringArray(R.array.filter_list_country));
        listSpecies = Arrays.asList(getResources().getStringArray(R.array.ait_species));
        listStatePlate = Arrays.asList(getResources().getStringArray(R.array.state_array));

        // Configurar adapters com layouts adequados para o item principal e o dropdown
        aaaCountry = new AnyArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, android.R.id.text1, listCountry);
        aaaCountry.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        aaaCategory = new AnyArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, android.R.id.text1, listCategory);
        aaaCategory.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        aaaSpecies = new AnyArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, android.R.id.text1, listSpecies);
        aaaSpecies.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        aaaStatePlate = new AnyArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, android.R.id.text1, listStatePlate);
        aaaStatePlate.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Inicializar os MaterialAutoCompleteTextView com configurações adicionais
        spinnerPlateState = view.findViewById(R.id.spinner_uf_veiculo);
        if (spinnerPlateState != null) {
            spinnerPlateState.setAdapter(aaaStatePlate);
            spinnerPlateState.setOnItemClickListener((parent, view1, position, id) -> {
                String selectedItem = (String) parent.getItemAtPosition(position);
                aitData.setStateVehicle(selectedItem);
                spinnerPlateState.setText(selectedItem, false);
            });
            // Garantir que o dropdown abra ao clicar
            spinnerPlateState.setOnClickListener(v -> {
                if (spinnerPlateState.getAdapter() != null && spinnerPlateState.getAdapter().getCount() > 0) {
                    spinnerPlateState.showDropDown();
                } else {
                    Log.w("TabAitVehicleFragment", "spinnerPlateState adapter is empty or null");
                }
            });
            spinnerPlateState.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerPlateState.showDropDown();
                }
                return false;
            });
            spinnerPlateState.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            spinnerPlateState.setThreshold(0); // Abre o dropdown mesmo sem digitar
            spinnerPlateState.setDropDownBackgroundResource(android.R.color.white);
        } else {
            Log.e("TabAitVehicleFragment", "spinnerPlateState is null");
        }

        spinnerCountry = view.findViewById(R.id.spinner_auto_pais);
        if (spinnerCountry != null) {
            spinnerCountry.setAdapter(aaaCountry);
            spinnerCountry.setOnItemClickListener((parent, view1, position, id) -> {
                String selectedItem = (String) parent.getItemAtPosition(position);
                aitData.setCountry(selectedItem);
                spinnerCountry.setText(selectedItem, false);
            });
            spinnerCountry.setOnClickListener(v -> {
                if (spinnerCountry.getAdapter() != null && spinnerCountry.getAdapter().getCount() > 0) {
                    spinnerCountry.showDropDown();
                } else {
                    Log.w("TabAitVehicleFragment", "spinnerCountry adapter is empty or null");
                }
            });
            spinnerCountry.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerCountry.showDropDown();
                }
                return false;
            });
            spinnerCountry.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            spinnerCountry.setThreshold(0);
            spinnerCountry.setDropDownBackgroundResource(android.R.color.white);
        } else {
            Log.e("TabAitVehicleFragment", "spinnerCountry is null");
        }

        spinnerSpecies = view.findViewById(R.id.spinner_auto_especie);
        if (spinnerSpecies != null) {
            spinnerSpecies.setAdapter(aaaSpecies);
            spinnerSpecies.setOnItemClickListener((parent, view1, position, id) -> {
                String selectedItem = (String) parent.getItemAtPosition(position);
                aitData.setVehicleSpecies(selectedItem);
                spinnerSpecies.setText(selectedItem, false);
            });
            spinnerSpecies.setOnClickListener(v -> {
                if (spinnerSpecies.getAdapter() != null && spinnerSpecies.getAdapter().getCount() > 0) {
                    spinnerSpecies.showDropDown();
                } else {
                    Log.w("TabAitVehicleFragment", "spinnerSpecies adapter is empty or null");
                }
            });
            spinnerSpecies.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerSpecies.showDropDown();
                }
                return false;
            });
            spinnerSpecies.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            spinnerSpecies.setThreshold(0);
            spinnerSpecies.setDropDownBackgroundResource(android.R.color.white);
        } else {
            Log.e("TabAitVehicleFragment", "spinnerSpecies is null");
        }

        spinnerCategory = view.findViewById(R.id.spinner_auto_categoria);
        if (spinnerCategory != null) {
            spinnerCategory.setAdapter(aaaCategory);
            spinnerCategory.setOnItemClickListener((parent, view1, position, id) -> {
                String selectedItem = (String) parent.getItemAtPosition(position);
                aitData.setVehicleCategory(selectedItem);
                spinnerCategory.setText(selectedItem, false);
            });
            spinnerCategory.setOnClickListener(v -> {
                if (spinnerCategory.getAdapter() != null && spinnerCategory.getAdapter().getCount() > 0) {
                    spinnerCategory.showDropDown();
                } else {
                    Log.w("TabAitVehicleFragment", "spinnerCategory adapter is empty or null");
                }
            });
            spinnerCategory.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerCategory.showDropDown();
                }
                return false;
            });
            spinnerCategory.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            spinnerCategory.setThreshold(0);
            spinnerCategory.setDropDownBackgroundResource(android.R.color.white);
        } else {
            Log.e("TabAitVehicleFragment", "spinnerCategory is null");
        }

        spinner = view.findViewById(R.id.spinner_auto_especie);

        llVehicleState = (LinearLayout) view.findViewById(R.id.ll_field_uf_veiculo);
        llVehicleCountry = (LinearLayout) view.findViewById(R.id.ll_field_pais_veiculo);
        cbIfForeignVehicle = (MaterialCheckBox) view.findViewById(R.id.cb_se_veiculo_estrangeiro);
        cbAitConfirm = (MaterialCheckBox) view.findViewById(R.id.cb_auto_confirmar);
        tvSearchPlate = (TextView) view.findViewById(R.id.edit_vehicle_plate);

        // Adicionar logs para depuração
        Log.d("TabAitVehicleFragment", "listStatePlate: " + listStatePlate);
        Log.d("TabAitVehicleFragment", "listCountry: " + listCountry);
        Log.d("TabAitVehicleFragment", "listSpecies: " + listSpecies);
        Log.d("TabAitVehicleFragment", "listCategory: " + listCategory);
    }

    private void addListener() {
        editVehicleRenavan.addTextChangedListener(new ChangeText(R.id.edit_vehicle_renavan));
        editVehicleChassi.addTextChangedListener(new ChangeText(R.id.edit_vehicle_chassi));

        cbIfForeignVehicle.setOnClickListener(v -> {
            if (cbIfForeignVehicle.isChecked()) {
                llVehicleState.setVisibility(View.GONE);
                llVehicleCountry.setVisibility(View.VISIBLE);
            } else {
                llVehicleState.setVisibility(View.VISIBLE);
                llVehicleCountry.setVisibility(View.GONE);
            }
        });

        cbAitConfirm.setOnClickListener(v -> {
            if (cbAitConfirm.isChecked()) {
                tvSaveData.setVisibility(View.VISIBLE);
                tvSaveData.show(); // Mostra o FAB (em vez de setVisibility)
                Routine.closeKeyboard(requireActivity().getCurrentFocus(), requireActivity());
            } else {
                tvSaveData.setVisibility(View.GONE);
                tvSaveData.hide(); // Esconde o FAB (em vez de setVisibility)
            }
        });

        tvSearchPlate.setOnClickListener(v -> {
            searchType = "plate";

            if (!editVehiclePlate.getText().toString().isEmpty()) {
                if (NetworkConnection.isNetworkAvailable(requireActivity())) {
                    httpResultAsyncTask = new PlateHttpResultAsyncTask(
                            new CallBackPlate() {
                                @Override
                                public void callBack(DataFromPlate dataFromPlate, boolean isOffline) {
                                    resultCallBack(dataFromPlate, isOffline);
                                }
                            }, requireActivity(), true, editVehiclePlate.getText().toString(), searchType, null);
                    httpResultAsyncTask.execute("");
                } else {
                    Routine.showAlert(getResources().getString(R.string.no_network_connection), requireActivity());
                }
            } else {
                Routine.showAlert(getResources().getString(R.string.plate_search_screen_title), requireActivity());
            }
        });

        tvSaveData.setOnClickListener(v -> {
            if (checkInput()) {
                if (!DatabaseCreator.getInfractionDatabaseAdapter(requireActivity()).insertAitData(aitData))
                    Routine.showAlert(getResources().getString(R.string.update_erro), requireActivity());

                ((AitActivity) requireActivity()).setTabActual(1);
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
        // Verificar e definir o texto para spinnerPlateState
        if (aitData.getStateVehicle() != null && !aitData.getStateVehicle().isEmpty()) {
            int selectionStateVehicle = listStatePlate.indexOf(aitData.getStateVehicle());
            if (selectionStateVehicle >= 0 && spinnerPlateState != null && spinnerPlateState.getAdapter() != null) {
                spinnerPlateState.setText(listStatePlate.get(selectionStateVehicle), false);
            } else {
                if (!listStatePlate.isEmpty()) {
                    spinnerPlateState.setText(listStatePlate.get(0), false);
                } else {
                    spinnerPlateState.setText("", false);
                }
                Log.w("TabAitVehicleFragment", "StateVehicle not found in listStatePlate or spinner not ready");
            }
        } else {
            if (!listStatePlate.isEmpty()) {
                spinnerPlateState.setText(listStatePlate.get(0), false);
            } else {
                spinnerPlateState.setText("", false);
            }
        }

        // Verificar e definir o texto para spinnerCountry
        if (aitData.getCountry() != null && !aitData.getCountry().isEmpty()) {
            int selectionCountry = listCountry.indexOf(aitData.getCountry());
            if (selectionCountry >= 0 && spinnerCountry != null && spinnerCountry.getAdapter() != null) {
                spinnerCountry.setText(listCountry.get(selectionCountry), false);
            } else {
                if (!listCountry.isEmpty()) {
                    spinnerCountry.setText(listCountry.get(0), false);
                } else {
                    spinnerCountry.setText("", false);
                }
                Log.w("TabAitVehicleFragment", "Country not found in listCountry or spinner not ready");
            }
        } else {
            if (!listCountry.isEmpty()) {
                spinnerCountry.setText(listCountry.get(0), false);
            } else {
                spinnerCountry.setText("", false);
            }
        }

        // Verificar e definir o texto para spinnerSpecies
        if (aitData.getVehicleSpecies() != null && !aitData.getVehicleSpecies().isEmpty()) {
            int selectionVehicleSpecies = listSpecies.indexOf(aitData.getVehicleSpecies());
            if (selectionVehicleSpecies >= 0 && spinnerSpecies != null && spinnerSpecies.getAdapter() != null) {
                spinnerSpecies.setText(listSpecies.get(selectionVehicleSpecies), false);
            } else {
                if (!listSpecies.isEmpty()) {
                    spinnerSpecies.setText(listSpecies.get(0), false);
                } else {
                    spinnerSpecies.setText("", false);
                }
                Log.w("TabAitVehicleFragment", "VehicleSpecies not found in listSpecies or spinner not ready");
            }
        } else {
            if (!listSpecies.isEmpty()) {
                spinnerSpecies.setText(listSpecies.get(0), false);
            } else {
                spinnerSpecies.setText("", false);
            }
        }

        // Verificar e definir o texto para spinnerCategory
        if (aitData.getVehicleCategory() != null && !aitData.getVehicleCategory().isEmpty()) {
            int selectionVehicleCategory = listCategory.indexOf(aitData.getVehicleCategory());
            if (selectionVehicleCategory >= 0 && spinnerCategory != null && spinnerCategory.getAdapter() != null) {
                spinnerCategory.setText(listCategory.get(selectionVehicleCategory), false);
            } else {
                if (!listCategory.isEmpty()) {
                    spinnerCategory.setText(listCategory.get(0), false);
                } else {
                    spinnerCategory.setText("", false);
                }
                Log.w("TabAitVehicleFragment", "VehicleCategory not found in listCategory or spinner not ready");
            }
        } else {
            if (!listCategory.isEmpty()) {
                spinnerCategory.setText(listCategory.get(0), false);
            } else {
                spinnerCategory.setText("", false);
            }
        }
    }

    private class ChangeText implements TextWatcher {
        private int id;

        public ChangeText(int id) {
            this.id = id;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !s.toString().isEmpty()) {
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
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
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
            }, requireActivity(), AppConstants.AIT_NUMBER)).execute("");
        } else {
            requireActivity().finish();
        }
    }

    private void setViewEnable(boolean isEnable) {
        editVehiclePlate.setEnabled(isEnable);
        editVehicleColor.setEnabled(isEnable);
        editVehicleBrand.setEnabled(isEnable);
        editVehicleModel.setEnabled(isEnable);

        if (isEnable) {
            editVehiclePlate.addTextChangedListener(new ChangeText(R.id.edit_vehicle_plate));
            editVehicleColor.addTextChangedListener(new ChangeText(R.id.edit_vehicle_color));
            editVehicleBrand.addTextChangedListener(new ChangeText(R.id.edit_vehicle_brand));
            editVehicleModel.addTextChangedListener(new ChangeText(R.id.edit_vehicle_model));
        }
    }

    private void resultCallBack(DataFromPlate plateFormat, boolean offLine) {
        if (plateFormat == null) {
            Routine.showAlert(getResources().getString(R.string.no_result_returned), requireActivity());
            return;
        }

        String state = plateFormat.getState() != null ? plateFormat.getState().toUpperCase() : "";
        String species = plateFormat.getSpecies() != null ? plateFormat.getSpecies().toUpperCase() : "";
        String category = plateFormat.getCategory() != null ? plateFormat.getCategory().toUpperCase() : "";

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

        Routine.closeKeyboard(editVehiclePlate, requireActivity());
    }

    private void checkAitNumber() {
        String sAitNumber = DatabaseCreator.getNumberDatabaseAdapter(requireActivity()).getAitNumber();

        if (sAitNumber == null) {
            dialogFragment = new AnyDialogFragment();
            dialogFragment.setTargetFragment(this, 0);
            bundle = new Bundle();
            bundle.putInt(AppConstants.DIALOG_TITLE_ID, R.string.synchronization_screen_title);
            bundle.putInt(AppConstants.DIALOG_MGS_ID, R.string.loading_ait);
            dialogFragment.setArguments(bundle);
            dialogFragment.setCancelable(false);
            dialogFragment.show(requireActivity().getSupportFragmentManager(), "dialog");
        } else {
            if (sAitNumberRetained != null) {
                aitData.setAitNumber(sAitNumberRetained);
                tvIdAit.setText(getResources().getString(R.string.capital_number) + aitData.getAitNumber());
            } else {
                aitData.setAitNumber(sAitNumber);
                tvIdAit.setText(getResources().getString(R.string.capital_number) + aitData.getAitNumber());
                DatabaseCreator.getInfractionDatabaseAdapter(requireActivity()).insertAitNumber(aitData);
            }
        }
    }

}