# 蓝牙Ble多设备连接

1. compile 'io.javac:ManyBlue:1.0.1' 添加依赖
2. compile 'org.greenrobot:eventbus:3.0.0' 内部使用到了EventBus 所以需要添加

仓库中有例子 可以clone到本地看看

## 蓝牙操作API ##
主要api操作都在ManyBlue类当中 并且在内部运行了一个Service

	 /**
     * 判断服务是否运行
     * @return
     */
    ManyBlue.runing(Context context);

	 /**
     * 启动蓝牙服务
     *
     * @param context
     */
    ManyBlue.blueStartService(Context context);

	/**
     * 停止蓝牙服务
     *
     * @param context
     */
	ManyBlue.blueStopService(Context context);

	/**
     * 手机蓝牙打开状态
     */
	ManyBlue.blueEnableState();
	
	/**
     * 是否启用系统蓝牙
     */
	ManyBlue.blueEnable(true);
	
	/**
     * 打开蓝牙扫描
     */
	ManyBlue.blueStartScaner();

	/**
     * 关闭蓝牙扫描
     */
    ManyBlue.blueStopScaner();

	/**
     * 连接某一个设备
     *
     * @param address 蓝牙设备的MAC地址
     * @param tag     蓝牙设备的标识 （自定义）
     */
    ManyBlue.blueConnectDevice(String address, Object tag) ;

	 /**
     * 注册设备通道
     *
     * @param uuidMessage
     * @param tag
     */
    ManyBlue.blueRegisterDevice(UUIDMessage uuidMessage, Object tag);

	/**
     * 向蓝牙写出字符数据 不转换
     *
     * @param data
     * @param tag
     */
    ManyBlue.blueWriteData(String data, Object tag);

	/**
     * 向蓝牙写入字符十六进制数据
     *
     * @param data
     * @param tag
     */
    ManyBlue.blueWriteDataStr2Hex(String data, Object tag);

	/**
     * 向蓝牙写入字节数组数据
     *
     * @param data
     * @param tag
     */
    ManyBlue.blueWriteDataByteArray(Byte data[], Object tag);

	/**
     * 读取原始字符数据 (非Notify方式使用)
     *
     * @param tag
     */
    ManyBlue.blueReadDataStr(Object tag);

	 /**
     * 读取蓝牙传递的十六进制 并且转换为字符 (非Notify方式使用)
     *
     * @param tag
     */
    ManyBlue.blueReadDataHex2Str(Object tag);

	/**
     * 读取蓝牙传递的字节数组数据 (非Notify方式使用)
     *
     * @param tag
     */
    ManyBlue.blueReadDataByteArray(Object tag);

	 /**
     * 断开指定的设备
     * @param tag
     */
    ManyBlue.blueDisconnectedDevice(Object tag);

	/**
     * 断开所有的设备
     * @param tag
     */
    ManyBlue.blueDisconnectedDeviceAll(Object tag);

## 监听蓝牙的回调 ##
> **注册回调 EventManager.getLibraryEvent().register(this);**
> 
> **取消回调  EventManager.getLibraryEvent().unregister(this);**

 	/**
     * 订阅消息
     *
     * @param notifyMessage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotifyMessage notifyMessage) {
		 switch (notifyMessage.getCode()) {
            case CodeUtils.SERVICE_ONBLUEENABLE:
                appToast("系统蓝牙开启状态:" + notifyMessage.getData());
                break;
            case CodeUtils.SERVICE_ONSTART:
                appToast("蓝牙服务已启动");
                break;
            case CodeUtils.SERVICE_ONENABLEBLUE:
                appToast("系统蓝牙已打开");
                break;
            case CodeUtils.SERVICE_ONDISABLEBLUE:
                appToast("系统蓝牙已关闭");
                break;
			case CodeUtils.SERVICE_ONDEVICE://扫描到蓝牙设备
            {
                BluetoothDevice device =  notifyMessage.getData();
                adapter.addDevice(device);//添加到展示列表当中
            }
            break;
            case CodeUtils.SERVICE_ONCONNEXT_STATE://连接蓝牙状态回调
            {
                Boolean aBoolean = Boolean.valueOf(notifyMessage.getData().toString());
                if (!aBoolean) {
                    appToast("连接失败");
                    dismissDialog();
                } else setDialog("连接成功 正在发现服务");
            }
            break;
            case CodeUtils.SERVICE_ONSERVICESDISCOVERED://发现服务后的回调
            {
                setDialog("正在注册服务");
                List<BluetoothGattService> services = notifyMessage.getData();//这是该设备中所有的服务 在这里找到需要的服务 然后再进行注册
				//services.get(0).getUuid().toString();//这是获取UUID的方法
                //找到需要的UUID服务  然后进行连接  比如说我需要的服务UUID是00003f00-0000-1000-8000-00805f9b34fb UUID的话  一般设备厂家会提供文档 都有写的
                UUIDMessage uuidMessage = new UUIDMessage();//创建UUID的配置类
                uuidMessage.setCharac_uuid_service("00003f00-0000-1000-8000-00805f9b34fb");//需要注册的服务UUID
                uuidMessage.setCharac_uuid_write("00003f02-0000-1000-8000-00805f9b34fb");//写出数据的通道UUID
                uuidMessage.setCharac_uuid_read("00003f01-0000-1000-8000-00805f9b34fb");//读取通道的UUID
                uuidMessage.setDescriptor_uuid_notify("00002902-0000-1000-8000-00805f9b34fb");//这是读取通道当中的notify通知
                /**
                 * 这里简单说一下  如果设备返回数据的方式不是Notify的话  那就意味着向设备写出数据之后   再自己去获取数据
                 * Notify的话 是如果蓝牙设备有数据传递过来  能接受到通知
                 * 使用场景中如果没有notify的话  notify uuid留空即可
                 */
                ManyBlue.blueRegisterDevice(uuidMessage, notifyMessage.getTag());
            }
            break;
            case CodeUtils.SERVICE_ONREGISTER_DEVICE://设备注册通道完毕
                dismissDialog();
                appToast("蓝牙设备初始化完毕");
                break;
			case CodeUtils.SERVICE_ONWRITE: //数据写到蓝牙的回调
            {
                dismissDialog();
                appToast("数据写出:" + notifyMessage.getData());//返回data类型为boolean
                /**
                 * 如果是非Notify的接收方式的话 这里需要手动去调用读取通道
                 */
	//                ManyBlue.blueReadDataStr(notifyMessage.getTag()); 主动获取数据
            }
            break;
            case CodeUtils.SERVICE_ONNOTIFY://接收到的notify数据
            {
                appToast("Notify:" + notifyMessage.getData());
                LogUtils.log("Notify:" + notifyMessage.getData());
            }
            break;
            case CodeUtils.SERVICE_ONREAD://接收到主动调用读取通道 返回的数据
            {
                LogUtils.log("read:" + notifyMessage.getData());
            }
            break;
        }
	｝

