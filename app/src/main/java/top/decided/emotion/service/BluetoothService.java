package top.decided.emotion.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

public class BluetoothService {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothHidDevice hidDevice;

    public BluetoothService(Context context){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        init(context);
    }

    private void init(Context context){
        BluetoothProfile.ServiceListener listener = new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
            }

            @Override
            public void onServiceDisconnected(int profile) {
            }
        };
    }

}
