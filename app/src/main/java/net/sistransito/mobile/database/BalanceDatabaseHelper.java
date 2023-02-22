package net.sistransito.mobile.database;

//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

public class BalanceDatabaseHelper extends SQLiteOpenHelper {
	public static final String DB_NAME_BALANCE = "database_balance.db";
	public static final int VERSION = 1;
	public static final String TABLE_NAME = "tb_balance";
	public static final String ID_BALANCE = "_id";
	public static final String AIT_PERFORMED = "ait_performed";
	public static final String TAV_PERFORMED = "tav_performed";
	public static final String TCA_PERFORMED = "tca_performed";
	public static final String RRD_PERFORMED = "rrd_performed";
	public static final String AIT_REMAINING = "ait_remaining";
	public static final String TAV_REMAINING = "tav_remaining";
	public static final String TCA_REMAINING = "tca_remaining";
	public static final String RRD_REMAINING = "rrd_remaining";
	public static final String AIT_NUMBER = "ait_number";
	public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
			+ ID_BALANCE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ AIT_PERFORMED + " INTEGER, " + TAV_PERFORMED + " INTEGER, "
			+ TCA_PERFORMED + " INTEGER, " + RRD_PERFORMED + " INTEGER, "
			+ AIT_REMAINING + " INTEGER, " + TAV_REMAINING + " INTEGER, "
			+ TCA_REMAINING + " INTEGER, " + RRD_REMAINING + " INTEGER )";

	public BalanceDatabaseHelper(Context context) {
		super(context, TABLE_NAME, null, VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("TABLE SQL", CREATE_TABLE_SQL);
		db.execSQL(CREATE_TABLE_SQL);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// UPGRADE LOGIC

	}
}
