package net.sistransito.ui;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.widget.Toast;

import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.SetttingDatabaseHelper;
import net.sistransito.mobile.fragment.AnyAlertDialog;
import net.sistransito.mobile.bluetoothprint.PrintBitmap.base.BasePrintBitmap;
import net.sistransito.mobile.bluetoothprint.bluetooth.Bluetooth;
import net.sistransito.mobile.bluetoothprint.bluetooth.BluetoothPrinterListener;
import net.sistransito.mobile.bluetoothprint.bluetooth.ESCP;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

import java.security.Permission;
import java.util.Arrays;
import java.util.Set;

public abstract class BasePrintActivity extends AppCompatActivity
        implements BluetoothPrinterListener {
    private Bluetooth mBth = null;
    private BasePrintBitmap tempPrintBitmap = null;
    private int BLUETOOTH_PERMISSION_REQUEST_CODE = 0010;

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

        if(!checkBluetoothPermission()){
            tempPrintBitmap = printBitmap;
            return ;
        }

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

    private boolean checkBluetoothPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN},BLUETOOTH_PERMISSION_REQUEST_CODE);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE){
            if(grantResults.length >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                if(tempPrintBitmap != null){
                    startPrint(tempPrintBitmap);
                }else{
                    Toast.makeText(this, "There was an error ,please re run the app.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Please allow bluetooth permission to print.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
