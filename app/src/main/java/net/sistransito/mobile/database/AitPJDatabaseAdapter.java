package net.sistransito.mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sistransito.mobile.aitcompany.pjData;
import net.sistransito.mobile.timeandime.TimeAndIme;
//import net.sqlcipher.database.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class AitPJDatabaseAdapter {

    private TimeAndIme ime;
    private SQLiteDatabase database;
    private InfractionDatabaseHelper databaseHelper;
    private Context context;

    public AitPJDatabaseAdapter(Context context){
        ime = new TimeAndIme(context);
        databaseHelper = new InfractionDatabaseHelper(context);
        //database = databaseHelper.getReadableDatabase(ime.getIME());
        database = databaseHelper.getReadableDatabase();
        this.context = context;
    }

    public pjData getAitDataFromPlate(String plate) {

        Cursor myCursor = this.database.query(
                InfractionDatabaseHelper.TABLE_NAME, null,
                InfractionDatabaseHelper.PLATE + "=?", new String[] { plate
                        + "" }, null, null, null);

        if (myCursor.getCount() > 0) {
            pjData dadosAuto = new pjData();
            myCursor.moveToFirst();
            dadosAuto.setAutoDataFromCursor(myCursor);
            myCursor.close();
            return dadosAuto;
        } else {
            myCursor.close();
            return null;
        }
    }

    public pjData getPJDataFromPJ(String cnpj) {

        Cursor myCursor = this.database.query(
                InfractionDatabaseHelper.TABLE_NAME, null,
                InfractionDatabaseHelper.PLATE + "=?", new String[] { cnpj
                        + "" }, null, null, null);

        if (myCursor.getCount() > 0) {
            pjData dadosAuto = new pjData();
            myCursor.moveToFirst();
            dadosAuto.setAutoDataFromCursor(myCursor);
            myCursor.close();
            return dadosAuto;
        } else {
            myCursor.close();
            return null;
        }
    }

    public boolean isSameCNPJExist(String cnpj) {
        Cursor myCursor = this.database.query(
                InfractionDatabaseHelper.TABLE_NAME, null,
                InfractionDatabaseHelper.PLATE + "=?", new String[] { cnpj
                        + "" }, null, null, null);
        if (myCursor.getCount() > 0) {
            myCursor.close();
            return true;
        } else {
            myCursor.close();
            return false;
        }
    }

    public void close() {
        database.close();
    }

    //Inseri o ID do auto sempre que se iniciar um novo AIT
    public boolean insertNumeroAuto(pjData data){

        ContentValues values = new ContentValues();
        values.put(InfractionDatabaseHelper.AIT_NUMBER,
                data.getAitNumber());

        long insert = this.database.insert(InfractionDatabaseHelper.TABLE_NAME, null, values);

        if (insert > 0) {
            (DatabaseCreator.getNumberDatabaseAdapter(context))
                    .deleteAitNumber(data.getAitNumber());
            (DatabaseCreator.getBalanceDatabaseAdapter(context))
                    .setAitPerformed();
            (DatabaseCreator.getBalanceDatabaseAdapter(context))
                    .setAitRemaining((DatabaseCreator
                            .getNumberDatabaseAdapter(context))
                            .getRemainAitNumber());
            return true;
        } else {
            return false;
        }

    }

    public boolean setCancelarAuto(pjData data, String motivo) {

        ContentValues values = new ContentValues();

        values.put(InfractionDatabaseHelper.CANCEL_STATUS, "1");
        values.put(InfractionDatabaseHelper.REASON_FOR_CANCEL, motivo);

        int update = this.database.update(InfractionDatabaseHelper.TABLE_NAME,
                values, InfractionDatabaseHelper.AIT_NUMBER + "= ? ",
                new String[] { data.getAitNumber() });

        Log.d("setCancelamento", update + "-" + motivo + " | number Ait: " + data.getAitNumber());

        if (update > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean setDadosPJInfrator(pjData dadosAuto) {

        ContentValues values = new ContentValues();

        values.put(AitPJDatabaseHelper.COMPANY_SOCIAL, dadosAuto.getCompanySocial());
        values.put(AitPJDatabaseHelper.CPF, dadosAuto.getCpf());
        values.put(AitPJDatabaseHelper.CNPJ, dadosAuto.getCnpj());
        values.put(AitPJDatabaseHelper.ADDRESS, dadosAuto.getAddress());

        int update = this.database.update(AitPJDatabaseHelper.TABLE_NAME,
                values, AitPJDatabaseHelper.AIT_NUMBER + "= ? ",
                new String[] { dadosAuto.getAitNumber() });

        Log.d("setInfrator", update + "-" + dadosAuto);

        if (update > 0) {
            (DatabaseCreator.getNumberDatabaseAdapter(context))
                    .deleteAitNumber(dadosAuto.getAitNumber());
            (DatabaseCreator.getBalanceDatabaseAdapter(context))
                    .setAitPerformed();
            (DatabaseCreator.getBalanceDatabaseAdapter(context))
                    .setAitRemaining((DatabaseCreator
                            .getNumberDatabaseAdapter(context))
                            .getRemainAitNumber());
            return true;
        } else {
            return false;
        }

    }

    public boolean setDadosPJEnd(pjData data) {

        ContentValues values = new ContentValues();

        values.put(AitPJDatabaseHelper.PLACE, data.getPlace());
        values.put(AitPJDatabaseHelper.DATE, data.getAitDate());
        values.put(AitPJDatabaseHelper.TIME, data.getAitTime());
        values.put(AitPJDatabaseHelper.CITY, data.getCity());
        values.put(AitPJDatabaseHelper.STATE, data.getState());

        int update = this.database.update(AitPJDatabaseHelper.TABLE_NAME,
                values, AitPJDatabaseHelper.AIT_NUMBER + "= ? ",
                new String[] { data.getAitNumber() });

        Log.d("setEndereço", update + "-" + data);

        if (update > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean setDadosPJInfracao(pjData dadosAuto) {

        ContentValues values = new ContentValues();

        values.put(InfractionDatabaseHelper.INFRACTION, dadosAuto.getInfration());
        values.put(InfractionDatabaseHelper.FLAMING_CODE, dadosAuto.getFramingCode());
        values.put(InfractionDatabaseHelper.UNFOLDING, dadosAuto.getUnfolding());
        values.put(InfractionDatabaseHelper.ARTICLE, dadosAuto.getArticle());
        values.put(InfractionDatabaseHelper.OBSERVATION, dadosAuto.getObservation());
        values.put(AitPJDatabaseHelper.CANCELL_STATUS, "0");
        values.put(AitPJDatabaseHelper.CANCELL_REASON, "");
        values.put(AitPJDatabaseHelper.SYNC_STATUS, "0");
        values.put(AitPJDatabaseHelper.AIT_LATITUDE, dadosAuto.getLatitude());
        values.put(AitPJDatabaseHelper.AIT_LONGITUDE, dadosAuto.getLongitude());
        values.put(AitPJDatabaseHelper.AIT_DATE_TIME, dadosAuto.getAitDateTime());
        values.put(AitPJDatabaseHelper.COMPLETED_STATUS, "0");

        int update = this.database.update(InfractionDatabaseHelper.TABLE_NAME,
                values, InfractionDatabaseHelper.AIT_NUMBER + "= ? ",
                new String[] { dadosAuto.getAitNumber() });

        Log.d("setLocal", update + "-" + dadosAuto);

        if (update > 0) {
            return true;
        } else {
            return false;
        }

    }

    public Cursor getAutoCursor() {
        Cursor myCursor = this.database.query(
                InfractionDatabaseHelper.TABLE_NAME, null, null, null, null,
                null, AitPJDatabaseHelper.COLUMN_ID + " DESC");
        return myCursor;

    }

    public Cursor getAutoAtivoCursor() {
        Cursor myCursor = this.database.query(AitPJDatabaseHelper.TABLE_NAME, null,
                AitPJDatabaseHelper.COMPLETED_STATUS + "=?",
                new String[] { "1" }, null,
                null, AitPJDatabaseHelper.COLUMN_ID + " DESC");
        return myCursor;

    }

    public Cursor getAutoCursorFromID(int id) {
        Cursor myCursor = this.database.query(
                AitPJDatabaseHelper.TABLE_NAME, null,
                AitPJDatabaseHelper.COLUMN_ID + "=?", new String[] { id
                        + "" }, null, null, null);
        return myCursor;

    }

    public pjData getDataFromNumeroAuto(String numeroAit) {
        Cursor myCursor = this.database.query(
                AitPJDatabaseHelper.TABLE_NAME, null,
                AitPJDatabaseHelper.AIT_NUMBER + "=?",
                new String[] { numeroAit + "" }, null, null, null);

        if (myCursor != null && myCursor.moveToFirst()) {
            pjData dadosAuto = new pjData();
            dadosAuto.setAutoDataFromCursor(myCursor);
            myCursor.close();
            return dadosAuto;
        }
        return null;

    }

    public String autoComposeJSONfromSQLite_() {
        ArrayList<HashMap<String, String>> arrayListaAuto = new ArrayList<HashMap<String, String>>();
        Cursor cursor = this.database.query(
                AitPJDatabaseHelper.TABLE_NAME, null, null, null, null,
                null, null);
        pjData dadosAuto;

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                dadosAuto = new pjData();
                dadosAuto.setAutoDataFromCursor(cursor);
                HashMap<String, String> map = new HashMap<String, String>();

                // condutor
                map.put(AitPJDatabaseHelper.AIT_NUMBER, dadosAuto.getAitNumber());
                map.put(AitPJDatabaseHelper.CPF, dadosAuto.getCpf());
                map.put(AitPJDatabaseHelper.CNPJ, dadosAuto.getCnpj());
                map.put(AitPJDatabaseHelper.COMPANY_SOCIAL, dadosAuto.getCompanySocial());
                map.put(AitPJDatabaseHelper.ADDRESS, dadosAuto.getAddress());
                map.put(AitPJDatabaseHelper.CITY, dadosAuto.getCity());
                map.put(AitPJDatabaseHelper.STATE, dadosAuto.getState());
                map.put(AitPJDatabaseHelper.PLACE, dadosAuto.getPlace());
                map.put(AitPJDatabaseHelper.DATE, dadosAuto.getAitDate());
                map.put(AitPJDatabaseHelper.TIME, dadosAuto.getAitTime());

                // infracao
                map.put(AitPJDatabaseHelper.INFRATION, dadosAuto.getInfration());
                map.put(AitPJDatabaseHelper.FLAMING_CODE, dadosAuto.getFramingCode());
                map.put(AitPJDatabaseHelper.UNFOLDING, dadosAuto.getUnfolding());
                map.put(AitPJDatabaseHelper.ARTICLE, dadosAuto.getArticle());
                map.put(AitPJDatabaseHelper.OBSERVATION, dadosAuto.getObservation());

                map.put(AitPJDatabaseHelper.CANCELL_STATUS,
                        dadosAuto.getCancelStatus());
                map.put(AitPJDatabaseHelper.CANCELL_REASON,
                        dadosAuto.getCancelReason());
                map.put(InfractionDatabaseHelper.SYNC_STATUS,
                        dadosAuto.getSyncStatus());

                arrayListaAuto.add(map);
            } while (cursor.moveToNext());
        } else {
            return null;
        }
        cursor.close();
        Gson gson = new GsonBuilder().create();
        // Use GSON to serialize Array List to JSON
        return gson.toJson(arrayListaAuto);
    }

    public String autoComposeJSONfromSQLite() {

        ArrayList<HashMap<String, String>> arrayListaAuto = new ArrayList<HashMap<String, String>>();

        Cursor cursor = this.database.query(
                InfractionDatabaseHelper.TABLE_NAME, null, null, null, null,
                null, null);
        pjData dadosAuto;

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                dadosAuto = new pjData();
                dadosAuto.setAutoDataFromCursor(cursor);
                HashMap<String, String> map = new HashMap<String, String>();

                //map.put(AutoInfracaoDatabaseHelper.AUTO_FOTO1, foto);
                map.put(AitPJDatabaseHelper.AIT_NUMBER,
                        dadosAuto.getAitNumber());
                map.put(AitPJDatabaseHelper.CPF, dadosAuto.getCpf());
                map.put(AitPJDatabaseHelper.CNPJ, dadosAuto.getCnpj());

                //FileInputStream inputStream = new FileInputStream(file);
                //compressBitmapToFile(destination);

                arrayListaAuto.add(map);
            } while (cursor.moveToNext());
        } else {
            return null;
        }
        cursor.close();

        Gson gson = new GsonBuilder().create();
        return gson.toJson(arrayListaAuto);
    }

    public void autoUpdateSyncStatus(String ait) {
        this.database.delete(AitPJDatabaseHelper.TABLE_NAME,
                AitPJDatabaseHelper.AIT_NUMBER + "=?",
                new String[] { ait });
    }

}
