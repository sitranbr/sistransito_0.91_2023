package net.sistransito.mobile.ait;

import android.content.Context;
import android.database.Cursor;

import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.database.InfractionDatabaseHelper;
import net.sistransito.mobile.timeandime.TimeAndIme;
import net.sistransito.R;

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

	TimeAndIme time;

	public AitData(Context context) {
		time = new TimeAndIme(context);
		setAitDateTime(time.getDate() + time.getTime());
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
	private String photo1, photo2, photo3, photo4;
	private String completedStatus;

	public String getApproach() {
		return approach;
	}

	public void setApproach(String approach) {
		this.approach = approach;
	}

	public String getPhoto1() {
		return photo1;
	}

	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}

	public String getPhoto2() {
		return photo2;
	}

	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}

	public String getPhoto3() {
		return photo3;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

	public String getPhoto4() {
		return photo4;
	}

	public void setPhoto4(String photo4) {
		this.photo4 = photo4;
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
				city = state = address = aitDate = aitTime = description = equipmentBrand = equipmentModel = serialNumber = measurementPerformed
                        = regulatedValue = valueConsidered = tcaNumber = alcoholTestNumber = retreat = procedures =
				observation = shipperIdentification = cpfShipper = cnpShipper =
						carrierIdentification = cpfCarrier = cnpjCarrier = cancellationStatus = reasonForCancellation
								= syncStatus = AitLatitude = AitLongitude = aitDateTime = completedStatus = "";
	}

	public void setAitDataFromCursor(Cursor myCursor) {

		this.setAitNumber(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.AIT_NUMBER)));
		this.setPlate(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.PLATE)));
		this.setStateVehicle(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.VEHICLE_STATE)));
		this.setRenavam(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.RENAVAM)));
		this.setChassi(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.CHASSI)));
		this.setCountry(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.COUNTRY)));
        this.setVehicleBrand(myCursor.getString(myCursor
                .getColumnIndex(InfractionDatabaseHelper.VEHICLE_BRAND)));
		this.setVehicleModel(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.VEHICLE_MODEL)));
		this.setVehycleColor(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.VEHICLE_COLOR)));
		this.setVehicleSpecies(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.SPECIES)));
		this.setVehicleCategory(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.CATEGORY)));
		this.setApproach(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.APPROACH)));
		this.setConductorName(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.DRIVER_NAME)));
		this.setForeignDriver(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.DRIVER_FOREIGN)));
		this.setDriverCountry(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.DRIVER_COUNTRY)));
		this.setQualifiedDriver(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.ENABLED_DRIVER)));
		this.setCnhPpd(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.DRIVER_LICENSE)));
		this.setCnhState(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.DRIVER_LICENSE_STATE)));
		this.setDocumentType(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.DOCUMENT_TYPE)));
		this.setDocumentNumber(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.DOCUMENT_NUMBER)));
		this.setInfraction(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.INFRACTION)));
		this.setFramingCode(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.FLAMING_CODE)));
		this.setUnfolding(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.UNFOLDING)));
		this.setArticle(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.ARTICLE)));
		this.setCityCode(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.CITY_CODE)));
		this.setCity(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.CITY)));
		this.setState(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.STATE)));
		this.setAddress(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.ADDRESS)));
		this.setAitDate(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.AIT_DATE)));
		this.setAitTime(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.AIT_TIME)));
		this.setDescription(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.EQUIPMENT_DESCRIPTION)));
		this.setEquipmentBrand(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.EQUIPMENT_BRAND)));
		this.setEquipmentModel(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.EQUIPMENT_MODEL)));
		this.setSerialNumber(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.EQUIPMENT_SERIAL)));
		this.setMeasurementPerformed(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.MEASUREMENT_PERFORMED)));
        this.setRegulatedValue(myCursor.getString(myCursor
                .getColumnIndex(InfractionDatabaseHelper.REGULATED_VALUE)));
		this.setValueConsidered(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.VALUE_CONSIDERED)));
		this.setTcaNumber(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.TCA_NUMBER)));
		this.setAlcoholTestNumber(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.ALCOHOL_TEST_NUMBER)));
		this.setRetreat(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.RETREAT)));
		this.setProcedures(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.PROCEDURES)));
		this.setObservation(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.OBSERVATION)));
		this.setShipperIdentification(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.SHIPPER_IDENTIFICATION)));
		this.setCpfShipper(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.CPF_SHIPPER)));
		this.setCnpShipper(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.CNPJ_SHIPPER)));
		this.setCarrierIdentification(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.CARRIER_IDENTIFICATION)));
		this.setCpfCarrier(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.CPF_CARRIER)));
		this.setCnpjCarrier(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.CNPJ_CARRIER)));
		this.setCancellationStatus(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.CANCEL_STATUS)));
		this.setReasonForCancellation(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.REASON_FOR_CANCEL)));
		this.setSyncStatus(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.SYNC_STATUS)));
		this.setAitDateTime(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.AIT_DATE_TIME)));
		this.setPhoto1(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.AIT_PHOTO1)));
		this.setPhoto2(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.AIT_PHOTO2)));
		this.setPhoto3(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.AIT_PHOTO3)));
		this.setPhoto4(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.AIT_PHOTO4)));
		this.setCompletedStatus(myCursor.getString(myCursor
				.getColumnIndex(InfractionDatabaseHelper.COMPLETED_STATUS)));

		this.setStoreFullData(true);

	}

	public String getAitListViewData(Context context) {
		String aitCheckData =
				context.getResources().getString(R.string.plate_title)
						+ getNewLine()
						+ plate
						+ getTwoLines()
						+ context.getResources().getString(R.string.renavam_number)
						+ getNewLine()
						+ renavam
						+ getTwoLines()
				+ context.getResources().getString(R.string.chassi_number)
				+ getNewLine()
				+ chassi
				+ getTwoLines()
		        + context.getResources().getString(R.string.rrd_state)
				+ getNewLine()
				+ state
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_country_name)
				+ getNewLine()
				+ country
				+ getTwoLines()
				+ context.getResources().getString(
						R.string.ait_vehicle_color)
				+ getNewLine()
				+ vehycleColor
				+ getTwoLines()
				+ context.getResources().getString(R.string.species)
				+ getNewLine()
				+ vehicleSpecies
				+ getTwoLines()
				+ context.getResources().getString(R.string.category)
				+ getNewLine()
				+ vehicleCategory
				+ getTwoLines()
				+ context.getResources().getString(R.string.driver_name)
				+ getNewLine()
				+ conductorName
				+ getTwoLines()
				+ context.getResources().getString(R.string.case_driver_is_foreign)
				+ getNewLine()
				+ foreignDriver
				+ getTwoLines()
				+ context.getResources().getString(R.string.driver_country)
				+ getNewLine()
				+ driverCountry
				+ getTwoLines()
				+ context.getResources().getString(R.string.case_driver_qualified)
				+ getNewLine()
				+ qualifiedDriver
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_cnh_ppd)
				+ getNewLine()
				+ cnhPpd
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_state_cnh)
				+ getNewLine()
				+ cnhState
				+ getTwoLines()

				+ context.getResources().getString(
						R.string.type_document_presented)
				+ getNewLine()
				+ documentType
				+ getNewLine()
				+ documentNumber
				+ getTwoLines()
				+ context.getResources().getString(R.string.infraction)
				+ getNewLine()
				+ infraction
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_framing_code)
				+ getNewLine()
				+ framingCode
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_unfold_code)
				+ getNewLine()
				+ unfolding
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_legal_support)
				+ getNewLine()
				+ article
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_city_name)
				+ getNewLine()
				+ city
				+ getTwoLines()
				+ context.getResources().getString(
						R.string.ait_city_code)
				+ getNewLine()
				+ cityCode
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_state)
				+ getNewLine()
				+ state
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_location)
				+ getNewLine()
				+ address
				+ getTwoLines()
				+ context.getResources().getString(R.string.date_field)
				+ getNewLine()
				+ aitDate
				+ getTwoLines()
				+ context.getResources().getString(R.string.time_field)
				+ getNewLine()
				+ aitTime
				+ getTwoLines()
				+ context.getResources().getString(
						R.string.ait_equipment_description)
				+ getNewLine()
				+ description
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_equipment_brand)
				+ getNewLine()
				+ equipmentBrand
				+ getTwoLines()

				+ context.getResources().getString(R.string.ait_equipment_model)
				+ getNewLine()
				+ equipmentModel
				+ getTwoLines()
				+ context.getResources().getString(
						R.string.ait_serial_number)
				+ getNewLine()
				+ serialNumber
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_measuremet_performed)
				+ getNewLine()
				+ measurementPerformed
				+ getTwoLines()
				+ context.getResources().getString(
						R.string.ait_considered_value)
				+ getNewLine()
				+ valueConsidered
						+ getTwoLines()
						+ context.getResources().getString(
						R.string.ait_tca_number)
						+ getNewLine()
						+ tcaNumber
				+ getTwoLines()
				+ context.getResources().getString(
						R.string.ait_sample_number_in_tca)
				+ getNewLine()
				+ alcoholTestNumber
				+ getTwoLines()
				+ context.getResources().getString(
						R.string.collection_documents)
				+ getNewLine()
				+ retreat
				+ getTwoLines()
				+ context.getResources().getString(R.string.procedures)
				+ getNewLine()
				+ procedures
				+ getTwoLines()
				+ context.getResources().getString(R.string.observations)
				+ getNewLine()
				+ observation
				+ getTwoLines()
				+ context.getResources().getString(
						R.string.ait_shipper_identification)
				+ getNewLine()
				+ shipperIdentification
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_cnpj_cpf_text)
				+ getNewLine()
				+ cpfShipper
				+ getTwoLines()
				+ context.getResources().getString(
						R.string.carrier_identification)
				+ getTwoLines()
				+ carrierIdentification
				+ getTwoLines()
				+ context.getResources().getString(R.string.ait_cnpj_cpf_text)
				+ getTwoLines()
				+ cpfCarrier
				+ getTwoLines()
				+ context.getResources().getString(R.string.cancel_ait_title)
				+ getTwoLines()
				+ reasonForCancellation +
				getNewLine();
		return aitCheckData;
	}

	public String getNewLine() {
		return AppConstants.NEW_LINE;
	}

	public String getTwoLines() {
		return AppConstants.NEW_LINE + AppConstants.NEW_LINE;
	}
}
