package io.javac.example.ui.aleradevice;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;

import io.javac.ManyBlue.ManyBlue;
import io.javac.ManyBlue.bean.NotifyMessage;
import io.javac.ManyBlue.callback.BlueGattCallBack;
import io.javac.ManyBlue.code.CodeUtils;
import io.javac.ManyBlue.manager.BluetoothGattManager;
import io.javac.ManyBlue.manager.EventManager;
import io.javac.ManyBlue.utils.LogUtils;
import io.javac.example.R;
import io.javac.example.adapter.BlueDeviceAdapter;
import io.javac.example.base.BaseActivity;


public class AleraConnDeviceActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private BlueDeviceAdapter adapter;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.act_scanner);
        recyclerView = (RecyclerView) findViewById(R.id.act_scanner_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Collection<BlueGattCallBack> values = BluetoothGattManager.getGattMap().values();
        ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>(values.size());
        for (BlueGattCallBack callback :
                values) {
            bluetoothDevices.add(callback.getDevice());
        }
        adapter = new BlueDeviceAdapter(bluetoothDevices, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        final String address = view.getTag().toString();
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("发送指令");
        final View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == AlertDialog.BUTTON_POSITIVE) {
                    AppCompatEditText editText = (AppCompatEditText) inflate.findViewById(R.id.dialog_input_editText);
                    String str = editText.getText().toString();
                    if (str.trim().length() != 0) {
                        setDialog("正在向设备写出数据");
                        ManyBlue.blueWriteDataStr2Hex(str, address);
                    }
                }
                dialog.dismiss();
            }
        };
        alertDialog.setView(inflate);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "发送", listener);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "取消", listener);
        alertDialog.show();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotifyMessage notifyMessage) {
        switch (notifyMessage.getCode()) {
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
    }
}
