package io.javac.bluelibrary.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.javac.bluelibrary.bean.NotifyMessage;
import io.javac.bluelibrary.bean.UUIDMessage;
import io.javac.bluelibrary.callback.BlueGattCallBack;
import io.javac.bluelibrary.code.CodeUtils;
import io.javac.bluelibrary.manager.BluetoothGattManager;
import io.javac.bluelibrary.manager.EventManager;
import io.javac.bluelibrary.utils.HexUtils;

/**
 * Created by Pencilso on 2017/7/22.
 */
public class BlueLibraryService extends Service implements BluetoothAdapter.LeScanCallback {
    private BluetoothAdapter bluetoothAdapter;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
         * 这里返回状态有三个值，分别是:
         * 1、START_STICKY：当服务进程在运行时被杀死，系统将会把它置为started状态，但是不保存其传递的Intent对象，之后，系统会尝试重新创建服务;
         * 2、START_NOT_STICKY：当服务进程在运行时被杀死，并且没有新的Intent对象传递过来的话，系统将会把它置为started状态，
         *   但是系统不会重新创建服务，直到startService(Intent intent)方法再次被调用;
         * 3、START_REDELIVER_INTENT：当服务进程在运行时被杀死，它将会在隔一段时间后自动创建，并且最后一个传递的Intent对象将会再次传递过来。
         */
        initConfig();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 初始化方法
     */
    private void initConfig() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        EventManager.getLibraryEvent().register(this);
        /**
         * 服务启动完毕  发送一个Event通知
         */
        NotifyMessage notifyMessage = new NotifyMessage(CodeUtils.SERVICE_ONSTART, null);
        EventManager.getLibraryEvent().post(notifyMessage);
    }

    /**
     * 释放EventBus
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventManager.getLibraryEvent().unregister(this);
        EventManager.removeAllEvent();
        if (bluetoothAdapter != null) {
            bluetoothAdapter.stopLeScan(this);
        }
    }

    @Override
    public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
        NotifyMessage notifyMessage = new NotifyMessage(CodeUtils.SERVICE_ONDEVICE, bluetoothDevice);
        EventManager.getLibraryEvent().post(notifyMessage);
    }

    /**
     * 收到订阅消息
     *
     * @param notifyMessage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotifyMessage notifyMessage) {
        if (notifyMessage == null) return;
        NotifyMessage message = new NotifyMessage();
        switch (notifyMessage.getCode()) {
            case CodeUtils.SERVICE_STARTSCANER://启动扫描
                bluetoothAdapter.startLeScan(this);
                break;
            case CodeUtils.SERVICE_STOPSCANER://停止扫描
                bluetoothAdapter.stopLeScan(this);
                break;
            case CodeUtils.SERVICE_BLUEISOPEN://蓝牙是否打开
            {
                message.setData(bluetoothAdapter.isEnabled());
                message.setCode(CodeUtils.SERVICE_ONBLUEOPEN);
                EventManager.getLibraryEvent().post(message);
            }
            break;
            case CodeUtils.SERVICE_OPENBLUE://打开手机蓝牙
            {
                message.setData(bluetoothAdapter.enable());
                message.setCode(CodeUtils.SERVICE_ONOPENBLUE);
                EventManager.getLibraryEvent().post(message);
            }
            break;
            case CodeUtils.SERVICE_CLOSEBLUE://关闭手机蓝牙
            {
                message.setCode(CodeUtils.SERVICE_ONSTOPBLUE);
                message.setData(bluetoothAdapter.disable());
                EventManager.getLibraryEvent().post(message);
            }
            break;
            case CodeUtils.SERVICE_DEVICE_CONN://连接某一个设备  并且主动发现服务
            {
                BluetoothDevice remoteDevice = bluetoothAdapter.getRemoteDevice(notifyMessage.getData().toString());
                BlueGattCallBack blueGattCallBack = new BlueGattCallBack();
                remoteDevice.connectGatt(this, false, blueGattCallBack);
                blueGattCallBack.setTag(notifyMessage.getTag());
                BluetoothGattManager.putGatt(notifyMessage.getTag(), blueGattCallBack);
            }
            break;
            case CodeUtils.SERVICE_REGDEVICE://注册通道
            {
                UUIDMessage uuidMessage = (UUIDMessage) notifyMessage.getData();
                BlueGattCallBack gatt = BluetoothGattManager.getGatt(notifyMessage.getTag());
                gatt.registerDevice(uuidMessage);
            }
            break;
            case CodeUtils.SERVICE_WRITE_DATA://写出原始数据
            {
                BlueGattCallBack gatt = BluetoothGattManager.getGatt(notifyMessage.getTag());
                gatt.write_data(notifyMessage.getData().toString());
            }
            break;
            case CodeUtils.SERVICE_WRITE_DATA_TOHEX://写出十六进制
            {
                BlueGattCallBack gatt = BluetoothGattManager.getGatt(notifyMessage.getTag());
                gatt.write_data(HexUtils.getHexBytes(notifyMessage.getData().toString()));
            }
            break;
            case CodeUtils.SERVICE_READ_DATA://读取原始数据
            {
                BlueGattCallBack gatt = BluetoothGattManager.getGatt(notifyMessage.getTag());
                gatt.read_data(CodeUtils.SERVICE_READ_DATA);
            }
                break;
            case CodeUtils.SERVICE_READ_DATA_HEX2STR://读取十六进制转字符
            {
                BlueGattCallBack gatt = BluetoothGattManager.getGatt(notifyMessage.getTag());
                gatt.read_data(CodeUtils.SERVICE_READ_DATA_HEX2STR);
            }

                break;
        }
    }

}