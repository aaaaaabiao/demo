package site.abiao.demo.JDK;

import java.lang.ref.SoftReference;

public class BaseTypeTest {

    public static void main(String[] args) {
        //整型3中
        short s = 0;
        int i = 0;
        long l = 0;

        //浮点型2中
        float f = 1.0f;
        double d = 1.0;

        //char采用unicode编码。占2个字节
        char c = 'f';

        //占一个字节
        byte by = 0x7f;
        boolean bo = false;

        Integer i1 = -128;
        Integer i2 = -128;
        System.out.println(i1 == i2);//由于有缓存池,范围为-128~127。所以返回true

        Integer i3 = 1000;
        Integer i4 = 1000;
        System.out.println(i3 == i4);//不在缓存池内部。两个不同的对象。返回false

        String str = null;
        if (str == null || str.length() == 0) {
            System.out.println("hello");
        }
    }
}
