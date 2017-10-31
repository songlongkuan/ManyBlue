package io.javac.ManyBlue.interfaces;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;

import java.util.List;

import io.javac.ManyBlue.bean.CharacteristicValues;
import io.javac.ManyBlue.bean.NotifyMessage;

/**
 * Created by Pencilso on 2017/7/27.
 */

public interface BaseNotifyListener {
//    /**
//     * 收到的原始数据
//     *
//     * @param notifyMessage
//     */
//    void onNotifyMessage(NotifyMessage notifyMessage);

    interface MobileBlueListener extends BaseNotifyListener {
        /**
         * 手机蓝牙状态回调
         *
         * @param enabled 是否打开了手机蓝牙
         */
        void onMobileBlueState(boolean enabled);

        /**
         * 调用打开手机系统蓝牙
         *
         * @param success 是否打开成功
         */
        void onMobileBlueEnabled(boolean success);

        /**
         * 调用关闭手机系统蓝牙
         *
         * @param success 是否关闭成功
         */
        void onMobileBlueDisable(boolean success);

    }

    interface ServiceListener extends BaseNotifyListener {
        /**
         * 蓝牙服务开启
         */
        void onServiceStart();

        /**
         * 蓝牙服务关闭
         */
        void onServiceStop();
    }

    interface DeviceListener extends BaseNotifyListener {
        /**
         * 扫描到蓝牙设备信号
         *
         * @param device
         */
        void onDeviceScanner(BluetoothDevice device);

        /**
         * 扫描到蓝牙设备信号列表
         * 当调用的
         * @param device
         */
        void onDeviceScanner(List<BluetoothDevice> device);
        /**
         * 蓝牙设备的连接状态
         *
         * @param connectState true为连接 false为断开
         */
        void onDeviceConnectState(boolean connectState, Object tag);

        /**
         * 发现蓝牙设备的服务后回调
         *
         * @param services 所有的设备服务
         * @param tag      设备的标识
         */
        void onDeviceServiceDiscover(List<BluetoothGattService> services, Object tag);

        /**
         * 设备注册回调
         *
         * @param registerState 注册状态
         */
        void onDeviceRegister(boolean registerState ,Object tag);


    }

    interface DeviceDataListener extends BaseNotifyListener {
        /**
         * 发送数据到设备的回调
         *
         * @param writeState 发送成功true  发送失败false
         * @param tag   设备的标识
         */
        void onDeviceWriteState(boolean writeState, Object tag);

        /**
         * 主动读取设备的通道数据
         *
         * @param characteristicValues 读取到的数据
         */
        void onDeviceReadMessage(CharacteristicValues characteristicValues);

        /**
         * 收到设备的Notify通知 十六进制转字符
         *
         * @param characteristicValues 读取到的数据
         */
        void onDeviceNotifyMessage(CharacteristicValues characteristicValues);


    }

    interface NotifyListener extends DeviceDataListener, DeviceListener, MobileBlueListener, ServiceListener {

    }
}
