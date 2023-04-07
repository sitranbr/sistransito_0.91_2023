package net.sistransito.mobile.bluetoothprint.bluetooth;

public interface BluetoothPrinterListener {

	void print(Object mainData, Object aitData);

	void print(Object mainData, Object aitData, String copy);

}
