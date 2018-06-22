package com.yj.buletoothtest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by yj on 2018/6/21.
 */

public class BlueToothUtil {
    final static String T = "BlueToothUtil";
    public static final int REQUEST_ENABLE_BT = 1098;

    // 获取蓝牙适配器
    public static BluetoothAdapter getBlueToothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    // 是否有蓝牙模块
    public static boolean isHasBlueTooth() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    // 是否已经打开蓝牙
    public static boolean isOpenBlueTooth() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    // 静默打开蓝牙：使用意图来打开蓝牙, 需要在activity的onActivityResult（）接收
    public static void openBlueToothWithIntent(Activity activity) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    // 静默打开蓝牙：需要权限<uses-permission android:name='android.permission.BLUETOOTH_ADMIN' />
    public static void openBlueTooth() {
        BluetoothAdapter.getDefaultAdapter().enable();
    }

    // 关闭蓝牙
    public static void closeBlueTooth() {
        BluetoothAdapter.getDefaultAdapter().disable();
    }

    // 扫描蓝牙设备, 需要注册广播配合使用
    public static boolean discoveryBlueToothDevice() {
        return BluetoothAdapter.getDefaultAdapter().startDiscovery();
    }

    // 获取远程的蓝牙设备
    public static BluetoothDevice getRemoteBlueToothDevice(String macAddress) {
        return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress);
    }

    // 创建远程蓝牙设备的socket通道
    public static BluetoothSocket createBlueToothSocket(BluetoothDevice remoteBlueTooth, String strUuid) {
        BluetoothSocket bluetoothSocket = null;
        UUID uuid = UUID.fromString(strUuid);
        try {
            bluetoothSocket = remoteBlueTooth.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(T, "创建远程蓝牙设备的socket通道失败....");
        }
        return bluetoothSocket;
    }

    // 是否已经配对该蓝牙设备
    public static boolean isBondBlueToothDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE) {
            return false;
        } else {
            return true;
        }
    }

    // 配对蓝牙设备, true为配对成功, false为配对失败 // TODO: 2018/6/22 true、false有问题
    public static boolean bondBlueToothDevice(BluetoothDevice bluetoothDevice) {
        try {
            Method creMethod = BluetoothDevice.class.getMethod("createBond");
            return (boolean) creMethod.invoke(bluetoothDevice);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e(T, "获取createBond方法失败...");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.e(T, "调用createBond方法失败...");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e(T, "非法访问createBond方法...");
        }
        return false;
    }

    // 连接蓝牙设备, true为连接成功, false为连接失败
    public static boolean connectBlueToothDevice(BluetoothSocket bluetoothSocket) {
        try {
            bluetoothSocket.connect();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(T, "连接蓝牙失败 = " + e.getMessage());
            return false;
        }
    }

    // 关闭蓝牙连接, true为关闭成功, false为关闭失败
    public static boolean closeConnectBlueToothDevice(BluetoothSocket bluetoothSocket) {
        try {
            bluetoothSocket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 设置蓝牙设备可见(本机) discoverableDuration = 可见时间; 默认=120秒，最大=3600秒
    public static void setBlueToothDiscoverable(Activity activity, int discoverableDuration) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, discoverableDuration);
        activity.startActivity(intent);
    }

    // 设置蓝牙设备可见(本机)
    public static void setBlueToothDiscoverable(Activity activity) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        activity.startActivity(intent);
    }

    // 作为服务器，开启serviceSocket等待连接
    public static BluetoothServerSocket getBlueToothServiceSocket(String name, String strUuid) {
        BluetoothServerSocket bluetoothServerSocket = null;
        UUID uuid = UUID.fromString(strUuid);
        try {
            bluetoothServerSocket = getBlueToothAdapter().listenUsingRfcommWithServiceRecord(name, uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bluetoothServerSocket;
    }
}
