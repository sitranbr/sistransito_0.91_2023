package net.sistransito.mobile.ait;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.appobject.AppObject;
import net.sistransito.mobile.utility.Routine;
import net.sistransito.mobile.utility.User;

import net.sistransito.R;

public class AitPreviewFragment extends Fragment {

    private View view;
    private AitData aitData;
    private User user;

    private Context context;

    public static AitPreviewFragment newInstance() {
        return new AitPreviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ait_preview, null, false);
        context = getContext();
        getAitObject();

        return view;
    }

    private void getAitObject() {
        aitData = AitObject.getAitData();
        addListener();
    }

    public void addListener() {

        TextView tvCompanyCode, tvAitNumber, tvPlate, tvStateVehicle, tvChassi,
                 tvVehicleBrand, tvVehicleModel, tvConductorName, tvCnh, tvStateCnh,
                 tvVehicleSpecies, tvVehicleCategory, tvAddress, tvCityCode, tvCity, tvAitDate, tvAitTime,
                 tvAitCode, tvLegalSupport, tvDescription, tvMeasuresAndProcedures, tvObservations, tvAgent,
                 tvRegistrationAgent, tvApproach;

        tvCompanyCode = (TextView) view.findViewById(R.id.tv_ait_organ);
        tvAitNumber = (TextView) view.findViewById(R.id.tv_ait_number);
        tvPlate = (TextView) view.findViewById(R.id.tv_ait_plate);
        tvStateVehicle = (TextView) view.findViewById(R.id.tv_ait_vehicle_state);
        tvChassi = (TextView) view.findViewById(R.id.tv_ait_chassi);
        tvVehicleBrand = (TextView) view.findViewById(R.id.tv_ait_brand);
        tvVehicleModel = (TextView) view.findViewById(R.id.tv_ait_model);
        tvVehicleSpecies = (TextView) view.findViewById(R.id.tv_ait_specie);
        tvVehicleCategory = (TextView) view.findViewById(R.id.tv_ait_category);
        tvConductorName = (TextView) view.findViewById(R.id.tv_ait_driver_name);
        tvCnh = (TextView) view.findViewById(R.id.tv_ait_cnh_number);
        tvStateCnh = (TextView) view.findViewById(R.id.tv_ait_cnh_state);
        tvAddress = (TextView) view.findViewById(R.id.tv_ait_address);
        tvCityCode = (TextView) view.findViewById(R.id.tv_ait_city_code);
        tvCity = (TextView) view.findViewById(R.id.tv_ait_city);
        tvAitDate = (TextView) view.findViewById(R.id.tv_ait_date);
        tvAitTime = (TextView) view.findViewById(R.id.tv_ait_time);
        tvMeasuresAndProcedures = (TextView) view.findViewById(R.id.tv_ait_measures_procedures);
        tvAitCode = (TextView) view.findViewById(R.id.tv_ait_code);
        tvLegalSupport = (TextView) view.findViewById(R.id.tv_ait_legal_support);
        tvDescription = (TextView) view.findViewById(R.id.tv_ait_description);
        tvObservations  = (TextView) view.findViewById(R.id.tv_ait_observation);
        tvApproach = (TextView) view.findViewById(R.id.tv_ait_approach);

        Routine.TextAlignment normal = Routine.TextAlignment.NORMAL;
        Routine.TextAlignment center = Routine.TextAlignment.CENTER;

        user = AppObject.getTinyDB(context).getObject(AppConstants.user, User.class);

        SpannableString boldCompanyCode = Routine.textWithBoldAndCenter(getString(R.string.capital_transit_agency_code), user.getCompanyCode(), false, center);
        SpannableString boldAit = Routine.textWithBoldAndCenter(getString(R.string.capital_number), aitData.getAitNumber(), false, center);
        SpannableString boldPlate = Routine.textWithBoldAndCenter(getString(R.string.capital_vehicle_plate), aitData.getPlate(), false, center);

        SpannableString boldStateVehicle = Routine.textWithBoldAndCenter(getString(R.string.capital_cnh_state), aitData.getStateVehicle(), false, center);
        SpannableString boldChassi = Routine.textWithBoldAndCenter(getString(R.string.capital_chassi), aitData.getChassi(), false, center);
        SpannableString boldVehicleBrand = Routine.textWithBoldAndCenter(getString(R.string.capital_brand), aitData.getVehicleBrand(), false, center);
        SpannableString boldVehicleModel = Routine.textWithBoldAndCenter(getString(R.string.capital_model), aitData.getVehicleModel(), false, center);
        SpannableString boldVehicleSpecies = Routine.textWithBoldAndCenter(getString(R.string.capital_specie), aitData.getVehicleSpecies(), false, center);
        SpannableString boldVehicleCategory = Routine.textWithBoldAndCenter(getString(R.string.capital_category), aitData.getVehicleCategory(), false, center);
        SpannableString boldConductorName = Routine.textWithBoldAndCenter(getString(R.string.capital_driver_name), aitData.getConductorName(), false, normal);

        tvCompanyCode.setText(boldCompanyCode);
        tvAitNumber.setText(boldAit);
        tvPlate.setText(boldPlate);
        tvStateVehicle.setText(boldStateVehicle);
        tvChassi.setText(boldChassi);
        tvVehicleBrand.setText(boldVehicleBrand);
        tvVehicleModel.setText(boldVehicleModel);
        tvVehicleSpecies.setText(boldVehicleSpecies);
        tvVehicleCategory.setText(boldVehicleCategory);
        tvConductorName.setText(boldConductorName);
        tvCnh.setText(aitData.getCnhPpd());
        tvStateCnh.setText(aitData.getCnhState());
        tvAddress.setText(aitData.getAddress());
        tvCityCode.setText(aitData.getCityCode());
        tvCity.setText(aitData.getCity() + "/" + aitData.getState());
        tvAitDate.setText(aitData.getAitDate());
        tvAitTime.setText(aitData.getAitTime());
        tvAitCode.setText(aitData.getFramingCode() + "-" + aitData.getUnfolding());
        tvLegalSupport.setText(aitData.getArticle());
        tvDescription.setText(aitData.getInfraction());
        tvObservations.setText(aitData.getObservation());
        tvApproach.setText(aitData.getApproach());

        /*if(aitData.getRetreat() == null) {
            tMeasures.setText(aitData.getProcedures());
        } else if (aitData.getProcedures() == null) {
            tMeasures.setText(aitData.getRetreat());
        } else if (aitData.getProcedures() != null && aitData.getRetreat() != null) {
            tMeasures.setText(aitData.getRetreat() + ", " + aitData.getProcedures());
        }*/

        String retreat = aitData.getRetreat();
        String procedures = aitData.getProcedures();

        String measures = (retreat == null) ? procedures :
                (procedures == null) ? retreat :
                        retreat + ", " + procedures;

        tvMeasuresAndProcedures.setText(measures);

    }

}
