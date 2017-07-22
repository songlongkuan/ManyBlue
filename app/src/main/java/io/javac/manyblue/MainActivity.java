package io.javac.manyblue;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devspark.appmsg.AppMsg;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.javac.bluelibrary.bean.NotifyMessage;
import io.javac.bluelibrary.code.CodeUtils;
import io.javac.bluelibrary.manager.EventManager;
import io.javac.bluelibrary.service.BlueLibraryService;
import io.javac.manyblue.adapter.BlueDeviceAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private AppMsg appMsg;
    private BlueDeviceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(inflate);
        getAllChildViews(inflate);
        EventManager.getLibraryEvent().register(this);
        startService(new Intent(this, BlueLibraryService.class));


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.act_main_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new BlueDeviceAdapter(new ArrayList<BluetoothDevice>());
        recyclerView.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotifyMessage notifyMessage) {
        switch (notifyMessage.getCode()) {
            case CodeUtils.SERVICE_ONSTART:
                appToast("蓝牙服务已经启动");
                break;
            case CodeUtils.SERVICE_ONDEVICE:
                Log.e("扫描到设备", notifyMessage.toString());
            {
                BluetoothDevice device = (BluetoothDevice) notifyMessage.getData();
                adapter.addDevice(device);
            }
            break;
            case CodeUtils.SERVICE_ONBLUEOPEN:
                appToast("蓝牙开启状态:" + notifyMessage.getData());
                break;
            case CodeUtils.SERVICE_ONOPENBLUE:
                appToast("打开蓝牙是否成功:" + notifyMessage.getData());
                break;
            case CodeUtils.SERVICE_ONSTOPBLUE:
                appToast("关闭蓝牙是否成功:" + notifyMessage.getData());
                break;
            case CodeUtils.SERVICE_ONCONNEXT_STATE:
                appToast("蓝牙连接状态:" + notifyMessage.getData());
                break;
            case CodeUtils.SERVICE_ONSERVICESDISCOVERED://收到服务
            {
                List<BluetoothGattService> services = (List<BluetoothGattService>) notifyMessage.getData();
                for (BluetoothGattService bluetoothGattService : services) {
                    Log.e("bluetoothGattService", bluetoothGattService.getUuid().toString());
                }
            }
            break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_main_scaner: {//开始扫描
                EventManager.getLibraryEvent().post(new NotifyMessage(CodeUtils.SERVICE_STARTSCANER, null));
            }
            break;
            case R.id.act_main_stop_scaner://停止扫描
                EventManager.getLibraryEvent().post(new NotifyMessage(CodeUtils.SERVICE_STOPSCANER, null));
                break;
            case R.id.act_main_is_open://检测蓝牙是否打开
                EventManager.getLibraryEvent().post(new NotifyMessage(CodeUtils.SERVICE_BLUEISOPEN, null));
                break;
            case R.id.act_main_open_blue://开启蓝牙
                EventManager.getLibraryEvent().post(new NotifyMessage(CodeUtils.SERVICE_OPENBLUE, null));
                break;
            case R.id.act_main_close_blue://关闭蓝牙
                EventManager.getLibraryEvent().post(new NotifyMessage(CodeUtils.SERVICE_CLOSEBLUE, null));
                break;
        }
    }

    private void getAllChildViews(View view) {
        view = view.findViewById(R.id.act_layout);
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                viewchild.setOnClickListener(this);
            }
        }
    }

    public void appToast(String message) {
        if (appMsg == null) {
            AppMsg.Style style = new AppMsg.Style(1000, R.color.colorPrimaryDark);
            appMsg = AppMsg.makeText(this, message, style);
            appMsg.setLayoutGravity(Gravity.CENTER_VERTICAL);
        } else {
            appMsg.setText(message);
        }
        appMsg.show();
    }
}
