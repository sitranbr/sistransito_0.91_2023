package net.sistransito.mobile.database;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

public class TavDatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_TAV_NAME = "database_tav.db";
	public static final int VERSION = 1;
	public static final String TABLE_NAME = "tav";
	public static final String COLUMN_ID = "_id";

	public static final String PLATE = "plate";

	// Conductor
	public static final String AIT_NUMBER = "ait_number";
	public static final String TAV_NUMBER = "tav_number";
	public static final String OWNER_NAME = "owner_name";
	public static final String CPF_CNPJ = "cpf_cnpj";
	public static final String RENAVAM_NUMBER = "renavam_number";
	public static final String CHASSI_NUMBER = "chassi_number";

	// Vehicle
	// Structure
	public static final String LEVER_HEAD = "lever_head";
	public static final String BODYWORK = "bodywork";
	public static final String CEILING = "ceiling";
	public static final String HOOD_BODYWORK = "hood_bodywork";
	public static final String BODYWORK_RIGHT_SIDE = "bodywork_right_side";
	public static final String BODYWORK_LEFT_SIDE = "bodywork_left_side";
	public static final String TRUNK_BODYWORK = "trunk_bodywork";
	public static final String ROOF_BODYWORK = "roof_bodywork";
	public static final String ENGINE = "engine";
	public static final String DASHBOARD = "dashboard";
	public static final String HOOD_PAINTING = "hood_painting";
	public static final String WRIGHT_PAINTING = "wright_painting";
	public static final String LEFT_PAINTING = "left_painting";
	public static final String TRUNK_PAINTING = "trunk_painting";
	public static final String HOOF_PAINTING = "hoof_painting";
	public static final String RADIATOR = "radiator";
	public static final String SIDE_GLASS = "side_glass";
	public static final String WINDSHIELD = "windshield";
	public static final String HEAR_WINDSHIELD = "hear_windshield";

	// Vehicle
	// Acccessories
	public static final String RADIO_ANTENNA = "sp_radio_antenna";
	public static final String TRUNK = "sp_trunk";
	public static final String SEAT = "sp_seat";
	public static final String BATTERY = "sp_battery";
	public static final String HUBCAP = "sp_hubcap";
	public static final String AIR_CONDITIONER = "sp_air_conditioner";
	public static final String FIRE_EXTINGUISHER = "sp_fire_extinguisher";
	public static final String HEADLIGHT = "sp_headlight";
	public static final String REAR_LIGHT = "rear_light";
	public static final String JACK = "sp_jack";
	public static final String FRONT_BUMPER = "sp_front_bumper";
	public static final String REAR_BUMPER = "sp_rear_bumper";
	public static final String DRIVER_SUNSHADE = "sp_driver_sunshade";
	public static final String TIRES = "sp_tires";
	public static final String SPARE_TIRE = "sp_spare_tire";
	public static final String RADIO = "sp_radio";
	public static final String REARVIEW_MIRROR = "sp_rearview_mirror";
	public static final String OUTSIDE_MIRROR = "sp_outside_mirror";
	public static final String CARPET = "sp_carpet";
	public static final String TRIANGLE = "sp_triangle";
	public static final String STEERING_WHEEL = "sp_steering_wheel";
	public static final String HANDLEBARS = "sp_handlebars";

	// Geral
	public static final String ODOMETRO = "odometro";
	public static final String FUEL_MARKER = "fuel_marker";
	public static final String REMOVAL_THROUGH_OF = "removal_through_of";
	public static final String OBSERVATION = "observation";

	public static final String COMPANY_NAME = "company_name";
	public static final String TOW_TRUCK_DRIVER_NAME = "tow_truck_driver_name";

	// TCA DE TABLE SQL
	public static final String TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PLATE
			+ " TEXT, " + AIT_NUMBER + " TEXT, " + OWNER_NAME
			+ " TEXT, " + CPF_CNPJ + " TEXT, " + RENAVAM_NUMBER + " TEXT, "
			+ CHASSI_NUMBER + " TEXT, " + LEVER_HEAD + " TEXT, "
			+ BODYWORK + " TEXT, " + CEILING + " TEXT, " + HOOD_BODYWORK
			+ " TEXT, " + BODYWORK_RIGHT_SIDE + " TEXT, "
			+ BODYWORK_LEFT_SIDE + " TEXT, " + TRUNK_BODYWORK
			+ " TEXT, " + ROOF_BODYWORK + " TEXT, " + ENGINE + " TEXT, " + DASHBOARD
			+ " TEXT, " + HOOD_PAINTING + " TEXT, " + WRIGHT_PAINTING
			+ " TEXT, " + LEFT_PAINTING + " TEXT, "
			+ TRUNK_PAINTING + " TEXT, " + HOOF_PAINTING + " TEXT, "
			+ RADIATOR + " TEXT, " + SIDE_GLASS + " TEXT, "
			+ WINDSHIELD + " TEXT, " + HEAR_WINDSHIELD + " TEXT, "
			+ RADIO_ANTENNA + " TEXT, " + TRUNK + " TEXT, " + SEAT
			+ " TEXT, " + BATTERY + " TEXT, " + HUBCAP + " TEXT, "
			+ AIR_CONDITIONER + " TEXT, " + FIRE_EXTINGUISHER
			+ " TEXT, " + HEADLIGHT + " TEXT, " + REAR_LIGHT
			+ " TEXT, " + JACK + " TEXT, " + FRONT_BUMPER
			+ " TEXT, " + REAR_BUMPER + " TEXT, "
			+ DRIVER_SUNSHADE + " TEXT, " + TIRES + " TEXT, "
			+ SPARE_TIRE + " TEXT, " + RADIO + " TEXT, " + REARVIEW_MIRROR
			+ " TEXT, " + OUTSIDE_MIRROR + " TEXT, " + CARPET
			+ " TEXT, " + TRIANGLE + " TEXT, " + STEERING_WHEEL + " TEXT, " + HANDLEBARS
			+ " TEXT, " + ODOMETRO + " TEXT, " + FUEL_MARKER
			+ " TEXT, " + REMOVAL_THROUGH_OF + " TEXT, " + COMPANY_NAME
			+ " TEXT, " + TOW_TRUCK_DRIVER_NAME + " TEXT, " + OBSERVATION
			+ " TEXT, " + TAV_NUMBER + " TEXT UNIQUE )";

	public TavDatabaseHelper(Context context) {
		super(context, DATABASE_TAV_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("TABLE SQL", TABLE_SQL);
		db.execSQL(TABLE_SQL);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// UPGRADE LOGIC

	}

}