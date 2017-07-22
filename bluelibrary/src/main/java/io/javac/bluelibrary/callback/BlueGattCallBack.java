package io.javac.bluelibrary.callback;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import java.util.List;

import io.javac.bluelibrary.bean.NotifyMessage;
import io.javac.bluelibrary.code.CodeUtils;
import io.javac.bluelibrary.manager.EventManager;

/**
 * Created by Pencilso on 2017/7/22.
 */

public class BlueGattCallBack extends BluetoothGattCallback {
    private Object tag;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * 蓝牙设备连接状态被改变
     *
     * @param gatt
     * @param status
     * @param newState
     */
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        NotifyMessage notifyMessage = new NotifyMessage();
        notifyMessage.setTag(tag);
        notifyMessage.setCode(CodeUtils.SERVICE_ONCONNEXT_STATE);
        switch (newState) {//对蓝牙反馈的状态进行判断
            case BluetoothProfile.STATE_CONNECTED://已链接
                gatt.discoverServices();//调用发现设备中的服务
                notifyMessage.setData(true);
                break;
            case BluetoothProfile.STATE_DISCONNECTED://已断开
                notifyMessage.setData(false);
                break;
        }
        EventManager.getLibraryEvent().post(notifyMessage);
    }

    /**
     * 发现服务后回调方法
     *
     * @param gatt
     * @param status
     */
    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        /**
         * 遍历所有发现到的服务 把所有服务回调到EventBus当中
         */
        List<BluetoothGattService> services = gatt.getServices();
        NotifyMessage notifyMessage = new NotifyMessage(CodeUtils.SERVICE_ONSERVICESDISCOVERED, services, tag);
        EventManager.getLibraryEvent().post(notifyMessage);
    }
}
