package net.sistransito.mobile.database;

import android.content.Context;
import net.sistransito.mobile.cnh.dados.DataFromCnh;
import net.sistransito.mobile.plate.data.DataFromPlate;

public class SearchDataInCard {

    private final VehicleRepository vehicleRepository;
    private final CnhRepository cnhRepository;
    private final Utility utility;

    public SearchDataInCard(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        this.vehicleRepository = new VehicleRepository(dbHelper, context);
        this.cnhRepository = new CnhRepository(dbHelper, context);
        this.utility = new Utility(context);
    }

    public DataFromPlate getPlateData(String plate, String type) {
        return vehicleRepository.getPlateData(plate, type);
    }

    public DataFromCnh getCnhData(DataFromCnh fromCNH, String typeSearch) {
        return cnhRepository.getCnhData(fromCNH, typeSearch);
    }

    public String filterData(String data, String xml) {
        return utility.filter(data, xml);
    }
}
