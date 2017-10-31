# ManyBlue

> **最近在开发Ble的项目，自己也在用这个 有发现bug会第一时间修复提交更新**
> 
> **如果有好的建议 可以邮件联系我 admin@javac.io**

Github仓库地址 [https://github.com/pencilso/ManyBlue](https://github.com/pencilso/ManyBlue "ManyBlue")

> **1.0.8以后 已将libray发布到JitPack，jcenter经常发布不上去，放弃了。**
> 
> **Step 1. Add the JitPack repository to your build file:**


    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

> **Step 2. Add the dependency**


	dependencies {
	        compile 'com.github.pencilso:ManyBlue:v1.1.5'
	}


[![](https://jitpack.io/v/pencilso/ManyBlue.svg)](https://jitpack.io/#pencilso/ManyBlue)

	首先先进行判断设备是否支持BLE蓝牙

	ManyBlue.blueSupport(Context context)
	
	内部维护了一个Service 需要优先检测Service是否正在运行

    ManyBlue.runing(Context context)
    （有一点需要注意，6.0以上Android 部分手机 比如说华为 需要动态申请定位权限 才能扫描到设备）

- 启动服务 ` ManyBlue.blueStartService(Context context);`
- 关闭服务 ` ManyBlue.blueStopService(this);`
- 蓝牙开启状态 ` ManyBlue.blueEnableState();`
- 打开手机蓝牙 ` ManyBlue.blueEnable(true);`
- 关闭手机蓝牙 `ManyBlue.blueEnable(false);`
- 日志输出 `ManyBlue.DEBUG = true;`

## 注册|取消 事件 ##
	建议新建一个BaseActivity  然后继承自你现有的BaseActivity
	然后重写onStart onStop 进行取消 和注册事件 跟处理监听事件
     @Override
    protected void onStart() {
        super.onStart();
        EventManager.getLibraryEvent().register(this);//注册
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventManager.getLibraryEvent().unregister(this);//取消
    }
	
	
	//订阅消息
	@Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotifyMessage notifyMessage) {
        LogUtils.log(notifyMessage);
        if (this instanceof BaseNotifyListener)
            ManyBlue.dealtListener((BaseNotifyListener) this, notifyMessage);//处理监听
    }
## 手机蓝牙的监听 ##
实现接口 `BaseNotifyListener.MobileBlueListener`
     
	@Override
    public void onMobileBlueState(boolean enabled) {
        appToast("蓝牙开启状态:" + enabled);
    }

    @Override
    public void onMobileBlueEnabled(boolean success) {
        appToast("开启手机蓝牙:" + success);
    }

    @Override
    public void onMobileBlueDisable(boolean success) {
        appToast("关闭手机蓝牙:" + success);
    }

   
## 蓝牙服务的监听 ##
实现接口 BaseNotifyListener.ServiceListener

	`@Override
    public void onServiceStart() {
        appToast("蓝牙服务已开启");
    }

    @Override
    public void onServiceStop() {
        appToast("蓝牙服务已关闭");
    }`
## 扫描设备 ##
- 扫描蓝牙 `ManyBlue.blueStartScaner();//不延迟，扫描到一个返回一个 ManyBlue.blueStartScaner(3000);//延迟3秒返回一次列表`
- 停止扫描 `ManyBlue.blueStopScaner();`
- 连接设备 ` ManyBlue.blueConnectDevice(String address, Object tag);`//tag是自定义的标记 用来标记多设备

实现接口 BaseNotifyListener.DeviceListener

	`/**
     * 扫描到蓝牙设备
     *
     * @param device
     */
    @Override
    public void onDeviceScanner(BluetoothDevice device) {
        adapter.addDevice(device);
    }
	
	/**
     * 扫描到蓝牙设备信号列表
     * 当调用的
     * @param device
     */
     void onDeviceScanner(List<BluetoothDevice> device);	

    /**
     * 蓝牙设备连接或者断开
     *
     * @param state true为连接 false为断开
     */
    @Override
    public void onDeviceConnectState(boolean connectState, Object tag) {
        if (!state) {
            appToast("连接失败");
            dismissDialog();
        } else setDialog("连接成功 正在发现服务");
        //在蓝牙4.0上面，是需要主动查找服务的，在蓝牙设备连接成功后 会自动去查找服务 当服务查找完毕 并注册通道后 才能接发指令
    }

    @Override
    public void onDeviceServiceDiscover(List<BluetoothGattService> services, Object tag) {
        setDialog("正在注册服务");
        //services 这是该设备中所有的服务 在这里找到需要的服务 然后再进行注册
	//                services.get(0).getUuid().toString();//这是获取UUID的方法
        //找到需要的UUID服务  然后进行连接  比如说我需要的服务UUID是00003f00-0000-1000-8000-00805f9b34fb UUID的话  一般设备厂家会提供文档 都有写的
        UUIDMessage uuidMessage = new UUIDMessage();//创建UUID的配置类
        uuidMessage.setCharac_uuid_service("00003f00-0000-1000-8000-00805f9b34fb");//需要注册的服务UUID
        uuidMessage.setCharac_uuid_write("00003f02-0000-1000-8000-00805f9b34fb");//写出数据的通道UUID
        uuidMessage.setCharac_uuid_read("00003f01-0000-1000-8000-00805f9b34fb");//读取通道的UUID
        uuidMessage.setDescriptor_uuid_notify("00002902-0000-1000-8000-00805f9b34fb");//READ通道当中的notify通知
        /**
         * 有些设备的Notify是直接放在characteristic里的 有些是在READ通道 characteristic下的descriptor中
         * 视情况而定 选择使用
         */
        uuidMessage.setCharac_uuid_notify("0000ffe1-0000-1000-8000-00805f9b34fb");//这个是characteristic的Notify
        /**
         * 这里简单说一下  如果设备返回数据的方式不是Notify的话  那就意味着向设备写出数据之后   再自己去获取数据
         * Notify的话 是如果蓝牙设备有数据传递过来  能接受到通知
         * 使用场景中如果没有notify的话  notify uuid留空即可
         */
        ManyBlue.blueRegisterDevice(uuidMessage, tag);//注册设备
    }

    @Override
    public void onDeviceRegister(boolean registerState,Object tag) {
        dismissDialog();
        appToast(registerState ? "设备注册成功" : "设备注册失败");
    }


## 发送|接收 蓝牙数据 ##

- 获取已连接设备 `ManyBlue.getConnDeviceAll();`
- 发送字符转十六进制 ManyBlue.blueWriteDataStr2Hex(data, tag); //例如 "0a0a01"
- 发送字符指令 不进行十六进制转换 ManyBlue.blueWriteData(data,tag);
- 发送字节数组指令  不进行任何转换 ManyBlue.blueWriteDataByteArray(data,tag);

实现接口 `BaseNotifyListener.DeviceDataListener`

回调事件

    /**
     * 向蓝牙发送数据后的回调
     *
     * @param state 发送成功true  发送失败false
     */
    @Override
    public void onDeviceWriteState(boolean writeState, Object tag) {
        dismissDialog();//关闭Dialog
        appToast("指令发送状态:" + state);
        /**
         * 如果是非Notify的接收方式的话 这里需要手动去调用读取通道
         */
        //ManyBlue.blueReadData(tag); //主动获取数据
    }

    /**
     * 主动读取的通道数据
     *
     * @param characteristicValues 读取到的数据
     */
    @Override
    public void onDeviceReadMessage(CharacteristicValues characteristicValues) {
        LogUtils.log("onDeviceReadMessage   strValue:" + characteristicValues.getStrValue() + " hex2Str:" + characteristicValues.getHex2Str() + " byArr:" + characteristicValues.getByArr());
    }

    /**
     * Notify监听收到的数据
     *
     * @param characteristicValues 读取到的数据
     */
    @Override
    public void onDeviceNotifyMessage(CharacteristicValues characteristicValues) {
        LogUtils.log("onDeviceNotifyMessage    strValue:" + characteristicValues.getStrValue() + " hex2Str:" + characteristicValues.getHex2Str() + " byArr:" + characteristicValues.getByArr());
    }


## 直接监听所有回调 ##
实现接口 BaseNotifyListener.NotifyListener

## 已连接设备 ##
- 获取所有已连接设备 `ManyBlue.getConnDeviceAll()`
- 获取指定标识设备 `ManyBlue.getConnDevice(tag);`
- 断开指定设备 `ManyBlue.blueDisconnectedDevice(tag);`
- 断开所有设备 `ManyBlue.blueDisconnectedDeviceAll();`