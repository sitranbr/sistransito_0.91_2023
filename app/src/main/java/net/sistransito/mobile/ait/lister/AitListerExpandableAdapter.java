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
				TavData data = new TavData();
				data.setPlate(aitData.getPlate());
				data.setAitNumber(aitData.getAitNumber());
				data.setChassisNumber(aitData.getChassi());
				data.setRenavamNumber(aitData.getRenavam());
				openTAV(data);

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

}
