package io.javac.bluelibrary.bean;

/**
 * Created by Administrator on 2017/7/22.
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

    public void setCode(int code) {
        this.code = code;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
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

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
