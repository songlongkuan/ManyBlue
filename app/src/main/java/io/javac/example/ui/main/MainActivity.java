package io.javac.example.ui.main;

import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.javac.ManyBlue.ManyBlue;
import io.javac.ManyBlue.bean.NotifyMessage;
import io.javac.ManyBlue.code.CodeUtils;
import io.javac.ManyBlue.manager.EventManager;
import io.javac.example.R;
import io.javac.example.base.BaseActivity;
import io.javac.example.ui.aleradevice.AleraConnDeviceActivity;
import io.javac.example.ui.scanner.ScannerActivity;

public class MainActivity extends BaseActivity {

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_main);
        registAllView(findViewById(R.id.act_main_layout));
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
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventManager.getLibraryEvent().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventManager.getLibraryEvent().unregister(this);
    }
}
