package io.javac.ManyBlue.code;

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
     * 蓝牙服务关闭
     */
    public static final int SERVICE_ONSTOP = 0xac;
    /**
     * 蓝牙服务扫描到设备
     */
    public static final int SERVICE_ONDEVICE = 0xa2;
    /**
     * 系统蓝牙开启|关闭 状态
     */
    public static final int SERVICE_ONBLUEENABLE = 0xa3;
    /**
     * 打开蓝牙后回调
     */
    public static final int SERVICE_ONENABLEBLUE = 0xa4;
    /**
     * 关闭蓝牙后回调
     */
    public static final int SERVICE_ONDISABLEBLUE = 0xa5;
    /**
     * 蓝牙设备的连接状态被改变
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
    /**
     * 向通道写出数据的回调
     */
    public static final int SERVICE_ONWRITE = 0xaa;
    /**
     * 调用读取数据的  数据回调
     */
    public static final int SERVICE_ONREAD = 0xab;


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
    public static final int SERVICE_BLUEENABLE = 0xb3;
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
     * 向服务写出字符数据
     */
    public static final int SERVICE_WRITE_DATA = 0xb8;
    /**
     * 向服务写出数据  自动转十六进制
     */
    public static final int SERVICE_WRITE_DATA_TOHEX = 0xb9;
    /**
     * 向服务写出Byte数组数据
     */
    public static final int SERVICE_WRITE_DATA_TOBYTE = 0xbc;
    /**
     * 主动读取数据
     */
    public static final int SERVICE_READ_DATA = 0xba;
//    /**
//     * 读取十六进制转字符的数据
//     */
//    public static final int SERVICE_READ_DATA_HEX2STR = 0xbb;
//    /**
//     * 读取原始字节数据数据
//     */
//    public static final int SERVICE_READ_DATA_BYTEARRAY = 0xbd;
    /**
     * 断开指定设备
     */
    public static final int SERVICE_DISCONN_DEVICE = 0xbe;
    /**
     * 断开所有设备
     */
    public static final int SERVICE_DISCONN_DEVICE_ALL = 0xbf;

    /**
     * 设置指令类  与设备进行关联
     */
    public static final int SERVICE_SET_INSTRUCTIONS_CLASS = 0xbf1;
}