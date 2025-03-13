package net.sistransito.mobile.rrd;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.widget.AppCompatButton;
import android.widget.EditText;

import net.sistransito.mobile.adapter.CustomSpinnerAdapter;

import net.sistransito.mobile.adapter.AnyArrayAdapter;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.AnyAlertDialog;
import net.sistransito.mobile.rrd.lister.RrdLister;
import net.sistransito.R;

import java.util.Arrays;
import java.util.List;

public class TabRrdInformationFragment extends Fragment {
	private View view;
	private EditText etReasonForCollecting;
	private AutoCompleteTextView autoCompleteDays;
	private RrdData rrdData;
	private List<String> listQuantity;
	private AnyArrayAdapter<String> aaaQuantity;

	private AppCompatButton btnRrdCheck, btnRrdPrint;

	public static TabRrdInformationFragment newInstance() {
		return new TabRrdInformationFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.rrd_information_fragment, null, false);
		initializedView();
		getAitObject();
		return view;
	}

	private void addListener() {

		Log.d("tipoRRD", rrdData.getRrdType());

	}

	private void getAitObject() {
		rrdData = RrdObject.getRRDOject();
		getRecomandedUpdate();
		addListener();
		initializedSelectetItems();
	}

	private void initializedSelectetItems() {
		etReasonForCollecting.setText("");
		//autoCompleteDays.setText(listQuantity.get(4));
	}

	private void initializedView() {

		btnRrdCheck = (AppCompatButton) view.findViewById(R.id.btn_rrd_check);
		btnRrdPrint = (AppCompatButton) view.findViewById(R.id.ll_btn_rrd_print);

		etReasonForCollecting = (EditText) view
				.findViewById(R.id.et_rrd_reason_for_collection);
		etReasonForCollecting.addTextChangedListener(new ChangeText(
				R.id.et_rrd_reason_for_collection));

		listQuantity = Arrays.asList(getResources().getStringArray(
				R.array.array_quantity));
		aaaQuantity = new AnyArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				listQuantity);

		CustomSpinnerAdapter aaaQuantity = CustomSpinnerAdapter.createStateAdapter(getActivity(), listQuantity);
		autoCompleteDays = view.findViewById(R.id.spinner_rrd_days_to_regularize);
		autoCompleteDays.setAdapter(aaaQuantity);

		autoCompleteDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String selectedItem = (String) parent.getItemAtPosition(position);
				// Faça algo com o item selecionado, como:
				rrdData.setDaysForRegularization(selectedItem); // Atualiza o objeto rrdData
				// ou exiba um Toast:
				// Toast.makeText(getContext(), "Selecionado: " + selectedItem, Toast.LENGTH_SHORT).show();
			}
		});


		btnRrdPrint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				(DatabaseCreator.getRrdDatabaseAdapter(getActivity()))
						.insertRrdInformation(rrdData);
				
				startActivity(new Intent(getActivity(), RrdLister.class));
				getActivity().finish();
			}
		});

		btnRrdCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AnyAlertDialog.dialogShow(rrdData, getActivity(), getResources().getString(R.string.previous_title_rrd));
			}
		});

	}

	private void getRecomandedUpdate() {

	}

	private class ChangeText implements TextWatcher {
		private int id;

		public ChangeText(int id) {
			this.id = id;
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (id == R.id.et_rrd_reason_for_collection) {
				rrdData.setReasonCollected((s.toString()).trim());
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

	}
}