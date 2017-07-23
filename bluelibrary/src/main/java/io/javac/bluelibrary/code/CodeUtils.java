package io.javac.bluelibrary.code;

/**
 * Created by Pencilso on 2017/7/22.
 */

public class CodeUtils {
    //收到通知
    /**
     * 服务启动完毕
     */
    public static final int SERVICE_ONSTART = 0xa1;
    /**
     * 服务扫描到设备
     */
    public static final int SERVICE_ONDEVICE = 0xa2;
    /**
     * 蓝牙是否已经开启
     */
    public static final int SERVICE_ONBLUEOPEN = 0xa3;
    /**
     * 打开蓝牙后回调
     */
    public static final int SERVICE_ONOPENBLUE = 0xa4;
    /**
     * 关闭蓝牙后回调
     */
    public static final int SERVICE_ONSTOPBLUE = 0xa5;
    /**
     * 蓝牙的连接状态被改变
     */
    public static final int SERVICE_ONCONNEXT_STATE = 0xa6;
    /**
     * 发现了蓝牙设备当中的服务
     */
    public static final int SERVICE_ONSERVICESDISCOVERED = 0xa7;
    /**
     * 注册设备后的回调
     */
    public static final int SERVICE_ONREGISTER_DEVICE = 0xa8;
    /**
     * 收到蓝牙的Notify数据
     */
    public static final int SERVICE_ONNOTIFY = 0xa9;

    //发送通知
    /**
     * 调用服务的启动扫描
     */
    public static final int SERVICE_STARTSCANER = 0xb1;
    /**
     * 调用服务的停止扫描
     */
    public static final int SERVICE_STOPSCANER = 0xb2;
    /**
     * 调用服务检测蓝牙是否打开
     */
    public static final int SERVICE_BLUEISOPEN = 0xb3;
    /**
     * 调用打开蓝牙
     */
    public static final int SERVICE_OPENBLUE = 0xb4;
    /**
     * 调用关闭蓝牙
     */
    public static final int SERVICE_CLOSEBLUE = 0xb5;
    /**
     * 调用服务连接某一个蓝牙
     */
    public static final int SERVICE_DEVICE_CONN = 0xb6;
    /**
     * 注册设备
     */
    public static final int SERVICE_REGDEVICE = 0xb7;
    /**
     * 向服务写出数据
     */
    public static final int SERVICE_WRITE_DATA = 0xb8;
    /**
     * 向服务写出数据  自动转十六进制
     */
    public static final int SERVICE_WRITE_DATA_TOHEX = 0xb9;
}