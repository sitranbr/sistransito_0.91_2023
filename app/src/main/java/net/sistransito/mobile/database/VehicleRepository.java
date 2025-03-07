package net.sistransito.mobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import net.sistransito.R;
import net.sistransito.mobile.plate.data.DataFromPlate;

import java.util.StringTokenizer;

public class VehicleRepository {

    private static final String TAG = "VehicleRepository";
    private static final String TABLE_NAME = "VEICULO_RENAVAM";
    private static final String PLATE = "PLACA";
    private static final String CHASSIS = "CHASSI";
    private static final String VIS = "VIS";
    private static final String STATE = "ID_ESTADO_VEICULO";
    private static final String CITY = "ID_MUNICIPIO";
    private static final String RENAVAM = "RENAVAM";
    private static final String IDENTITY_OWNER = "NUMERO_IDENT_PROPRIETARIO";
    private static final String VEHICLE_OWNER = "NM_PROPRIETARIO";
    private static final String MODEL = "NM_MARCA_MODELO";
    private static final String COLOR = "COR";
    private static final String SPECIES = "ID_ESPECIE";
    private static final String TYPE = "TIPO_VEICULO";
    private static final String CATEGORY = "CATEGORIA";
    private static final String LICENSING_YEAR = "ANO_LICENCIADO";
    private static final String LICENSING_DATE = "DTH_ULT_LICENC";
    private static final String MODEL_YEAR = "ANO_MODELO";
    private static final String YEAR_MANUFACTURE = "ANO_FABRICACAO";
    private static final String LICENSING_STATUS = "INDICADOR_LICENCIAMENTO";
    private static final String RETENTION_INDICATOR = "INDICADOR_RETENCAO_PARQUE";
    private static final String IMPEDIMENT_INDICATOR = "INDICADOR_IMPEDIMENTO_JUDICIAL_ADMIN";
    private static final String OCCURRENCE_INDICATOR = "INDICADOR_OCORRENCIA_POLICIAL";
    private static final String NUMBER_SEAL = "NUMERO_LACRE";
    private static final String NUMBER_ENGINER = "NUMERO_MOTOR";
    private static final String DOUBLE = "DUBLE";

    private final DatabaseHelper dbHelper;
    private final Context context;

    public VehicleRepository(DatabaseHelper dbHelper, Context context) {
        this.dbHelper = dbHelper;
        this.context = context;
    }

    public DataFromPlate getPlateData(String sPlate, String sType) {
        DataFromPlate dataPlate = new DataFromPlate(context);
        Cursor myCursor = null;
        String fieldToSearch = getFieldToSearch(sType);
        String indexedField = getIndexedField(sType);
        String query = getQuery(fieldToSearch, indexedField, sPlate, sType);

        try {
            myCursor = dbHelper.openDatabase().rawQuery(query, sType.equals("vis") ? null : new String[]{sPlate}, null);
            if (myCursor != null && myCursor.getCount() > 0) {
                myCursor.moveToFirst();
                dataPlate = populateDataFromPlate(myCursor);
                myCursor.close();
            } else {
                if (myCursor != null) myCursor.close();
                return null;
            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error querying database", e);
            showErrorToast("Error accessing the database. Please check if the table VEICULO_RENAVAM exists.");
        }

        return dataPlate;
    }

    private String getFieldToSearch(String sType) {
        switch (sType) {
            case "plate":
                return PLATE;
            case "chassi":
                return CHASSIS;
            case "seal":
                return NUMBER_SEAL;
            case "engine":
                return NUMBER_ENGINER;
            case "vis":
                return VIS;
            default:
                throw new IllegalArgumentException("Invalid search type");
        }
    }

    private String getIndexedField(String sType) {
        switch (sType) {
            case "plate":
                return "plate";
            case "chassi":
                return "chassi";
            case "seal":
                return "seal";
            case "engine":
                return "engine";
            case "vis":
                return "vis";
            default:
                throw new IllegalArgumentException("Invalid search type");
        }
    }

    private String getQuery(String fieldToSearch, String indexedField, String sPlate, String sType) {
        if (sType.equals("vis")) {
            return "SELECT * FROM " + TABLE_NAME + " INDEXED BY " + indexedField + " WHERE " + fieldToSearch + " LIKE '%" + sPlate + "%'";
        } else {
            return "SELECT * FROM " + TABLE_NAME + " INDEXED BY " + indexedField + " WHERE " + fieldToSearch + "=?";
        }
    }

    private DataFromPlate populateDataFromPlate(Cursor cursor) {
        DataFromPlate dataPlate = new DataFromPlate(context);

        dataPlate.setPlate(getColumnValue(cursor, PLATE));
        dataPlate.setState(filter(getColumnValue(cursor, STATE), STATE));
        dataPlate.setCity(getCityName(getColumnValue(cursor, CITY)));
        dataPlate.setChassi(getColumnValue(cursor, CHASSIS));
        dataPlate.setRenavam(getColumnValue(cursor, RENAVAM));

        StringTokenizer tokens = new StringTokenizer(getColumnValue(cursor, MODEL), "/");
        if (tokens.countTokens() >= 2) {
            dataPlate.setBrand(tokens.nextToken());
            dataPlate.setModel(tokens.nextToken());
        }

        dataPlate.setColor(filter(getColumnValue(cursor, COLOR), COLOR));
        dataPlate.setSpecies(filter(getColumnValue(cursor, SPECIES), SPECIES));
        dataPlate.setType(filter(getColumnValue(cursor, TYPE), TYPE));
        dataPlate.setCategory(filter(getColumnValue(cursor, CATEGORY), CATEGORY));
        dataPlate.setOwnerId(getColumnValue(cursor, IDENTITY_OWNER));
        dataPlate.setOwnerName(getColumnValue(cursor, VEHICLE_OWNER));
        dataPlate.setLicenceYear(getColumnValue(cursor, LICENSING_YEAR));
        dataPlate.setLicenceData(getColumnValue(cursor, LICENSING_DATE));
        dataPlate.setLicenceStatus(filter(getColumnValue(cursor, LICENSING_STATUS), LICENSING_STATUS));
        dataPlate.setYearModel(getColumnValue(cursor, MODEL_YEAR));
        dataPlate.setYearManufacture(getColumnValue(cursor, YEAR_MANUFACTURE));
        dataPlate.setRetentionIndication(getColumnValue(cursor, RETENTION_INDICATOR));
        dataPlate.setDuble(getColumnValue(cursor, DOUBLE));
        dataPlate.setSealNumber(getColumnValue(cursor, NUMBER_SEAL));
        dataPlate.setEngineNumber(getColumnValue(cursor, NUMBER_ENGINER));
        dataPlate.setOffSideRecord(filter(getColumnValue(cursor, IMPEDIMENT_INDICATOR), IMPEDIMENT_INDICATOR));
        dataPlate.setTheftRecord(filter(getColumnValue(cursor, OCCURRENCE_INDICATOR), OCCURRENCE_INDICATOR));

        return dataPlate;
    }

    private String getColumnValue(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex >= 0 && !cursor.isNull(columnIndex)) {
            return cursor.getString(columnIndex);
        } else {
            Log.e(TAG, "Column " + columnName + " not found or null");
            return null;
        }
    }

    private String getCityName(String id) {
        PrepopulatedDBOpenHelper database = new PrepopulatedDBOpenHelper(context);
        try {
            return database.getCityNameCursor(id);
        } catch (ArrayIndexOutOfBoundsException exception) {
            Log.e(TAG, "Error city: " + exception.getMessage());
            return null;
        }
    }

    private String filter(String filterData, String xml) {
        try {
            int index = Integer.parseInt(filterData);
            String[] listXml = getXmlArray(xml);
            return listXml[index];
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException exception) {
            Log.d("Error: ", String.valueOf(exception));
            return null;
        }
    }

    private String[] getXmlArray(String xml) {
        switch (xml) {
            case LICENSING_STATUS:
                return context.getResources().getStringArray(R.array.filter_vehicle_licensing_status);
            case TYPE:
                return context.getResources().getStringArray(R.array.filter_vehicle_type);
            case SPECIES:
                return context.getResources().getStringArray(R.array.ait_species);
            case COLOR:
                return context.getResources().getStringArray(R.array.filter_color);
            case STATE:
                return context.getResources().getStringArray(R.array.state_array);
            case IMPEDIMENT_INDICATOR:
            case OCCURRENCE_INDICATOR:
                return context.getResources().getStringArray(R.array.filter_theft_occurrence);
            case CATEGORY:
                return context.getResources().getStringArray(R.array.list_category);
            default:
                throw new IllegalArgumentException("Invalid xml type");
        }
    }

    private void showErrorToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
