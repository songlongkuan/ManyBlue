package io.javac.manybluesample.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pencilso on 2017/7/22.
 */

public class BlueDeviceAdapter extends RecyclerView.Adapter<io.javac.manybluesample.adapter.BlueDeviceAdapter.ViewHodler> {
    private final List<BluetoothDevice> list;
    private View.OnClickListener listener;
    public BlueDeviceAdapter(List<BluetoothDevice> list, View.OnClickListener listener) {
        this.list = list;
        this.listener = listener;
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


    class ViewHodler extends RecyclerView.ViewHolder {
        private TextView textView;
        private String address;

        public ViewHodler(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
            textView.setOnClickListener(listener);
        }

//        @Override
//        public void onClick(View view) {
//
//        }
//
//        @Override
//        public void onClick(DialogInterface dialogInterface, int i) {
//            if (i == AlertDialog.BUTTON_POSITIVE){
//                /**
//                 * data 赋值mac地址
//                 * 标记 我这里用的是mac地址做标记
//                 */
//                NotifyMessage notifyMessage = new NotifyMessage(CodeUtils.SERVICE_DEVICE_CONN, address,address);
//                EventManager.getLibraryEvent().post(notifyMessage);
//            }
//        }
    }
}