package net.sistransito.mobile.tav;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import net.sistransito.mobile.adapter.AnyArrayAdapter;
import net.sistransito.R;

import java.util.Arrays;

public class TavAccessoriesFragment extends Fragment implements
		OnItemSelectedListener {
	private View view;
	private FilterName filterName;

	private TavData data;
	private Spinner antenna, trunck, seats, baterry, wheelCover,
			airConditioner, fireExtinguisher, headLight,
			taiLight, jack, frontBumper,
			backBumper, driverHood, tires, spareTire,
			radio, rearviewMirror, rightOutsideMirror, carpet,
			triangle, steeringWheel, motorcycleHandlebar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tav_vehicle_accessories_fragment, null,
				false);
		filterName = new FilterName(getActivity());
		initializedView();
		getAitObject();
		return view;
	}

	private void addListener() {
		antenna.setOnItemSelectedListener(this);
		trunck.setOnItemSelectedListener(this);
		seats.setOnItemSelectedListener(this);
		baterry.setOnItemSelectedListener(this);
		wheelCover.setOnItemSelectedListener(this);
		airConditioner.setOnItemSelectedListener(this);
		fireExtinguisher.setOnItemSelectedListener(this);
		headLight.setOnItemSelectedListener(this);
		taiLight.setOnItemSelectedListener(this);
		jack.setOnItemSelectedListener(this);
		frontBumper.setOnItemSelectedListener(this);
		backBumper.setOnItemSelectedListener(this);
		driverHood.setOnItemSelectedListener(this);
		tires.setOnItemSelectedListener(this);
		spareTire.setOnItemSelectedListener(this);
		radio.setOnItemSelectedListener(this);
		rearviewMirror.setOnItemSelectedListener(this);
		rightOutsideMirror.setOnItemSelectedListener(this);
		carpet.setOnItemSelectedListener(this);
		triangle.setOnItemSelectedListener(this);
		steeringWheel.setOnItemSelectedListener(this);
		motorcycleHandlebar.setOnItemSelectedListener(this);

	}

	private void getAitObject() {

		data = TavObject.getTAVObject();

	}

	private void initializedView() {
		antenna = (Spinner) view.findViewById(R.id.sp_radio_antenna);
		trunck = (Spinner) view.findViewById(R.id.sp_baggage_handler);
		seats = (Spinner) view.findViewById(R.id.sp_seat);
		baterry = (Spinner) view.findViewById(R.id.sp_battery);
		wheelCover = (Spinner) view.findViewById(R.id.sp_hubcap);
		airConditioner = (Spinner) view
				.findViewById(R.id.sp_air_conditioner);
		fireExtinguisher = (Spinner) view
				.findViewById(R.id.sp_fire_extinguisher);
		headLight = (Spinner) view
				.findViewById(R.id.sp_headlight);
		taiLight = (Spinner) view.findViewById(R.id.rear_light);
		jack = (Spinner) view.findViewById(R.id.sp_wheel_jack);
		frontBumper = (Spinner) view
				.findViewById(R.id.sp_front_bumper);
		backBumper = (Spinner) view
				.findViewById(R.id.sp_rear_bumper);
		driverHood = (Spinner) view
				.findViewById(R.id.sp_driver_sunshade);
		tires = (Spinner) view.findViewById(R.id.sp_tires);
		spareTire = (Spinner) view.findViewById(R.id.sp_step_tire);
		radio = (Spinner) view.findViewById(R.id.sp_radio);
		rearviewMirror = (Spinner) view
				.findViewById(R.id.sp_internal_rearview);
		rightOutsideMirror = (Spinner) view
				.findViewById(R.id.sp_right_outside_mirror);
		carpet = (Spinner) view.findViewById(R.id.sp_carpet);
		triangle = (Spinner) view.findViewById(R.id.sp_triangle);
		steeringWheel = (Spinner) view.findViewById(R.id.sp_steering_wheel);
		motorcycleHandlebar = (Spinner) view.findViewById(R.id.sp_handlebars);
		addAdapter();
		addListener();
	}

	private void addAdapter() {

		antenna.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_0))));
		trunck.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_1))));
		seats.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_2))));
		baterry.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_3))));
		wheelCover.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_4))));
		airConditioner.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.ac_5))));
		fireExtinguisher.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.ac_6))));
		headLight.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.ac_7))));
		taiLight.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_8))));
		jack.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_9))));
		frontBumper.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.ac_10))));
		backBumper.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.ac_11))));
		driverHood.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.ac_12))));
		tires.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_13))));
		spareTire.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_14))));
		radio.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_15))));
		rearviewMirror.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.ac_16))));
		rightOutsideMirror.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.ac_17))));
		carpet.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_18))));
		triangle.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_19))));
		steeringWheel.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_20))));
		motorcycleHandlebar.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.ac_21))));

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
		String value = (String) parent.getItemAtPosition(pos);
		value = filterName.filter(value);
		switch (parent.getId()) {
		case R.id.sp_radio_antenna:
			data.setAntenna(value);
			break;
		case R.id.sp_baggage_handler:
			data.setTrunk(value);
			break;
		case R.id.sp_seat:
			data.setSeats(value);
			break;
		case R.id.sp_battery:
			data.setBaterry(value);
			break;
		case R.id.sp_hubcap:
			data.setWheelCover(value);
			break;
		case R.id.sp_air_conditioner:
			data.setAirConditioner(value);
			break;
		case R.id.sp_fire_extinguisher:
			data.setFireExtinguisher(value);
			break;
		case R.id.sp_headlight:
			data.setHeadLight(value);
			break;
		case R.id.rear_light:
			data.setRearLight(value);
			break;
		case R.id.sp_wheel_jack:
			data.setJack(value);
			break;
		case R.id.sp_front_bumper:
			data.setFrontBumper(value);
			break;
		case R.id.sp_rear_bumper:
			data.setHearBumper(value);
			break;
		case R.id.sp_driver_sunshade:
			data.setDriverSunVisor(value);
			break;
		case R.id.sp_tires:
			data.setTires(value);
			break;
		case R.id.sp_step_tire:
			data.setSpareTire(value);
			break;
		case R.id.sp_radio:
			data.setRadio(value);
			break;
		case R.id.sp_internal_rearview:
			data.setRearviewMirror(value);
			break;
		case R.id.sp_right_outside_mirror:
			data.setOutsideMirror(value);
			break;
		case R.id.sp_carpet:
			data.setCarpet(value);
			break;
		case R.id.sp_triangle:
			data.setTriangle(value);
			break;
		case R.id.sp_steering_wheel:
			data.setSteeringWheel(value);
			break;
		case R.id.sp_handlebars:
			data.setMotorcycleHandlebar(value);
			break;

		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
