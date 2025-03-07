package net.sistransito.mobile.sync;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Environment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import net.sistransito.mobile.ait.AitData;
import net.sistransito.mobile.database.InfractionDatabaseHelper;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.http.WebClient;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SyncFiles {

    private SQLiteDatabase database;
    private InfractionDatabaseHelper databaseHelper;

    StringRequest stringRequest;
    RequestQueue requestQueue;

    private Context context;

    public SyncFiles(Context context) {
        databaseHelper = new InfractionDatabaseHelper(context);
        //database = databaseHelper.getReadableDatabase(ime.getIME());
        database = databaseHelper.getReadableDatabase();
        this.context = context;
    }

    public void sendAitData(final String ait){

        requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        stringRequest = new StringRequest(Request.Method.POST, WebClient.URL_REQUEST_SSL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            String isSucess = jsonObject.getString("success");

                            if(isSucess.equals("true")){
                                Log.v("Response: ", String.valueOf(isSucess));
                                DatabaseCreator.getInfractionDatabaseAdapter(context).synchronizeAit(ait);
                            }else{
                                Log.v("Response: ", String.valueOf(isSucess));
                            }

                        }catch (Exception e){
                            Log.v("Erro: ", e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Error 109: ", error.getMessage());
                    }
                }) {
                @Override
                protected Map<String, String> getParams(){

                    String stringImage1, stringImage2, stringImage3, stringImage4;

                    String root = Environment.getExternalStorageDirectory().toString() + "/" + context.getApplicationContext().getString(R.string.folder_app) + "/";
                    int lengthRoot = root.length();

                    AitData aitData = DatabaseCreator
                            .getInfractionDatabaseAdapter(context.getApplicationContext())
                            .getDataFromAitNumber(ait);

                    Map<String, String> params = new HashMap<>();

                    if (aitData.getPhoto1() != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(aitData.getPhoto1());
                        stringImage1 = Routine.getStringImage(bitmap);
                        params.put("foto1", aitData.getPhoto1().substring(lengthRoot));
                        params.put("image1", stringImage1);
                    } else if (aitData.getPhoto2() != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(aitData.getPhoto2());
                        stringImage2 = Routine.getStringImage(bitmap);
                        params.put("foto2", aitData.getPhoto2().substring(lengthRoot));
                        params.put("image2", stringImage2);
                    } else if (aitData.getPhoto3() != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(aitData.getPhoto3());
                        stringImage3 = Routine.getStringImage(bitmap);
                        params.put("foto3", aitData.getPhoto3().substring(lengthRoot));
                        params.put("image3", stringImage3);
                    } else if (aitData.getPhoto4() != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(aitData.getPhoto4());
                        stringImage4 = Routine.getStringImage(bitmap);
                        params.put("foto4", aitData.getPhoto4().substring(lengthRoot));
                        params.put("image4", stringImage4);
                    }

                    params.put(InfractionDatabaseHelper.AIT_NUMBER, aitData.getAitNumber());
                    params.put(InfractionDatabaseHelper.PLATE, aitData.getPlate());
                    params.put(InfractionDatabaseHelper.VEHICLE_STATE, aitData.getStateVehicle());
                    params.put(InfractionDatabaseHelper.RENAVAM, aitData.getRenavam());
                    params.put(InfractionDatabaseHelper.CHASSI, aitData.getChassi());
                    params.put(InfractionDatabaseHelper.COUNTRY, aitData.getCountry());
                    params.put(InfractionDatabaseHelper.VEHICLE_MODEL, aitData.getVehicleModel());
                    params.put(InfractionDatabaseHelper.VEHICLE_COLOR, aitData.getVehycleColor());
                    params.put(InfractionDatabaseHelper.SPECIES, aitData.getVehicleSpecies());
                    params.put(InfractionDatabaseHelper.CATEGORY, aitData.getVehicleCategory());
                    params.put(InfractionDatabaseHelper.CANCEL_STATUS, aitData.getCancellationStatus());
                    params.put(InfractionDatabaseHelper.REASON_FOR_CANCEL, aitData.getReasonForCancellation());
                    params.put(InfractionDatabaseHelper.SYNC_STATUS, aitData.getSyncStatus());
                    params.put(InfractionDatabaseHelper.AIT_LATITUDE, aitData.getAitLatitude());
                    params.put(InfractionDatabaseHelper.AIT_LONGITUDE, aitData.getAitLongitude());
                    params.put(InfractionDatabaseHelper.AIT_DATE_TIME, aitData.getAitDateTime());
                    params.put(InfractionDatabaseHelper.COMPLETED_STATUS, aitData.getCompletedStatus());
                    params.put(InfractionDatabaseHelper.APPROACH, aitData.getApproach());
                    params.put(InfractionDatabaseHelper.DRIVER_NAME, aitData.getConductorName());
                    params.put(InfractionDatabaseHelper.DRIVER_LICENSE, aitData.getCnhPpd());
                    params.put(InfractionDatabaseHelper.DRIVER_LICENSE_STATE, aitData.getCnhState());
                    params.put(InfractionDatabaseHelper.DOCUMENT_TYPE, aitData.getDocumentType());
                    params.put(InfractionDatabaseHelper.DOCUMENT_NUMBER, aitData.getDocumentNumber());
                    params.put(InfractionDatabaseHelper.ADDRESS, aitData.getAddress());
                    params.put(InfractionDatabaseHelper.AIT_DATE, aitData.getAitDate());
                    params.put(InfractionDatabaseHelper.AIT_TIME, aitData.getAitTime());
                    params.put(InfractionDatabaseHelper.CITY_CODE, aitData.getCityCode());
                    params.put(InfractionDatabaseHelper.CITY, aitData.getCity());
                    params.put(InfractionDatabaseHelper.STATE, aitData.getState());
                    params.put(InfractionDatabaseHelper.INFRACTION, aitData.getInfraction());
                    params.put(InfractionDatabaseHelper.FLAMING_CODE, aitData.getFramingCode());
                    params.put(InfractionDatabaseHelper.UNFOLDING, aitData.getUnfolding());
                    params.put(InfractionDatabaseHelper.ARTICLE, aitData.getArticle());
                    params.put(InfractionDatabaseHelper.TCA_NUMBER, aitData.getTcaNumber());
                    params.put(InfractionDatabaseHelper.EQUIPMENT_DESCRIPTION, aitData.getDescription());
                    params.put(InfractionDatabaseHelper.EQUIPMENT_BRAND, aitData.getEquipmentBrand());
                    params.put(InfractionDatabaseHelper.EQUIPMENT_MODEL, aitData.getEquipmentModel());
                    params.put(InfractionDatabaseHelper.EQUIPMENT_SERIAL, aitData.getSerialNumber());
                    params.put(InfractionDatabaseHelper.MEASUREMENT_PERFORMED, aitData.getMeasurementPerformed());
                    params.put(InfractionDatabaseHelper.REGULATED_VALUE, aitData.getRegulatedValue());
                    params.put(InfractionDatabaseHelper.VALUE_CONSIDERED, aitData.getValueConsidered());
                    params.put(InfractionDatabaseHelper.ALCOHOL_TEST_NUMBER, aitData.getAlcoholTestNumber());
                    params.put(InfractionDatabaseHelper.RETREAT, aitData.getRetreat());
                    params.put(InfractionDatabaseHelper.PROCEDURES, aitData.getProcedures());
                    params.put(InfractionDatabaseHelper.OBSERVATION, aitData.getObservation());
                    params.put(InfractionDatabaseHelper.SHIPPER_IDENTIFICATION, aitData.getShipperIdentification());
                    params.put(InfractionDatabaseHelper.CPF_SHIPPER, aitData.getCpfShipper());
                    params.put(InfractionDatabaseHelper.CNPJ_SHIPPER, aitData.getCnpShipper());
                    params.put(InfractionDatabaseHelper.CARRIER_IDENTIFICATION, aitData.getCarrierIdentification());
                    params.put(InfractionDatabaseHelper.CPF_CARRIER, aitData.getCpfCarrier());
                    params.put(InfractionDatabaseHelper.CNPJ_CARRIER, aitData.getCnpjCarrier());
                    params.put(InfractionDatabaseHelper.COMPLETED_STATUS, aitData.getCompletedStatus());

                    //Log.d("Params", String.valueOf(params));

                    return params;

                }

        };

        requestQueue.add(stringRequest);

    }

    public void sendAitDataJson(final String ait) {

        requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        stringRequest = new StringRequest(Request.Method.POST, WebClient.URL_REQUEST_SSL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String isSucess = jsonObject.getString("success");

                            if (isSucess.equals("true")) {
                                Log.v("Response: ", String.valueOf(isSucess));
                                DatabaseCreator.getInfractionDatabaseAdapter(context).synchronizeAit(ait);
                            } else {
                                Log.v("Response: ", String.valueOf(isSucess));
                            }

                        } catch (Exception e) {
                            Log.v("Erro: ", e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Error 109: ", error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                String root = Environment.getExternalStorageDirectory().toString() + "/" + context.getApplicationContext().getString(R.string.folder_app) + "/";
                int lengthRoot = root.length();

                AitData aitData = DatabaseCreator
                        .getInfractionDatabaseAdapter(context.getApplicationContext())
                        .getDataFromAitNumber(ait);

                Map<String, String> params = new HashMap<>();

                JSONObject aitDataJson = new JSONObject();
                JSONArray photosJsonArray = new JSONArray();

                try {
                    // Incluir todos os campos da infração no objeto JSON aitDataJson
                    aitDataJson.put(InfractionDatabaseHelper.AIT_NUMBER, aitData.getAitNumber());
                    aitDataJson.put(InfractionDatabaseHelper.PLATE, aitData.getPlate());
                    // Outros campos da infração

                    // Adicionar fotos ao JSONArray
                    for (int i = 1; i <= 4; i++) {
                        String photoPath = (String) AitData.class.getMethod("getPhoto" + i).invoke(aitData);
                        if (photoPath != null) {
                            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                            String base64Image = Routine.getStringImage(bitmap);

                            JSONObject photoJson = new JSONObject();
                            photoJson.put("name", "foto" + i + ".jpg");
                            photoJson.put("data", base64Image);

                            photosJsonArray.put(photoJson);
                        }
                    }

                    // Adicionar aitDataJson e photosJsonArray ao objeto params
                    params.put("aitdata", aitDataJson.toString());
                    params.put("photos", photosJsonArray.toString());

                } catch (Exception e) {
                    Log.v("Erro: ", e.getMessage());
                }

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

}
