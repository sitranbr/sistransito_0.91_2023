package net.sistransito.library.datepicker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class MyTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	public static final String MY_TIME_PICKER_ID = "my_time_picker_id";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int viewId = getArguments().getInt(MY_TIME_PICKER_ID);

		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		return new TimePickerDialog(getActivity(), this, hour, minute, true);
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);

		Bundle result = new Bundle();
		result.putString("selectedValue", time);
		result.putInt("viewId", getArguments().getInt(MY_TIME_PICKER_ID));

		getParentFragmentManager().setFragmentResult("pickerResult", result);
		dismiss();
	}
}