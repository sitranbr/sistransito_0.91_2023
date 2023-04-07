package net.sistransito.ui;

import android.bluetooth.BluetoothDevice;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.SetttingDatabaseHelper;
import net.sistransito.mobile.fragment.AnyAlertDialog;
import net.sistransito.mobile.bluetoothprint.PrintBitmap.base.BasePrintBitmap;
import net.sistransito.mobile.bluetoothprint.bluetooth.Bluetooth;
import net.sistransito.mobile.bluetoothprint.bluetooth.BluetoothPrinterListener;
import net.sistransito.mobile.bluetoothprint.bluetooth.ESCP;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

import java.util.Set;

public abstract class BasePrintActivity extends AppCompatActivity
        implements BluetoothPrinterListener {
    private Bluetooth mBth = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void print(Object mainData, Object aitData) {
        if (mBth == null) {
            mBth = new Bluetooth();
        }
        onPrintData(mainData, aitData);
    }

    @Override
    public void print(Object mainData, Object aitData, String copy) {
        if (mBth == null) {
            mBth = new Bluetooth();
        }
        onPrintData(mainData, aitData, copy);
    }

    protected abstract void onPrintData(Object mainData, Object aitData);

    protected abstract void onPrintData(Object mainData, Object aitData, String copy);

    protected void startPrint(final BasePrintBitmap printBitmap) {
        if (mBth == null) {
            mBth = new Bluetooth();
        }
        if (checkBth()) {
            Routine.showAlert(getResources().getString(R.string.mgs_print), this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ESCP.ImageToEsc(printBitmap.getBitmap(),
                            mBth.Ostream, 8, 8);
                    closeBth();
                }
            }).start();
        }
    }

    public void closeBth() {
        if (mBth.isConnected()) {
            mBth.Close();
        }
    }

    public boolean checkBth() {

        Cursor cursor = (DatabaseCreator
                .getSettingDatabaseAdapter(getApplicationContext()))
                .getSettingCursor();

        String nPrinter = cursor.getString(cursor
                .getColumnIndex(SetttingDatabaseHelper.SETTING_PRINTER));

        if (!mBth.isConnected()) {
            if (!mBth.Enable()) {
                AnyAlertDialog.simpleAletMessage(getResources().getString(R.string.error_load_bluetooth), this);
                return false;
            }
            String mac = null;
            Set<BluetoothDevice> devices = mBth.GetBondedDevices();
            for (BluetoothDevice device : devices) {
                if (nPrinter.equals(device.getName())) {
                    mac = device.getAddress();
                }
            }
            if (mac == null) {
                AnyAlertDialog.simpleAletMessage(getResources().getString(R.string.pairing_error)
                        + " " + nPrinter + "\n\n" + getResources().getString(R.string.pairing_message), this);
                return false;
            }
            if (!mBth.Open(mac)) {
                AnyAlertDialog.simpleAletMessage(getResources().getString(R.string.error_connection_printer) + " ["
                        + mac
                        + " ]\n\n " + getResources().getString(R.string.reconnect_printer), this);
                return false;
            }
        }
        return true;
    }

}
