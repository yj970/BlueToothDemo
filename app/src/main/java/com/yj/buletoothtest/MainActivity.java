package com.yj.buletoothtest;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    String T = "MyTAG";
    final String DATA = "data";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(MainActivity.this, msg.getData().getString(DATA), Toast.LENGTH_LONG).show();
            }
        };


//        Log.d(T, BlueToothUtil.isHasBlueTooth()+"");
//        Log.d(T, BlueToothUtil.isOpenBlueTooth()+"");
////        BlueToothUtil.openBlueToothWithIntent(this);
//        BlueToothUtil.openBlueTooth();
//
//        // 注册蓝牙广播接收者
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
//        BlueToothReceiver receiver = new BlueToothReceiver();
//        registerReceiver(receiver, intentFilter);
//        BlueToothUtil.discoveryBlueToothDevice();

//        Log.d(T, BlueToothUtil.getBlueToothAdapter().getAddress());

//        new  Thread(new Runnable() {
//            @Override
//            public void run() {
//                BluetoothDevice bluetoothDevice = BlueToothUtil.getRemoteBlueToothDevice("50:8F:4C:E9:5F:93");// 要连接服务器（蓝牙）的mac地址
//                if (!BlueToothUtil.isBondBlueToothDevice(bluetoothDevice)) {
//                    boolean bood = BlueToothUtil.bondBlueToothDevice(bluetoothDevice);
//                    Log.d(T, "配对蓝牙 = " + bood);
//                } else {
//                    Log.d(T, "已经配对蓝牙");
//                }
////                boolean flag = BlueToothUtil.connectBlueToothDevice(BlueToothUtil.createBlueToothSocket(bluetoothDevice, "00001101-0000-1000-8000-00805F9B34FB"));
////                Log.d(T, "连接蓝牙 = " + flag);
//            }
//        }).start();


        //1服务端
        new Thread(new Runnable() {
            @Override
            public void run() {
                String uuid = "00001101-0000-1000-8000-00805F9B34FB";
                BluetoothServerSocket serverSocket = BlueToothUtil.getBlueToothServiceSocket("黑暗王", uuid);
                BluetoothSocket socket = null;
                try {
                    while (true) {
                        sentMessage("等待连接...");
                        socket = serverSocket.accept();// 等待连接，处于阻塞状态

                        sentMessage(socket.getRemoteDevice().getName() + " 已经连接....");

                        // 读取蓝牙用户端传来的数据
                        InputStream inputStream = null;
                        try {
                            inputStream = socket.getInputStream();
                            byte[] buffer = new byte[1024];
                            int bytes;
                            // 是否继续读取数据
                            boolean isRead = true;
                            while (isRead) {
//                                 获取inputStream的长度，大部分情况第一时间不能获取，所以需要死循环获取
                                int count = 0;
                                while (count == 0) {
                                    count = inputStream.available();
                                }
                                // 如果inputStream的长度比缓冲区小，说明已经可以读完了，所以取消死循环
                                if (count <= buffer.length) {
                                    isRead = false;
                                }
                                //读取数据
                                bytes = inputStream.read(buffer);
                                if (bytes > 0) {
                                    final byte[] data = new byte[bytes];
                                    System.arraycopy(buffer, 0, data, 0, bytes);
                                    sentMessage(new String(data));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        }
                    }
                } catch (IOException e) {
                    sentMessage("启动BlueToothServiceSocket失败...");
                }
            }
        }).start();


        //2客户端
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                 String uuid = "00001101-0000-1000-8000-00805F9B34FB";
//                 BluetoothDevice bluetoothDevice = BlueToothUtil.getRemoteBlueToothDevice("B6:11:46:03:37:0E");
//                if (!BlueToothUtil.isBondBlueToothDevice(bluetoothDevice)) {
//                    boolean bood = BlueToothUtil.bondBlueToothDevice(bluetoothDevice);
//                    Log.d(T, "配对蓝牙 = " + bood);
//                } else {
//                    Log.d(T, "已经配对蓝牙");
//                }
//                BluetoothSocket bluetoothSocket = BlueToothUtil.createBlueToothSocket(bluetoothDevice, uuid);
//                boolean flag = BlueToothUtil.connectBlueToothDevice(bluetoothSocket);
//                Log.d(T, "连接蓝牙 = " + flag);
//                // 写
//                OutputStream out = null;
//                try {
//                    out = bluetoothSocket.getOutputStream();
//                    out.write("你好吗？~~~~~~~~".getBytes());// 输出给服务器（蓝牙）
//                    out.flush();// 输出
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
////                    if (out != null) {
////                        try {
////                            out.close();
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////                    }
//                }
//            }
//        }).start();


    }


    private void sentMessage(String msg) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(DATA, msg);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BlueToothUtil.REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Log.d(T, "开启蓝牙成功");
            } else {
                Log.d(T, "开启蓝牙失败");
            }
        }
    }
}
