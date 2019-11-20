package site.abiao.demo.reflect;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyDemo {

    static class DemoImpl{
        public void execute() {
            System.out.println("hello, world!!!");
        }
    }

    private DemoImpl demo;
    public CglibProxyDemo(DemoImpl demo) {
        this.demo = demo;
    }
    public DemoImpl getInstance() {
        Enhancer enhancer = new Enhancer();
        //设置需要创建子类的类
        enhancer.setSuperclass(demo.getClass());
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("开始时间:" + System.currentTimeMillis());
                Object obj =  method.invoke(demo, objects);
                System.out.println("结束时间:" + System.currentTimeMillis());
                return obj;
            }
        });
        //通过字节码技术动态创建子类实例
        return (DemoImpl) enhancer.create();
    }


    /**
     * 代理类的使用
     * */
    public static void usage() {
        //普通类
        DemoImpl demo = new DemoImpl();
        demo.execute();

        CglibProxyDemo JDKProxyDemo = new CglibProxyDemo(demo);
        //代理类
        DemoImpl pDemo = JDKProxyDemo.getInstance();
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
        DemoImpl demo = new DemoImpl();
        long endTime = System.currentTimeMillis();
        System.out.println("创建普通类花费:" + (endTime - startTime) + "ms");

        CglibProxyDemo cglibProxyDemo = new CglibProxyDemo(demo);
        //代理类
        startTime = System.currentTimeMillis();
        DemoImpl pDemo = cglibProxyDemo.getInstance();
        endTime = System.currentTimeMillis();
        System.out.println("创建代理类花费:" + (endTime - startTime) + "ms");

        //再次创建代理类
        startTime = System.currentTimeMillis();
        DemoImpl pDemo1 = cglibProxyDemo.getInstance();
        endTime = System.currentTimeMillis();
        System.out.println("再次创建代理类花费:" + (endTime - startTime) + "ms");

    }

    public static void main(String[] args) {
//        usage();
        performance();
    }
}
