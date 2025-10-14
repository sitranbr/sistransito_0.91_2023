package net.sistransito.mobile.ait.lister;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorTreeAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.sistransito.mobile.ait.AitActivity;
import net.sistransito.mobile.ait.AitData;
import net.sistransito.mobile.database.InfractionDatabaseHelper;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.bluetoothprint.bluetooth.BluetoothPrinterListener;
import net.sistransito.mobile.rrd.RrdActivity;
import net.sistransito.mobile.rrd.RrdData;
import net.sistransito.mobile.tav.TavActivity;
import net.sistransito.mobile.tav.TavData;
import net.sistransito.mobile.tca.TcaActivity;
import net.sistransito.mobile.tca.TcaData;
import net.sistransito.R;

public class AitListerExpandableAdapter extends CursorTreeAdapter {
	private LayoutInflater mInflator;
	private Context context;
	private BluetoothPrinterListener printListener;
	private String vehiclePlate;

	public AitListerExpandableAdapter(Cursor cursor, Context context) {
		super(cursor, context);
		this.context = context;
		mInflator = LayoutInflater.from(context);
		printListener = (BluetoothPrinterListener) context;
	}

	@Override
	protected void bindChildView(View arg0, Context arg1, Cursor arg2,
			boolean arg3) {
	}

	@Override
	protected void bindGroupView(View view, Context arg1, Cursor cursor,
			boolean arg3) {
		TextView logPlate, logModel, logArticle, logAit;

		logPlate = (TextView) view.findViewById(R.id.lv_rrd_plate);
		logModel = (TextView) view.findViewById(R.id.log_model);
		logArticle = (TextView) view.findViewById(R.id.log_art);
		logAit = (TextView) view.findViewById(R.id.log_ait);

		logPlate.setText(cursor.getString(cursor
				.getColumnIndex(InfractionDatabaseHelper.PLATE)));
		logModel.setText(cursor.getString(cursor
				.getColumnIndex(InfractionDatabaseHelper.VEHICLE_MODEL)));
		logArticle.setText(cursor.getString(cursor
				.getColumnIndex(InfractionDatabaseHelper.ARTICLE)));
		logAit.setText(cursor.getString(cursor
				.getColumnIndex(InfractionDatabaseHelper.AIT_NUMBER)));

		vehiclePlate = cursor.getString(cursor
				.getColumnIndex(InfractionDatabaseHelper.PLATE));

		/*TimeAndIme dozeHoras = new TimeAndIme(mycontext);

		Log.d("Hora depois das doze: ", String.valueOf(dozeHoras.addHalfDay()));

		String horaInicio = "02:10:30";
		String horaFim = "10:10:30";

		Log.d("Hora subtraida: ", String.valueOf(dozeHoras.subtraiHoras(horaInicio, horaFim)));

		Coordenadas location = new Coordenadas();

		Log.d("Localização: ", String.valueOf(location));*/

	}

	@Override
	protected View newChildView(Context arg0, Cursor arg1, boolean arg2,
			ViewGroup arg3) {
		return null;
	}

	@Override
	protected View newGroupView(Context context, Cursor cursor,
			boolean isExpendable, ViewGroup parent) {
		View view = mInflator.inflate(R.layout.ait_list_listview_parent,
				null);
		TextView logPlate, logModel, logArticle, logAit;
		ImageView imgSync;

		logPlate = (TextView) view.findViewById(R.id.lv_rrd_plate);
		logModel = (TextView) view.findViewById(R.id.log_model);
		logArticle = (TextView) view.findViewById(R.id.log_art);
		logAit = (TextView) view.findViewById(R.id.log_ait);
		imgSync = (ImageView) view.findViewById(R.id.img_sinc);

		String sync = (cursor.getString(cursor
				.getColumnIndex(InfractionDatabaseHelper.SYNC_STATUS)));

		String plate = (cursor.getString(cursor
				.getColumnIndex(InfractionDatabaseHelper.PLATE)));

		//DatabaseCreator.getDatabaseAdapterAitInfraction(context).aitUpdateSyncStatus("TL00006683");

		try {
			if(sync.equals("0")) {
				imgSync.setImageDrawable(context.getDrawable(R.drawable.icons_sincronizar));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logPlate.setText(cursor.getString(cursor
				.getColumnIndex(InfractionDatabaseHelper.PLATE)));
		logModel.setText(cursor.getString(cursor
				.getColumnIndex(InfractionDatabaseHelper.VEHICLE_MODEL)));
		logArticle.setText(cursor.getString(cursor
				.getColumnIndex(InfractionDatabaseHelper.ARTICLE)));
		logAit.setText(cursor.getString(cursor
				.getColumnIndex(InfractionDatabaseHelper.AIT_NUMBER)));

		return view;
	}

	@Override
	protected Cursor getChildrenCursor(Cursor groupCursor) {
		int groupId = groupCursor.getInt(groupCursor
				.getColumnIndex(InfractionDatabaseHelper.COLUMN_ID));
		return (DatabaseCreator.getInfractionDatabaseAdapter(context))
				.getAitCursorFromID(groupId);
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		// final int gPosition = groupPosition;
		TextView tvAitChildView;
		FrameLayout fragmentContainerAit;
		Button btnOpenTav, btnOpenTca, btnOpenRrd, btnNewAit;
		android.widget.ImageButton btnPrintAit;

		View mView = view != null ? view : mInflator.inflate(R.layout.ait_list_listview_child,
				parent, false);

		fragmentContainerAit = (FrameLayout) mView.findViewById(R.id.fragment_container_list);

		tvAitChildView = (TextView) mView.findViewById(R.id.tv_ver_dados_auto_childview);

		final AitData aitData = new AitData();

		aitData.setAitDataFromCursor(getGroup(groupPosition));

		/*fragmentContainerAit.setVisibility(view.VISIBLE);
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.fragment_container_list, AitPreviewFragment.newInstance()).commit();*/

		tvAitChildView.setText(aitData.getAitListViewData(context));

		btnOpenTav = (Button) mView.findViewById(R.id.btn_open_tav);
		btnOpenTca = (Button) mView.findViewById(R.id.btn_open_tca);
		btnOpenRrd = (Button) mView.findViewById(R.id.btn_open_rrd);
		btnPrintAit = (android.widget.ImageButton) mView.findViewById(R.id.btn_print_ait);
		btnNewAit = (Button) mView.findViewById(R.id.btn_new_ait);

		// Verificar se já existe TAV para este AIT e ajustar visual e comportamento
		boolean tavExists = DatabaseCreator.getTavDatabaseAdapter(context).existsTavForAit(aitData.getAitNumber());
		if (tavExists) {
			btnOpenTav.setEnabled(true);
			btnOpenTav.setBackgroundResource(R.drawable.btn_tav_view);
			btnOpenTav.setText(context.getString(R.string.tav) + " (Ver)");
		} else {
			btnOpenTav.setEnabled(true);
			btnOpenTav.setBackgroundResource(R.drawable.btn_green);
			btnOpenTav.setText(context.getString(R.string.tav));
		}

		btnNewAit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				/*openAitData((DatabaseCreator
						.getInfractionDatabaseAdapter(context))
						.getAitDataFromPlate(vehiclePlate));*/

				dialogNewAit(context.getString(R.string.question_new_ait) + vehiclePlate + " ?", context);

			}

		});

		btnOpenTav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Verificar se já existe TAV para este AIT
				boolean tavExists = DatabaseCreator.getTavDatabaseAdapter(context).existsTavForAit(aitData.getAitNumber());
				
				if (tavExists) {
					// Se TAV já existe, navegar para o TAV existente
					openExistingTav(aitData.getAitNumber());
				} else {
					// Se TAV não existe, criar novo TAV
					TavData data = new TavData();
					data.setPlate(aitData.getPlate());
					data.setAitNumber(aitData.getAitNumber());
					data.setChassisNumber(aitData.getChassi());
					data.setRenavamNumber(aitData.getRenavam());
					openTAV(data);
				}
			}
		});

		btnOpenTca.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TcaData data = new TcaData();
				data.setDriverName(aitData.getConductorName());
				data.setCnhPpd(aitData.getCnhPpd());
				data.setCpf(aitData.getCnhState());
				data.setPlate(aitData.getPlate());
				data.setPlateState(aitData.getState());
				data.setBrandModel(aitData.getVehicleModel());
				data.setAitNumber(aitData.getAitNumber());
				openTCA(data);

			}
		});

		btnOpenRrd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				RrdData rrdData = new RrdData();
				rrdData.setAitNumber(aitData.getAitNumber());
				rrdData.setPlate(aitData.getPlate());
				rrdData.setDriverName(aitData.getConductorName());
				rrdData.setPlateState(aitData.getState());
				rrdData.setRegistrationNumber(aitData.getCnhPpd());
				rrdData.setRegistrationState(aitData.getCnhState());
				rrdData.setDateCollected(aitData.getAitDate());
				rrdData.setTimeCollected(aitData.getAitTime());
				rrdData.setCityCollected(aitData.getCity());
				rrdData.setStateCollected(aitData.getState());
				openRRD(rrdData);
			}
		});

		btnPrintAit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialogPrintCopy(aitData, context);
			}
		});

		return mView;
	}

	public void dialogNewAit(String mgs, Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle(context.getString(R.string.new_ait));
		builder.setMessage(mgs);
		builder.setPositiveButton(context.getString(R.string.new_ait_yes), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				openAitData((DatabaseCreator
						.getInfractionDatabaseAdapter(AitListerExpandableAdapter.this.context))
						.getAitDataFromPlate(vehiclePlate));
			}
		});
		builder.setNegativeButton(context.getString(R.string.new_ait_out), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	public void dialogPrintCopy(final AitData aitData, Context context){
		LayoutInflater linear = LayoutInflater.from(context);
		View view = linear.inflate(R.layout.layout_option_print, null);

		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		alertDialog.setView(view);

		final AlertDialog dialog = alertDialog.create();
		dialog.show();

		Button btnAgentCopy, btnConductorCopy;

		btnAgentCopy = (Button) view.findViewById(R.id.btn_via_agente);
		btnConductorCopy = (Button) view.findViewById(R.id.btn_via_condutor);

		btnAgentCopy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				printListener.print(aitData, null, context.getString(R.string.capital_agent_copy));
				dialog.dismiss();
			}
		});

		btnConductorCopy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				printListener.print(aitData, null, context.getString(R.string.capital_driver_copy));
				dialog.dismiss();
			}
		});

	}

	private void openRRD(RrdData data) {
		Intent intent = new Intent(context, RrdActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(RrdData.getRRDId(), data);
		intent.putExtras(bundle);
		context.startActivity(intent);
		((Activity) context).finish();
	}

	private void openTCA(TcaData data) {
		Intent intent = new Intent(context, TcaActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(TcaData.getTcaId(), data);
		intent.putExtras(bundle);
		context.startActivity(intent);
		((Activity) context).finish();
	}

	private void openTAV(TavData data) {
		Intent intent = new Intent(context, TavActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(TavData.getTavId(), data);
		intent.putExtras(bundle);
		context.startActivity(intent);
		((Activity) context).finish();
	}

	private void openAitData(AitData aitData) {
		Intent intent = new Intent(context, AitActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(AitData.getAitID(), aitData);
		intent.putExtras(bundle);
		context.startActivity(intent);
		((Activity) context).finish();
	}

	/**
	 * Abre o TAV existente associado ao AIT em um diálogo
	 * @param aitNumber Número do AIT
	 */
	private void openExistingTav(String aitNumber) {
		try {
			TavData existingTav = DatabaseCreator.getTavDatabaseAdapter(context).getTavByAitNumber(aitNumber);
			if (existingTav != null) {
				showTavInfoDialog(existingTav);
			} else {
				android.widget.Toast.makeText(context, "TAV não encontrado", android.widget.Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			android.util.Log.e("AitListerAdapter", "Erro ao buscar TAV existente", e);
			android.widget.Toast.makeText(context, "Erro ao carregar informações do TAV", android.widget.Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Mostra um diálogo com as informações do TAV
	 * @param tavData Dados do TAV
	 */
	private void showTavInfoDialog(TavData tavData) {
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
		builder.setTitle("Informações do TAV");
		
		// Criar layout para mostrar as informações
		android.widget.LinearLayout layout = new android.widget.LinearLayout(context);
		layout.setOrientation(android.widget.LinearLayout.VERTICAL);
		layout.setPadding(32, 32, 32, 32);
		
		// Adicionar informações do TAV
		addInfoRow(layout, "Número do TAV:", tavData.getTavNumber());
		addInfoRow(layout, "Número do AIT:", tavData.getAitNumber());
		addInfoRow(layout, "Placa:", tavData.getPlate());
		addInfoRow(layout, "Nome do Proprietário:", tavData.getOwnerName());
		addInfoRow(layout, "Chassi:", tavData.getChassisNumber());
		addInfoRow(layout, "Renavam:", tavData.getRenavamNumber());
		
		builder.setView(layout);
		builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		// Opção para ir para a listagem completa
		builder.setNeutralButton("Ver Lista Completa", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// Navegar para a listagem completa com o TAV específico para expansão
				openTavListerWithExpansion(tavData);
			}
		});
		
		android.app.AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * Navega para o TavLister com o TAV específico para expansão automática
	 * @param tavData Dados do TAV que deve ser expandido
	 */
	private void openTavListerWithExpansion(TavData tavData) {
		try {
			Intent intent = new Intent(context, net.sistransito.mobile.tav.lister.TavLister.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("EXPAND_TAV_DATA", tavData);
			bundle.putBoolean("SHOULD_EXPAND", true);
			intent.putExtras(bundle);
			context.startActivity(intent);
			((Activity) context).finish();
		} catch (Exception e) {
			android.util.Log.e("AitListerAdapter", "Erro ao abrir TavLister com expansão", e);
			android.widget.Toast.makeText(context, "Erro ao abrir listagem de TAVs", android.widget.Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Adiciona uma linha de informação ao layout
	 */
	private void addInfoRow(android.widget.LinearLayout layout, String label, String value) {
		android.widget.LinearLayout rowLayout = new android.widget.LinearLayout(context);
		rowLayout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
		rowLayout.setPadding(0, 8, 0, 8);
		
		TextView labelView = new TextView(context);
		labelView.setText(label);
		labelView.setTextAppearance(context, android.R.style.TextAppearance_Medium);
		labelView.setTextColor(android.graphics.Color.BLACK);
		labelView.setTypeface(null, android.graphics.Typeface.BOLD);
		labelView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, 
			android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f));
		
		TextView valueView = new TextView(context);
		valueView.setText(value != null ? value : "N/A");
		valueView.setTextAppearance(context, android.R.style.TextAppearance_Medium);
		valueView.setTextColor(android.graphics.Color.BLACK);
		valueView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, 
			android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f));
		
		rowLayout.addView(labelView);
		rowLayout.addView(valueView);
		layout.addView(rowLayout);
	}

}
