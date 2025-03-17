package net.sistransito.mobile.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import net.sistransito.mobile.ait.AitActivity;
import net.sistransito.mobile.aitcompany.AitCompanyActivity;
import net.sistransito.mobile.rrd.RrdData;
import net.sistransito.mobile.utility.Routine;
import net.sistransito.R;

public class AnyAlertDialog {

	public static void dialogShow(String mgs, Context context, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(mgs);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Ação ao clicar em OK (pode deixar vazio se não precisar de ação)
			}
		});
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	public static void simpleAlertMessage(String message, Context context) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setCancelable(false);
		dialog.setMessage(message);
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public static void dialogView(final Context context, String title, final String type) {
		LayoutInflater linear = LayoutInflater.from(context);
		View viewCancel = linear.inflate(R.layout.ait_cancel, null);
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setView(viewCancel);

		final EditText etReason = viewCancel.findViewById(R.id.edit_reason);
		alertDialog.setTitle(title);

		alertDialog.setPositiveButton(context.getString(R.string.btn_send), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (etReason.getText().toString().equals("")) {
					Routine.showAlert(context.getString(R.string.alert_motive), context);
				} else {
					if (type.equals("ait")) {
						((AitActivity) context).setCancel(context, etReason.getText().toString());
					} else {
						((AitCompanyActivity) context).setCancel(context, etReason.getText().toString());
					}
					((Activity) context).finish();
				}
			}
		});
		alertDialog.setNegativeButton(context.getString(R.string.btn_back), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		AlertDialog dialog = alertDialog.create();
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.rgb(100, 100, 100));
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(100, 100, 100));
	}

	public static void dialogShow(RrdData rrdData, Context context, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// builder.setTitle(title); // Comentado no original, mantido assim

		LayoutInflater inflater = LayoutInflater.from(context);
		View dialogView = inflater.inflate(R.layout.rrd_preview, null);
		builder.setView(dialogView);

		// Preenche os TextViews com os dados de rrdData
		rrdData.fillRrdData(dialogView, context);

		builder.setPositiveButton("OK", (dialog, which) -> {
			// Ação ao clicar em OK (vazia por enquanto)
		});

		builder.setIcon(android.R.drawable.ic_dialog_info);
		AlertDialog alertDialog = builder.create();

		// Aplicar o fundo com cantos arredondados
		alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog_background);

		alertDialog.show();
	}

}