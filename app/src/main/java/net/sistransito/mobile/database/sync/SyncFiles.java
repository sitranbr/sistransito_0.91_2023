package net.sistransito.mobile.database.sync;

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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import net.sistransito.mobile.ait.AitData;
import net.sistransito.mobile.database.InfractionDatabaseHelper;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.http.WebClient;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

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

    public void sendCanceledAit(final String ait){

        AitData aitData = DatabaseCreator
                .getInfractionDatabaseAdapter(context.getApplicationContext())
                .getDataFromAitNumber(ait);

            Ion.with(context)
                .load(WebClient.URL_REQUEST_CANCEL)
                .setMultipartParameter(InfractionDatabaseHelper.AIT_NUMBER, aitData.getAitNumber())
                .setMultipartParameter(InfractionDatabaseHelper.CANCEL_STATUS, aitData.getCancellationStatus())
                .setMultipartParameter(InfractionDatabaseHelper.REASON_FOR_CANCEL, aitData.getReasonForCancellation())
                .setMultipartParameter(InfractionDatabaseHelper.COMPLETED_STATUS, aitData.getCompletedStatus())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(result.get("success").getAsString().equals("true")) {
                            Log.d("Success: ", "true");
                            (DatabaseCreator.getInfractionDatabaseAdapter(context))
                                    .synchronizeAit(ait);
                        }else{
                            Log.d("Success: ", "NO");
                        }
                    }
                });

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
                    params.put(InfractionDatabaseHelper.RENAVAN, aitData.getRenavam());
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
                    params.put(InfractionDatabaseHelper.INFRATION, aitData.getInfraction());
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

    /*params.put(AutoInfracaoDatabaseHelper.NUMERO_AUTO, dadosAuto.getNumeroAuto());
    params.put(AutoInfracaoDatabaseHelper.PLACA, dadosAuto.getPlaca());
    params.put(AutoInfracaoDatabaseHelper.UF_VEICULO, dadosAuto.getUfVeiculo());
    params.put(AutoInfracaoDatabaseHelper.CHASSI, dadosAuto.getChassi());
    params.put(AutoInfracaoDatabaseHelper.PAIS, dadosAuto.getPais());
    params.put(AutoInfracaoDatabaseHelper.MODELO_DO_VEICULO, dadosAuto.getModeloVeiculo());
    params.put(AutoInfracaoDatabaseHelper.COR_DO_VEICULO, dadosAuto.getCorVeiculo());
    params.put(AutoInfracaoDatabaseHelper.ESPECIE, dadosAuto.getEspecie());
    params.put(AutoInfracaoDatabaseHelper.CATEGORIA, dadosAuto.getCategoria());
    params.put(AutoInfracaoDatabaseHelper.STATUS_CANCELAMENTO, dadosAuto.getStatusCancelameto());
    params.put(AutoInfracaoDatabaseHelper.MOTIVO_CANCELAMENTO, dadosAuto.getMotivoCancelamento());
    params.put(AutoInfracaoDatabaseHelper.STATUS_SINCRONIZACAO, dadosAuto.getStatusSincronizacao());
    params.put(AutoInfracaoDatabaseHelper.LATITUDE_AUTO, dadosAuto.getLatitudeAuto());
    params.put(AutoInfracaoDatabaseHelper.LONGITUDE_AUTO, dadosAuto.getLongitudeAuto());
    params.put(AutoInfracaoDatabaseHelper.DATA_HORA_AUTO, dadosAuto.getDataHoraAuto());
    params.put(AutoInfracaoDatabaseHelper.STATUS_CONCLUIDO, dadosAuto.getStatusConcluido());
    params.put(AutoInfracaoDatabaseHelper.NOME_DO_CONDUTOR, dadosAuto.getNomeCondutor());
    params.put(AutoInfracaoDatabaseHelper.CNH_PPD, dadosAuto.getCnhPpd());
    params.put(AutoInfracaoDatabaseHelper.UF_CNH, dadosAuto.getUfCnh());
    params.put(AutoInfracaoDatabaseHelper.TIPO_DE_DOCUMENTO, dadosAuto.getTipoDocumento());
    params.put(AutoInfracaoDatabaseHelper.NUMERO_DOCUMENTO, dadosAuto.getNumeroDocumento());
    params.put(AutoInfracaoDatabaseHelper.LOCAL, dadosAuto.getLocal());
    params.put(AutoInfracaoDatabaseHelper.DATA, dadosAuto.getData());
    params.put(AutoInfracaoDatabaseHelper.HORA, dadosAuto.getHora());
    params.put(AutoInfracaoDatabaseHelper.CODIGO_DO_MUNICIPIO, dadosAuto.getCodigo_do_municipio());
    params.put(AutoInfracaoDatabaseHelper.MUNICIPIO, dadosAuto.getMunicipio());
    params.put(AutoInfracaoDatabaseHelper.UF, dadosAuto.getUf());
    params.put(AutoInfracaoDatabaseHelper.INFRACAO, dadosAuto.getInfracao());
    params.put(AutoInfracaoDatabaseHelper.ENQUADRA, dadosAuto.getEnquadra());
    params.put(AutoInfracaoDatabaseHelper.DESDOB, dadosAuto.getDesdob());
    params.put(AutoInfracaoDatabaseHelper.ART, dadosAuto.getAmparoLegal());
    params.put(AutoInfracaoDatabaseHelper.NUMERO_TCA, dadosAuto.getNumeroTca());
    params.put(AutoInfracaoDatabaseHelper.DESCRICAO, dadosAuto.getDescricao());
    params.put(AutoInfracaoDatabaseHelper.MARCA, dadosAuto.getMarcaDoEquipamento());
    params.put(AutoInfracaoDatabaseHelper.MODELO, dadosAuto.getModeloDoEquipamento());
    params.put(AutoInfracaoDatabaseHelper.NUMERO_DE_SERIE, dadosAuto.getNumeroDeSerie());
    params.put(AutoInfracaoDatabaseHelper.MEDICAO_REALIZADA, dadosAuto.getMedicaoRealizada());
    params.put(AutoInfracaoDatabaseHelper.VALOR_REGULAMENTADO, dadosAuto.getValorRegulamentado());
    params.put(AutoInfracaoDatabaseHelper.VALOR_CONSIDERADA, dadosAuto.getValorConsiderada());
    params.put(AutoInfracaoDatabaseHelper.NUMERO_AMOSTRA, dadosAuto.getNumeroAmostra());
    params.put(AutoInfracaoDatabaseHelper.RECOLHIMENTO, dadosAuto.getRecolhimento());
    params.put(AutoInfracaoDatabaseHelper.PROCEDIMENTOS, dadosAuto.getProcedimentos());
    params.put(AutoInfracaoDatabaseHelper.OBSERVACAO, dadosAuto.getObservacao());
    params.put(AutoInfracaoDatabaseHelper.IDENTIFICACAO_EMBARCADOR, dadosAuto.getIdetificacaoEmbarcador());
    params.put(AutoInfracaoDatabaseHelper.CPF_EMBARCADOR, dadosAuto.getCpfEmbarcador());
    params.put(AutoInfracaoDatabaseHelper.CNPJ_EMBARCADOR, dadosAuto.getCnpjEmbarcador());
    params.put(AutoInfracaoDatabaseHelper.IDENTIFICACAO_DO_TRANSPORTADOR, dadosAuto.getIdentificacaoTransportador());
    params.put(AutoInfracaoDatabaseHelper.CPF_TRANSPORTADOR, dadosAuto.getCpfTransportador());
    params.put(AutoInfracaoDatabaseHelper.CNPJ_TRANSPORTADOR, dadosAuto.getCnpjTransportador());
    params.put(AutoInfracaoDatabaseHelper.STATUS_CONCLUIDO, dadosAuto.getStatusConcluido());*/

}
