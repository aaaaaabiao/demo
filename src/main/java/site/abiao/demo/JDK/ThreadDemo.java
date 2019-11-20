package site.abiao.demo.JDK;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ThreadDemo {

    /**
     * 模拟一个任务
     * */
    static class task implements Runnable{
        @Override
        public void run() {
            try {
                long startTime = System.currentTimeMillis();
                log.info("task start:{}", startTime);
                Thread.sleep(1000);
                long endTime = System.currentTimeMillis();
                log.info("task end:{}", endTime);
                log.info("total cost : {}", (endTime - startTime) / 1000);

                for (int i = 0; i < 10*100*1000; i++) {
                    log.info(Thread.currentThread().isInterrupted() + "");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 测试线程池关闭线程
     * */
    private static void shutdownThread() throws InterruptedException {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        threadPool.submit(new task());
        threadPool.shutdown();
    }


    /**
     *
     * */
    private static void shutdownThread1() throws InterruptedException {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        Future future = threadPool.submit(new task());
        Thread.sleep(1100);

        //future关闭阻塞状态的线程。抛异常
        future.cancel(true);


    }




    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        shutdownThread();
        shutdownThread1();
    }
}
