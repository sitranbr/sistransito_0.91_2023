package net.sistransito.mobile.rrd.lister;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import net.sistransito.mobile.ait.AitData;
import net.sistransito.mobile.database.InfractionDatabaseHelper;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.RrdDatabaseHelper;
import net.sistransito.mobile.bluetoothprint.bluetooth.BluetoothPrinterListener;
import net.sistransito.mobile.rrd.RrdData;
import net.sistransito.R;

public class RrdListerExpandableAdapter extends CursorTreeAdapter {
	private static final String TAG = "RrdListerAdapter";
	private LayoutInflater mInflator;
	private Context mycontext;
	private BluetoothPrinterListener printListener;

	public RrdListerExpandableAdapter(Cursor cursor, Context context) {
		super(cursor, context);
		mycontext = context;
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
		TextView tvLogPlate, tvLogDocument;
		tvLogPlate = (TextView) view.findViewById(R.id.lv_parent_rrd_plate);
		tvLogDocument = (TextView) view.findViewById(R.id.lv_parent_rrd_document);

		try {
			if(cursor.getString(cursor.getColumnIndex(RrdDatabaseHelper.RRD_TYPE)).equals("avulso")) {
				tvLogPlate.setText(cursor.getString(cursor
						.getColumnIndex(RrdDatabaseHelper.RRD_NUMBER)) + " - Avulso");
			}else{
				tvLogPlate.setText(cursor.getString(cursor
						.getColumnIndex(RrdDatabaseHelper.PLATE)));
			}
			tvLogDocument.setText("Documento: " + cursor.getString(cursor
					.getColumnIndex(RrdDatabaseHelper.DOCUMENT_TYPE)));
		} catch (Exception e) {
			android.util.Log.e(TAG, "Erro ao bind group view", e);
			tvLogPlate.setText("Erro ao carregar");
			tvLogDocument.setText("Documento: N/A");
		}
	}

	@Override
	protected View newChildView(Context arg0, Cursor arg1, boolean arg2,
								ViewGroup arg3) {
		return null;
	}

	@Override
	protected View newGroupView(Context context, Cursor cursor,
								boolean isExpendable, ViewGroup parent) {
		View view = mInflator
				.inflate(R.layout.rrd_list_listview_parent, null);
		TextView tvLogPlate, tvLogDocument;
		tvLogPlate = (TextView) view.findViewById(R.id.lv_parent_rrd_plate);
		tvLogDocument = (TextView) view.findViewById(R.id.lv_parent_rrd_document);

		tvLogPlate.setText(cursor.getString(cursor.getColumnIndex(RrdDatabaseHelper.RRD_NUMBER)));
		tvLogDocument.setText(cursor.getString(cursor
				.getColumnIndex(RrdDatabaseHelper.DOCUMENT_TYPE)));

		return view;
	}

	@Override
	protected Cursor getChildrenCursor(Cursor groupCursor) {
		int groupId = groupCursor.getInt(groupCursor
				.getColumnIndex(InfractionDatabaseHelper.COLUMN_ID));
		return (DatabaseCreator.getRrdDatabaseAdapter(mycontext))
				.getRrdCursorFromID(groupId);
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
							 boolean isLastChild, View view, ViewGroup parent) {
		TextView verRrdChildview;
		android.widget.ImageButton btnRrdPrint;
		View mView = null;
		if (view != null) {
			mView = view;
		} else {
			mView = mInflator.inflate(R.layout.rrd_list_listview_child,
					parent, false);
		}
		
		try {
			final RrdData RRDData = new RrdData();
			
			// Verificar se o cursor está válido antes de usar
			Cursor groupCursor = getGroup(groupPosition);
			android.util.Log.d(TAG, "Group position: " + groupPosition + ", Cursor: " + (groupCursor != null ? "not null" : "null"));
			
			if (groupCursor != null && !groupCursor.isClosed()) {
				android.util.Log.d(TAG, "Cursor count: " + groupCursor.getCount() + ", Position: " + groupCursor.getPosition());
				RRDData.setRRDDataFromCursor(groupCursor);
				
				// Preencher os campos da tabela
				fillTableFields(mView, RRDData);
				
			} else {
				android.util.Log.w(TAG, "Cursor is null or closed");
				showErrorMessage(mView, "Erro ao carregar dados");
			}
			
			btnRrdPrint = (android.widget.ImageButton) mView.findViewById(R.id.rl_btn_rrd_print);
			if (btnRrdPrint != null) {
				btnRrdPrint.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						try {
							AitData aitData = DatabaseCreator
									.getInfractionDatabaseAdapter(mycontext)
									.getDataFromAitNumber(
											RRDData.getAitNumber());

							printListener.print(RRDData, aitData);
						} catch (Exception e) {
							// Log do erro e mostrar mensagem para o usuário
							android.util.Log.e(TAG, "Erro ao imprimir RRD", e);
							android.widget.Toast.makeText(mycontext, 
								"Erro ao imprimir documento", 
								android.widget.Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		} catch (Exception e) {
			android.util.Log.e(TAG, "Erro ao criar view do child", e);
			showErrorMessage(mView, "Erro ao carregar dados do documento");
		}
		
		return mView;
	}
	
	private void fillTableFields(View view, RrdData rrdData) {
		try {
			// Preencher campos da tabela
			setTextViewText(view, R.id.tv_rrd_number_value, rrdData.getRrdNumber());
			setTextViewText(view, R.id.tv_ait_number_value, rrdData.getAitNumber());
			setTextViewText(view, R.id.tv_driver_name_value, rrdData.getDriverName());
			setTextViewText(view, R.id.tv_registration_value, rrdData.getRegistrationNumber());
			setTextViewText(view, R.id.tv_validity_value, rrdData.getValidity());
			setTextViewText(view, R.id.tv_state_value, rrdData.getPlateState());
			setTextViewText(view, R.id.tv_reason_value, rrdData.getReasonCollected());
			setTextViewText(view, R.id.tv_date_value, rrdData.getDateCollected());
			setTextViewText(view, R.id.tv_time_value, rrdData.getTimeCollected());
			setTextViewText(view, R.id.tv_city_value, rrdData.getCityCollected());
			setTextViewText(view, R.id.tv_collection_state_value, rrdData.getStateCollected());
			
			// Dados do agente (usuário)
			net.sistransito.mobile.utility.User user = net.sistransito.mobile.appobject.AppObject.getTinyDB(mycontext)
				.getObject(net.sistransito.mobile.appconstants.AppConstants.user, net.sistransito.mobile.utility.User.class);
			if (user != null) {
				setTextViewText(view, R.id.tv_agent_name_value, user.getCompanyName());
				setTextViewText(view, R.id.tv_agent_registration_value, user.getRegistration());
			}
			
		} catch (Exception e) {
			android.util.Log.e(TAG, "Erro ao preencher campos da tabela", e);
			showErrorMessage(view, "Erro ao carregar dados");
		}
	}
	
	private void setTextViewText(View parentView, int textViewId, String text) {
		TextView textView = parentView.findViewById(textViewId);
		if (textView != null) {
			textView.setText(text != null ? text : "");
		}
	}
	
	private void showErrorMessage(View view, String message) {
		// Criar uma mensagem de erro simples
		android.widget.LinearLayout errorLayout = new android.widget.LinearLayout(mycontext);
		errorLayout.setOrientation(android.widget.LinearLayout.VERTICAL);
		errorLayout.setPadding(16, 16, 16, 16);
		
		TextView errorText = new TextView(mycontext);
		errorText.setText(message);
		errorText.setTextColor(android.graphics.Color.RED);
		errorText.setGravity(android.view.Gravity.CENTER);
		errorText.setTextAppearance(mycontext, android.R.style.TextAppearance_Medium);
		
		errorLayout.addView(errorText);
		
		// Substituir o conteúdo da LinearLayout principal
		android.widget.LinearLayout mainLayout = view.findViewById(R.id.rrd_table_layout);
		if (mainLayout != null) {
			mainLayout.removeAllViews();
			mainLayout.addView(errorLayout);
		}
	}
}
