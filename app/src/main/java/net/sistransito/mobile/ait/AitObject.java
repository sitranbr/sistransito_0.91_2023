package net.sistransito.mobile.ait;

public class AitObject {

	private static AitData aitData;

	private AitObject() {
		aitData = new AitData();
	}

	public static AitData getAitData() {
		return aitData;
	}

	public static void setAitData(AitData data) {
		aitData = data;
	}

}
