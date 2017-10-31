package io.javac.ManyBlue.bean;

/**
 * Created by Pencilso on 2017/7/23.
 * 注册设备通道的时候用
 */

public class UUIDMessage {
    private String charac_uuid_service;// 服务通道
    private String charac_uuid_read;// 读的通道
    private String charac_uuid_write;// 写的通道

    /**
     * 有些设备的Notify是直接放在characteristic里的 有些是在characteristic下的descriptor中
     * 视情况而定
     */
    private String descriptor_uuid_notify;//Notify通知的descriptor UUID 如果没有Notify的话 留空即可
    private String charac_uuid_notify;//Notify通知的characteristic UUID 如果没有的话 留空即可

    public String getCharac_uuid_service() {
        return charac_uuid_service;
    }

    public void setCharac_uuid_service(String charac_uuid_service) {
        this.charac_uuid_service = charac_uuid_service;
    }

    public String getCharac_uuid_read() {
        return charac_uuid_read;
    }

    public void setCharac_uuid_read(String charac_uuid_read) {
        this.charac_uuid_read = charac_uuid_read;
    }

    public String getCharac_uuid_write() {
        return charac_uuid_write;
    }

    public void setCharac_uuid_write(String charac_uuid_write) {
        this.charac_uuid_write = charac_uuid_write;
    }

    public String getDescriptor_uuid_notify() {
        return descriptor_uuid_notify;
    }

    public void setDescriptor_uuid_notify(String descriptor_uuid_notify) {
        this.descriptor_uuid_notify = descriptor_uuid_notify;
    }

    @Override
    public String toString() {
        return "UUIDMessage{" +
                "charac_uuid_service='" + charac_uuid_service + '\'' +
                ", charac_uuid_read='" + charac_uuid_read + '\'' +
                ", charac_uuid_write='" + charac_uuid_write + '\'' +
                ", descriptor_uuid_notify='" + descriptor_uuid_notify + '\'' +
                '}';
    }

    public String getCharac_uuid_notify() {
        return charac_uuid_notify;
    }

    public void setCharac_uuid_notify(String charac_uuid_notify) {
        this.charac_uuid_notify = charac_uuid_notify;
    }
}
