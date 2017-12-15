package io.javac.ManyBlue.interfaces;
/**
 * Created by Pencilso on 2017/12/13.
 */

import io.javac.ManyBlue.ManyBlue;

/**
 * 指令类
 */
public class Instructions {

    private final Object tag;

    public Instructions(Object tag) {
        this.tag = tag;

    }

    protected void blueWriteData(String data) {
        ManyBlue.blueWriteData(data, tag);
    }

    protected void blueWriteDataByteArray(byte data[]) {
        ManyBlue.blueWriteDataByteArray(data, tag);
    }

    protected void blueWriteDataStr2Hex(String data) {
        ManyBlue.blueWriteDataStr2Hex(data, tag);
    }

    @Override
    public String toString() {
        return "Instructions{" +
                "tag=" + tag +
                '}';
    }
}