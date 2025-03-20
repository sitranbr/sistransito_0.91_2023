package net.sistransito.mobile.database;

//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InfractionDatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "database_ait.db";
	public static final int VERSION = 2;
	public static final String TABLE_NAME = "tb_infraction";

	// conductor TAB
	public static final String COLUMN_ID = "_id";
	public static final String AIT_NUMBER = "ait_number";
	public static final String PLATE = "plate";
	public static final String VEHICLE_STATE = "state_vehicle";
	public static final String CHASSI = "chassi";
	public static final String RENAVAM = "renavam";
	public static final String VEHICLE_BRAND = "vehicle_brand";
	public static final String VEHICLE_MODEL = "vehicle_model";
	public static final String COUNTRY = "country";
	public static final String VEHICLE_COLOR = "vehicle_color";
	public static final String SPECIES = "species";
	public static final String CATEGORY = "category";
	public static final String APPROACH = "approach";
	public static final String DRIVER_NAME = "driver_name";
	public static final String DRIVER_FOREIGN = "foreign_driver";
	public static final String DRIVER_COUNTRY = "driver_country";
	public static final String ENABLED_DRIVER = "enabled_driver";
	public static final String DRIVER_LICENSE = "cnh_pdd";
	public static final String DRIVER_LICENSE_STATE = "uf_cnh";
	public static final String DOCUMENT_TYPE = "document_type";
	public static final String DOCUMENT_NUMBER = "document_number";

	// infration TAB
	public static final String INFRACTION = "infraction";
	public static final String FLAMING_CODE = "framing_code";
	public static final String UNFOLDING = "unfolding";
	public static final String ARTICLE = "article";
	public static final String CITY_CODE = "city_code";
	public static final String CITY = "city";
	public static final String STATE = "state_city";
	public static final String ADDRESS = "location";
	public static final String AIT_DATE = "date_ait";
	public static final String AIT_TIME = "time_ait";
	public static final String EQUIPMENT_DESCRIPTION = "description_equipment";
	public static final String EQUIPMENT_BRAND = "equipment_brand";
	public static final String EQUIPMENT_MODEL = "model_equipment";
	public static final String EQUIPMENT_SERIAL = "serial_number";
	public static final String MEASUREMENT_PERFORMED = "medication_performed";
	public static final String REGULATED_VALUE = "regulated_value";
	public static final String VALUE_CONSIDERED = "value_considered";
	public static final String ALCOHOL_TEST_NUMBER = "test_sample_number";
	public static final String TCA_NUMBER = "tca_number";

	// generation TAB
	public static final String RETREAT = "retreat";
	public static final String PROCEDURES = "procedures";
	public static final String OBSERVATION = "note";
	public static final String SHIPPER_IDENTIFICATION = "shipper";
	public static final String CPF_SHIPPER = "cpf_shipper";
	public static final String CNPJ_SHIPPER = "cnpj_shipper";
	public static final String CARRIER_IDENTIFICATION = "carrier";
	public static final String CPF_CARRIER = "cpf_carrier";
	public static final String CNPJ_CARRIER = "cnpj_carrier";
	public static final String CANCEL_STATUS = "status_cancel";
	public static final String REASON_FOR_CANCEL = "reason_cancel";
	public static final String SYNC_STATUS = "sync_status";
	public static final String AIT_LATITUDE = "latitude";
	public static final String AIT_LONGITUDE = "longitude";
	public static final String AIT_DATE_TIME = "date_time_ait";
	public static final String COMPLETED_STATUS = "status_completed";
	public static final String TIME_CREATION = "creation_time";
	public static final String TIME_LIMIT = "timeout"; //limit time to sync ait

	// AUTO DE TABLE SQL

	public static final String TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ AIT_NUMBER + " TEXT UNIQUE, " + PLATE + " TEXT, " + VEHICLE_STATE + " TEXT, "
			+ RENAVAM + " TEXT, "  + CHASSI + " TEXT, " + COUNTRY + " TEXT, " + VEHICLE_BRAND + " TEXT, "
			+ VEHICLE_MODEL + " TEXT, " + VEHICLE_COLOR + " TEXT, "
			+ SPECIES + " TEXT, " + CATEGORY + " TEXT, " + APPROACH + " TEXT, "
			+ DRIVER_NAME + " TEXT, " + DRIVER_FOREIGN + " TEXT, "
			+ DRIVER_COUNTRY + " TEXT, " + ENABLED_DRIVER + " TEXT, "
			+ DRIVER_LICENSE + " TEXT, " + DRIVER_LICENSE_STATE + " TEXT, "
			+ DOCUMENT_TYPE + " TEXT, " + DOCUMENT_NUMBER + " TEXT, "
			+ INFRACTION + " TEXT, " + FLAMING_CODE + " TEXT, " + UNFOLDING + " TEXT, "
			+ ARTICLE + " TEXT, " + TCA_NUMBER + " TEXT, " + CITY_CODE + " TEXT, "
			+ CITY + " TEXT, " + STATE + " TEXT, "
			+ ADDRESS + " TEXT, " + AIT_DATE + " TEXT, "
			+ AIT_TIME + " TEXT, " + EQUIPMENT_DESCRIPTION + " TEXT, " + EQUIPMENT_BRAND + " TEXT, "
			+ EQUIPMENT_MODEL + " TEXT, " + EQUIPMENT_SERIAL + " TEXT, "
			+ MEASUREMENT_PERFORMED + " TEXT, " + REGULATED_VALUE + " TEXT, " + VALUE_CONSIDERED + " TEXT, "
			+ ALCOHOL_TEST_NUMBER + " TEXT, " + RETREAT + " TEXT, "
			+ PROCEDURES + " TEXT, " + OBSERVATION + " TEXT, "
			+ SHIPPER_IDENTIFICATION + " TEXT, "
			+ CPF_SHIPPER + " TEXT, "
			+ CNPJ_SHIPPER + " TEXT, "
			+ CARRIER_IDENTIFICATION + " TEXT, "
			+ CPF_CARRIER + " TEXT,"
			+ CNPJ_CARRIER + " TEXT,"
			+ CANCEL_STATUS + " TEXT, "
			+ REASON_FOR_CANCEL + " TEXT, "
			+ SYNC_STATUS + " TEXT, "
			+ AIT_LATITUDE + " TEXT, "
			+ AIT_LONGITUDE + " TEXT, "
			+ AIT_DATE_TIME + " TEXT, "
			+ COMPLETED_STATUS + " TEXT, "
			+ TIME_CREATION + " TEXT, "
			+ TIME_LIMIT + " TEXT )";

	public static final String PHOTO_TABLE_NAME = "tb_ait_photos";
	public static final String PHOTO_COLUMN_ID = "_id";
	public static final String PHOTO_AIT_NUMBER = "ait_number"; // Chave estrangeira
	public static final String PHOTO_PATH = "photo_path";

	public static final String PHOTO_TABLE_SQL = "CREATE TABLE " + PHOTO_TABLE_NAME + " ("
			+ PHOTO_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ PHOTO_AIT_NUMBER + " TEXT NOT NULL, "
			+ PHOTO_PATH + " TEXT NOT NULL, "
			+ "FOREIGN KEY (" + PHOTO_AIT_NUMBER + ") REFERENCES " + TABLE_NAME + "(" + AIT_NUMBER + ")"
			+ ")";

	public InfractionDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_SQL);
		db.execSQL(PHOTO_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		db.execSQL("DROP TABLE IF EXISTS " + PHOTO_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}