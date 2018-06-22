package com.yj.buletoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;
import android.util.Log;

/**
 * Created by yj on 2018/6/21.
 */

public class BlueToothReceiver extends BroadcastReceiver {
    String T = "MyTAG";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        // discovery过程中没有接到这2个action，咋回事？？？ // TODO: 2018/6/22  
//        if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
//            Log.d(T, "开始搜寻蓝牙设备...");
//        }
//
//        if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
//            Log.d(T, "搜寻蓝牙设备结束...");
//        }

        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
            Log.d(T, "-----");
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Log.d(T, device.getName()+"");// device.getName()有可能返回null
            Log.d(T, device.getAddress());
            Log.d(T, "-----");
        }


    }
}
