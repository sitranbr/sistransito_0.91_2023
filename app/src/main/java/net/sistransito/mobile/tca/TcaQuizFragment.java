package net.sistransito.mobile.tca;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.rey.material.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import net.sistransito.mobile.fragment.BasePickerFragment;
import net.sistransito.library.datepicker.MyDatePicker;
import net.sistransito.library.datepicker.MyTimePicker;
import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.AnyAlertDialog;
import net.sistransito.mobile.tca.lister.TcaLister;
import net.sistransito.R;

public class TcaQuizFragment extends BasePickerFragment implements
		OnClickListener, OnCheckedChangeListener,
		CompoundButton.OnCheckedChangeListener {
	private Bundle bundle;
	private MyTimePicker myTimePicker;
	private MyDatePicker myDatePicker;
	private View view;
	private Button btnTcaCheck, btnTcaGeneration,
			tcaDateAlcoholConductor,
			tcaTimeAlcoholConductor,
			tcaDateToxicConductor,
			tcaTimeToxicConductor;
	private TcaData tcaData;
	private RadioGroup rgTrafficConductor, rgAlcoholConductor,
			rgToxicConductor, rgKnowsWhereItIs, rgKnowsTheDateAndTime,
			rgKnowsItsAddress, rgRememberTheActsCommitted, rgConclusion;

	private LinearLayout llAlcoholParent,
			llAlcoholChild, llToxicParent,
			llToxicChild;

	private String sYes, sNo;
	private String sButtonTextData, sButttonTextTime;

	private CheckBox cbConductorShowsSignsOf1,
			cbConductorShowsSignsOf2, cbConductorShowsSignsOf3,
			cbConductorShowsSignsOf4, cbConductorShowsSignsOf5,
			cbConductorShowsSignsOf6;

	private CheckBox cbInHisAttitudeOccurs1, cbInHisAttitudeOccurs2,
			cbInHisAttitudeOccurs3, cbInHisAttitudeOccurs4, cbInHisAttitudeOccurs5,
			cbInHisAttitudeOccurs6;

	private CheckBox cbInRelationToTheirMotorAndVerbalAbilityOccurs1,
			cbInRelationToTheirMotorAndVerbalAbilityOccurs2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater
				.inflate(R.layout.tca_quiz_fragment, null, false);
		initializedView();
		getTcaObject();
		addListener();

		addPicker(R.id.tca_date_drunk_driver, "date");
		addPicker(R.id.tca_time_drunk_driver, "time");
		addPicker(R.id.tca_date_toxica, "date");
		addPicker(R.id.tca_time_toxica, "time");

		tcaDateAlcoholConductor.setOnClickListener(this);
		tcaTimeAlcoholConductor.setOnClickListener(this);
		tcaDateToxicConductor.setOnClickListener(this);
		tcaTimeToxicConductor.setOnClickListener(this);

		return view;
	}

	public static TcaQuizFragment newInstance() {
		return new TcaQuizFragment();
	}

	private void addListener() {
		btnTcaGeneration.setOnClickListener(this);
		btnTcaCheck.setOnClickListener(this);
		rgTrafficConductor.setOnCheckedChangeListener(this);
		rgAlcoholConductor.setOnCheckedChangeListener(this);
		rgToxicConductor.setOnCheckedChangeListener(this);

		rgKnowsWhereItIs.setOnCheckedChangeListener(this);
		rgKnowsTheDateAndTime.setOnCheckedChangeListener(this);
		rgKnowsItsAddress.setOnCheckedChangeListener(this);
		rgRememberTheActsCommitted.setOnCheckedChangeListener(this);
		rgConclusion.setOnCheckedChangeListener(this);

		tcaDateToxicConductor.setOnClickListener(this);
		tcaTimeToxicConductor.setOnClickListener(this);

		tcaDateAlcoholConductor.setOnClickListener(this);
		tcaTimeAlcoholConductor.setOnClickListener(this);

		cbConductorShowsSignsOf1.setOnCheckedChangeListener(this);
		cbConductorShowsSignsOf2.setOnCheckedChangeListener(this);
		cbConductorShowsSignsOf3.setOnCheckedChangeListener(this);
		cbConductorShowsSignsOf4.setOnCheckedChangeListener(this);
		cbConductorShowsSignsOf5.setOnCheckedChangeListener(this);
		cbConductorShowsSignsOf6.setOnCheckedChangeListener(this);

		cbInHisAttitudeOccurs1.setOnCheckedChangeListener(this);
		cbInHisAttitudeOccurs2.setOnCheckedChangeListener(this);
		cbInHisAttitudeOccurs3.setOnCheckedChangeListener(this);
		cbInHisAttitudeOccurs4.setOnCheckedChangeListener(this);
		cbInHisAttitudeOccurs5.setOnCheckedChangeListener(this);
		cbInHisAttitudeOccurs6.setOnCheckedChangeListener(this);

		cbInRelationToTheirMotorAndVerbalAbilityOccurs1
				.setOnCheckedChangeListener(this);
		cbInRelationToTheirMotorAndVerbalAbilityOccurs2
				.setOnCheckedChangeListener(this);

	}

	private void getTcaObject() {
		tcaData = TcaObject.getTCAOject();
		getRecomandedUpdate();
	}

	private void initializedView() {
		sYes = (getResources().getString(R.string.tca_yes)).trim();
		sNo = (getResources().getString(R.string.tca_no_text_title)).trim();

		sButtonTextData = getResources().getString(R.string.tca_date);
		sButttonTextTime = getResources().getString(R.string.tca_time);

		btnTcaGeneration = (Button) view.findViewById(R.id.btn_tca_generation);
		btnTcaCheck = (Button) view.findViewById(R.id.btn_tca_check);
		rgTrafficConductor = (RadioGroup) view
				.findViewById(R.id.rg_condutor_transito);
		rgAlcoholConductor = (RadioGroup) view
				.findViewById(R.id.rg_condutor_alcoolica);
		rgToxicConductor = (RadioGroup) view
				.findViewById(R.id.rg_condutor_toxica);
		rgKnowsWhereItIs = (RadioGroup) view
				.findViewById(R.id.rg_sabe_onde_esta);
		rgKnowsTheDateAndTime = (RadioGroup) view
				.findViewById(R.id.rg_sabe_a_data_e_a_hora);

		rgKnowsItsAddress = (RadioGroup) view
				.findViewById(R.id.rg_sabe_seu_enderecao);

		rgRememberTheActsCommitted = (RadioGroup) view
				.findViewById(R.id.rg_lembra_dos_atos_cometidos);
		rgConclusion = (RadioGroup) view.findViewById(R.id.rg_conclusao);

		llAlcoholParent = (LinearLayout) view
				.findViewById(R.id.linear_layout_alcoolica_parent);
		llAlcoholChild = (LinearLayout) view
				.findViewById(R.id.linear_layout_alcoolica_child);

		llToxicParent = (LinearLayout) view
				.findViewById(R.id.linear_layout_toxica_parent);
		llToxicChild = (LinearLayout) view
				.findViewById(R.id.linear_layout_toxica_child);

		tcaDateToxicConductor = (Button) view
				.findViewById(R.id.tca_date_toxica);
		tcaTimeToxicConductor = (Button) view
				.findViewById(R.id.tca_time_toxica);
		tcaDateAlcoholConductor = (Button) view
				.findViewById(R.id.tca_date_drunk_driver);
		tcaTimeAlcoholConductor = (Button) view
				.findViewById(R.id.tca_time_drunk_driver);

		removeAlcoholView();
		removeToxicView();

		cbConductorShowsSignsOf1 = (CheckBox) view
				.findViewById(R.id.checkBox_condutor_apresenta_1);
		cbConductorShowsSignsOf2 = (CheckBox) view
				.findViewById(R.id.checkBox_condutor_apresenta_2);
		cbConductorShowsSignsOf3 = (CheckBox) view
				.findViewById(R.id.checkBox_condutor_apresenta_3);
		cbConductorShowsSignsOf4 = (CheckBox) view
				.findViewById(R.id.checkBox_condutor_apresenta_4);
		cbConductorShowsSignsOf5 = (CheckBox) view
				.findViewById(R.id.checkBox_condutor_apresenta_5);
		cbConductorShowsSignsOf6 = (CheckBox) view
				.findViewById(R.id.checkBox_condutor_apresenta_6);

		cbInHisAttitudeOccurs1 = (CheckBox) view
				.findViewById(R.id.cb_atitude_ocorre_1);
		cbInHisAttitudeOccurs2 = (CheckBox) view
				.findViewById(R.id.cb_atitude_ocorre_2);
		cbInHisAttitudeOccurs3 = (CheckBox) view
				.findViewById(R.id.cb_atitude_ocorre_3);
		cbInHisAttitudeOccurs4 = (CheckBox) view
				.findViewById(R.id.cb_atitude_ocorre_4);
		cbInHisAttitudeOccurs5 = (CheckBox) view
				.findViewById(R.id.cb_atitude_ocorre_5);
		cbInHisAttitudeOccurs6 = (CheckBox) view
				.findViewById(R.id.cb_atitude_ocorre_6);

		cbInRelationToTheirMotorAndVerbalAbilityOccurs1 = (CheckBox) view
				.findViewById(R.id.cb_tca_em_relacao_a_sua_verbal_ocorre_1);

		cbInRelationToTheirMotorAndVerbalAbilityOccurs2 = (CheckBox) view
				.findViewById(R.id.cb_tca_em_relacao_a_sua_verbal_ocorre_2);
	}

	private void getRecomandedUpdate() {
		
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		if (id == R.id.btn_tca_check) {
			AnyAlertDialog
					.dialogShow(tcaData.getTCAViewData(getActivity()),
							getActivity(), getActivity().getResources()
									.getString(R.string.list_tca_issued));
		} else if (id == R.id.btn_tca_generation) {
			(DatabaseCreator.getTcaDatabaseAdapter(getActivity()))
					.setData(tcaData);
			getActivity().startActivity(
					new Intent(getActivity(), TcaLister.class));
			getActivity().finish();
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup view, int check_id) {
		int id = view.getId();
		if (id == R.id.rg_condutor_transito) {
			if (check_id != -1) {
				String value = ((RadioButton) view
						.findViewById(rgTrafficConductor
								.getCheckedRadioButtonId())).getText()
						.toString();
				value = value.trim();
				tcaData.setDriverInvolvedInCarAccident(value);
			}
		} else if (id == R.id.rg_condutor_alcoolica) {
			if (check_id != -1) {
				if (rgAlcoholConductor.getCheckedRadioButtonId() == R.id.rd_condutor_alcoolica_sim) {
					addAlcoholView();
					tcaData.setDriverClaimsToHaveDrunkAlcohol(sYes);
				} else if (rgAlcoholConductor.getCheckedRadioButtonId() == R.id.rd_condutor_alcoolica_nao) {
					removeAlcoholView();
					tcaData.setDriverClaimsToHaveDrunkAlcohol(sNo);
					tcaDateAlcoholConductor
							.setText(sButtonTextData);
					tcaTimeAlcoholConductor
							.setText(sButttonTextTime);
				}
			}
		} else if (id == R.id.rg_condutor_toxica) {
			if (check_id != -1) {

				if (rgToxicConductor.getCheckedRadioButtonId() == R.id.rd_condutor_toxica_sim) {
					addToxicView();
					tcaData.setDriverClaimsToHaveUsedToxicSubstance(sYes);

				} else if (rgToxicConductor.getCheckedRadioButtonId() == R.id.rd_condutor_toxica_nao) {
					removeToxicView();
					tcaData.setDriverClaimsToHaveUsedToxicSubstance(sNo);

					tcaDateToxicConductor
							.setText(sButtonTextData);
					tcaTimeToxicConductor
							.setText(sButttonTextTime);
				}

			}
		} else if (id == R.id.rg_sabe_onde_esta) {
			if (check_id != -1) {

				tcaData.setKnowsWhereItIs(((RadioButton) view
						.findViewById(rgKnowsWhereItIs
								.getCheckedRadioButtonId())).getText()
						.toString().trim());

			}
		} else if (id == R.id.rg_sabe_a_data_e_a_hora) {
			if (check_id != -1) {

				tcaData.setKnowsTheDateAndTime(((RadioButton) view
						.findViewById(rgKnowsTheDateAndTime
								.getCheckedRadioButtonId())).getText()
						.toString().trim());

			}
		} else if (id == R.id.rg_sabe_seu_enderecao) {
			if (check_id != -1) {

				tcaData.setKnowsItsAddress(((RadioButton) view
						.findViewById(rgKnowsItsAddress
								.getCheckedRadioButtonId())).getText()
						.toString().trim());

			}
		} else if (id == R.id.rg_lembra_dos_atos_cometidos) {
			if (check_id != -1) {

				tcaData.setRememberTheActsCommitted(((RadioButton) view
						.findViewById(rgRememberTheActsCommitted
								.getCheckedRadioButtonId())).getText()
						.toString().trim());

			}
		} else if (id == R.id.rg_conclusao) {
			if (check_id != -1) {
				tcaData.setConclusion(((RadioButton) view.findViewById(rgConclusion
						.getCheckedRadioButtonId())).getText().toString()
						.trim());
			}
		}

	}

	private void removeAlcoholView() {
		if (llAlcoholChild.getParent() != null)
			llAlcoholParent
					.removeView(llAlcoholChild);
	}

	private void addAlcoholView() {
		if (llAlcoholChild.getParent() == null)
			llAlcoholParent
					.addView(llAlcoholChild);
	}

	private void removeToxicView() {
		if (llToxicChild.getParent() != null)
			llToxicParent.removeView(llToxicChild);
	}

	private void addToxicView() {
		if (llToxicChild.getParent() == null)
			llToxicParent.addView(llToxicChild);
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {

		int id = view.getId();
		if (id == R.id.checkBox_condutor_apresenta_1 || id == R.id.checkBox_condutor_apresenta_2 || id == R.id.checkBox_condutor_apresenta_3 || id == R.id.checkBox_condutor_apresenta_4 || id == R.id.checkBox_condutor_apresenta_5 || id == R.id.checkBox_condutor_apresenta_6) {
			getConductorShowsSignsOf();
		} else if (id == R.id.cb_atitude_ocorre_1 || id == R.id.cb_atitude_ocorre_2 || id == R.id.cb_atitude_ocorre_3 || id == R.id.cb_atitude_ocorre_4 || id == R.id.cb_atitude_ocorre_5 || id == R.id.cb_atitude_ocorre_6) {
			getInHisAttitudeOccurs();
		} else if (id == R.id.cb_tca_em_relacao_a_sua_verbal_ocorre_1 || id == R.id.cb_tca_em_relacao_a_sua_verbal_ocorre_2) {
			getInRelationVerbalAbilityOccurs();
		}
	}

	private void getConductorShowsSignsOf() {
		String sFormat = "";

		if (cbConductorShowsSignsOf1.isChecked()) {
			sFormat += (AppConstants.COMMA + cbConductorShowsSignsOf1
					.getText().toString());
		}
		if (cbConductorShowsSignsOf2.isChecked()) {
			sFormat += (AppConstants.COMMA + cbConductorShowsSignsOf2
					.getText().toString());
		}
		if (cbConductorShowsSignsOf3.isChecked()) {
			sFormat += (AppConstants.COMMA + cbConductorShowsSignsOf3
					.getText().toString());
		}
		if (cbConductorShowsSignsOf4.isChecked()) {
			sFormat += (AppConstants.COMMA + cbConductorShowsSignsOf4
					.getText().toString());
		}
		if (cbConductorShowsSignsOf5.isChecked()) {
			sFormat += (AppConstants.COMMA + cbConductorShowsSignsOf5
					.getText().toString());
		}
		if (cbConductorShowsSignsOf6.isChecked()) {
			sFormat += (AppConstants.COMMA + cbConductorShowsSignsOf6
					.getText().toString());
		}
		if (!sFormat.equals("")) {
			sFormat = (sFormat.substring(AppConstants.COMMA.length()));
			sFormat = sFormat.trim();
			tcaData.setDriverShowsSignsOf(sFormat);

		}
	}

	private void getInHisAttitudeOccurs() {
		String sFormat = "";
		if (cbInHisAttitudeOccurs1.isChecked()) {
			sFormat += (AppConstants.COMMA + cbInHisAttitudeOccurs1.getText()
					.toString());
		}
		if (cbInHisAttitudeOccurs2.isChecked()) {
			sFormat += (AppConstants.COMMA + cbInHisAttitudeOccurs2.getText()
					.toString());
		}
		if (cbInHisAttitudeOccurs3.isChecked()) {
			sFormat += (AppConstants.COMMA + cbInHisAttitudeOccurs3.getText()
					.toString());
		}
		if (cbInHisAttitudeOccurs4.isChecked()) {
			sFormat += (AppConstants.COMMA + cbInHisAttitudeOccurs4.getText()
					.toString());
		}
		if (cbInHisAttitudeOccurs5.isChecked()) {
			sFormat += (AppConstants.COMMA + cbInHisAttitudeOccurs5.getText()
					.toString());
		}
		if (cbInHisAttitudeOccurs6.isChecked()) {
			sFormat += (AppConstants.COMMA + cbInHisAttitudeOccurs6.getText()
					.toString());
		}
		if (!sFormat.equals("")) {
			sFormat = (sFormat.substring(AppConstants.COMMA.length()));
			sFormat = sFormat.trim();
			tcaData.setInHisAttitudeOccurs(sFormat);
		}
	}

	private void getInRelationVerbalAbilityOccurs() {

		String sFormat = "";
		if (cbInRelationToTheirMotorAndVerbalAbilityOccurs1.isChecked()) {
			sFormat += (AppConstants.NEW_LINE + cbInRelationToTheirMotorAndVerbalAbilityOccurs1
					.getText().toString());
		}
		if (cbInRelationToTheirMotorAndVerbalAbilityOccurs2.isChecked()) {
			sFormat += (AppConstants.NEW_LINE + cbInRelationToTheirMotorAndVerbalAbilityOccurs2
					.getText().toString());

		}
		if (!sFormat.equals("")) {
			sFormat = (sFormat.substring(AppConstants.NEW_LINE.length()));
			sFormat = sFormat.trim();
			tcaData.setInRelationToTheirMotorAndVerbalAbilityOccurs(sFormat);
		}
	}

	@Override
	protected void handlePickerResult(String selectedValue, int viewId) {
		if (viewId == R.id.tca_date_drunk_driver) {
			tcaData.setTimeThatDrankAlcohol(selectedValue);
			tcaDateAlcoholConductor.setText(tcaData.getTimeThatDrankAlcohol());
		} else if (viewId == R.id.tca_time_drunk_driver) {
			tcaData.setDateThatDrankAlcohol(selectedValue);
			tcaTimeAlcoholConductor.setText(tcaData.getDateThatDrankAlcohol());
		} else if (viewId == R.id.tca_date_toxica) {
			tcaData.setDateIngestedSubstance(selectedValue);
			tcaDateToxicConductor.setText(tcaData.getDateIngestedSubstance());
		} else if (viewId == R.id.tca_time_toxica) {
			tcaData.setTimeIngestedSubstance(selectedValue);
			tcaTimeToxicConductor.setText(tcaData.getTimeIngestedSubstance());
		}
	}

}