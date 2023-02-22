package net.sistransito.mobile.database;

import android.content.Context;

public class DatabaseCreator {

	public static final int VERSION = 1;

	private static SettingDatabaseAdapter databaseAdapterSetting;
	private static SearchPlateDatabaseAdapter searchPlateDatabaseAdapter;
	private static InfractionDatabaseAdapter databaseAdapterAutoInfracao;
	private static AitPJDatabaseAdapter databaseAdapterPJInfracao;
	private static PrepopulatedDBOpenHelper prepopulatedDBOpenHelper;
	private static SearchDataInCard searchDataInCard;
	private static RrdDatabaseAdapter rrddatabaseAdapter;
	private static TcaDatabaseAdapter tcaDatabaseAdapter;
	private static TavDatabaseAdapter tavDatabaseAdapter;
	private static BalanceDatabaseAdapter balanceDatabaseAdapter;
	private static NumberDatabaseAdapter numberDatabaseAdapter;
	private DatabaseCreator(Context context) {
		openAllDatabase(context);
	}

	public static void openAllDatabase(Context context) {

		databaseAdapterSetting = new SettingDatabaseAdapter(context);
		searchPlateDatabaseAdapter = new SearchPlateDatabaseAdapter(context);
		databaseAdapterAutoInfracao = new InfractionDatabaseAdapter(context);
		databaseAdapterPJInfracao = new AitPJDatabaseAdapter(context);
		prepopulatedDBOpenHelper = new PrepopulatedDBOpenHelper(context);
		searchDataInCard = new SearchDataInCard(context);
		rrddatabaseAdapter = new RrdDatabaseAdapter(context);
		tcaDatabaseAdapter = new TcaDatabaseAdapter(context);
		tavDatabaseAdapter = new TavDatabaseAdapter(context);
		numberDatabaseAdapter = new NumberDatabaseAdapter(context);
		balanceDatabaseAdapter = new BalanceDatabaseAdapter(context);

	}

	public static BalanceDatabaseAdapter getBalanceDatabaseAdapter(
			Context context) {
		if (balanceDatabaseAdapter == null)
			balanceDatabaseAdapter = new BalanceDatabaseAdapter(context);
		return balanceDatabaseAdapter;
	}

	public static NumberDatabaseAdapter getNumberDatabaseAdapter(Context context) {
		if (numberDatabaseAdapter == null)
			numberDatabaseAdapter = new NumberDatabaseAdapter(context);
		return numberDatabaseAdapter;
	}

	public static TavDatabaseAdapter getTavDatabaseAdapter(Context context) {
		if (tavDatabaseAdapter == null)
			tavDatabaseAdapter = new TavDatabaseAdapter(context);
		return tavDatabaseAdapter;
	}

	public static TcaDatabaseAdapter getTcaDatabaseAdapter(Context context) {
		if (tcaDatabaseAdapter == null)
			tcaDatabaseAdapter = new TcaDatabaseAdapter(context);
		return tcaDatabaseAdapter;
	}

	public static RrdDatabaseAdapter getRrdDatabaseAdapter(Context context) {
		if (rrddatabaseAdapter == null)
			rrddatabaseAdapter = new RrdDatabaseAdapter(context);
		return rrddatabaseAdapter;
	}

	public static InfractionDatabaseAdapter getInfractionDatabaseAdapter(
			Context context) {
		if (databaseAdapterAutoInfracao == null)
			databaseAdapterAutoInfracao = new InfractionDatabaseAdapter(
					context);
		return databaseAdapterAutoInfracao;
	}

	public static AitPJDatabaseAdapter getAitPJDatabaseAdapter(
			Context context) {
		if (databaseAdapterPJInfracao == null)
			databaseAdapterPJInfracao = new AitPJDatabaseAdapter(
					context);
		return databaseAdapterPJInfracao;
	}

	public static SettingDatabaseAdapter getSettingDatabaseAdapter(Context cnt) {
		if (databaseAdapterSetting == null) {
			databaseAdapterSetting = new SettingDatabaseAdapter(cnt);
		}
		return databaseAdapterSetting;
	}

	public static SearchPlateDatabaseAdapter getSearchPlateDatabaseAdapter(
			Context context) {
		if (searchPlateDatabaseAdapter == null) {
			searchPlateDatabaseAdapter = new SearchPlateDatabaseAdapter(context);
		}
		return searchPlateDatabaseAdapter;
	}

	public synchronized void close() {
		searchPlateDatabaseAdapter.close();
		databaseAdapterSetting.close();
		databaseAdapterAutoInfracao.close();
		prepopulatedDBOpenHelper.close();
		tcaDatabaseAdapter.close();
	}

	public static PrepopulatedDBOpenHelper getPrepopulatedDBOpenHelper(
			Context context) {
		if (prepopulatedDBOpenHelper == null) {
			prepopulatedDBOpenHelper = new PrepopulatedDBOpenHelper(context);
		}
		return prepopulatedDBOpenHelper;
	}

	public static SearchDataInCard getSearchDataInCard(
			Context context) {
		if (searchDataInCard == null) {
			searchDataInCard = new SearchDataInCard(context);
		}
		return searchDataInCard;
	}

}
