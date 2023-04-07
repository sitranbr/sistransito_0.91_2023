package net.sistransito.mobile.tav;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.database.TavDatabaseHelper;
import net.sistransito.R;

import java.io.Serializable;

public class TavData implements Serializable {

	private static final String TAV_ID = "tav_id";
	private static final long serialVersionUID = 18393745L;

	private String plate, aitNumber, ownerName, cpfCnpj,
			renavamNumber, chassisNumber, leverHead,
			carBody, ceiling, hoodBody, bodyworkRightSide,
			bodyWorkLeftSide, trunkBodywork, roofBodywork,
			engine, dashboard, hoodPaint, rightSidePaint,
			leftSidePaint, trunkPainting, hoodPainting, radiator,
			sideGlass, windShield, rearWindshield, antenna,
			trunk, seats, baterry, wheelCover, airConditioner,
			fireExtinguisher, headLight, rearLight,
			jack, frontBumper, hearBumper,
			driverSunVisor, tires, spareTire, radio,
			rearviewMirror, outsideMirror, carpet, triangle,
			steeringWheel, motorcycleHandlebar, odometer, fuelGauge,
			removedVia, companyName, winchDriverName,
			observation, tavNumber, tavType;

	public String getTavType() {
		return tavType;
	}

	public void setTavType(String tavType) {
		this.tavType = tavType;
	}

	public String getTavNumber() {
		return tavNumber;
	}

	public void setTavNumber(String tavNumber) {
		this.tavNumber = tavNumber;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getWinchDriverName() {
		return winchDriverName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setWinchDriverName(
			String winchDriverName) {
		this.winchDriverName = winchDriverName;
	}

	public String getAitNumber() {
		return aitNumber;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public String getRenavamNumber() {
		return renavamNumber;
	}

	public String getChassisNumber() {
		return chassisNumber;
	}

	public String getLeverHead() {
		return leverHead;
	}

	public String getCarBody() {
		return carBody;
	}

	public String getCeiling() {
		return ceiling;
	}

	public String getHoodBody() {
		return hoodBody;
	}

	public String getBodyworkRightSide() {
		return bodyworkRightSide;
	}

	public String getBodyWorkLeftSide() {
		return bodyWorkLeftSide;
	}

	public String getTrunkBodywork() {
		return trunkBodywork;
	}

	public String getRoofBodywork() {
		return roofBodywork;
	}

	public String getEngine() {
		return engine;
	}

	public String getDashboard() {
		return dashboard;
	}

	public String getHoodPaint() {
		return hoodPaint;
	}

	public String getRightSidePaint() {
		return rightSidePaint;
	}

	public String getLeftSidePaint() {
		return leftSidePaint;
	}

	public String getTrunkPainting() {
		return trunkPainting;
	}

	public String getHoodPainting() {
		return hoodPainting;
	}

	public String getRadiator() {
		return radiator;
	}

	public String getSideGlass() {
		return sideGlass;
	}

	public String getWindShield() {
		return windShield;
	}

	public String getRearWindshield() {
		return rearWindshield;
	}

	public String getAntenna() {
		return antenna;
	}

	public String getTrunk() {
		return trunk;
	}

	public String getSeats() {
		return seats;
	}

	public String getBaterry() {
		return baterry;
	}

	public String getWheelCover() {
		return wheelCover;
	}

	public String getAirConditioner() {
		return airConditioner;
	}

	public String getFireExtinguisher() {
		return fireExtinguisher;
	}

	public String getHeadLight() {
		return headLight;
	}

	public String getRearLight() {
		return rearLight;
	}

	public String getJack() {
		return jack;
	}

	public String getFrontBumper() {
		return frontBumper;
	}

	public String getHearBumper() {
		return hearBumper;
	}

	public String getDriverSunVisor() {
		return driverSunVisor;
	}

	public String getTires() {
		return tires;
	}

	public String getSpareTire() {
		return spareTire;
	}

	public String getRadio() {
		return radio;
	}

	public String getRearviewMirror() {
		return rearviewMirror;
	}

	public String getOutsideMirror() {
		return outsideMirror;
	}

	public String getCarpet() {
		return carpet;
	}

	public String getTriangle() {
		return triangle;
	}

	public String getSteeringWheel() {
		return steeringWheel;
	}

	public String getMotorcycleHandlebar() {
		return motorcycleHandlebar;
	}

	public String getOdometer() {
		return odometer;
	}

	public String getFuelGauge() {
		return fuelGauge;
	}

	public String getRemovedVia() {
		return removedVia;
	}

	public String getObservation() {
		return observation;
	}

	public void setAitNumber(String aitNumber) {
		this.aitNumber = aitNumber;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public void setRenavamNumber(String renavamNumber) {
		this.renavamNumber = renavamNumber;
	}

	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	public void setLeverHead(String leverHead) {
		this.leverHead = leverHead;
	}

	public void setCarBody(String carBody) {
		this.carBody = carBody;
	}

	public void setCeiling(String ceiling) {
		this.ceiling = ceiling;
	}

	public void setHoodBody(String hoodBody) {
		this.hoodBody = hoodBody;
	}

	public void setBodyworkRightSide(String bodyworkRightSide) {
		this.bodyworkRightSide = bodyworkRightSide;
	}

	public void setBodyWorkLeftSide(String bodyWorkLeftSide) {
		this.bodyWorkLeftSide = bodyWorkLeftSide;
	}

	public void setTrunkBodywork(String trunkBodywork) {
		this.trunkBodywork = trunkBodywork;
	}

	public void setRoofBodywork(String roofBodywork) {
		this.roofBodywork = roofBodywork;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public void setDashboard(String dashboard) {
		this.dashboard = dashboard;
	}

	public void setHoodPaint(String hoodPaint) {
		this.hoodPaint = hoodPaint;
	}

	public void setRightSidePaint(String rightSidePaint) {
		this.rightSidePaint = rightSidePaint;
	}

	public void setLeftSidePaint(String leftSidePaint) {
		this.leftSidePaint = leftSidePaint;
	}

	public void setTrunkPainting(String trunkPainting) {
		this.trunkPainting = trunkPainting;
	}

	public void setHoodPainting(String hoodPainting) {
		this.hoodPainting = hoodPainting;
	}

	public void setRadiator(String radiator) {
		this.radiator = radiator;
	}

	public void setSideGlass(String sideGlass) {
		this.sideGlass = sideGlass;
	}

	public void setWindShield(String windShield) {
		this.windShield = windShield;
	}

	public void setRearWindshield(String rearWindshield) {
		this.rearWindshield = rearWindshield;
	}

	public void setAntenna(String antenna) {
		this.antenna = antenna;
	}

	public void setTrunk(String trunk) {
		this.trunk = trunk;
	}

	public void setSeats(String seats) {
		this.seats = seats;
	}

	public void setBaterry(String baterry) {
		this.baterry = baterry;
	}

	public void setWheelCover(String wheelCover) {
		this.wheelCover = wheelCover;
	}

	public void setAirConditioner(String airConditioner) {
		this.airConditioner = airConditioner;
	}

	public void setFireExtinguisher(String fireExtinguisher) {
		this.fireExtinguisher = fireExtinguisher;
	}

	public void setHeadLight(String headLight) {
		this.headLight = headLight;
	}

	public void setRearLight(String rearLight) {
		this.rearLight = rearLight;
	}

	public void setJack(String jack) {
		this.jack = jack;
	}

	public void setFrontBumper(String frontBumper) {
		this.frontBumper = frontBumper;
	}

	public void setHearBumper(String hearBumper) {
		this.hearBumper = hearBumper;
	}

	public void setDriverSunVisor(String driverSunVisor) {
		this.driverSunVisor = driverSunVisor;
	}

	public void setTires(String tires) {
		this.tires = tires;
	}

	public void setSpareTire(String spareTire) {
		this.spareTire = spareTire;
	}

	public void setRadio(String radio) {
		this.radio = radio;
	}

	public void setRearviewMirror(String rearviewMirror) {
		this.rearviewMirror = rearviewMirror;
	}

	public void setOutsideMirror(String outsideMirror) {
		this.outsideMirror = outsideMirror;
	}

	public void setCarpet(String carpet) {
		this.carpet = carpet;
	}

	public void setTriangle(String triangle) {
		this.triangle = triangle;
	}

	public void setSteeringWheel(String steeringWheel) {
		this.steeringWheel = steeringWheel;
	}

	public void setMotorcycleHandlebar(String motorcycleHandlebar) {
		this.motorcycleHandlebar = motorcycleHandlebar;
	}

	public void setOdometer(String odometer) {
		this.odometer = odometer;
	}

	public void setFuelGauge(String fuelGauge) {
		this.fuelGauge = fuelGauge;
	}

	public void setRemovedVia(String removedVia) {
		this.removedVia = removedVia;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public TavData() {

		aitNumber = ownerName = cpfCnpj = renavamNumber = chassisNumber = leverHead = carBody = ceiling = hoodBody = bodyworkRightSide = bodyWorkLeftSide = trunkBodywork = roofBodywork = engine = dashboard = hoodPaint = rightSidePaint = leftSidePaint = trunkPainting = hoodPainting = radiator = sideGlass = windShield = rearWindshield = antenna = trunk = seats = baterry = wheelCover = airConditioner = fireExtinguisher = headLight = rearLight = jack = frontBumper = hearBumper = driverSunVisor = tires = spareTire = radio = rearviewMirror = outsideMirror = carpet = triangle = steeringWheel = motorcycleHandlebar = odometer = fuelGauge = removedVia = companyName = winchDriverName = observation = tavNumber = tavType = "";

	}

	public static String getTavId() {
		return TAV_ID;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTAVListerView(Context context) {
		String tav = "";
		tav = context.getResources().getString(R.string.tav_ait_number)
				+ getNewline()
				+ aitNumber
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.tav_owner_name) + getNewline()
				+ ownerName + getNewline_2() + getviewData(context);

		return tav;

	}

	private String getNewline() {
		return AppConstants.NEW_LINE;
	}

	private String getNewline_2() {
		return AppConstants.NEW_LINE + AppConstants.NEW_LINE;
	}

	public void setTAVDataFromCursor(Cursor myCursor) {

		plate = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.PLATE));
		
		
		Log.d("plate", plate);
		aitNumber = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.AIT_NUMBER));
		ownerName = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.OWNER_NAME));
		cpfCnpj = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.CPF_CNPJ));
		renavamNumber = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.RENAVAM_NUMBER));
		chassisNumber = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.CHASSI_NUMBER));
		leverHead = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.LEVER_HEAD));
		carBody = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.BODYWORK));
		ceiling = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.CEILING));
		hoodBody = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.HOOD_BODYWORK));

		bodyworkRightSide = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.BODYWORK_RIGHT_SIDE));
		bodyWorkLeftSide = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.BODYWORK_LEFT_SIDE));
		trunkBodywork = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.TRUNK_BODYWORK));
		roofBodywork = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.ROOF_BODYWORK));

		engine = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.ENGINE));
		dashboard = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.DASHBOARD));
		hoodPaint = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.HOOD_PAINTING));
		rightSidePaint = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.WRIGHT_PAINTING));
		leftSidePaint = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.LEFT_PAINTING));
		trunkPainting = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.TRUNK_PAINTING));

		hoodPainting = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.HOOD_PAINTING));
		radiator = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.RADIATOR));
		sideGlass = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.SIDE_GLASS));

		windShield = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.WINDSHIELD));
		rearWindshield = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.HEAR_WINDSHIELD));

		antenna = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.RADIO_ANTENNA));
		trunk = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.TRUNK));
		seats = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.SEAT));
		baterry = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.BATTERY));
		wheelCover = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.HUBCAP));

		airConditioner = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.AIR_CONDITIONER));

		fireExtinguisher = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.FIRE_EXTINGUISHER));
		headLight = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.HEADLIGHT));
		rearLight = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.REAR_LIGHT));
		jack = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.JACK));

		frontBumper = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.FRONT_BUMPER));
		hearBumper = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.REAR_BUMPER));
		driverSunVisor = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.DRIVER_SUNSHADE));
		tires = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.TIRES));

		spareTire = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.SPARE_TIRE));
		radio = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.RADIO));
		rearviewMirror = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.REARVIEW_MIRROR));

		outsideMirror = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.OUTSIDE_MIRROR));
		carpet = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.CARPET));
		triangle = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.TRIANGLE));

		steeringWheel = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.STEERING_WHEEL));
		motorcycleHandlebar = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.HANDLEBARS));
		odometer = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.ODOMETRO));

		fuelGauge = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.FUEL_MARKER));
		removedVia = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.REMOVAL_THROUGH_OF));
		companyName = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.COMPANY_NAME));
		winchDriverName = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.TOW_TRUCK_DRIVER_NAME));
		observation = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.OBSERVATION));
		tavNumber = myCursor.getString(myCursor
				.getColumnIndex(TavDatabaseHelper.TAV_NUMBER));

	}

	public CharSequence getTCAListViewData(Context mycontext) {

		return getviewData(mycontext);
	}

	private String getviewData(Context context) {

		String data = context.getResources().getString(R.string.tav_cpf_cnpj)
				+ getNewline()
				+ cpfCnpj
				+ getNewline_2()
				+ context.getResources().getString(R.string.renavam_number)
				+ getNewline()
				+ renavamNumber
				+ getNewline_2()
				+ context.getResources().getString(R.string.chassi_number)
				+ getNewline()
				+ chassisNumber
				+ getNewline_2()
				+ context.getResources().getString(R.string.shift_lever)
				+ getNewline()
				+ leverHead
				+ getNewline_2()
				+ context.getResources().getString(R.string.bodywork)
				+ getNewline()
				+ carBody
				+ getNewline_2()
				+ context.getResources().getString(R.string.ceiling)
				+ getNewline()
				+ ceiling
				+ getNewline_2()
				+ context.getResources().getString(R.string.hood_bodywork)
				+ getNewline()
				+ hoodBody
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.bodywork_right_side)
				+ getNewline()
				+ bodyworkRightSide
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.bodywork_left_side)
				+ getNewline()
				+ bodyWorkLeftSide
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.trunk_tinwork)
				+ getNewline()
				+ trunkBodywork
				+ getNewline_2()
				+ context.getResources().getString(R.string.roof_bodywork)
				+ getNewline()
				+ roofBodywork
				+ getNewline_2()
				+ context.getResources().getString(R.string.engine)
				+ getNewline()
				+ engine
				+ getNewline_2()
				+ context.getResources().getString(R.string.dashboard)
				+ getNewline()
				+ dashboard
				+ getNewline_2()
				+ context.getResources().getString(R.string.hood_painting)
				+ getNewline()
				+ hoodPaint
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.wright_painting)
				+ getNewline()
				+ rightSidePaint
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.left_painting)
				+ getNewline()
				+ leftSidePaint
				+ getNewline_2()
				+ context.getResources().getString(R.string.trunk_painting)
				+ getNewline()
				+ trunkPainting
				+ getNewline_2()
				+ context.getResources().getString(R.string.hoof_painting)
				+ getNewline()
				+ hoodPainting
				+ getNewline_2()
				+ context.getResources().getString(R.string.radiator)
				+ getNewline()
				+ radiator
				+ getNewline_2()
				+ context.getResources().getString(R.string.side_glass)
				+ getNewline()
				+ sideGlass
				+ getNewline_2()
				+ context.getResources().getString(R.string.windshield)
				+ getNewline()
				+ windShield
				+ getNewline_2()
				+ context.getResources().getString(R.string.back_windshield)
				+ getNewline()
				+ rearWindshield
				+ getNewline_2()
				+ context.getResources().getString(R.string.radio_antenna)
				+ getNewline()
				+ antenna
				+ getNewline_2()
				+ context.getResources().getString(R.string.baggage_handler)
				+ getNewline()
				+ trunk
				+ getNewline_2()
				+ context.getResources().getString(R.string.seat)
				+ getNewline()
				+ seats
				+ getNewline_2()
				+ context.getResources().getString(R.string.battery)
				+ getNewline()
				+ baterry
				+ getNewline_2()
				+ context.getResources().getString(R.string.hubcap)
				+ getNewline()
				+ wheelCover
				+ getNewline_2()
				+ context.getResources()
						.getString(R.string.air_conditioner)
				+ getNewline()
				+ airConditioner
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.fire_extinguisher)
				+ getNewline()
				+ fireExtinguisher
				+ getNewline_2()
				+ context.getResources().getString(R.string.headlight)
				+ getNewline()
				+ headLight
				+ getNewline_2()
				+ context.getResources().getString(R.string.rear_light)
				+ getNewline()
				+ rearLight
				+ getNewline_2()
				+ context.getResources().getString(R.string.jack)
				+ getNewline()
				+ jack
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.front_bumper)
				+ getNewline()
				+ frontBumper
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.rear_bumper)
				+ getNewline()
				+ hearBumper
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.driver_sunshade)
				+ getNewline()
				+ driverSunVisor
				+ getNewline_2()
				+ context.getResources().getString(R.string.tires)
				+ getNewline()
				+ tires
				+ getNewline_2()
				+ context.getResources().getString(R.string.stepe_tire)
				+ getNewline()
				+ spareTire
				+ getNewline_2()
				+ context.getResources().getString(R.string.radio)
				+ getNewline()
				+ radio
				+ getNewline_2()
				+ context.getResources().getString(R.string.rearview_mirror)
				+ getNewline()
				+ rearviewMirror
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.right_outside_mirror)
				+ getNewline()
				+ outsideMirror
				+ getNewline_2()
				+ context.getResources().getString(R.string.carpet)
				+ getNewline()
				+ carpet
				+ getNewline_2()
				+ context.getResources().getString(R.string.triangle)
				+ getNewline()
				+ triangle
				+ getNewline_2()
				+ context.getResources().getString(R.string.steering_wheel)
				+ getNewline()
				+ steeringWheel
				+ getNewline_2()
				+ context.getResources().getString(R.string.handlebars)
				+ getNewline()
				+ motorcycleHandlebar
				+ getNewline_2()
				+ context.getResources().getString(R.string.odometro)
				+ getNewline()
				+ odometer
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.fuel_marker)
				+ getNewline()
				+ fuelGauge
				+ getNewline_2()
				+ context.getResources().getString(R.string.removal_through_of)
				+ getNewline()
				+ removedVia
				+ getNewline_2()
				+ context.getResources().getString(R.string.tav_company_name)
				+ getNewline()
				+ companyName
				+ getNewline_2()
				+ context.getResources().getString(
						R.string.tav_tow_truck_driver_name) + getNewline()
				+ winchDriverName + getNewline_2()
				+ context.getResources().getString(R.string.observations)
				+ getNewline() + observation + getNewline_2()
				+ context.getResources().getString(R.string.tav_number)
				+ getNewline() + tavNumber + getNewline_2();

		return data;

	}

}
