package io.javac.manyblue;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
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
import io.javac.bluelibrary.bean.UUIDMessage;
import io.javac.bluelibrary.code.CodeUtils;
import io.javac.bluelibrary.manager.EventManager;
import io.javac.bluelibrary.service.BlueLibraryService;
import io.javac.bluelibrary.utils.LogUtils;
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
            case CodeUtils.SERVICE_ONCONNEXT_STATE://注意  连接成功的话 只是连接成功  还没有注册通道 这时候还不能进行任何读写数据的操作
                appToast("蓝牙连接状态:" + notifyMessage.getData());
                break;
            case CodeUtils.SERVICE_ONSERVICESDISCOVERED://发现服务
            {
                List<BluetoothGattService> services = (List<BluetoothGattService>) notifyMessage.getData();
                for (BluetoothGattService bluetoothGattService : services) {
                    Log.e("bluetoothGattService", bluetoothGattService.getUuid().toString());
                }
                //找到需要的UUID服务  然后进行连接  比如说我需要的服务UUID是00003f00-0000-1000-8000-00805f9b34fb UUID的话  一般设备厂家会提供文档 都有写的
                UUIDMessage uuidMessage = new UUIDMessage();//创建UUID的配置类
                uuidMessage.setCharac_uuid_service("00003f00-0000-1000-8000-00805f9b34fb");//需要注册的服务UUID
                uuidMessage.setCharac_uuid_write("00003f02-0000-1000-8000-00805f9b34fb");//写出数据的通道UUID
                uuidMessage.setCharac_uuid_read("00003f01-0000-1000-8000-00805f9b34fb");//读取通道的UUID
                uuidMessage.setDescriptor_uuid_notify("00002902-0000-1000-8000-00805f9b34fb");//这是读取通道当中的notify通知
                /**
                 * 这里简单说一下  如果设备返回数据的方式不是Notify的话  那就意味着向设备写出数据之后   再自己去获取数据
                 * Notify的话 是如果蓝牙设备有数据传递过来  能接受到通知
                 * 使用场景中如果没有notify的话 留空即可
                 */
                NotifyMessage message = new NotifyMessage(CodeUtils.SERVICE_REGDEVICE, uuidMessage, notifyMessage.getTag());
                EventManager.getLibraryEvent().post(message);
            }
            break;
            case CodeUtils.SERVICE_ONREGISTER_DEVICE:
                appToast("设备通道已经注册完毕");
                break;
            case CodeUtils.SERVICE_ONNOTIFY:
                appToast("Notify:"+notifyMessage.getData());
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
            case R.id.act_main_close_openbulb://向蓝牙发送打开灯光的指令
            {
                String data = "a10801aa";
                NotifyMessage notifyMessage = new NotifyMessage();
//                notifyMessage.setCode(CodeUtils.SERVICE_WRITE_DATA);//发送原始数据
                notifyMessage.setCode(CodeUtils.SERVICE_WRITE_DATA_TOHEX);//自动转换成十六进制再发送
                notifyMessage.setTag(1);//我这里是先定死的设备标识是1  因为在连接的时候 我也是定死的传的1 这个标识是用来判断多连接用的
                notifyMessage.setData(data);
                EventManager.getLibraryEvent().post(notifyMessage);
            }
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
