package io.javac.manyblue.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import io.javac.bluelibrary.bean.DeviceMessage;
import io.javac.bluelibrary.bean.NotifyMessage;
import io.javac.bluelibrary.code.CodeUtils;
import io.javac.bluelibrary.manager.EventManager;

/**
 * Created by Pencilso on 2017/7/22.
 */

public class BlueDeviceAdapter extends RecyclerView.Adapter<io.javac.manyblue.adapter.BlueDeviceAdapter.ViewHodler> {
    private final List<BluetoothDevice> list;

    public BlueDeviceAdapter(List<BluetoothDevice> list) {
        this.list = list;
    }

    @Override
    public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHodler(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null));
    }

    @Override
    public void onBindViewHolder(ViewHodler holder, int position) {
        BluetoothDevice bluetoothDevice = list.get(position);
        StringBuilder sb = new StringBuilder();
        sb.append("name:" + bluetoothDevice.getName()).append("\n")
                .append("mac:").append(bluetoothDevice.getAddress());
        holder.textView.setText(sb.toString());
        holder.textView.setTag(bluetoothDevice.getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addDevice(BluetoothDevice device) {
        if (list.indexOf(device) == -1) {
            list.add(device);
            notifyItemInserted(list.size() - 1);
        }
    }


    class ViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener, DialogInterface.OnClickListener {
        private TextView textView;
        private String address;

        public ViewHodler(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            address = view.getTag().toString();
            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
            alertDialog.setMessage("是否连接该设备");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "取消", this);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", this);
            alertDialog.show();
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == AlertDialog.BUTTON_POSITIVE){
                DeviceMessage deviceMessage = new DeviceMessage(address, address);
                NotifyMessage notifyMessage = new NotifyMessage(CodeUtils.SERVICE_DEVICE_CONN, deviceMessage);
                EventManager.getLibraryEvent().post(notifyMessage);
            }
        }
    }
}