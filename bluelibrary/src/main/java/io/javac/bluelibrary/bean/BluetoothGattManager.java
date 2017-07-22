package io.javac.bluelibrary.bean;

import android.bluetooth.BluetoothGatt;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pencilso on 2017/7/22.
 */

public class BluetoothGattManager {
    private static final Map<Object, BluetoothGatt> gattMap = new HashMap<Object, BluetoothGatt>();

    public static void putGatt(Object tag, BluetoothGatt gatt) {
        gattMap.put(tag, gatt);
    }

    public static BluetoothGatt getGatt(Object tag) {
        return gattMap.get(tag);
    }

    public static void removeGatt(Object tag) {
        gattMap.remove(tag);
    }
}
