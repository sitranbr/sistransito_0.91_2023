package net.sistransito.mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sistransito.R;
import net.sistransito.mobile.ait.AitData;
import net.sistransito.mobile.ait.CancelAit;
import net.sistransito.mobile.sync.SyncFiles;
import net.sistransito.mobile.network.NetworkConnection;
import net.sistransito.mobile.timeandime.TimeAndIme;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import net.sqlcipher.database.SQLiteDatabase;

public class InfractionDatabaseAdapter {
	private TimeAndIme ime;
	private SQLiteDatabase database;
	private InfractionDatabaseHelper aitDatabaseHelper;
	private Context context;

	public InfractionDatabaseAdapter(Context context) {
		ime = new TimeAndIme(context);
		aitDatabaseHelper = new InfractionDatabaseHelper(context);
		//database = databaseHelper.getReadableDatabase(ime.getIME());
		database = aitDatabaseHelper.getReadableDatabase();
		this.context = context;
	}

	public AitData getAitDataFromPlate(String plate) {

		Cursor myCursor = this.database.query(
				InfractionDatabaseHelper.TABLE_NAME, null,
				InfractionDatabaseHelper.PLATE + "=?", new String[] { plate
						+ "" }, null, null, null);

		if (myCursor.getCount() > 0) {
			AitData aitData = new AitData();
			myCursor.moveToFirst();
			aitData.setAitDataFromCursor(myCursor);
			myCursor.close();
			return aitData;
		} else {
			myCursor.close();
			return null;
		}
	}

	public boolean isSamePlateExist(String placa) {
		Cursor myCursor = this.database.query(
				InfractionDatabaseHelper.TABLE_NAME, null,
				InfractionDatabaseHelper.PLATE + "=?", new String[] { placa
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

	public boolean setAitData(AitData data) {

		ContentValues values = new ContentValues();

		values.put(InfractionDatabaseHelper.AIT_NUMBER,
				data.getAitNumber());
		values.put(InfractionDatabaseHelper.PLATE, data.getPlate());
		values.put(InfractionDatabaseHelper.VEHICLE_STATE, data.getStateVehicle());
		values.put(InfractionDatabaseHelper.RENAVAM, data.getRenavam());
		values.put(InfractionDatabaseHelper.CHASSI, data.getChassi());
		values.put(InfractionDatabaseHelper.COUNTRY, data.getCountry());
		values.put(InfractionDatabaseHelper.VEHICLE_MODEL,
				data.getVehicleModel());
		values.put(InfractionDatabaseHelper.VEHICLE_COLOR,
				data.getVehycleColor());
		values.put(InfractionDatabaseHelper.SPECIES, data.getVehicleSpecies());
		values.put(InfractionDatabaseHelper.CATEGORY, data.getVehicleCategory());
		values.put(InfractionDatabaseHelper.DRIVER_NAME,
				data.getConductorName());
		values.put(InfractionDatabaseHelper.DRIVER_FOREIGN,
				data.getForeignDriver());
		values.put(InfractionDatabaseHelper.DRIVER_COUNTRY,
				data.getDriverCountry());
		values.put(InfractionDatabaseHelper.ENABLED_DRIVER,
				data.getQualifiedDriver());
		values.put(InfractionDatabaseHelper.DRIVER_LICENSE, data.getCnhPpd());
		values.put(InfractionDatabaseHelper.DRIVER_LICENSE_STATE, data.getCnhState());
		values.put(InfractionDatabaseHelper.DOCUMENT_TYPE,
				data.getDocumentType());
		values.put(InfractionDatabaseHelper.DOCUMENT_NUMBER,
				data.getDocumentNumber());
		values.put(InfractionDatabaseHelper.INFRACTION, data.getInfraction());
		values.put(InfractionDatabaseHelper.FLAMING_CODE, data.getFramingCode());
		values.put(InfractionDatabaseHelper.UNFOLDING, data.getUnfolding());
		values.put(InfractionDatabaseHelper.ARTICLE, data.getArticle());
		values.put(InfractionDatabaseHelper.CITY_CODE,
				data.getCityCode());
		values.put(InfractionDatabaseHelper.CITY, data.getCity());
		values.put(InfractionDatabaseHelper.STATE, data.getState());
		values.put(InfractionDatabaseHelper.ADDRESS, data.getAddress());
		values.put(InfractionDatabaseHelper.AIT_DATE, data.getAitDate());
		values.put(InfractionDatabaseHelper.AIT_TIME, data.getAitTime());
		values.put(InfractionDatabaseHelper.EQUIPMENT_DESCRIPTION, data.getDescription());
		values.put(InfractionDatabaseHelper.EQUIPMENT_BRAND,
				data.getVehicleBrand());
		values.put(InfractionDatabaseHelper.EQUIPMENT_MODEL,
				data.getVehicleModel());
		values.put(InfractionDatabaseHelper.EQUIPMENT_SERIAL,
				data.getSerialNumber());
		values.put(InfractionDatabaseHelper.MEASUREMENT_PERFORMED,
				data.getMeasurementPerformed());
		values.put(InfractionDatabaseHelper.REGULATED_VALUE,
				data.getRegulatedValue());
		values.put(InfractionDatabaseHelper.VALUE_CONSIDERED,
				data.getValueConsidered());
		values.put(InfractionDatabaseHelper.ALCOHOL_TEST_NUMBER,
				data.getAlcoholTestNumber());
		values.put(InfractionDatabaseHelper.RETREAT,
				data.getRetreat());
		values.put(InfractionDatabaseHelper.PROCEDURES,
				data.getProcedures());
		values.put(InfractionDatabaseHelper.OBSERVATION,
				data.getObservation());
		values.put(InfractionDatabaseHelper.SHIPPER_IDENTIFICATION,
				data.getShipperIdentification());
		values.put(InfractionDatabaseHelper.CPF_SHIPPER,
				data.getCpfShipper());
		values.put(InfractionDatabaseHelper.CNPJ_SHIPPER,
				data.getCnpShipper());
		values.put(InfractionDatabaseHelper.CARRIER_IDENTIFICATION,
				data.getCarrierIdentification());
		values.put(InfractionDatabaseHelper.CPF_CARRIER,
				data.getCpfCarrier());
		values.put(InfractionDatabaseHelper.CNPJ_CARRIER,
				data.getCnpjCarrier());
		values.put(InfractionDatabaseHelper.CANCEL_STATUS,
				data.getCancellationStatus());
		values.put(InfractionDatabaseHelper.REASON_FOR_CANCEL,
				data.getReasonForCancellation());
		values.put(InfractionDatabaseHelper.SYNC_STATUS,
				data.getSyncStatus());

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

	//Inseri o ID do auto sempre que se iniciar um novo AIT
	public boolean insertAitNumber(AitData data){

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

	public boolean cancelAit(AitData data, String motive) {

		ContentValues values = new ContentValues();

		values.put(InfractionDatabaseHelper.CANCEL_STATUS, "1");
		values.put(InfractionDatabaseHelper.REASON_FOR_CANCEL, motive);
		values.put(InfractionDatabaseHelper.COMPLETED_STATUS,"1");

		int update = this.database.update(InfractionDatabaseHelper.TABLE_NAME,
				values, InfractionDatabaseHelper.AIT_NUMBER + "= ? ",
				new String[] { data.getAitNumber() });

		Log.d("setCancel: ", update + " - " +
				"" +
				"" + motive + " | Ait number: " + data.getAitNumber());

		if (update > 0 && NetworkConnection.isInternetConnected(context)) {
			CancelAit sync = new CancelAit(context);
			sync.sendCanceledAit(data.getAitNumber());
			return true;
		} else {
			return false;
		}

	}

	public boolean synchronizeAit(String ait) {

		ContentValues values = new ContentValues();

		values.put(InfractionDatabaseHelper.SYNC_STATUS,"1");

		int update = this.database.update(InfractionDatabaseHelper.TABLE_NAME,
				values, InfractionDatabaseHelper.AIT_NUMBER + "= ? ",
				new String[] { ait });

		Log.d("set synchronize", "Ait number: " + ait + " - update: " + update);

		if (update > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean insertAitData(AitData aitData) {

		ContentValues values = new ContentValues();

		String country;

		country = (aitData.getCountry() == null) ? context.getString(R.string.capital_country_acronym) : aitData.getCountry();

		values.put(InfractionDatabaseHelper.PLATE, aitData.getPlate());
		values.put(InfractionDatabaseHelper.VEHICLE_STATE, aitData.getStateVehicle());
		values.put(InfractionDatabaseHelper.RENAVAM, aitData.getRenavam());
		values.put(InfractionDatabaseHelper.CHASSI, aitData.getChassi());
		values.put(InfractionDatabaseHelper.COUNTRY, country);
		values.put(InfractionDatabaseHelper.VEHICLE_BRAND,
				aitData.getVehicleBrand());
		values.put(InfractionDatabaseHelper.VEHICLE_MODEL,
				aitData.getVehicleModel());
		values.put(InfractionDatabaseHelper.VEHICLE_COLOR,
				aitData.getVehycleColor());
		values.put(InfractionDatabaseHelper.SPECIES, aitData.getVehicleSpecies());
		values.put(InfractionDatabaseHelper.CATEGORY, aitData.getVehicleCategory());
		values.put(InfractionDatabaseHelper.CANCEL_STATUS, "0");
		values.put(InfractionDatabaseHelper.REASON_FOR_CANCEL, "");
		values.put(InfractionDatabaseHelper.SYNC_STATUS, "0");
		values.put(InfractionDatabaseHelper.AIT_LATITUDE, aitData.getAitLatitude());
		values.put(InfractionDatabaseHelper.AIT_LONGITUDE, aitData.getAitLongitude());
		values.put(InfractionDatabaseHelper.AIT_DATE_TIME, aitData.getAitDateTime());
		values.put(InfractionDatabaseHelper.COMPLETED_STATUS, "0");

		int update = this.database.update(InfractionDatabaseHelper.TABLE_NAME,
				values, InfractionDatabaseHelper.AIT_NUMBER + "= ? ",
				new String[] { aitData.getAitNumber() });

		Log.d("set vehicle ", update + " - " + aitData);

		if (update > 0) {
			(DatabaseCreator.getNumberDatabaseAdapter(context))
					.deleteAitNumber(aitData.getAitNumber());
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

	public boolean updateAitDataConductor(AitData data) {

		ContentValues values = new ContentValues();

        values.put(InfractionDatabaseHelper.APPROACH, data.getApproach());
		values.put(InfractionDatabaseHelper.DRIVER_NAME, data.getConductorName());
		values.put(InfractionDatabaseHelper.DRIVER_LICENSE, data.getCnhPpd());
		values.put(InfractionDatabaseHelper.DRIVER_LICENSE_STATE, data.getCnhState());
		values.put(InfractionDatabaseHelper.DOCUMENT_TYPE, data.getDocumentType());
		values.put(InfractionDatabaseHelper.DOCUMENT_NUMBER, data.getDocumentNumber());

		int update = this.database.update(InfractionDatabaseHelper.TABLE_NAME,
				values, InfractionDatabaseHelper.AIT_NUMBER + "= ? ",
				new String[] { data.getAitNumber() });

		Log.d("set conductor ", update + " - " + data);

		if (update > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean updateAitDataAddress(AitData data) {

		ContentValues values = new ContentValues();

		values.put(InfractionDatabaseHelper.ADDRESS, data.getAddress());
		values.put(InfractionDatabaseHelper.AIT_DATE, data.getAitDate());
		values.put(InfractionDatabaseHelper.AIT_TIME, data.getAitTime());
		values.put(InfractionDatabaseHelper.CITY_CODE, data.getCityCode());
		values.put(InfractionDatabaseHelper.CITY, data.getCity());
		values.put(InfractionDatabaseHelper.STATE, data.getState());

		int update = this.database.update(InfractionDatabaseHelper.TABLE_NAME,
				values, InfractionDatabaseHelper.AIT_NUMBER + "= ? ",
				new String[] { data.getAitNumber() });

		Log.d("set address ", update + " - " + data);

		if (update > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean updateAitDataInfraction(AitData data) {

		ContentValues values = new ContentValues();

		values.put(InfractionDatabaseHelper.INFRACTION, data.getInfraction());
		values.put(InfractionDatabaseHelper.FLAMING_CODE, data.getFramingCode());
		values.put(InfractionDatabaseHelper.UNFOLDING, data.getUnfolding());
		values.put(InfractionDatabaseHelper.ARTICLE, data.getArticle());
		values.put(InfractionDatabaseHelper.TCA_NUMBER, data.getTcaNumber());
		values.put(InfractionDatabaseHelper.EQUIPMENT_DESCRIPTION, data.getDescription());
		values.put(InfractionDatabaseHelper.EQUIPMENT_BRAND, data.getEquipmentBrand());
		values.put(InfractionDatabaseHelper.EQUIPMENT_MODEL, data.getEquipmentModel());
		values.put(InfractionDatabaseHelper.EQUIPMENT_SERIAL, data.getSerialNumber());
		values.put(InfractionDatabaseHelper.MEASUREMENT_PERFORMED, data.getMeasurementPerformed());
		values.put(InfractionDatabaseHelper.REGULATED_VALUE, data.getRegulatedValue());
		values.put(InfractionDatabaseHelper.VALUE_CONSIDERED, data.getValueConsidered());
		values.put(InfractionDatabaseHelper.ALCOHOL_TEST_NUMBER, data.getAlcoholTestNumber());
		values.put(InfractionDatabaseHelper.RETREAT, data.getRetreat());
		values.put(InfractionDatabaseHelper.PROCEDURES, data.getProcedures());
		values.put(InfractionDatabaseHelper.OBSERVATION, data.getObservation());
		values.put(InfractionDatabaseHelper.SHIPPER_IDENTIFICATION, data.getShipperIdentification());
		values.put(InfractionDatabaseHelper.CPF_SHIPPER, data.getCpfShipper());
		values.put(InfractionDatabaseHelper.CNPJ_SHIPPER, data.getCnpShipper());
		values.put(InfractionDatabaseHelper.CARRIER_IDENTIFICATION, data.getCarrierIdentification());
		values.put(InfractionDatabaseHelper.CPF_CARRIER, data.getCpfCarrier());
		values.put(InfractionDatabaseHelper.CNPJ_CARRIER, data.getCnpjCarrier());

		int update = this.database.update(InfractionDatabaseHelper.TABLE_NAME,
				values, InfractionDatabaseHelper.AIT_NUMBER + "= ? ",
				new String[] { data.getAitNumber() });

		Log.d("set infraction ", update + " - " + data);

		if (update > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean insertAitPhoto(String aitNumber, String photoPath) {
		ContentValues values = new ContentValues();
		values.put(InfractionDatabaseHelper.PHOTO_AIT_NUMBER, aitNumber);
		values.put(InfractionDatabaseHelper.PHOTO_PATH, photoPath);

		long insertId = database.insert(InfractionDatabaseHelper.PHOTO_TABLE_NAME, null, values);
		return insertId != -1; // Retorna true se a inserção foi bem-sucedida
	}

	public List<String> getAitPhotos(String aitNumber) {
		List<String> photos = new ArrayList<>();
		Cursor cursor = database.query(
				InfractionDatabaseHelper.PHOTO_TABLE_NAME,
				new String[]{InfractionDatabaseHelper.PHOTO_PATH},
				InfractionDatabaseHelper.PHOTO_AIT_NUMBER + " = ?",
				new String[]{aitNumber},
				null, null, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				photos.add(cursor.getString(cursor.getColumnIndexOrThrow(InfractionDatabaseHelper.PHOTO_PATH)));
			}
			cursor.close();
		}
		return photos;
	}

	public boolean updateAitData(AitData aitData) {
		ContentValues values = new ContentValues();
		values.put(InfractionDatabaseHelper.COMPLETED_STATUS, "1");

		int update = database.update(
				InfractionDatabaseHelper.TABLE_NAME,
				values,
				InfractionDatabaseHelper.AIT_NUMBER + " = ?",
				new String[]{aitData.getAitNumber()}
		);

		if (update > 0 && NetworkConnection.isInternetConnected(context)) {
			SyncFiles sync = new SyncFiles(context);
			sync.sendAitData(aitData.getAitNumber());
			return true;
		}
		return false;
	}

	public boolean deleteAitPhoto(String aitNumber, String photoPath) {
		int deletedRows = database.delete(
				InfractionDatabaseHelper.PHOTO_TABLE_NAME,
				InfractionDatabaseHelper.PHOTO_AIT_NUMBER + " = ? AND " + InfractionDatabaseHelper.PHOTO_PATH + " = ?",
				new String[]{aitNumber, photoPath}
		);
		return deletedRows > 0;
	}

	/**
	 * Exclui todos os dados da tabela de infrações (AITs)
	 * @return Número de registros excluídos
	 */
	public int deleteAllInfractions() {
		try {
			// Primeiro excluir todas as fotos relacionadas
			int photosDeleted = database.delete(InfractionDatabaseHelper.PHOTO_TABLE_NAME, null, null);
			Log.d("InfractionDatabaseAdapter", "Excluídas " + photosDeleted + " fotos de AIT");
			
			// Depois excluir todos os AITs
			int infractionsDeleted = database.delete(InfractionDatabaseHelper.TABLE_NAME, null, null);
			Log.d("InfractionDatabaseAdapter", "Excluídos " + infractionsDeleted + " AITs");
			
			return infractionsDeleted;
		} catch (Exception e) {
			Log.e("InfractionDatabaseAdapter", "Erro ao excluir todos os AITs", e);
			return 0;
		}
	}

	public boolean insertAitDataPhotos(String ait, String local, String photo) {

		ContentValues values = new ContentValues();

		int update = this.database.update(InfractionDatabaseHelper.TABLE_NAME,
				values, InfractionDatabaseHelper.AIT_NUMBER + "= ? ",
				new String[] { ait });

		if (update > 0 && NetworkConnection.isInternetConnected(context)) {
			return true;
		} else {
			return false;
		}

	}

	public boolean updateAitDataPhotos(AitData aitData) {

		ContentValues values = new ContentValues();

		values.put(InfractionDatabaseHelper.COMPLETED_STATUS, "1");

		int update = this.database.update(InfractionDatabaseHelper.TABLE_NAME,
				values, InfractionDatabaseHelper.AIT_NUMBER + "= ? ",
				new String[] { aitData.getAitNumber() });

		//Log.d("set photos ", update + " - " + aitData);

		if (update > 0 && NetworkConnection.isInternetConnected(context)) {
			SyncFiles sync = new SyncFiles(context);
			sync.sendAitData(aitData.getAitNumber());
			return true;
		} else {
			return false;
		}

	}

	public Cursor getAitCursor() {

		Cursor myCursor = this.database.query(
				InfractionDatabaseHelper.TABLE_NAME, null, null, null, null,
				null, InfractionDatabaseHelper.COLUMN_ID + " DESC");
		return myCursor;

	}

	public String getIdAit() {
		Cursor myCursor = this.database.query(
				InfractionDatabaseHelper.TABLE_NAME, null, null, null, null,
				null, null);
		if (myCursor.getCount() > 0) {
			myCursor.moveToLast();
			String num = myCursor
					.getString(myCursor.getColumnIndex(InfractionDatabaseHelper.COLUMN_ID));
			myCursor.close();
			return num;
		} else {
			myCursor.close();
			return null;
		}
	}

	public String getAitDataFromId(String idAuto) {
		Cursor myCursor = this.database.query(
				InfractionDatabaseHelper.TABLE_NAME, null,
				InfractionDatabaseHelper.COLUMN_ID + "=?",
				new String[] { idAuto }, null, null, null);

		if (myCursor.getCount() > 0) {
			myCursor.moveToLast();
			String num = myCursor
					.getString(myCursor.getColumnIndex(InfractionDatabaseHelper.AIT_NUMBER));
			myCursor.close();
			return num;
		} else {
			myCursor.close();
			return null;
		}

	}

	public String getStatusCursor(String idAuto) {

		String completedStatus;

		Cursor myCursor = this.database.query(
				InfractionDatabaseHelper.TABLE_NAME, null,
				InfractionDatabaseHelper.COLUMN_ID + "=?",
				new String[] { idAuto }, null, null, null);

		if (myCursor.getCount() > 0) {
			myCursor.moveToFirst();

			completedStatus = (myCursor.getString(myCursor
					.getColumnIndex(InfractionDatabaseHelper.COMPLETED_STATUS)));

			myCursor.close();
		} else {
			myCursor.close();
			return null;
		}

		return completedStatus;

	}

	public Cursor getAitActiveCursor() {
		Cursor myCursor = this.database.query(InfractionDatabaseHelper.TABLE_NAME, null,
				InfractionDatabaseHelper.COMPLETED_STATUS + "=?",
						new String[] { "1" }, null,
						null, InfractionDatabaseHelper.COLUMN_ID + " DESC");
		return myCursor;

	}

	public Cursor getAitCursorFromID(int id) {
		Cursor myCursor = this.database.query(
				InfractionDatabaseHelper.TABLE_NAME, null,
				InfractionDatabaseHelper.COLUMN_ID + "=?", new String[] { id
						+ "" }, null, null, null);
		return myCursor;
	}

	public Cursor getAitCancelCursor() {
		Cursor myCursor = this.database.query(InfractionDatabaseHelper.TABLE_NAME, null,
				InfractionDatabaseHelper.AIT_NUMBER + "=?",
				new String[] { "TL00007452" }, null, null, null);
		return myCursor;
	}

	public AitData getDataFromAitNumber(String aitNumber) {
		Cursor myCursor = this.database.query(
				InfractionDatabaseHelper.TABLE_NAME, null,
				InfractionDatabaseHelper.AIT_NUMBER + "=?",
				new String[] { aitNumber + "" }, null, null, null);

		if (myCursor != null && myCursor.moveToFirst()) {
			AitData aitData = new AitData();
			aitData.setAitDataFromCursor(myCursor);
			myCursor.close();
			return aitData;
		}
		return null;

	}

	public String aitDataToJSon() {

		Cursor cursor = this.database.query(
				InfractionDatabaseHelper.TABLE_NAME, null, null, null, null,
				null, null);

		AitData aitData;

			if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
				do {

				    try{

						// Here we convert Java Object to JSON
						aitData = new AitData();
						aitData.setAitDataFromCursor(cursor);

						JSONObject jsonArr = new JSONObject();

						jsonArr.put(InfractionDatabaseHelper.AIT_NUMBER,
								aitData.getAitNumber());

						jsonArr.put("AUTOJSON", jsonArr);

						return jsonArr.toString();

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

				} while (cursor.moveToNext());

			} else {
				return null;
			}

		cursor.close();
		return null;

	}

	public JSONArray getResults() {

		Cursor cursor = this.database.query(
				InfractionDatabaseHelper.TABLE_NAME, null, null, null, null,
				null, InfractionDatabaseHelper.COLUMN_ID,"3");

		JSONArray resultSet = new JSONArray();

		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {

			int totalColumn = cursor.getColumnCount();
			JSONObject rowObject = new JSONObject();
			for (int i = 0; i < totalColumn; i++) {
				if (cursor.getColumnName(i) != null) {

					try {

						if (cursor.getString(i) != null) {
							Log.d("TAG_NAME", cursor.getString(i));
							rowObject.put(cursor.getColumnName(i), cursor.getString(i));
						} else {
							rowObject.put(cursor.getColumnName(i), "");
						}
					} catch (Exception e) {
						Log.d("TAG_NAME", e.getMessage());
					}

				}

			}
			resultSet.put(rowObject);
			cursor.moveToNext();
		}

		cursor.close();
		//Log.d("TAG_NAME", resultSet.toString());
		return resultSet;
	}

	public String aitComposeJsonFromSqLite() {

		ArrayList<HashMap<String, String>> arrayListAit = new ArrayList<HashMap<String, String>>();

		Cursor cursor = this.database.query(
				InfractionDatabaseHelper.TABLE_NAME, null, null, null, null,
				null, InfractionDatabaseHelper.COLUMN_ID,"1");

		AitData aitData;

		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
			do {
				aitData = new AitData();
				aitData.setAitDataFromCursor(cursor);
				HashMap<String, String> map = new HashMap<String, String>();

				// conductor
				map.put(InfractionDatabaseHelper.AIT_NUMBER,
						aitData.getAitNumber());
				map.put(InfractionDatabaseHelper.PLATE, aitData.getPlate());
				map.put(InfractionDatabaseHelper.VEHICLE_STATE, aitData.getStateVehicle());
				map.put(InfractionDatabaseHelper.RENAVAM, aitData.getRenavam());
				map.put(InfractionDatabaseHelper.CHASSI, aitData.getChassi());
				map.put(InfractionDatabaseHelper.COUNTRY, aitData.getCountry());
				map.put(InfractionDatabaseHelper.VEHICLE_BRAND,
						aitData.getVehicleBrand());
				map.put(InfractionDatabaseHelper.VEHICLE_MODEL,
						aitData.getVehicleModel());
				map.put(InfractionDatabaseHelper.VEHICLE_COLOR,
						aitData.getVehycleColor());
				map.put(InfractionDatabaseHelper.SPECIES, aitData.getVehicleSpecies());
				map.put(InfractionDatabaseHelper.CATEGORY,
						aitData.getVehicleCategory());
				map.put(InfractionDatabaseHelper.DRIVER_NAME,
						aitData.getConductorName());
				map.put(InfractionDatabaseHelper.DRIVER_LICENSE, aitData.getCnhPpd());
				map.put(InfractionDatabaseHelper.DRIVER_LICENSE_STATE, aitData.getCnhState());
				map.put(InfractionDatabaseHelper.DOCUMENT_TYPE,
						aitData.getDocumentType());
				map.put(InfractionDatabaseHelper.DOCUMENT_NUMBER,
						aitData.getDocumentNumber());

				// infraction

				map.put(InfractionDatabaseHelper.INFRACTION, aitData.getInfraction());
				map.put(InfractionDatabaseHelper.FLAMING_CODE, aitData.getFramingCode());
				map.put(InfractionDatabaseHelper.UNFOLDING, aitData.getUnfolding());
				map.put(InfractionDatabaseHelper.ARTICLE, aitData.getArticle());
				map.put(InfractionDatabaseHelper.CITY_CODE,
						aitData.getCityCode());
				map.put(InfractionDatabaseHelper.CITY,
						aitData.getCity());
				map.put(InfractionDatabaseHelper.STATE, aitData.getState());
				map.put(InfractionDatabaseHelper.ADDRESS, aitData.getAddress());
				map.put(InfractionDatabaseHelper.AIT_DATE, aitData.getAitDate());
				map.put(InfractionDatabaseHelper.AIT_TIME, aitData.getAitTime());

				map.put(InfractionDatabaseHelper.EQUIPMENT_DESCRIPTION,
						aitData.getDescription());
				map.put(InfractionDatabaseHelper.EQUIPMENT_BRAND, aitData.getEquipmentBrand());
				map.put(InfractionDatabaseHelper.EQUIPMENT_MODEL, aitData.getEquipmentModel());
				map.put(InfractionDatabaseHelper.EQUIPMENT_SERIAL,
						aitData.getSerialNumber());
				map.put(InfractionDatabaseHelper.MEASUREMENT_PERFORMED,
						aitData.getMeasurementPerformed());
				map.put(InfractionDatabaseHelper.VALUE_CONSIDERED,
						aitData.getValueConsidered());
				map.put(InfractionDatabaseHelper.ALCOHOL_TEST_NUMBER,
						aitData.getAlcoholTestNumber());

				// generation
				map.put(InfractionDatabaseHelper.RETREAT,
						aitData.getRetreat());
				map.put(InfractionDatabaseHelper.PROCEDURES,
						aitData.getProcedures());
				map.put(InfractionDatabaseHelper.OBSERVATION,
						aitData.getObservation());
				map.put(InfractionDatabaseHelper.SHIPPER_IDENTIFICATION,
						aitData.getShipperIdentification());
				map.put(InfractionDatabaseHelper.CPF_SHIPPER,
						aitData.getCpfShipper());
				map.put(InfractionDatabaseHelper.CNPJ_SHIPPER,
						aitData.getCnpShipper());
				map.put(InfractionDatabaseHelper.CARRIER_IDENTIFICATION,
						aitData.getCarrierIdentification());
				map.put(InfractionDatabaseHelper.CPF_CARRIER,
						aitData.getCpfCarrier());
				map.put(InfractionDatabaseHelper.CNPJ_CARRIER,
						aitData.getCnpjCarrier());
				map.put(InfractionDatabaseHelper.CANCEL_STATUS,
						aitData.getCancellationStatus());
				map.put(InfractionDatabaseHelper.REASON_FOR_CANCEL,
						aitData.getReasonForCancellation());
				map.put(InfractionDatabaseHelper.SYNC_STATUS,
						aitData.getSyncStatus());

				arrayListAit.add(map);
			} while (cursor.moveToNext());

		} else {
			return null;
		}

		cursor.close();

		Gson gson = new GsonBuilder().create();
		return gson.toJson(arrayListAit);

	}

	private String getStringImage(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		return Base64.encodeToString(imageBytes, Base64.DEFAULT);
	}

	public void aitUpdateSyncStatus(String ait) {
		this.database.delete(InfractionDatabaseHelper.TABLE_NAME,
				InfractionDatabaseHelper.AIT_NUMBER + "=?",
				new String[] { ait });
	}

}
