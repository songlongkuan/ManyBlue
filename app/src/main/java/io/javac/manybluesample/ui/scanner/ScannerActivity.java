package io.javac.manybluesample.ui.scanner;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.javac.ManyBlue.ManyBlue;
import io.javac.ManyBlue.bean.UUIDMessage;
import io.javac.ManyBlue.interfaces.BaseNotifyListener;
import io.javac.manybluesample.R;
import io.javac.manybluesample.adapter.BlueDeviceAdapter;
import io.javac.manybluesample.base.BaseActivity;

public class ScannerActivity extends BaseActivity implements BaseNotifyListener.DeviceListener {
    private RecyclerView recyclerView;
    private BlueDeviceAdapter adapter;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.act_scanner);
        recyclerView = (RecyclerView) findViewById(R.id.act_scanner_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new BlueDeviceAdapter(new ArrayList<BluetoothDevice>(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {
        super.initData();
        ManyBlue.blueStartScaner();//启动蓝牙扫描
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case android.R.id.text1: {
                String address = view.getTag().toString();
                setDialog("正在连接该设备");
                ManyBlue.blueConnectDevice(address, address);//连接该设备  并且以该设备的mac地址作为标识
            }
            break;
        }
    }

    /**
     * 扫描到蓝牙设备
     *
     * @param device
     */
    @Override
    public void onDeviceScanner(BluetoothDevice device) {
        adapter.addDevice(device);
    }

    /**
     * 蓝牙设备连接活着断开
     *
     * @param state true为连接 false为断开
     */
    @Override
    public void onDeviceConnectState(boolean state, Object tag) {
        if (!state) {
            appToast("连接失败");
            dismissDialog();
        } else setDialog("连接成功 正在发现服务");
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
        uuidMessage.setDescriptor_uuid_notify("00002902-0000-1000-8000-00805f9b34fb");//这是读取通道当中的notify通知
        /**
         * 这里简单说一下  如果设备返回数据的方式不是Notify的话  那就意味着向设备写出数据之后   再自己去获取数据
         * Notify的话 是如果蓝牙设备有数据传递过来  能接受到通知
         * 使用场景中如果没有notify的话  notify uuid留空即可
         */
        ManyBlue.blueRegisterDevice(uuidMessage, tag);//注册设备
    }

    @Override
    public void onDeviceRegister(boolean state) {
        dismissDialog();
        appToast(state ? "设备注册成功" : "设备注册失败");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ManyBlue.blueStopScaner();//关闭蓝牙扫描
    }
}
