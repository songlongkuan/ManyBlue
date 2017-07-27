package io.javac.ManyBlue.bean;

/**
 * Created by Pencilso on 2017/7/22.
 */

public class NotifyMessage {
    private int code;
    private Object data;
    private Object tag;

    public NotifyMessage() {

    }

    public int getCode() {
        return code;
    }

    public NotifyMessage setCode(int code) {
        this.code = code;
        return this;
    }

    public NotifyMessage(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public NotifyMessage(int code, Object data, Object tag) {
        this.code = code;
        this.data = data;
        this.tag = tag;
    }

    public <T> T getData() {
        return (T) data;
    }

    public NotifyMessage setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "NotifyMessage{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }

    public Object getTag() {
        return tag;
    }

    public NotifyMessage setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public static NotifyMessage newInstance() {
        return new NotifyMessage();
    }
}
