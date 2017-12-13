package io.javac.ManyBlue.interfaces;
/**
 * Created by Pencilso on 2017/12/13.
 */

import io.javac.ManyBlue.ManyBlue;

/**
 * 指令类
 */
public abstract class Instructions {

    private final Object tag;

    public Instructions(Object tag) {
        this.tag = tag;

    }

    protected void blueWriteData(String data) {
        ManyBlue.blueWriteData(data, tag);
    }

    protected void blueWriteDataByteArray(Byte data[]) {
        ManyBlue.blueWriteDataByteArray(data, tag);
    }

    protected void blueWriteDataStr2Hex(String data) {
        ManyBlue.blueWriteDataStr2Hex(data, tag);
    }

}