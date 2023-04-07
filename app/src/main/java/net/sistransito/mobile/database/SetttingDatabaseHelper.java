package net.sistransito.mobile.database;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteOpenHelper;

public class SetttingDatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME_SETTING = "database_setting.db";
	public static final int VERSION = 1;
	public static final String SETTING_TABLE_NAME = "setting";
	public static final String SETTING_ID = "_id";
	public static final String SETTING_AUTO_BACKUP = "ait_backup";
	public static final String SETTING_LANGUAGE = "language";
	public static final String SETTING_VIBRATOR = "vibrator";
	public static final String SETTING_RINGTONE = "ringtone";
	public static final String SETTING_FONT = "font";
	public static final String SETTING_STATE = "state";
	public static final String SETTING_PRINTER = "printer";

	/*public final String SETTING_TABLE_SQL = "CREATE TABLE [setting] (_id integer NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
			"ait_backup text DEFAULT false, language text, vibrator text DEFAULT true," +
			"ringtone text DEFAULT true,font text DEFAULT 1, state text DEFAULT 'PA', printer text DEFAULT 'MPT_III')";*/

	public final String SETTING_TABLE_SQL = "CREATE TABLE " + SETTING_TABLE_NAME + " (" +
				SETTING_ID + " integer NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
				SETTING_AUTO_BACKUP + " text, " +
				SETTING_LANGUAGE + " text, " +
				SETTING_VIBRATOR + " text, " +
				SETTING_RINGTONE + " text, " +
				SETTING_FONT + " text, " +
				SETTING_STATE + " text, " +
				SETTING_PRINTER + " text" + ")";

	public final String POPULATION_SETTINGS = "INSERT INTO " + SETTING_TABLE_NAME + " (" +
			SETTING_AUTO_BACKUP + ", " +
			SETTING_LANGUAGE + ", " +
			SETTING_VIBRATOR + ", " +
			SETTING_RINGTONE + ", " +
			SETTING_FONT + ", " +
			SETTING_STATE + ", " +
			SETTING_PRINTER + " )" +
			" VALUES ('false', 'pt-br', 'true', 'true', '1', 'PA', 'MPT_III')";

	public SetttingDatabaseHelper(Context context) {
		super(context, DATABASE_NAME_SETTING, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(SETTING_TABLE_SQL);
		db.execSQL(POPULATION_SETTINGS);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
