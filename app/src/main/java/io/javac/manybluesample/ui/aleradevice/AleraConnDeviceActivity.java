package io.javac.manybluesample.ui.aleradevice;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import io.javac.ManyBlue.ManyBlue;
import io.javac.ManyBlue.bean.CharacteristicValues;
import io.javac.ManyBlue.interfaces.BaseNotifyListener;
import io.javac.ManyBlue.utils.LogUtils;
import io.javac.manybluesample.R;
import io.javac.manybluesample.adapter.BlueDeviceAdapter;
import io.javac.manybluesample.base.BaseActivity;


public class AleraConnDeviceActivity extends BaseActivity implements BaseNotifyListener.DeviceDataListener {
    private RecyclerView recyclerView;
    private BlueDeviceAdapter adapter;

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.act_scanner);
        recyclerView = (RecyclerView) findViewById(R.id.act_scanner_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new BlueDeviceAdapter(ManyBlue.getConnDeviceAll(), this);
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

    /**
     * 向蓝牙发送数据后的回调
     *
     * @param state 发送成功true  发送失败false
     */
    @Override
    public void onDeviceWriteState(boolean state, Object tag) {
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
    public void onDeviceReadMessage(CharacteristicValues characteristicValues,Object tag) {
        LogUtils.log("onDeviceReadMessage   strValue:" + characteristicValues.getStrValue() + " hex2Str:" + characteristicValues.getHex2Str() + " byArr:" + characteristicValues.getByArr());
        appToast("Read:" + characteristicValues.getHex2Str());
    }

    /**
     * Notify监听收到的数据
     *
     * @param characteristicValues 读取到的数据
     */
    @Override
    public void onDeviceNotifyMessage(CharacteristicValues characteristicValues,Object tag) {
        LogUtils.log("onDeviceNotifyMessage    strValue:" + characteristicValues.getStrValue() + " hex2Str:" + characteristicValues.getHex2Str() + " byArr:" + characteristicValues.getByArr());
        appToast("Notify:" + characteristicValues.getHex2Str());
    }


}
