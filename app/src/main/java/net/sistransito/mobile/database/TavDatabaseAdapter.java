package net.sistransito.mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sistransito.mobile.tav.TavData;
import net.sistransito.mobile.timeandime.TimeAndIme;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class TavDatabaseAdapter {
	private TimeAndIme ime;
	private SQLiteDatabase database;
	private TavDatabaseHelper databaseHelper;
	private Context context;

	public TavDatabaseAdapter(Context context) {
		ime = new TimeAndIme(context);
		databaseHelper = new TavDatabaseHelper(context);
		database = databaseHelper.getWritableDatabase(ime.getIME());
		this.context = context;
	}

	// public DadosAuto getDataAutoFromPlate(String placa) {
	//
	// Cursor myCursor = this.database.query(TAVDatabaseHelper.TABLE_NAME,
	// null, TAVDatabaseHelper.PLACA + "=?",
	// new String[] { placa + "" }, null, null, null);
	//
	// if (myCursor.getCount() > 0) {
	// DadosAuto autode_data = new DadosAuto();
	// myCursor.moveToFirst();
	// autode_data.setAutoDeDataFromCursor(myCursor);
	// myCursor.close();
	// return autode_data;
	// } else {
	// myCursor.close();
	// return null;
	// }
	// }

	// public boolean isSamePlacaExist(String placa) {
	// Cursor myCursor = this.database.query(
	// TAVDatabaseHelper.TABLE_NAME, null,
	// TAVDatabaseHelper.PLACA + "=?", new String[] { placa
	// + "" }, null, null, null);
	// if (myCursor.getCount() > 0) {
	// myCursor.close();
	// return true;
	// } else {
	// myCursor.close();
	// return false;
	// }
	// }

	public void close() {
		database.close();
	}

	public boolean setData(TavData data) {
		ContentValues values = new ContentValues();

		values.put(TavDatabaseHelper.PLATE, data.getPlate());

		values.put(TavDatabaseHelper.AIT_NUMBER, data.getAitNumber());
		values.put(TavDatabaseHelper.OWNER_NAME,
				data.getOwnerName());
		values.put(TavDatabaseHelper.CPF_CNPJ, data.getCpfCnpj());
		values.put(TavDatabaseHelper.RENAVAM_NUMBER,
				data.getRenavamNumber());
		values.put(TavDatabaseHelper.CHASSI_NUMBER,
				data.getChassisNumber());

		// Estrutur Veiculo

		values.put(TavDatabaseHelper.LEVER_HEAD,
				data.getLeverHead());
		values.put(TavDatabaseHelper.BODYWORK, data.getCarBody());
		values.put(TavDatabaseHelper.CEILING, data.getCeiling());
		values.put(TavDatabaseHelper.HOOD_BODYWORK, data.getHoodBody());
		values.put(TavDatabaseHelper.BODYWORK_RIGHT_SIDE,
				data.getBodyworkRightSide());
		values.put(TavDatabaseHelper.BODYWORK_LEFT_SIDE,
				data.getBodyWorkLeftSide());
		values.put(TavDatabaseHelper.TRUNK_BODYWORK,
				data.getTrunkBodywork());
		values.put(TavDatabaseHelper.ROOF_BODYWORK, data.getRoofBodywork());

		// public static final String MOTOR = "motor";
		// public static final String PAINEL = "panel";
		// public static final String PINTURA_CAPO = "pintura_capo";
		// public static final String PINTURA_LADO_DIREITO =
		// "pintura_lado_direito";
		// public static final String PINTURA_LADO_ESQUERDO =
		// "pintura_lado_esquerdo";
		// public static final String PINTURA_PORTA_MALA = "pintura_porta_mala";
		// public static final String PINTURA_TETO = "pintura_teto";
		// public static final String RADIADOR = "radiador";
		// public static final String VIDROS_LATERAIS = "vidros_laterais";
		// public static final String VIDRO_PARA_BRISA = "vidro_para_brisa";
		// public static final String VIDRO_TRASEIRO = "vidro_traseiro";

		values.put(TavDatabaseHelper.ENGINE, data.getEngine());
		values.put(TavDatabaseHelper.DASHBOARD, data.getDashboard());
		values.put(TavDatabaseHelper.HOOD_PAINTING, data.getHoodPaint());
		values.put(TavDatabaseHelper.WRIGHT_PAINTING,
				data.getRightSidePaint());
		values.put(TavDatabaseHelper.LEFT_PAINTING,
				data.getLeftSidePaint());
		values.put(TavDatabaseHelper.TRUNK_PAINTING,
				data.getTrunkPainting());
		values.put(TavDatabaseHelper.HOOF_PAINTING, data.getHoodPainting());
		values.put(TavDatabaseHelper.RADIATOR, data.getRadiator());
		values.put(TavDatabaseHelper.SIDE_GLASS, data.getSideGlass());
		values.put(TavDatabaseHelper.WINDSHIELD,
				data.getWindShield());
		values.put(TavDatabaseHelper.HEAR_WINDSHIELD, data.getRearWindshield());

		// Ve�culo
		// Acess�rios
		// public static final String ANTENA_DE_RADIO = "sp_radio_antenna";
		// public static final String BAGAGEIRO = "sp_baggage_handler";
		// public static final String BANCOS = "sp_seat";
		// public static final String BATERIA = "sp_battery";
		// public static final String CALOTA = "sp_hubcap";
		values.put(TavDatabaseHelper.RADIO_ANTENNA, data.getAntenna());
		values.put(TavDatabaseHelper.TRUNK, data.getTrunk());
		values.put(TavDatabaseHelper.SEAT, data.getSeats());
		values.put(TavDatabaseHelper.BATTERY, data.getBaterry());
		values.put(TavDatabaseHelper.HUBCAP, data.getWheelCover());

		// public static final String CONDICIONADOR_DE_AR =
		// "sp_air_conditioner";
		// public static final String EXTINTOR_DE_INCENDIO =
		// "sp_fire_extinguisher";
		// public static final String FAROLETE_DIANTEIRO = "sp_headlight";
		// public static final String FAROLETE_TRASEIRO = "rear_light";
		// public static final String MACACO = "sp_wheel_jack";

		values.put(TavDatabaseHelper.AIR_CONDITIONER,
				data.getAirConditioner());
		values.put(TavDatabaseHelper.FIRE_EXTINGUISHER,
				data.getFireExtinguisher());
		values.put(TavDatabaseHelper.HEADLIGHT,
				data.getHeadLight());
		values.put(TavDatabaseHelper.REAR_LIGHT,
				data.getRearLight());
		values.put(TavDatabaseHelper.JACK, data.getEngine());

		// public static final String PARA_CHOQUE_DIANTEIRO =
		// "sp_front_bumper";
		// public static final String PARA_CHOQUE_TRASEIRO =
		// "sp_rear_bumper";
		// public static final String PARA_SOL_DO_CONDUTOR =
		// "sp_driver_sunshade";
		// public static final String PNEUS = "sp_tires";
		// public static final String PNEUS_ESTEPE = "sp_step_tire";

		values.put(TavDatabaseHelper.FRONT_BUMPER,
				data.getFrontBumper());
		values.put(TavDatabaseHelper.REAR_BUMPER,
				data.getHearBumper());
		values.put(TavDatabaseHelper.DRIVER_SUNSHADE,
				data.getDriverSunVisor());
		values.put(TavDatabaseHelper.TIRES, data.getTires());
		values.put(TavDatabaseHelper.SPARE_TIRE, data.getSpareTire());

		// public static final String RADIO = "radio";
		// public static final String RETROVISOR_INTERNO = "sp_internal_rearview";
		// public static final String RETROVISOR_EXTERNO_DIREITO =
		// "sp_right_outside_mirror";
		// public static final String TAPETE = "sp_carpet";
		// public static final String TRIANGULO = "sp_triangle";
		// public static final String VOLANTE = "sp_steering_wheel";
		// public static final String GUIDAM = "sp_handlebars";

		values.put(TavDatabaseHelper.RADIO, data.getRadio());
		values.put(TavDatabaseHelper.REARVIEW_MIRROR,
				data.getRearviewMirror());
		values.put(TavDatabaseHelper.OUTSIDE_MIRROR,
				data.getOutsideMirror());
		values.put(TavDatabaseHelper.CARPET, data.getCarpet());
		values.put(TavDatabaseHelper.TRIANGLE, data.getTriangle());
		values.put(TavDatabaseHelper.STEERING_WHEEL, data.getSteeringWheel());
		values.put(TavDatabaseHelper.HANDLEBARS, data.getMotorcycleHandlebar());

		// // Geral
		// public static final String ODOMETRO = "odometro";
		// public static final String MARCADOR_DE_CONBUTIVEL =
		// "marcador_de_conbutivel";
		// public static final String REMOCAO_ATRAVES_DE = "removal_through_of";
		// public static final String OBSERVACAO = "observacao";
		//
		// public static final String NOME_DA_EMPRESA = "nome_da_empresa";
		// public static final String NOME_DO_CONDUTOR_DO_GUINCHO =
		// "nome_condutor_guincho";

		values.put(TavDatabaseHelper.ODOMETRO, data.getOdometer());
		values.put(TavDatabaseHelper.FUEL_MARKER,
				data.getFuelGauge());
		values.put(TavDatabaseHelper.REMOVAL_THROUGH_OF,
				data.getRemovedVia());
		values.put(TavDatabaseHelper.OBSERVATION, data.getObservation());
		values.put(TavDatabaseHelper.COMPANY_NAME, data.getCompanyName());
		values.put(TavDatabaseHelper.TOW_TRUCK_DRIVER_NAME,
				data.getWinchDriverName());

		values.put(TavDatabaseHelper.TAV_NUMBER, data.getTavNumber());

		long insert = this.database.insert(TavDatabaseHelper.TABLE_NAME, null,
				values);

		if (insert > 0) {
			(DatabaseCreator.getNumberDatabaseAdapter(context))
					.deleteTavNumber(data.getTavNumber());

			(DatabaseCreator.getBalanceDatabaseAdapter(context))
					.setTavPerformed();
			(DatabaseCreator.getBalanceDatabaseAdapter(context))
					.setTavRemaining((DatabaseCreator
							.getNumberDatabaseAdapter(context))
							.getRemainTavNumber());

			return true;
		} else {
			return false;
		}
	}

	public Cursor getTavCursor() {
		Cursor myCursor = this.database.query(TavDatabaseHelper.TABLE_NAME,
				null, null, null, null, null, TavDatabaseHelper.COLUMN_ID
						+ " DESC");
		return myCursor;

	}

	public Cursor getTAVCursorFromID(int id) {
		Cursor myCursor = this.database.query(TavDatabaseHelper.TABLE_NAME,
				null, TavDatabaseHelper.COLUMN_ID + "=?", new String[] { id
						+ "" }, null, null, null);
		return myCursor;
	}

	public String tavComposeJSONfromSQLite() {
		ArrayList<HashMap<String, String>> tavList = new ArrayList<HashMap<String, String>>();
		Cursor cursor = this.database.query(TavDatabaseHelper.TABLE_NAME, null,
				null, null, null, null, null);
		TavData data;
		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
			do {
				data = new TavData();
				data.setTAVDataFromCursor(cursor);
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				Log.d("ppp", data.getPlate());
				map.put(TavDatabaseHelper.PLATE, data.getPlate());
				// Condutor
				map.put(TavDatabaseHelper.AIT_NUMBER,
						data.getAitNumber());
				map.put(TavDatabaseHelper.OWNER_NAME,
						data.getOwnerName());
				map.put(TavDatabaseHelper.CPF_CNPJ, data.getCpfCnpj());
				map.put(TavDatabaseHelper.RENAVAM_NUMBER,
						data.getRenavamNumber());
				map.put(TavDatabaseHelper.CHASSI_NUMBER,
						data.getChassisNumber());
				// Ve�culo
				// Estrutur
				map.put(TavDatabaseHelper.LEVER_HEAD,
						data.getLeverHead());
				map.put(TavDatabaseHelper.BODYWORK, data.getCarBody());
				map.put(TavDatabaseHelper.CEILING, data.getCeiling());
				map.put(TavDatabaseHelper.HOOD_BODYWORK, data.getHoodBody());
				map.put(TavDatabaseHelper.BODYWORK_RIGHT_SIDE,
						data.getBodyworkRightSide());
				map.put(TavDatabaseHelper.BODYWORK_LEFT_SIDE,
						data.getBodyWorkLeftSide());
				map.put(TavDatabaseHelper.TRUNK_BODYWORK,
						data.getTrunkBodywork());
				map.put(TavDatabaseHelper.ROOF_BODYWORK, data.getRoofBodywork());
				map.put(TavDatabaseHelper.ENGINE, data.getEngine());
				map.put(TavDatabaseHelper.DASHBOARD, data.getDashboard());
				map.put(TavDatabaseHelper.HOOD_PAINTING, data.getHoodPaint());
				map.put(TavDatabaseHelper.WRIGHT_PAINTING,
						data.getRightSidePaint());
				map.put(TavDatabaseHelper.LEFT_PAINTING,
						data.getLeftSidePaint());
				map.put(TavDatabaseHelper.TRUNK_PAINTING,
						data.getTrunkPainting());
				map.put(TavDatabaseHelper.HOOF_PAINTING, data.getHoodPainting());
				map.put(TavDatabaseHelper.RADIATOR, data.getRadiator());
				map.put(TavDatabaseHelper.SIDE_GLASS,
						data.getSideGlass());
				map.put(TavDatabaseHelper.WINDSHIELD,
						data.getWindShield());
				map.put(TavDatabaseHelper.HEAR_WINDSHIELD,
						data.getRearWindshield());

				// Ve�culo
				// Acess�rios

				map.put(TavDatabaseHelper.RADIO_ANTENNA,
						data.getAntenna());
				map.put(TavDatabaseHelper.TRUNK, data.getSeats());
				map.put(TavDatabaseHelper.SEAT, data.getSeats());
				map.put(TavDatabaseHelper.BATTERY, data.getBaterry());
				map.put(TavDatabaseHelper.HUBCAP, data.getWheelCover());
				map.put(TavDatabaseHelper.AIR_CONDITIONER,
						data.getAirConditioner());
				map.put(TavDatabaseHelper.FIRE_EXTINGUISHER,
						data.getFireExtinguisher());
				map.put(TavDatabaseHelper.HEADLIGHT,
						data.getHeadLight());
				map.put(TavDatabaseHelper.REAR_LIGHT,
						data.getRearLight());
				map.put(TavDatabaseHelper.JACK, data.getJack());
				map.put(TavDatabaseHelper.FRONT_BUMPER,
						data.getFrontBumper());
				map.put(TavDatabaseHelper.REAR_BUMPER,
						data.getHearBumper());
				map.put(TavDatabaseHelper.DRIVER_SUNSHADE,
						data.getDriverSunVisor());
				map.put(TavDatabaseHelper.TIRES, data.getTires());
				map.put(TavDatabaseHelper.SPARE_TIRE, data.getSpareTire());
				map.put(TavDatabaseHelper.RADIO, data.getRadio());
				map.put(TavDatabaseHelper.REARVIEW_MIRROR,
						data.getRearviewMirror());
				map.put(TavDatabaseHelper.OUTSIDE_MIRROR,
						data.getOutsideMirror());
				map.put(TavDatabaseHelper.CARPET, data.getCarpet());
				map.put(TavDatabaseHelper.TRIANGLE, data.getTriangle());
				map.put(TavDatabaseHelper.STEERING_WHEEL, data.getSteeringWheel());
				map.put(TavDatabaseHelper.HANDLEBARS, data.getMotorcycleHandlebar());

				// Geral
				map.put(TavDatabaseHelper.ODOMETRO, data.getOdometer());
				map.put(TavDatabaseHelper.FUEL_MARKER,
						data.getFuelGauge());
				map.put(TavDatabaseHelper.REMOVAL_THROUGH_OF,
						data.getRemovedVia());
				map.put(TavDatabaseHelper.OBSERVATION, data.getObservation());
				map.put(TavDatabaseHelper.COMPANY_NAME,
						data.getCompanyName());
				map.put(TavDatabaseHelper.TOW_TRUCK_DRIVER_NAME,
						data.getWinchDriverName());
				map.put(TavDatabaseHelper.TAV_NUMBER, data.getTavNumber());
	
				tavList.add(map);
			} while (cursor.moveToNext());
		} else {

			return null;
		}
		cursor.close();
		Gson gson = new GsonBuilder().create();
		// Use GSON to serialize Array List to JSON
		return gson.toJson(tavList);
	}

	public void TavUpdateSyncStatus(String numero_tav) {
		this.database.delete(TavDatabaseHelper.TABLE_NAME,
				TavDatabaseHelper.TAV_NUMBER + "=?",
				new String[] { numero_tav });
	}

	/**
	 * Verifica se já existe um TAV associado ao número do AIT
	 * @param aitNumber Número do AIT
	 * @return true se existe TAV para o AIT, false caso contrário
	 */
	public boolean existsTavForAit(String aitNumber) {
		Cursor cursor = null;
		try {
			cursor = this.database.query(
				TavDatabaseHelper.TABLE_NAME,
				new String[]{TavDatabaseHelper.TAV_NUMBER},
				TavDatabaseHelper.AIT_NUMBER + "=?",
				new String[]{aitNumber},
				null, null, null
			);
			return cursor.getCount() > 0;
		} catch (Exception e) {
			Log.e("TavDatabaseAdapter", "Erro ao verificar TAV para AIT: " + aitNumber, e);
			return false;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Busca o TAV existente associado ao número do AIT
	 * @param aitNumber Número do AIT
	 * @return TavData do TAV encontrado ou null se não existir
	 */
	public TavData getTavByAitNumber(String aitNumber) {
		Cursor cursor = null;
		try {
			cursor = this.database.query(
				TavDatabaseHelper.TABLE_NAME,
				null,
				TavDatabaseHelper.AIT_NUMBER + "=?",
				new String[]{aitNumber},
				null, null, null
			);
			
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				TavData tavData = new TavData();
				tavData.setTAVDataFromCursor(cursor);
				return tavData;
			}
			return null;
		} catch (Exception e) {
			Log.e("TavDatabaseAdapter", "Erro ao buscar TAV por AIT: " + aitNumber, e);
			return null;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Exclui todos os dados da tabela TAV
	 * @return Número de registros excluídos
	 */
	public int deleteAllTavData() {
		try {
			int deletedRows = this.database.delete(TavDatabaseHelper.TABLE_NAME, null, null);
			Log.d("TavDatabaseAdapter", "Excluídos " + deletedRows + " TAVs");
			return deletedRows;
		} catch (Exception e) {
			Log.e("TavDatabaseAdapter", "Erro ao excluir todos os TAVs", e);
			return 0;
		}
	}
}
