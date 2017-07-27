package io.javac.ManyBlue.manager;

import java.util.HashMap;
import java.util.Map;

import io.javac.ManyBlue.callback.BlueGattCallBack;

/**
 * Created by Pencilso on 2017/7/22.
 */

public class BluetoothGattManager {
    private static final Map<Object, BlueGattCallBack> gattMap = new HashMap<Object, BlueGattCallBack>();

    public static void putGatt(Object tag, BlueGattCallBack gatt) {
        gattMap.put(tag, gatt);
    }

    public static BlueGattCallBack getGatt(Object tag) {
        return gattMap.get(tag);
    }

    public static void removeGatt(Object tag) {
        gattMap.remove(tag);
    }

    public static Map<Object,BlueGattCallBack> getGattMap(){ return gattMap;}
}
