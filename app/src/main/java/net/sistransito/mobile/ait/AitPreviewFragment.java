package net.sistransito.mobile.ait;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.sistransito.mobile.util.User;

import net.sistransito.R;

public class AitPreviewFragment extends Fragment {

    private View view;
    private AitData aitData;
    private User user;

    public static AitPreviewFragment newInstance() {
        return new AitPreviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.auto_preview, null, false);

        getAitObject();

        return view;
    }

    private void getAitObject() {
        aitData = ObjectAit.getAitData();
        addListener();
    }

    public void addListener() {

        TextView tvCompanyCode, tvAitNumber, tvPlate, tvStateVehicle, tvChassi,
                 tvVehicleBrand, tvVehicleModel, tvConductorName, tvCnh, tvStateCnh,
                 tvVehicleSpecies, tvVehicleCategory, tvAddress, tvCity, tvAitDate, tvAitTime,
                 tvAitCode, tvLegalProtection, tvDescription, tMeasures, tvObservations, tvAgent,
                 tvRegistrationAgent;

        tvCompanyCode = (TextView) view.findViewById(R.id.tv_auto_orgao);
        tvAitNumber = (TextView) view.findViewById(R.id.tv_auto_numero_ait);
        tvPlate = (TextView) view.findViewById(R.id.tv_auto_placa);
        tvStateVehicle = (TextView) view.findViewById(R.id.tv_auto_uf_veiculo);
        tvChassi = (TextView) view.findViewById(R.id.tv_auto_chassi);
        tvVehicleBrand = (TextView) view.findViewById(R.id.tv_auto_marca);
        tvVehicleModel = (TextView) view.findViewById(R.id.tv_auto_modelo);
        tvVehicleSpecies = (TextView) view.findViewById(R.id.tv_auto_especie);
        tvVehicleCategory = (TextView) view.findViewById(R.id.tv_auto_categoria);
        tvConductorName = (TextView) view.findViewById(R.id.tv_auto_condutor);
        tvCnh = (TextView) view.findViewById(R.id.tv_auto_registro_cnh);
        tvStateCnh = (TextView) view.findViewById(R.id.tv_auto_uf_cnh);
        tvAddress = (TextView) view.findViewById(R.id.tv_auto_local);
        tvCity = (TextView) view.findViewById(R.id.tv_auto_municipio);
        tvAitDate = (TextView) view.findViewById(R.id.tv_auto_data);
        tvAitTime = (TextView) view.findViewById(R.id.tv_auto_hora);
        tMeasures = (TextView) view.findViewById(R.id.tv_auto_medidas);
        tvAitCode = (TextView) view.findViewById(R.id.tv_auto_codigo);
        tvLegalProtection = (TextView) view.findViewById(R.id.tv_auto_amparo);
        tvDescription = (TextView) view.findViewById(R.id.tv_auto_descricao);
        tvObservations  = (TextView) view.findViewById(R.id.tv_auto_obs);

       // tvCompanyCode.setText(user.getCodigoOrgao());
        tvAitNumber.setText(aitData.getAitNumber());
        tvPlate.setText(aitData.getPlate());
        tvStateVehicle.setText(aitData.getStateVehicle());
        tvChassi.setText(aitData.getChassi());
        tvVehicleBrand.setText(aitData.getVehicleBrand());
        tvVehicleModel.setText(aitData.getVehicleModel());
        tvVehicleSpecies.setText(aitData.getVehicleSpecies());
        tvVehicleCategory.setText(aitData.getVehicleCategory());
        tvConductorName.setText(aitData.getConductorName());
        tvCnh.setText(aitData.getCnhPpd());
        tvStateCnh.setText(aitData.getCnhState());
        tvAddress.setText(aitData.getAddress());
        tvCity.setText(aitData.getCity() + "/" + aitData.getState());
        tvAitDate.setText(aitData.getAitDate());
        tvAitTime.setText(aitData.getAitTime());
        tvAitCode.setText(aitData.getFramingCode() + "-" + aitData.getUnfolding());
        tvLegalProtection.setText(aitData.getArticle());
        tvDescription.setText(aitData.getInfraction());
        tvObservations.setText(aitData.getObservation());

        if(aitData.getRetreat() == null){
            tMeasures.setText(aitData.getProcedures());
        } else if (aitData.getProcedures() == null){
            tMeasures.setText(aitData.getRetreat());
        } else if (aitData.getProcedures() != null && aitData.getRetreat() != null){
            tMeasures.setText(aitData.getRetreat() + ", " + aitData.getProcedures());
        }

    }

}
