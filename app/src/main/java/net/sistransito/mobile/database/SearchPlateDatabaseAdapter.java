package net.sistransito.mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.sistransito.mobile.plate.data.DataFromPlate;
import net.sistransito.mobile.timeandime.TimeAndIme;

public class SearchPlateDatabaseAdapter {

	private static final String TAG = "SearchPlateDBAdapter";
	private TimeAndIme ime;
	private PlateSearchDatabaseHelper plateSearchDatabaseHelper;
	private SQLiteDatabase database;

	public SearchPlateDatabaseAdapter(Context context) {
		ime = new TimeAndIme(context);
		plateSearchDatabaseHelper = new PlateSearchDatabaseHelper(context);
		database = plateSearchDatabaseHelper.getWritableDatabase();
	}

	public boolean insertPlateSearchData(DataFromPlate dataPlate) {
		long checkInsert = -1;

		if (dataPlate != null) {
			if (!isSamePlate(dataPlate.getPlate())) {
				ContentValues values = new ContentValues();
				values.put(PlateSearchDatabaseHelper.PLATE, dataPlate.getPlate());
				values.put(PlateSearchDatabaseHelper.STATE, dataPlate.getState());
				values.put(PlateSearchDatabaseHelper.COUNTRY, dataPlate.getCountry());
				values.put(PlateSearchDatabaseHelper.RENAVAM, dataPlate.getRenavam());
				values.put(PlateSearchDatabaseHelper.CHASSIS, dataPlate.getChassi());
				values.put(PlateSearchDatabaseHelper.BRAND, dataPlate.getBrand());
				values.put(PlateSearchDatabaseHelper.MODEL, dataPlate.getModel());
				values.put(PlateSearchDatabaseHelper.COLOR, dataPlate.getColor());
				values.put(PlateSearchDatabaseHelper.TYPE, dataPlate.getType());
				values.put(PlateSearchDatabaseHelper.CATEGORY, dataPlate.getCategory());
				values.put(PlateSearchDatabaseHelper.LICENSING_YEAR, dataPlate.getLicenceYear());
				values.put(PlateSearchDatabaseHelper.LICENSING_DATE, dataPlate.getLicenceData());
				values.put(PlateSearchDatabaseHelper.LICENSING_STATUS, dataPlate.getLicenceStatus());
				values.put(PlateSearchDatabaseHelper.IPVA, dataPlate.getIpva());
				values.put(PlateSearchDatabaseHelper.INSURANCE, dataPlate.getInsurance());
				values.put(PlateSearchDatabaseHelper.STATUS, dataPlate.getStatus());
				values.put(PlateSearchDatabaseHelper.INFRATIONS, dataPlate.getInfractions());
				values.put(PlateSearchDatabaseHelper.RESTRICTIONS, dataPlate.getRestrictions());
				values.put(PlateSearchDatabaseHelper.DATE, dataPlate.getDate());
				values.put(PlateSearchDatabaseHelper.LATITUDE, dataPlate.getLatitude());
				values.put(PlateSearchDatabaseHelper.LONGITUDE, dataPlate.getLongitude());

				try {
					checkInsert = database.insert(PlateSearchDatabaseHelper.TABLE_NAME, null, values);
					Log.d(TAG, "Inserted data for plate: " + dataPlate.getPlate());
				} catch (Exception e) {
					Log.e(TAG, "Error inserting data for plate: " + dataPlate.getPlate(), e);
				}
			} else {
				Log.d(TAG, "Plate already exists: " + dataPlate.getPlate());
			}
		} else {
			Log.e(TAG, "DataFromPlate is null");
		}

		return checkInsert > 0;
	}

	public boolean deletePlateData(String plateName) {
		return false;
	}

	public synchronized void close() {
		database.close();
	}

	public Cursor getPlateCursor() {
		return database.query(PlateSearchDatabaseHelper.TABLE_NAME, null, null, null, null, null, PlateSearchDatabaseHelper.COLUMN_ID + " DESC");
	}

	public Cursor getPlateCursor_2() {
		return database.query(PlateSearchDatabaseHelper.TABLE_NAME, null, null, null, null, null, PlateSearchDatabaseHelper.COLUMN_ID + " DESC");
	}

	public Cursor getPlateFromIDCursor(int id) {
		return this.database.query(PlateSearchDatabaseHelper.TABLE_NAME, null, PlateSearchDatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
	}

	public boolean deleteDataFromIdField(String id) {
		return this.database.delete(PlateSearchDatabaseHelper.TABLE_NAME, PlateSearchDatabaseHelper.COLUMN_ID + "=?", new String[]{id}) > 0;
	}

	private boolean isSamePlate(String plate) {
		Cursor myCursor = database.query(PlateSearchDatabaseHelper.TABLE_NAME, null, PlateSearchDatabaseHelper.PLATE + "=?", new String[]{plate}, null, null, null);
		boolean exists = myCursor.getCount() > 0;
		myCursor.close();
		return exists;
	}
}
