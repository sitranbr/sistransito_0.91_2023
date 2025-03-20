package net.sistransito.mobile.rrd;

import android.content.Context;
import android.database.Cursor;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.appobject.AppObject;
import net.sistransito.mobile.database.RrdDatabaseHelper;
import net.sistransito.R;
import net.sistransito.mobile.utility.Routine;
import net.sistransito.mobile.utility.User;
import net.sqlcipher.SQLException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class RrdData implements Serializable {

	private static final String RRD_ID = "rrd_id";
	private static final long SERIAL_VERSION_UID = 14393750L;
	private String idField, rrdNumber, aitNumber, driverName, documentType, cnhNumber,
			crlvNumber, plate, plateState, registrationNumber, registrationState, validity,
			reasonCollected, daysForRegularization, dateCollected, timeCollected, cityCollected, stateCollected, rrdType;

	private User user;

	public String getStateCollected() {
		return stateCollected;
	}

	public void setStateCollected(String stateCollected) {
		this.stateCollected = stateCollected;
	}

	public String getRrdType() {
        return rrdType;
    }

    public void setRrdType(String rrdType) {
        this.rrdType = rrdType;
    }

    public String getRegistrationState() {
        return registrationState;
    }

    public void setRegistrationState(String registrationState) {
        this.registrationState = registrationState;
    }

    public String getDateCollected() {
        return dateCollected;
    }

    public void setDateCollected(String dateCollected) {
        this.dateCollected = dateCollected;
    }

    public String getTimeCollected() {
        return timeCollected;
    }

    public void setTimeCollected(String timeCollected) {
        this.timeCollected = timeCollected;
    }

    public String getCityCollected() {
        return cityCollected;
    }

    public void setCityCollected(String cityCollected) {
        this.cityCollected = cityCollected;
    }

    public String getRrdNumber() {
		return rrdNumber;
	}

	public void setRrdNumber(String rrdNumber) {
		this.rrdNumber = rrdNumber;
	}

	public static String getRRDId() {
		return RRD_ID;
	}

	public static long getSerialversionuid() {
		return SERIAL_VERSION_UID;
	}

	public String getIdField() {
		return idField;
	}

	public String getAitNumber() {
		return aitNumber;
	}

	public String getDriverName() {
		return driverName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public String getCrlvNumber() {
		return crlvNumber;
	}

	public String getPlate() {
		return plate;
	}

	public String getPlateState() {
		return plateState;
	}

	public String getDaysForRegularization() {
		return daysForRegularization;
	}

	public String getReasonCollected() {
		return reasonCollected;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}

	public void setAitNumber(String aitNumber) {
		this.aitNumber = aitNumber;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public void setCrlvNumber(String crlvNumber) {
		this.crlvNumber = crlvNumber;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public void setPlateState(String plateState) {
		this.plateState = plateState;
	}

	public void setDaysForRegularization(
			String daysForRegularization) {
		this.daysForRegularization = daysForRegularization;
	}

	public void setReasonCollected(String reasonCollected) {
		this.reasonCollected = reasonCollected;
	}

	public RrdData() {
		idField = rrdNumber = aitNumber = driverName = documentType = cnhNumber = crlvNumber = plate = registrationNumber = registrationState = validity = plateState
                = daysForRegularization = reasonCollected = dateCollected = timeCollected = cityCollected = stateCollected = rrdType = "";
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public String getValidity() {
		return validity;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}



	public String getRRDListViewData(Context context) {
		Log.d("getRRDListViewData", "chamado");
		String rrd =
				context.getResources().getString(R.string.rrd_ait_number)
					+ Routine.getNewline(1)
					+ aitNumber
					+ Routine.getNewline(2)
					+ context.getResources().getString(R.string.rrd_number)
					+ Routine.getNewline(1)
					+ rrdNumber
					+ Routine.getNewline(2)
					+ context.getResources().getString(R.string.rrd_get_from)
					+ Routine.getNewline(1)
					+ driverName
					+ Routine.getNewline(2)
					+ context.getResources().getString(R.string.rrd_crlv_number)
					+ Routine.getNewline(1)
					+ crlvNumber
					+ Routine.getNewline(2)
					+ context.getResources().getString(R.string.rrd_register_number)
					+ Routine.getNewline(1)
					+ registrationNumber
					+ Routine.getNewline(2)
					+ context.getResources().getString(R.string.rrd_validity)
					+ Routine.getNewline(1)
					+ validity
					+ Routine.getNewline(2)
					+ context.getResources().getString(R.string.rrd_state)
					+ Routine.getNewline(1)
					+ plateState
					+ Routine.getNewline(2)
					+ context.getResources().getString(
					R.string.rrd_qty_of_days_to_regularization)
					+ Routine.getNewline(1)
					+ daysForRegularization
					+ Routine.getNewline(2)
					+ context.getResources().getString(
					R.string.rrd_reason_for_collection) + Routine.getNewline(1)
					+ reasonCollected + Routine.getNewline(2);

		return rrd;

	}

	public void setRRDDataFromCursor(@Nullable Cursor cursor) {
		if (cursor == null || !cursor.moveToFirst()) {
			clearRRDData();
			return;
		}

		try {
			// Mapa de colunas para setters
			Map<String, Consumer<String>> columnSetters = new HashMap<>();
			columnSetters.put(RrdDatabaseHelper.RRD_NUMBER, this::setRrdNumber);
			columnSetters.put(RrdDatabaseHelper.AIT_NUMBER, this::setAitNumber);
			columnSetters.put(RrdDatabaseHelper.DRIVER_NAME, this::setDriverName);
			columnSetters.put(RrdDatabaseHelper.DOCUMENT_TYPE, this::setDocumentType);
			columnSetters.put(RrdDatabaseHelper.CRLV_NUMBER, this::setCrlvNumber);
			columnSetters.put(RrdDatabaseHelper.PLATE, this::setPlate);
			columnSetters.put(RrdDatabaseHelper.CNH_NUMBER, this::setRegistrationNumber);
			columnSetters.put(RrdDatabaseHelper.CNH_STATE, this::setRegistrationState);
			columnSetters.put(RrdDatabaseHelper.CNH_VALIDITY, this::setValidity);
			columnSetters.put(RrdDatabaseHelper.PLATE_STATE, this::setPlateState);
			columnSetters.put(RrdDatabaseHelper.REASON_FOR_COLLECTION, this::setReasonCollected);
			columnSetters.put(RrdDatabaseHelper.DAYS_FOR_REGULARIZATION, this::setDaysForRegularization);
			columnSetters.put(RrdDatabaseHelper.COLLECTION_DATE, this::setDateCollected);
			columnSetters.put(RrdDatabaseHelper.COLLECTION_TIME, this::setTimeCollected);
			columnSetters.put(RrdDatabaseHelper.COLLECTION_CITY, this::setCityCollected);
			columnSetters.put(RrdDatabaseHelper.COLLECTION_STATE, this::setStateCollected);
			columnSetters.put(RrdDatabaseHelper.RRD_TYPE, this::setRrdType);

			// Processa cada coluna
			columnSetters.forEach((column, setter) ->
					setter.accept(getStringSafe(cursor, column)));

		} catch (SQLException e) {
			Log.e("RRDData", "Erro ao ler dados do cursor", e);
			clearRRDData();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close(); // Garante que o cursor seja fechado
			}
		}
	}

	private String getStringSafe(@NonNull Cursor cursor, String columnName) {
		try {
			int columnIndex = cursor.getColumnIndexOrThrow(columnName);
			return Objects.requireNonNullElse(cursor.getString(columnIndex), "");
		} catch (IllegalArgumentException e) {
			Log.w("RRDData", "Coluna não encontrada: " + columnName);
			return "";
		}
	}

	private void clearRRDData() {
		setRrdNumber("");
		setAitNumber("");
		setDriverName("");
		setDocumentType("");
		setCrlvNumber("");
		setPlate("");
		setRegistrationNumber("");
		setRegistrationState("");
		setValidity("");
		setPlateState("");
		setReasonCollected("");
		setDaysForRegularization("");
		setDateCollected("");
		setTimeCollected("");
		setCityCollected("");
		setStateCollected("");
		setRrdType("");
	}

	public void fillRrdData(View dialogView, Context context) {

		user = AppObject.getTinyDB(context).getObject(AppConstants.user, User.class);
		Routine.TextAlignment normal = Routine.TextAlignment.NORMAL;
		Routine.TextAlignment center = Routine.TextAlignment.CENTER;

		LinearLayout layoutCnh = dialogView.findViewById(R.id.ll_cnh);
		LinearLayout layoutCrlv = dialogView.findViewById(R.id.ll_crlv);

		//TODO
		boolean showDocumentLayout = false;

		if (showDocumentLayout) {
			layoutCnh.setVisibility(View.VISIBLE);
			layoutCrlv.setVisibility(View.GONE);
		} else {
			layoutCrlv.setVisibility(View.VISIBLE);
			layoutCnh.setVisibility(View.GONE);
		}

		TextView tvAitNumber = dialogView.findViewById(R.id.tv_rrd_ait_number);
		SpannableString boldAitNumber = Routine.textWithBoldAndCenter(context.getString(R.string.rrd_number), aitNumber, false, center);

		TextView tvRrdNumber = dialogView.findViewById(R.id.tv_rrd_number);
		SpannableString boldRrdNumber = Routine.textWithBoldAndCenter(context.getString(R.string.rrd_number), rrdNumber, false, center);

		TextView tvDriverName = dialogView.findViewById(R.id.tv_rrd_driver_name);
		SpannableString boldDriverName = Routine.textWithBoldAndCenter(context.getString(R.string.rrd_get_from), driverName, false, normal);

		TextView tvCnhNumber = dialogView.findViewById(R.id.tv_rrd_cnh_number);
		SpannableString boldCnhNumber = Routine.textWithBoldAndCenter(context.getString(R.string.capital_cnh_ppd_acc), cnhNumber, false, center);

		TextView tvCnhValidity = dialogView.findViewById(R.id.tv_rrd_cnh_validity);
		SpannableString boldCnhValidity = Routine.textWithBoldAndCenter(context.getString(R.string.rrd_plate), plate, false, center);

		TextView tvCnhState = dialogView.findViewById(R.id.tv_ait_cnh_state);
		SpannableString boldCnhState = Routine.textWithBoldAndCenter(context.getString(R.string.capital_cnh_state), plate, false, center);

		TextView tvPlate = dialogView.findViewById(R.id.tv_rrd_plate);
		SpannableString boldPlate = Routine.textWithBoldAndCenter(context.getString(R.string.rrd_plate), plate, false, center);

		TextView tvCrlvNumber = dialogView.findViewById(R.id.tv_rrd_crlv_number);
		SpannableString boldCrlvNumber = Routine.textWithBoldAndCenter(context.getString(R.string.rrd_crlv_number), crlvNumber, false, center);

		TextView tvPlateState = dialogView.findViewById(R.id.tv_rrd_plate_uf);
		SpannableString boldPlateState = Routine.textWithBoldAndCenter(context.getString(R.string.rrd_plate), plateState, false, center);

		TextView tvReasonCollected = dialogView.findViewById(R.id.tv_rrd_reason_for_collection);
		SpannableString boldReasonCollected = Routine.textWithBoldAndCenter(context.getString(R.string.rrd_reason_for_collection), reasonCollected, false, normal);

		TextView tvAgent = dialogView.findViewById(R.id.tv_rrd_user_name);
		SpannableString boldAgent = Routine.textWithBoldAndCenter(context.getString(R.string.capital_name), user.getCompanyName(), false, normal);

		TextView tvRegisterAgent = dialogView.findViewById(R.id.tv_register_number);
		SpannableString boldRegisterAgent = Routine.textWithBoldAndCenter(context.getString(R.string.capital_register_number), user.getRegistration(), false, center);

		tvAitNumber.setText(boldAitNumber);
		tvRrdNumber.setText(boldRrdNumber);
		tvDriverName.setText(boldDriverName);
		tvCnhNumber.setText(boldCnhNumber);
		tvCnhValidity.setText(boldCnhValidity);
		tvCnhState.setText(boldCnhState);
		tvPlateState.setText(boldPlateState);
		tvCrlvNumber.setText(boldCrlvNumber);
		tvPlate.setText(boldPlate);
		tvPlateState.setText(plateState);
		tvReasonCollected.setText(boldReasonCollected);
		tvAgent.setText(boldAgent);
		tvRegisterAgent.setText(boldRegisterAgent);
	}

}
