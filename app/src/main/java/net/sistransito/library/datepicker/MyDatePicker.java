package net.sistransito.library.datepicker;

import android.app.Dialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class MyDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	public static final String MY_DATE_PICKER_ID = "my_date_picker_id";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int viewId = getArguments().getInt(MY_DATE_PICKER_ID);

		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
		String date = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);

		Bundle result = new Bundle();
		result.putString("selectedValue", date);
		result.putInt("viewId", getArguments().getInt(MY_DATE_PICKER_ID));

		getParentFragmentManager().setFragmentResult("pickerResult", result);
		dismiss();
	}
}