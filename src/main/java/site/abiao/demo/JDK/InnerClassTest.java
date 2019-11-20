package site.abiao.demo.JDK;

import org.testng.annotations.Test;

/**
 * 内部类测试
 *
 * @author hubiao
 */
class InnerClassTest {

    private int attr = 0;

    private static int staticAttr = 0;

    private void f() {
        System.out.println("hello, world!");

    }

    /**
     * 静态内部类
     * */
     static class A{
        private int x;
        private int y;

        public void f() {
            //可以访问静态成员。
            System.out.println(staticAttr);
            //System.out.println(attr);错误,不能访问外部类的成员
        }
    }


    /**
     * 普通内部类
     * */
    class B{
        private int x;
        private int y;


        public void f() {
            //能访问外部类的成员
            f();
            System.out.println(attr);
        }
    }


    public static void main(String[] args) {
        //静态内部类在实例化的时候,不依赖外部类
        A a = new A();

        //内部类实例化的时候，依赖外部类。需要先实例化外部类
        InnerClassTest test = new InnerClassTest();
        InnerClassTest.B b = test.new B();
    }
}
