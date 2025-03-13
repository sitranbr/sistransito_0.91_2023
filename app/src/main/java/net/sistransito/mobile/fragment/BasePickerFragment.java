package net.sistransito.mobile.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import net.sistransito.library.datepicker.MyDatePicker;
import net.sistransito.library.datepicker.MyTimePicker;

import java.util.HashMap;
import java.util.Map;

public abstract class BasePickerFragment extends Fragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Map<Integer, String> pickerMap = new HashMap<>();

    public void addPicker(int viewId, String pickerType) {
        pickerMap.put(viewId, pickerType);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        handlePicker(id);
    }

    private void handlePicker(int viewId) {
        String pickerType = pickerMap.get(viewId);
        if (pickerType != null) {
            if (pickerType.equals("date")) {
                showDatePicker(viewId);
            } else if (pickerType.equals("time")) {
                showTimePicker(viewId);
            }
        }
    }

    private void showDatePicker(int viewId) {
        MyDatePicker datePicker = new MyDatePicker();
        Bundle bundle = new Bundle();
        bundle.putInt(MyDatePicker.MY_DATE_PICKER_ID, viewId);
        datePicker.setArguments(bundle);
        datePicker.show(getParentFragmentManager(), "date");
    }

    private void showTimePicker(int viewId) {
        MyTimePicker timePicker = new MyTimePicker();
        Bundle bundle = new Bundle();
        bundle.putInt(MyTimePicker.MY_TIME_PICKER_ID, viewId);
        timePicker.setArguments(bundle);
        timePicker.show(getParentFragmentManager(), "time");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        handlePickerResult(String.format("%02d/%02d/%d", dayOfMonth, month + 1, year), view.getId());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        handlePickerResult(String.format("%02d:%02d", hourOfDay, minute), view.getId());
    }

    protected abstract void handlePickerResult(String selectedValue, int viewId);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("pickerResult", this, (requestKey, result) -> {
            if (requestKey.equals("pickerResult")) {
                String selectedValue = result.getString("selectedValue");
                int viewId = result.getInt("viewId");
                handlePickerResult(selectedValue, viewId);
            }
        });
    }
}