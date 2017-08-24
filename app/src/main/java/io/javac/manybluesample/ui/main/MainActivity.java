package io.javac.manybluesample.ui.main;

import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.javac.ManyBlue.ManyBlue;
import io.javac.ManyBlue.bean.NotifyMessage;
import io.javac.ManyBlue.code.CodeUtils;
import io.javac.ManyBlue.interfaces.BaseNotifyListener;
import io.javac.ManyBlue.manager.EventManager;
import io.javac.ManyBlue.utils.LogUtils;
import io.javac.manybluesample.R;
import io.javac.manybluesample.base.BaseActivity;
import io.javac.manybluesample.ui.aleradevice.AleraConnDeviceActivity;
import io.javac.manybluesample.ui.scanner.ScannerActivity;

public class MainActivity extends BaseActivity implements BaseNotifyListener.MobileBlueListener, BaseNotifyListener.ServiceListener {

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_main);
        registAllView(findViewById(R.id.act_main_layout));
        LogUtils.log("是否支持BLE蓝牙:"+ManyBlue.blueSupport(this));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.act_main_service_runing://服务是否运行
                appToast(ManyBlue.runing(this));
                break;
            case R.id.act_main_start_service://启动服务
                ManyBlue.blueStartService(this);
                break;
            case R.id.act_main_stop_service://关闭服务
                ManyBlue.blueStopService(this);
                break;
            case R.id.act_main_blue_state://系统蓝牙状态
                ManyBlue.blueEnableState();
                break;
            case R.id.act_main_blue_enable://打开系统蓝牙
                ManyBlue.blueEnable(true);
                break;
            case R.id.act_main_blue_disable://关闭系统蓝牙
                ManyBlue.blueEnable(false);
                break;
            case R.id.act_main_blue_scanner://打开扫描蓝牙界面
                startActivity(ScannerActivity.class);
                break;
            case R.id.act_main_blue_conndevice://已连接设备
                startActivity(AleraConnDeviceActivity.class);
                break;
        }
    }

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

    @Override
    public void onServiceStart() {
        appToast("蓝牙服务已开启");
    }

    @Override
    public void onServiceStop() {
        appToast("蓝牙服务已关闭");
    }
}
