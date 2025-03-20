package net.sistransito.mobile.ait;

import android.content.Context;
import android.database.Cursor;

import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.database.InfractionDatabaseHelper;
import net.sistransito.mobile.timeandime.TimeAndIme;
import net.sistransito.R;
import net.sistransito.mobile.utility.Routine;

import java.io.Serializable;

public class AitData implements Serializable {

	public boolean isStoreFullData;
	public boolean isDataisNull = false;

	public boolean isDataisNull() {
		return isDataisNull;
	}

	public void setDataisNull(boolean isDataisNull) {
		this.isDataisNull = isDataisNull;
	}

	//TimeAndIme time;

	//private String aitDate;
	//private String aitTime;

	/*
	public AitData(Context context) {
		time = new TimeAndIme(context);
		setAitDateTime(time.getDate() + time.getTime());
		//setAitDateTime("09:39:10" + "05/03/2025");
	}
	 */

	public AitData(Context context) {
		TimeAndIme time = new TimeAndIme(context);
		this.aitDate = time.getDate();
		this.aitTime = time.getTime();
		setAitDateTime(aitDate + aitTime);
	}

	public static final String IDAuto = "ait_id";
	public static final long serialVersionUID = 14393745L;
	private String idAit;
	private String AitNumber;
	private String plate;
	private String stateVehicle;
	private String country;
	private String vehicleBrand;
	private String vehicleModel;
	private String renavam;
	private String chassi;
	private String vehycleColor;
	private String vehicleSpecies;
	private String vehicleCategory;
	private String approach;
	private String conductorName;
	private String foreignDriver;
	private String driverCountry;
	private String qualifiedDriver;
	private String cnhPpd;
	private String documentType;
	private String documentNumber;
	private String cnhState;
	private String infraction;
	private String framingCode;
	private String unfolding;
	private String article;
	private String cityCode;
	private String city;
	private String state;
	private String address;
	private String aitDate;
	private String aitTime;
	private String description;
	private String equipmentBrand;
	private String equipmentModel;
	private String serialNumber;
	private String measurementPerformed;
    private String regulatedValue;
	private String valueConsidered;
	private String tcaNumber;
	private String alcoholTestNumber;
	private String retreat;
	private String procedures;
	private String observation;
	private String shipperIdentification;
	private String cpfShipper;
	private String cnpShipper;
	private String carrierIdentification;
	private String cpfCarrier;
	private String cnpjCarrier;
	private String cancellationStatus;
	private String reasonForCancellation;
	private String syncStatus;
	private String AitLatitude;
	private String AitLongitude;
	private String aitDateTime;
	private String completedStatus;

	public String getApproach() {
		return approach;
	}

	public void setApproach(String approach) {
		this.approach = approach;
	}

	public String getTcaNumber() {
		return tcaNumber;
	}

	public void setTcaNumber(String tcaNumber) {
		this.tcaNumber = tcaNumber;
	}

    public String getRegulatedValue() {
        return regulatedValue;
    }

    public void setRegulatedValue(String regulatedValue) {
        this.regulatedValue = regulatedValue;
    }

    public String getCompletedStatus() {
		return completedStatus;
	}

	public void setCompletedStatus(String completedStatus) {
		this.completedStatus = completedStatus;
	}

	public String getAitLatitude() {
		return AitLatitude;
	}

	public void setAitLatitude(String aitLatitude) {
		this.AitLatitude = aitLatitude;
	}

	public String getAitLongitude() {
		return AitLongitude;
	}

	public void setAitLongitude(String aitLongitude) {
		this.AitLongitude = aitLongitude;
	}

	public String getAitDateTime() {
		return aitDateTime;
	}

	public void setAitDateTime(String aitDateTime) {
		this.aitDateTime = aitDateTime;
	}

	public String getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}

	public String getCancellationStatus() {
		return cancellationStatus;
	}

	public void setCancellationStatus(String cancellationStatus) {
		this.cancellationStatus = cancellationStatus;
	}

	public String getReasonForCancellation() {
		return reasonForCancellation;
	}

	public void setReasonForCancellation(String reasonForCancellation) {
		this.reasonForCancellation = reasonForCancellation;
	}

	public String getStateVehicle() {
		return stateVehicle;
	}

	public void setStateVehicle(String stateVehicle) {
		this.stateVehicle = stateVehicle;
	}

	public String getVehicleBrand() {
		return vehicleBrand;
	}

	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getRenavam() { return renavam; }

	public void setRenavam(String renavam) { this.renavam = renavam; }

	public String getChassi() {
		return chassi;
	}

	public void setChassi(String chassi) {
		this.chassi = chassi;
	}

	public String getCnhState() {
		return cnhState;
	}

	public void setCnhState(String cnhState) {
		this.cnhState = cnhState;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAitNumber() {
		return AitNumber;
	}

	public void setAitNumber(String aitNumber) {
		this.AitNumber = aitNumber;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getVehycleColor() {
		return vehycleColor;
	}

	public void setVehycleColor(String vehycleColor) {
		this.vehycleColor = vehycleColor;
	}

	public String getVehicleSpecies() {
		return vehicleSpecies;
	}

	public void setVehicleSpecies(String vehicleSpecies) {
		this.vehicleSpecies = vehicleSpecies;
	}

	public String getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(String vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}

	public String getConductorName() {
		return conductorName;
	}

	public void setConductorName(String conductorName) {
		this.conductorName = conductorName;
	}

	public String getForeignDriver() {
		return foreignDriver;
	}

	public void setForeignDriver(String foreignDriver) {
		this.foreignDriver = foreignDriver;
	}

	public String getDriverCountry() {
		return driverCountry;
	}

	public void setDriverCountry(String driverCountry) {
		this.driverCountry = driverCountry;
	}

	public String getQualifiedDriver() {
		return qualifiedDriver;
	}

	public void setQualifiedDriver(String qualifiedDriver) {
		this.qualifiedDriver = qualifiedDriver;
	}

	public String getCnhPpd() {
		return cnhPpd;
	}

	public void setCnhPpd(String cnhPpd) {
		this.cnhPpd = cnhPpd;
	}

	public String getInfraction() {
		return infraction;
	}

	public void setInfraction(String infraction) {
		this.infraction = infraction;
	}

	public String getFramingCode() {
		return framingCode;
	}

	public void setFramingCode(String framingCode) {
		this.framingCode = framingCode;
	}

	public String getUnfolding() {
		return unfolding;
	}

	public void setUnfolding(String unfolding) {
		this.unfolding = unfolding;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAitDate() {
		return aitDate;
	}

	public void setAitDate(String aitDate) {
		this.aitDate = aitDate;
	}

	public String getAitTime() {
		return aitTime;
	}

	public void setAitTime(String aitTime) {
		this.aitTime = aitTime;
	}

	public String getEquipmentBrand() {
		return equipmentBrand;
	}

	public void setEquipmentBrand(String equipmentBrand) {
		this.equipmentBrand = equipmentBrand;
	}

	public String getEquipmentModel() {
		return equipmentModel;
	}

	public void setEquipmentModel(String equipmentModel) {
		this.equipmentModel = equipmentModel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getMeasurementPerformed() {
		return measurementPerformed;
	}

	public void setMeasurementPerformed(String measurementPerformed) {
		this.measurementPerformed = measurementPerformed;
	}

	public String getValueConsidered() {
		return valueConsidered;
	}

	public void setValueConsidered(String valueConsidered) {
		this.valueConsidered = valueConsidered;
	}

	public String getAlcoholTestNumber() {
		return alcoholTestNumber;
	}

	public void setAlcoholTestNumber(String alcoholTestNumber) {
		this.alcoholTestNumber = alcoholTestNumber;
	}

	public String getRetreat() {
		return retreat;
	}

	public String getProcedures() {
		return procedures;
	}

	public void setRetreat(String retreat) {
		this.retreat = retreat;
	}

	public void setProcedures(String procedures) {
		this.procedures = procedures;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getShipperIdentification() {
		return shipperIdentification;
	}

	public void setShipperIdentification(String shipperIdentification) {
		this.shipperIdentification = shipperIdentification;
	}

	public String getCpfShipper() {
		return cpfShipper;
	}

	public void setCpfShipper(String cpfShipper) {
		this.cpfShipper = cpfShipper;
	}

	public String getCnpShipper() {
		return cnpShipper;
	}

	public void setCnpShipper(String cnpjShipper) {
		this.cnpShipper = cnpjShipper;
	}

	public String getCarrierIdentification() {
		return carrierIdentification;
	}

	public void setCarrierIdentification(
			String carrierIdentification) {
		this.carrierIdentification = carrierIdentification;
	}

	public String getCpfCarrier() {
		return cpfCarrier;
	}

	public void setCpfCarrier(String cpfCarrier) {
		this.cpfCarrier = cpfCarrier;
	}

	public String getCnpjCarrier() {
		return cnpjCarrier;
	}

	public void setCnpjCarrier(String cnpjCarrier) {
		this.cnpjCarrier = cnpjCarrier;
	}

	public static String getAitID() {
		return IDAuto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public AitData() {
		super();
		initializeAllData();
	}

	public boolean isStoreFullData() {
		return isStoreFullData;
	}

	public void setStoreFullData(boolean isStoreFullData) {
		this.isStoreFullData = isStoreFullData;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public void initializeAllData() {
		address = aitDate = aitTime = idAit = plate = vehicleModel = chassi = renavam = vehycleColor = vehicleSpecies = vehicleCategory = approach = conductorName =
				foreignDriver = driverCountry = qualifiedDriver = documentNumber = cnhPpd = cnhState = documentType = infraction =
						framingCode = unfolding = article = cityCode =
				city = state = description = equipmentBrand = equipmentModel = serialNumber = measurementPerformed
                        = regulatedValue = valueConsidered = tcaNumber = alcoholTestNumber = retreat = procedures =
				observation = shipperIdentification = cpfShipper = cnpShipper =
						carrierIdentification = cpfCarrier = cnpjCarrier = cancellationStatus = reasonForCancellation
								= syncStatus = AitLatitude = AitLongitude = aitDateTime = completedStatus = "";
	}

	public void setAitDataFromCursor(Cursor myCursor) {
		setColumnString(myCursor, InfractionDatabaseHelper.AIT_NUMBER, value -> this.setAitNumber(value));
		setColumnString(myCursor, InfractionDatabaseHelper.PLATE, value -> this.setPlate(value));
		setColumnString(myCursor, InfractionDatabaseHelper.VEHICLE_STATE, value -> this.setStateVehicle(value));
		setColumnString(myCursor, InfractionDatabaseHelper.RENAVAM, value -> this.setRenavam(value));
		setColumnString(myCursor, InfractionDatabaseHelper.CHASSI, value -> this.setChassi(value));
		setColumnString(myCursor, InfractionDatabaseHelper.COUNTRY, value -> this.setCountry(value));
		setColumnString(myCursor, InfractionDatabaseHelper.VEHICLE_BRAND, value -> this.setVehicleBrand(value));
		setColumnString(myCursor, InfractionDatabaseHelper.VEHICLE_MODEL, value -> this.setVehicleModel(value));
		setColumnString(myCursor, InfractionDatabaseHelper.VEHICLE_COLOR, value -> this.setVehycleColor(value));
		setColumnString(myCursor, InfractionDatabaseHelper.SPECIES, value -> this.setVehicleSpecies(value));
		setColumnString(myCursor, InfractionDatabaseHelper.CATEGORY, value -> this.setVehicleCategory(value));
		setColumnString(myCursor, InfractionDatabaseHelper.APPROACH, value -> this.setApproach(value));
		setColumnString(myCursor, InfractionDatabaseHelper.DRIVER_NAME, value -> this.setConductorName(value));
		setColumnString(myCursor, InfractionDatabaseHelper.DRIVER_FOREIGN, value -> this.setForeignDriver(value));
		setColumnString(myCursor, InfractionDatabaseHelper.DRIVER_COUNTRY, value -> this.setDriverCountry(value));
		setColumnString(myCursor, InfractionDatabaseHelper.ENABLED_DRIVER, value -> this.setQualifiedDriver(value));
		setColumnString(myCursor, InfractionDatabaseHelper.DRIVER_LICENSE, value -> this.setCnhPpd(value));
		setColumnString(myCursor, InfractionDatabaseHelper.DRIVER_LICENSE_STATE, value -> this.setCnhState(value));
		setColumnString(myCursor, InfractionDatabaseHelper.DOCUMENT_TYPE, value -> this.setDocumentType(value));
		setColumnString(myCursor, InfractionDatabaseHelper.DOCUMENT_NUMBER, value -> this.setDocumentNumber(value));
		setColumnString(myCursor, InfractionDatabaseHelper.INFRACTION, value -> this.setInfraction(value));
		setColumnString(myCursor, InfractionDatabaseHelper.FLAMING_CODE, value -> this.setFramingCode(value));
		setColumnString(myCursor, InfractionDatabaseHelper.UNFOLDING, value -> this.setUnfolding(value));
		setColumnString(myCursor, InfractionDatabaseHelper.ARTICLE, value -> this.setArticle(value));
		setColumnString(myCursor, InfractionDatabaseHelper.CITY_CODE, value -> this.setCityCode(value));
		setColumnString(myCursor, InfractionDatabaseHelper.CITY, value -> this.setCity(value));
		setColumnString(myCursor, InfractionDatabaseHelper.STATE, value -> this.setState(value));
		setColumnString(myCursor, InfractionDatabaseHelper.ADDRESS, value -> this.setAddress(value));
		setColumnString(myCursor, InfractionDatabaseHelper.AIT_DATE, value -> this.setAitDate(value));
		setColumnString(myCursor, InfractionDatabaseHelper.AIT_TIME, value -> this.setAitTime(value));
		setColumnString(myCursor, InfractionDatabaseHelper.EQUIPMENT_DESCRIPTION, value -> this.setDescription(value));
		setColumnString(myCursor, InfractionDatabaseHelper.EQUIPMENT_BRAND, value -> this.setEquipmentBrand(value));
		setColumnString(myCursor, InfractionDatabaseHelper.EQUIPMENT_MODEL, value -> this.setEquipmentModel(value));
		setColumnString(myCursor, InfractionDatabaseHelper.EQUIPMENT_SERIAL, value -> this.setSerialNumber(value));
		setColumnString(myCursor, InfractionDatabaseHelper.MEASUREMENT_PERFORMED, value -> this.setMeasurementPerformed(value));
		setColumnString(myCursor, InfractionDatabaseHelper.REGULATED_VALUE, value -> this.setRegulatedValue(value));
		setColumnString(myCursor, InfractionDatabaseHelper.VALUE_CONSIDERED, value -> this.setValueConsidered(value));
		setColumnString(myCursor, InfractionDatabaseHelper.TCA_NUMBER, value -> this.setTcaNumber(value));
		setColumnString(myCursor, InfractionDatabaseHelper.ALCOHOL_TEST_NUMBER, value -> this.setAlcoholTestNumber(value));
		setColumnString(myCursor, InfractionDatabaseHelper.RETREAT, value -> this.setRetreat(value));
		setColumnString(myCursor, InfractionDatabaseHelper.PROCEDURES, value -> this.setProcedures(value));
		setColumnString(myCursor, InfractionDatabaseHelper.OBSERVATION, value -> this.setObservation(value));
		setColumnString(myCursor, InfractionDatabaseHelper.SHIPPER_IDENTIFICATION, value -> this.setShipperIdentification(value));
		setColumnString(myCursor, InfractionDatabaseHelper.CPF_SHIPPER, value -> this.setCpfShipper(value));
		setColumnString(myCursor, InfractionDatabaseHelper.CNPJ_SHIPPER, value -> this.setCnpShipper(value));
		setColumnString(myCursor, InfractionDatabaseHelper.CARRIER_IDENTIFICATION, value -> this.setCarrierIdentification(value));
		setColumnString(myCursor, InfractionDatabaseHelper.CPF_CARRIER, value -> this.setCpfCarrier(value));
		setColumnString(myCursor, InfractionDatabaseHelper.CNPJ_CARRIER, value -> this.setCnpjCarrier(value));
		setColumnString(myCursor, InfractionDatabaseHelper.CANCEL_STATUS, value -> this.setCancellationStatus(value));
		setColumnString(myCursor, InfractionDatabaseHelper.REASON_FOR_CANCEL, value -> this.setReasonForCancellation(value));
		setColumnString(myCursor, InfractionDatabaseHelper.SYNC_STATUS, value -> this.setSyncStatus(value));
		setColumnString(myCursor, InfractionDatabaseHelper.AIT_DATE_TIME, value -> this.setAitDateTime(value));
		setColumnString(myCursor, InfractionDatabaseHelper.COMPLETED_STATUS, value -> this.setCompletedStatus(value));

		this.setStoreFullData(true);
	}

	private void setColumnString(Cursor cursor, String columnName, java.util.function.Consumer<String> setter) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (columnIndex >= 0) {
			setter.accept(cursor.getString(columnIndex));
		}
	}

	public String getAitListViewData(Context context) {
		String aitCheckData =
				context.getResources().getString(R.string.plate_title)
						+ Routine.getNewline(1)
						+ plate
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.renavam_number)
						+ Routine.getNewline(1)
						+ renavam
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.chassi_number)
						+ Routine.getNewline(1)
						+ chassi
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.rrd_state)
						+ Routine.getNewline(1)
						+ state
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_country_name)
						+ Routine.getNewline(1)
						+ country
						+ Routine.getNewline(2)
						+ context.getResources().getString(
						R.string.ait_vehicle_color)
						+ Routine.getNewline(1)
						+ vehycleColor
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.species)
						+ Routine.getNewline(1)
						+ vehicleSpecies
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.category)
						+ Routine.getNewline(1)
						+ vehicleCategory
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.driver_name)
						+ Routine.getNewline(1)
						+ conductorName
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.case_driver_is_foreign)
						+ Routine.getNewline(1)
						+ foreignDriver
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.driver_country)
						+ Routine.getNewline(1)
						+ driverCountry
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.case_driver_qualified)
						+ Routine.getNewline(1)
						+ qualifiedDriver
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_cnh_ppd)
						+ Routine.getNewline(1)
						+ cnhPpd
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_state_cnh)
						+ Routine.getNewline(1)
						+ cnhState
						+ Routine.getNewline(2)

						+ context.getResources().getString(
						R.string.type_document_presented)
						+ Routine.getNewline(1)
						+ documentType
						+ Routine.getNewline(1)
						+ documentNumber
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.infraction)
						+ Routine.getNewline(1)
						+ infraction
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_framing_code)
						+ Routine.getNewline(1)
						+ framingCode
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_unfold_code)
						+ Routine.getNewline(1)
						+ unfolding
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_legal_support)
						+ Routine.getNewline(1)
						+ article
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_city_name)
						+ Routine.getNewline(1)
						+ city
						+ Routine.getNewline(2)
						+ context.getResources().getString(
						R.string.ait_city_code)
						+ Routine.getNewline(1)
						+ cityCode
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_state)
						+ Routine.getNewline(1)
						+ state
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_location)
						+ Routine.getNewline(1)
						+ address
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.date_field)
						+ Routine.getNewline(1)
						+ aitDate
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.time_field)
						+ Routine.getNewline(1)
						+ aitTime
						+ Routine.getNewline(2)
						+ context.getResources().getString(
						R.string.ait_equipment_description)
						+ Routine.getNewline(1)
						+ description
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_equipment_brand)
						+ Routine.getNewline(1)
						+ equipmentBrand
						+ Routine.getNewline(2)

						+ context.getResources().getString(R.string.ait_equipment_model)
						+ Routine.getNewline(1)
						+ equipmentModel
						+ Routine.getNewline(2)
						+ context.getResources().getString(
						R.string.ait_serial_number)
						+ Routine.getNewline(1)
						+ serialNumber
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_measuremet_performed)
						+ Routine.getNewline(1)
						+ measurementPerformed
						+ Routine.getNewline(2)
						+ context.getResources().getString(
						R.string.ait_considered_value)
						+ Routine.getNewline(1)
						+ valueConsidered
						+ Routine.getNewline(2)
						+ context.getResources().getString(
						R.string.ait_tca_number)
						+ Routine.getNewline(1)
						+ tcaNumber
						+ Routine.getNewline(2)
						+ context.getResources().getString(
						R.string.ait_sample_number_in_tca)
						+ Routine.getNewline(1)
						+ alcoholTestNumber
						+ Routine.getNewline(2)
						+ context.getResources().getString(
						R.string.collection_documents)
						+ Routine.getNewline(1)
						+ retreat
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.procedures)
						+ Routine.getNewline(1)
						+ procedures
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.observations)
						+ Routine.getNewline(1)
						+ observation
						+ Routine.getNewline(2)
						+ context.getResources().getString(
						R.string.ait_shipper_identification)
						+ Routine.getNewline(1)
						+ shipperIdentification
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_cnpj_cpf_text)
						+ Routine.getNewline(1)
						+ cpfShipper
						+ Routine.getNewline(2)
						+ context.getResources().getString(
						R.string.carrier_identification)
						+ Routine.getNewline(2)
						+ carrierIdentification
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.ait_cnpj_cpf_text)
						+ Routine.getNewline(2)
						+ cpfCarrier
						+ Routine.getNewline(2)
						+ context.getResources().getString(R.string.cancel_ait_title)
						+ Routine.getNewline(2)
						+ reasonForCancellation +
						Routine.getNewline(1);
		return aitCheckData;
	}

}
