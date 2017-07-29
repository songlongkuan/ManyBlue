package io.javac.ManyBlue;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.javac.ManyBlue.bean.CharacteristicValues;
import io.javac.ManyBlue.bean.NotifyMessage;
import io.javac.ManyBlue.bean.UUIDMessage;
import io.javac.ManyBlue.callback.BlueGattCallBack;
import io.javac.ManyBlue.code.CodeUtils;
import io.javac.ManyBlue.interfaces.BaseNotifyListener;
import io.javac.ManyBlue.manager.BluetoothGattManager;
import io.javac.ManyBlue.manager.EventManager;
import io.javac.ManyBlue.service.BlueLibraryService;

/**
 * Created by Pencilso on 2017/7/24.
 */

public class ManyBlue {
    /**
     * 判断服务是否运行
     *
     * @return
     */
    public static boolean runing(Context context) {
        boolean isrun = false;
        ActivityManager myAM = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(Integer.MAX_VALUE);
        int size = myList.size();
        if (size <= 0) {
            return false;
        }
        String name = BlueLibraryService.class.getName();
        for (int i = 0; i < size; i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(name)) {
                isrun = true;
                break;
            }
        }
        return isrun;
    }

    /**
     * 启动蓝牙服务
     *
     * @param context
     */
    public static void blueStartService(Context context) {
        if (!runing(context))
            context.startService(new Intent(context, BlueLibraryService.class));
    }

    /**
     * 停止蓝牙服务
     *
     * @param context
     */
    public static void blueStopService(Context context) {
        context.stopService(new Intent(context, BlueLibraryService.class));
    }

    /**
     * 手机蓝牙打开状态 （异步）
     */
    public static void blueEnableState() {
        EventManager.getLibraryEvent().post(NotifyMessage.newInstance().setCode(CodeUtils.SERVICE_BLUEENABLE));
    }

    /**
     * 手机蓝牙打开状态 （同步）
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static boolean blueEnableState(Context context) {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        return bluetoothAdapter.isEnabled();
    }

    /**
     * 是否启用系统蓝牙
     */
    public static void blueEnable(boolean enable) {
        if (enable)
            EventManager.getLibraryEvent().post(NotifyMessage.newInstance().setCode(CodeUtils.SERVICE_OPENBLUE));
        else
            EventManager.getLibraryEvent().post(NotifyMessage.newInstance().setCode(CodeUtils.SERVICE_CLOSEBLUE));
    }

    /**
     * 打开蓝牙扫描
     */
    public static void blueStartScaner() {
        EventManager.getLibraryEvent().post(NotifyMessage.newInstance().setCode(CodeUtils.SERVICE_STARTSCANER));
    }

    /**
     * 关闭蓝牙扫描
     */
    public static void blueStopScaner() {
        EventManager.getLibraryEvent().post(NotifyMessage.newInstance().setCode(CodeUtils.SERVICE_STOPSCANER));
    }


    /**
     * 连接某一个设备
     *
     * @param address 蓝牙设备的MAC地址
     * @param tag     蓝牙设备的标识 （自定义）
     */
    public static void blueConnectDevice(String address, Object tag) {
        if (address == null || tag == null) throw new NullPointerException("param can'not null");
        NotifyMessage notifyMessage = NotifyMessage.newInstance().setCode(CodeUtils.SERVICE_DEVICE_CONN).setData(address).setTag(tag);
        EventManager.getLibraryEvent().post(notifyMessage);
    }

    /**
     * 注册设备通道
     *
     * @param uuidMessage
     * @param tag
     */
    public static void blueRegisterDevice(UUIDMessage uuidMessage, Object tag) {
        if (uuidMessage == null || tag == null)
            throw new NullPointerException("param can'not null");
        NotifyMessage notifyMessage = NotifyMessage.newInstance().setCode(CodeUtils.SERVICE_REGDEVICE).setData(uuidMessage).setTag(tag);
        EventManager.getLibraryEvent().post(notifyMessage);
    }

    /**
     * 向蓝牙写出字符数据 不转换
     *
     * @param data
     * @param tag
     */
    public static void blueWriteData(String data, Object tag) {
        if (data == null || tag == null) throw new NullPointerException("param can'not null");
        NotifyMessage notifyMessage = NotifyMessage.newInstance().setCode(CodeUtils.SERVICE_WRITE_DATA).setData(data).setTag(tag);
        EventManager.getLibraryEvent().post(notifyMessage);
    }

    /**
     * 向蓝牙写入字符十六进制数据
     *
     * @param data
     * @param tag
     */
    public static void blueWriteDataStr2Hex(String data, Object tag) {
        if (data == null || tag == null) throw new NullPointerException("param can'not null");
        NotifyMessage notifyMessage = NotifyMessage.newInstance().setCode(CodeUtils.SERVICE_WRITE_DATA_TOHEX).setData(data).setTag(tag);
        EventManager.getLibraryEvent().post(notifyMessage);
    }

    /**
     * 向蓝牙写入字节数组数据
     *
     * @param data
     * @param tag
     */
    public static void blueWriteDataByteArray(Byte data[], Object tag) {
        if (data == null || tag == null) throw new NullPointerException("param can'not null");
        NotifyMessage notifyMessage = NotifyMessage.newInstance().setTag(tag).setData(data).setCode(CodeUtils.SERVICE_WRITE_DATA_TOBYTE);
        EventManager.getLibraryEvent().post(notifyMessage);
    }

    /**
     * 主动读取通道数据 (非Notify方式使用)
     *
     * @param tag
     */
    public static void blueReadData(Object tag) {
        if (tag == null) throw new NullPointerException("tag can'not null");
        NotifyMessage notifyMessage = NotifyMessage.newInstance().setCode(CodeUtils.SERVICE_READ_DATA).setTag(tag);
        EventManager.getLibraryEvent().post(notifyMessage);
    }
//
//    /**
//     * 读取蓝牙传递的十六进制 并且转换为字符 (非Notify方式使用)
//     *
//     * @param tag
//     */
//    public static void blueReadDataHex2Str(Object tag) {
//        if (tag == null) throw new NullPointerException("tag can'not null");
//        NotifyMessage notifyMessage = NotifyMessage.newInstance().setCode(CodeUtils.SERVICE_READ_DATA_HEX2STR).setTag(tag);
//        EventManager.getLibraryEvent().post(notifyMessage);
//    }
//
//    /**
//     * 读取蓝牙传递的字节数组数据 (非Notify方式使用)
//     *
//     * @param tag
//     */
//    public static void blueReadDataByteArray(Object tag) {
//        if (tag == null) throw new NullPointerException("tag can'not null");
//        EventManager.getLibraryEvent().post(NotifyMessage.newInstance().setTag(tag).setCode(CodeUtils.SERVICE_READ_DATA_BYTEARRAY));
//    }

    /**
     * 断开指定的设备
     *
     * @param tag
     */
    public static void blueDisconnectedDevice(Object tag) {
        if (tag == null) throw new NullPointerException("tag can'not null");
        EventManager.getLibraryEvent().post(NotifyMessage.newInstance().setTag(tag).setCode(CodeUtils.SERVICE_DISCONN_DEVICE));
    }

    /**
     * 断开所有的设备
     *
     * @param tag
     */
    public static void blueDisconnectedDeviceAll(Object tag) {
        if (tag == null) throw new NullPointerException("tag can'not null");
        EventManager.getLibraryEvent().post(NotifyMessage.newInstance().setTag(tag).setCode(CodeUtils.SERVICE_DISCONN_DEVICE_ALL));
    }

    /**
     * 获取所有已连接的设备
     *
     * @return
     */
    public static List<BluetoothDevice> getConnDeviceAll() {
        Collection<BlueGattCallBack> values = BluetoothGattManager.getGattMap().values();
        List<BluetoothDevice> list = new ArrayList<>(values.size());
        for (BlueGattCallBack callBack : values) {
            list.add(callBack.getDevice());
        }
        return list;
    }

    /**
     * 获取指定设备标记的设备
     *
     * @param tag
     * @return
     */
    public static BluetoothDevice getConnDevice(Object tag) {
        BlueGattCallBack gatt = null;
        if ((gatt = BluetoothGattManager.getGatt(tag)) != null)
            return gatt.getDevice();
        return null;
    }

    /**
     * 处理监听
     *
     * @param listener      监听事件
     * @param notifyMessage 回调的数据
     */
    public static void dealtListener(BaseNotifyListener listener, NotifyMessage notifyMessage) {
        /**
         * 设备监听
         */
        if (listener instanceof BaseNotifyListener.MobileBlueListener) {
            BaseNotifyListener.MobileBlueListener mobileBlueListener = (BaseNotifyListener.MobileBlueListener) listener;
            switch (notifyMessage.getCode()) {
                case CodeUtils.SERVICE_ONBLUEENABLE: //手机蓝牙打开状态
                {
                    mobileBlueListener.onMobileBlueState(Boolean.valueOf(notifyMessage.getData().toString()));
                }
                break;
                case CodeUtils.SERVICE_ONENABLEBLUE://调用打开手机蓝牙回调
                {
                    mobileBlueListener.onMobileBlueEnabled(Boolean.valueOf(notifyMessage.getData().toString()));
                }
                break;
                case CodeUtils.SERVICE_ONDISABLEBLUE://调用关闭手机蓝牙回调
                {
                    mobileBlueListener.onMobileBlueDisable(Boolean.valueOf(notifyMessage.getData().toString()));
                }
                break;
            }
        }
        /**
         * 设备的监听
         */
        if (listener instanceof BaseNotifyListener.DeviceListener) {
            BaseNotifyListener.DeviceListener deviceListener = (BaseNotifyListener.DeviceListener) listener;
            switch (notifyMessage.getCode()) {
                case CodeUtils.SERVICE_ONCONNEXT_STATE://蓝牙连接状态
                {
                    deviceListener.onDeviceConnectState(Boolean.valueOf(notifyMessage.getData().toString()), notifyMessage.getTag());
                }
                break;
                case CodeUtils.SERVICE_ONSERVICESDISCOVERED://发现服务回调
                {
                    deviceListener.onDeviceServiceDiscover((List<BluetoothGattService>) notifyMessage.getData(), notifyMessage.getTag());
                }
                break;
                case CodeUtils.SERVICE_ONREGISTER_DEVICE:// 注册设备的回调
                {
                    deviceListener.onDeviceRegister(Boolean.valueOf(notifyMessage.getData().toString()));
                }
                break;
                case CodeUtils.SERVICE_ONDEVICE://扫描到蓝牙设备
                {
                    deviceListener.onDeviceScanner((BluetoothDevice) notifyMessage.getData());
                }
                break;
            }
        }
        if (listener instanceof BaseNotifyListener.DeviceDataListener) {//设备数据的回调
            BaseNotifyListener.DeviceDataListener deviceDataListener = (BaseNotifyListener.DeviceDataListener) listener;
            switch (notifyMessage.getCode()) {
                case CodeUtils.SERVICE_ONREAD://主动读取通道数据
                {
                    CharacteristicValues characteristicValues = notifyMessage.getData();
                    deviceDataListener.onDeviceReadMessage(characteristicValues);
                }
                break;
                case CodeUtils.SERVICE_ONNOTIFY://Notify收到的消息
                {
                    CharacteristicValues characteristicValues = notifyMessage.getData();
                    deviceDataListener.onDeviceNotifyMessage(characteristicValues);
                }
                break;
                case CodeUtils.SERVICE_ONWRITE://写出数据状态
                {
                    deviceDataListener.onDeviceWriteState(Boolean.valueOf(notifyMessage.getData().toString()), notifyMessage.getTag());
                }
                break;
            }
        }

        if (listener instanceof BaseNotifyListener.ServiceListener) {
            BaseNotifyListener.ServiceListener serviceListener = (BaseNotifyListener.ServiceListener) listener;
            switch (notifyMessage.getCode()) {
                case CodeUtils.SERVICE_ONSTART: //服务启动完毕
                {
                    serviceListener.onServiceStart();
                }
                break;
                case CodeUtils.SERVICE_ONSTOP: //服务已停止
                {
                    serviceListener.onServiceStop();
                }
                break;
            }
        }
    }
}