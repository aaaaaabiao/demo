package site.abiao.demo.thread;

public class VolatileTest {

    public  static volatile boolean stop = false;
    public  static volatile boolean init = false;

    public static String str = null;

    /**
     * 测试volatile可见性代码
     * */
    public static void visible() throws InterruptedException {
        Thread t = new Thread(() -> {
            while (!stop) {
                //加system.out.println("xxx"),会刷新stop的值
                //System.out.println();
            }
            System.out.println("stoped");
        });
        t.start();

        Thread.sleep(10);

        stop = true;
    }


    /**
     * 测试volatile可见性代码
     * */
    public static void order() throws InterruptedException {


        Thread t = new Thread(() -> {
            while (!init) {
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
            int a = str.length();

        });

        t.start();

        Thread.sleep(100);
        //初始化
        str = "123";
        init = true;

    }

    public static void main(String[] args) throws InterruptedException {
//        visible();
        order();
    }
}
