package io.javac.ManyBlue.bean;

import java.util.Arrays;

/**
 * Created by Pencilso on 2017/7/27.
 */

public class CharacteristicValues {
    private String strValue;
    private String hex2Str;
    private byte byArr[];

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public String getHex2Str() {
        return hex2Str;
    }

    public void setHex2Str(String hex2Str) {
        this.hex2Str = hex2Str;
    }

    public byte[] getByArr() {
        return byArr;
    }

    public void setByArr(byte[] byArr) {
        this.byArr = byArr;
    }

    public CharacteristicValues(String strValue, String hex2Str, byte[] byArr) {
        this.strValue = strValue;
        this.hex2Str = hex2Str;
        this.byArr = byArr;
    }

    public CharacteristicValues() {
    }

    @Override
    public String toString() {
        return "CharacteristicValues{" +
                "strValue='" + strValue + '\'' +
                ", hex2Str='" + hex2Str + '\'' +
                ", byArr=" + Arrays.toString(byArr) +
                '}';
    }
}
