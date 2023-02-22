package net.sistransito.mobile.plate.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class CreatePlateRawDataFromJson {
	private DataFromPlate dataFromPlate;

	public CreatePlateRawDataFromJson(String data, Context context) {
		setDataFromPlate(data);
		dataFromPlate = new DataFromPlate(context);
		setDataFromPlate(data);
	}

	public DataFromPlate getDataFromPlate() {
		return dataFromPlate;
	}

	public void setDataFromPlate(String data) {
		JSONObject jsonObject;

		int success = 10;
		final int SUCCESS = 1;

		try {
			jsonObject = new JSONObject(data);
			success = jsonObject.getInt("success");

			if (success == SUCCESS) {

				try {

					dataFromPlate.setPlate(jsonObject.getString("plate"));

					dataFromPlate.setCity(jsonObject.getString("city"));

					dataFromPlate.setState(jsonObject.getString("state"));

					dataFromPlate.setRenavam(jsonObject.getString("renavam"));

					dataFromPlate.setChassi(jsonObject.getString("chassi"));

					dataFromPlate.setBrand(jsonObject.getString("brand"));

					dataFromPlate.setModel(jsonObject.getString("model"));

					dataFromPlate.setColor(jsonObject.getString("color"));

					dataFromPlate.setType(jsonObject.getString("type"));

					dataFromPlate.setSpecies(jsonObject.getString("species"));

					dataFromPlate.setCategory(jsonObject.getString("category"));

					dataFromPlate.setYearManufacture(jsonObject.getString("year_manufacture"));

					dataFromPlate.setYearModel(jsonObject
							.getString("model_year"));

					dataFromPlate.setLicenceYear(jsonObject
							.getString("year_licence"));

					dataFromPlate.setLicenceData(jsonObject
							.getString("date_licence"));

					dataFromPlate.setLicenceStatus(jsonObject
							.getString("status_licence"));

					dataFromPlate.setIpva(jsonObject.getString("ipva"));

					dataFromPlate.setInsurance(jsonObject.getString("insurance"));

					dataFromPlate.setInfractions(jsonObject
							.getString("infractions"));

					dataFromPlate.setInfrationAmout(jsonObject.getString("amout_infraction"));

					dataFromPlate.setTheftRecord(jsonObject.getString("record_theft"));

					dataFromPlate.setRestrictions(jsonObject
							.getString("administrative_restrictions"));

				} catch (Exception e) {

				}

			} else {
				dataFromPlate = null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
