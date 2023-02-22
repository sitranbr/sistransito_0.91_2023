package net.sistransito.mobile.database;

//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlateSearchDatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_VEHICLE_NAME = "database_vehicle.db";
	public static final int VERSION = 1;
	public static final String TABLE_NAME = "tb_vehicle";
	public static final String COLUMN_ID = "_id";
	public static final String PLATE = "plate";
	public static final String STATE = "state";
	public static final String COUNTRY = "country";
	public static final String RENAVAM = "renavam";
	public static final String CHASSIS = "chassi";
	public static final String BRAND = "brand";
	public static final String MODEL = "model";
	public static final String COLOR = "color";
	public static final String TYPE = "type";
	public static final String CATEGORY = "category";
	public static final String LICENSING_YEAR = "licensing_year";
	public static final String LICENSING_DATE = "licensing_date";
	public static final String LICENSING_STATUS = "licensing_status";
	public static final String IPVA = "ipva";
	public static final String INSURANCE = "insurance";
	public static final String STATUS = "status";
	public static final String INFRATIONS = "infrations";
	public static final String RESTRICTIONS = "restrictions";
	public static final String DATE = "date";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	
	
	private final String TABLE_SQL = "CREATE TABLE [" + TABLE_NAME + "] (_id integer NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," + PLATE + " text," +
			STATE + " text, " + COUNTRY + " text DEFAULT 'Brasil'," +
			RENAVAM + " text," + CHASSIS + " text," + BRAND + " text," +
			MODEL + " text," + COLOR + " text," + TYPE + " text," + CATEGORY + " text," +
			LICENSING_YEAR + " text," + LICENSING_DATE + " text," + LICENSING_STATUS + " text," +
			IPVA + " text," + INSURANCE + " text," + STATUS + " text," + INFRATIONS + " text," +
			RESTRICTIONS + " text," + DATE + " text," + LATITUDE + " text," + LONGITUDE + " text)";

	public PlateSearchDatabaseHelper(Context context) {
		super(context, DATABASE_VEHICLE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
