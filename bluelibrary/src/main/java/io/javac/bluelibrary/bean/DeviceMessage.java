package io.javac.bluelibrary.bean;
/**
 * Created by Pencilso on 2017/7/22.
 */

public class DeviceMessage {
    private String address;//设备的mac地址
    private Object tag;//设备的标识

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public DeviceMessage(String address, Object tag) {
        this.address = address;
        this.tag = tag;
    }

    public DeviceMessage() {
    }
}
