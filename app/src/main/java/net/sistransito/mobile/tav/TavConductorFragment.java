package net.sistransito.mobile.tav;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.AnyDialogFragment;
import net.sistransito.mobile.fragment.AnyDialogListener;
import net.sistransito.mobile.number.AsyncListernerNumber;
import net.sistransito.mobile.number.NumberHttpResultAsyncTask;
import net.sistransito.R;

public class TavConductorFragment extends Fragment implements
		AnyDialogListener {
	private View view;

	private TavData tavData;
	private TextView etAitNumber;
	private EditText etOwnerName, etCpfCnpj,
			etRenavamNumber, etChassiNumber;
	private Bundle bundle;
	private AnyDialogFragment diaglogFragmentForFragment;

	public static TavConductorFragment newInstance() {
		return new TavConductorFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tav_driver_fragment, null, false);
		initializedView();
		getAitObject();
		checkAitNumber();
		return view;
	}

	private void checkAitNumber() {
		String value = (DatabaseCreator.getNumberDatabaseAdapter(getActivity()))
				.geTavNumber();
		if (value == null) {
			diaglogFragmentForFragment = new AnyDialogFragment();
			diaglogFragmentForFragment.setTargetFragment(this, 0);
			bundle = new Bundle();
			bundle.putInt(AppConstants.DIALOG_TITLE_ID, R.string.synchronization_screen_title);
			bundle.putInt(AppConstants.DIALOG_MGS_ID, R.string.message_need_synchronize_ait_number);
			diaglogFragmentForFragment.setArguments(bundle);
			diaglogFragmentForFragment.setCancelable(false);
			diaglogFragmentForFragment
					.show(getChildFragmentManager(), "dialog");
		} else {
			tavData.setTavNumber(value);
		}
	}

	private void addListener() {
		etAitNumber.addTextChangedListener(new ChangeText(
				R.id.et_tav_ait));
		etOwnerName.addTextChangedListener(new ChangeText(
				R.id.et_tav_proprietario));

		etCpfCnpj.addTextChangedListener(new ChangeText(R.id.et_tav_cpf_cnpj));

		etRenavamNumber.addTextChangedListener(new ChangeText(
				R.id.et_tav_renavan_number));

		etChassiNumber.addTextChangedListener(new ChangeText(
				R.id.et_tav_chassi_number));

	}

	private void getAitObject() {
		tavData = TavObject.getTAVObject();
		getRecomandedUpdate();
		addListener();
	}

	private void getRecomandedUpdate() {

		etAitNumber.setText(tavData.getAitNumber());

		if(!tavData.getTavType().equals("avulso")) {
			//etRenavamNumber.setText(tavData.getRenavamNumber());
			etChassiNumber.setText(tavData.getChassisNumber());
		}

	}

	private void initializedView() {
    
		etAitNumber = (TextView) view
				.findViewById(R.id.et_tav_ait);
		etOwnerName = (EditText) view
				.findViewById(R.id.et_tav_proprietario);

		etCpfCnpj = (EditText) view.findViewById(R.id.et_tav_cpf_cnpj);

		etRenavamNumber = (EditText) view
				.findViewById(R.id.et_tav_renavan_number);

		etChassiNumber = (EditText) view
				.findViewById(R.id.et_tav_chassi_number);

		addListener();

	}

	private class ChangeText implements TextWatcher {
		private int id;

		public ChangeText(int id) {
			this.id = id;
		}

		@Override
		public void afterTextChanged(Editable editable) {
			String s = editable.toString();
			if (id == R.id.et_tav_ait) {
				tavData.setAitNumber(s);
			} else if (id == R.id.et_tav_cpf_cnpj) {
				tavData.setCpfCnpj(s);
			} else if (id == R.id.et_tav_renavan_number) {
				tavData.setRenavamNumber(s);
			} else if (id == R.id.et_tav_chassi_number) {
				tavData.setChassisNumber(s);
			} else if (id == R.id.et_tav_proprietario) {
				tavData.setOwnerName(s);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}
	}

	@Override
	public void onDialogTaskWork(boolean isWork) {
		if (isWork) {

			(new NumberHttpResultAsyncTask(new AsyncListernerNumber() {

				@Override
				public void asyncTaskComplete(boolean isComplete) {
					if (isComplete) {
						checkAitNumber();
					}

				}
			}, getActivity(), AppConstants.TAV_NUMBER)).execute("");

		} else {
			getActivity().finish();
		}
	}
}