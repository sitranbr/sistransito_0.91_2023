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


public class TavStructureFragment extends Fragment implements
		OnItemSelectedListener {
	private View view;
	private TavData tavData;
	private FilterName filterName;

	private Spinner leverHead, carBody, lining, hoodBody,
			rightSideBody, leftSideBody,
			trunkBodywork, roofBodywork, enginer, dashboard, hoodPaint,
			rightSidePaint, leftSidePaint, trunkPainting,
			ceilingPainting, radiator, sideWindows, windShieldGlass,
			rearWindow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tav_vehicle_structure_fragment, null,
				false);
		filterName = new FilterName(getActivity());
		initializedView();
		getAitObject();
		return view;
	}

	private void addListener() {
		leverHead.setOnItemSelectedListener(this);
		carBody.setOnItemSelectedListener(this);
		lining.setOnItemSelectedListener(this);
		hoodBody.setOnItemSelectedListener(this);
		rightSideBody.setOnItemSelectedListener(this);
		leftSideBody.setOnItemSelectedListener(this);
		trunkBodywork.setOnItemSelectedListener(this);
		roofBodywork.setOnItemSelectedListener(this);
		enginer.setOnItemSelectedListener(this);
		dashboard.setOnItemSelectedListener(this);
		hoodPaint.setOnItemSelectedListener(this);
		rightSidePaint.setOnItemSelectedListener(this);
		leftSidePaint.setOnItemSelectedListener(this);
		trunkPainting.setOnItemSelectedListener(this);
		ceilingPainting.setOnItemSelectedListener(this);
		radiator.setOnItemSelectedListener(this);
		sideWindows.setOnItemSelectedListener(this);
		windShieldGlass.setOnItemSelectedListener(this);
		rearWindow.setOnItemSelectedListener(this);

	}

	private void getAitObject() {
		tavData = TavObject.getTAVObject();

	}

	private void initializedView() {

		leverHead = (Spinner) view.findViewById(R.id.cabeca_de_alavanca);
		carBody = (Spinner) view.findViewById(R.id.carroceria);
		lining = (Spinner) view.findViewById(R.id.forro);
		hoodBody = (Spinner) view.findViewById(R.id.lataria_capo);
		rightSideBody = (Spinner) view
				.findViewById(R.id.lataria_lado_direito);
		leftSideBody = (Spinner) view
				.findViewById(R.id.lataria_lado_esquerdo);
		trunkBodywork = (Spinner) view
				.findViewById(R.id.lataria_tapa_porta_mala);
		roofBodywork = (Spinner) view.findViewById(R.id.lataria_teto);
		enginer = (Spinner) view.findViewById(R.id.motor);
		dashboard = (Spinner) view.findViewById(R.id.painel);
		hoodPaint = (Spinner) view.findViewById(R.id.pintura_capo);
		rightSidePaint = (Spinner) view
				.findViewById(R.id.pintura_lado_direito);
		leftSidePaint = (Spinner) view
				.findViewById(R.id.pintura_lado_esquerdo);
		trunkPainting = (Spinner) view
				.findViewById(R.id.pintura_porta_mala);
		ceilingPainting = (Spinner) view.findViewById(R.id.pintura_teto);
		radiator = (Spinner) view.findViewById(R.id.radiador);
		sideWindows = (Spinner) view.findViewById(R.id.vidros_laterais);
		windShieldGlass = (Spinner) view.findViewById(R.id.vidro_para_brisa);
		rearWindow = (Spinner) view.findViewById(R.id.vidro_traseiro);

		setAdapter();

		addListener();
	}

	private void setAdapter() {

		leverHead.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.es_0))));

		carBody.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.es_1))));

		lining.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.es_2))));

		hoodBody.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.es_3))));

		rightSideBody.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.es_4))));

		leftSideBody.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.es_5))));

		trunkBodywork.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.es_6))));

		roofBodywork.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.es_7))));

		enginer.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.es_8))));

		dashboard.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.es_9))));

		hoodPaint.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.es_10))));

		rightSidePaint.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.es_11))));

		leftSidePaint.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.es_12))));

		trunkPainting.setAdapter(new AnyArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				android.R.id.text1, Arrays.asList(getResources()
						.getStringArray(R.array.es_13))));

		ceilingPainting.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.es_14))));

		radiator.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.es_15))));

		sideWindows.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.es_16))));

		windShieldGlass.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.es_17))));

		rearWindow.setAdapter(new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				Arrays.asList(getResources().getStringArray(R.array.es_18))));

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long is) {

		String value = (String) parent.getItemAtPosition(pos);
		value = filterName.filter(value);
		int id = parent.getId();
		if (id == R.id.cabeca_de_alavanca) {
			tavData.setLeverHead(value);
		} else if (id == R.id.carroceria) {
			tavData.setCarBody(value);
		} else if (id == R.id.forro) {
			tavData.setCeiling(value);
		} else if (id == R.id.lataria_capo) {
			tavData.setHoodBody(value);
		} else if (id == R.id.lataria_lado_direito) {
			tavData.setBodyworkRightSide(value);
		} else if (id == R.id.lataria_lado_esquerdo) {
			tavData.setBodyWorkLeftSide(value);
		} else if (id == R.id.lataria_tapa_porta_mala) {
			tavData.setTrunkBodywork(value);
		} else if (id == R.id.lataria_teto) {
			tavData.setRoofBodywork(value);
		} else if (id == R.id.motor) {
			tavData.setEngine(value);
		} else if (id == R.id.painel) {
			tavData.setDashboard(value);
		} else if (id == R.id.pintura_capo) {
			tavData.setHoodPaint(value);
		} else if (id == R.id.pintura_lado_direito) {
			tavData.setRightSidePaint(value);
		} else if (id == R.id.pintura_lado_esquerdo) {
			tavData.setLeftSidePaint(value);
		} else if (id == R.id.pintura_porta_mala) {
			tavData.setTrunkPainting(value);
		} else if (id == R.id.pintura_teto) {
			tavData.setHoodPainting(value);
		} else if (id == R.id.radiador) {
			tavData.setRadiator(value);
		} else if (id == R.id.vidros_laterais) {
			tavData.setSideGlass(value);
		} else if (id == R.id.vidro_para_brisa) {
			tavData.setWindShield(value);
		} else if (id == R.id.vidro_traseiro) {
			tavData.setRearWindshield(value);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
