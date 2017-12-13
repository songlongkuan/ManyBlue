package io.javac.manybluesample;

import org.junit.Test;

import io.javac.ManyBlue.utils.HexUtils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

       byte[] bytes = new byte[]{(byte) 0xf1};
        byte[] f1s = HexUtils.getHexBytes("f1");
        for (byte b:bytes)
            System.out.println("bytes:"+b);
        for (byte b:f1s)
            System.out.println("f1s:"+b);
    }
}