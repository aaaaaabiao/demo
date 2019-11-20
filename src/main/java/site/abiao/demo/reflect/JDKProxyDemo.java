package site.abiao.demo.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理的流程为:
 * 1.据接口的class信息，如果缓存命中，从缓存中获取class对象。
 * 2.否则生成byte数组,即为字节码，
 * 3.然后加载字节码生成proxy对象。
 * （这个对象经过反编译得到有一下特点
 *  1. 继承proxy类,实现了的接口的方法。
 *  2. 有一个以InvocationHander作为参数的构造函数
 *  3. 每一个方法的实现都为h.invoke(obj, method, args);h即为InvocationHander对象
 * 4.然后使用构造器的方式，实例化一个对象。构造器的参数为InvocationHander
 *
 * @author hubiao
 */
public class JDKProxyDemo {
    interface Demo{
        void execute();
    }

    static class DemoImpl implements Demo{
        @Override
        public void execute() {
            System.out.println("hello, world!!!");
        }
    }

    private Demo demo;
    public JDKProxyDemo(Demo demo) {
        this.demo = demo;
    }

    public Demo getInstance() {
        return (Demo) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Demo.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("开始时间:" + System.currentTimeMillis());
                        Object obj = method.invoke(demo, args);
                        System.out.println("结束时间:" + System.currentTimeMillis());
                        return obj;
                    }
                });
    }


    /**
     * 代理类的使用
     * */
    public static void usage() {
        //普通类
        Demo demo = new DemoImpl();
        demo.execute();

        JDKProxyDemo JDKProxyDemo = new JDKProxyDemo(demo);
        //代理类
        Demo pDemo = JDKProxyDemo.getInstance();
        pDemo.execute();
    }


    /**
     * 测试一下生成代理类的性能的性能
     * 结论为：
     * 1.生成代理类肯定比普通类慢
     * 2.由于有缓存机制,再次生成代理类很快。
     * */
    public static void performance() {

        //普通类
        long startTime = System.currentTimeMillis();
        Demo demo = new DemoImpl();
        long endTime = System.currentTimeMillis();
        System.out.println("创建普通类花费:" + (endTime - startTime) + "ms");

        JDKProxyDemo JDKProxyDemo = new JDKProxyDemo(demo);
        //代理类
        startTime = System.currentTimeMillis();
        Demo pDemo = JDKProxyDemo.getInstance();
        endTime = System.currentTimeMillis();
        System.out.println("创建代理类花费:" + (endTime - startTime) + "ms");

        //再次创建代理类
        startTime = System.currentTimeMillis();
        Demo pDemo1 = JDKProxyDemo.getInstance();
        endTime = System.currentTimeMillis();
        System.out.println("再次创建代理类花费:" + (endTime - startTime) + "ms");

    }

    public static void main(String[] args) {
        usage();
//        performance();
    }
}
