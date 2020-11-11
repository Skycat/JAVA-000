package name.skycat.study.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-11-11 14:59:13
 */
public class Test {
    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        // 方法1
        Between between = new Between();
        method1();
        between.getMillis();
        
        // 方法2
        method2();
        between.getMillis();
        
        // 方法3
        method3();
        between.getMillis();
        
        // 方法4
        method4();
        between.getMillis();
        
        // 方法5
        method5();
        between.getMillis();
        
        // 方法6
        method6();
        between.getMillis();
        
        // 退出main线程
        System.out.println("main线程结束...");
    }
    
    // 方法1: 同步
    private static void method1() {
        int result = sum(); //这是得到的返回值
        // 确保  拿到result 并输出
        System.out.println("同步计算结果为："+result);
    }
    
    // 方法2: 通过阻塞队列获取结果
    private static void method2() {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(1);
        Thread thread = new Thread(() -> {
            int result = sum();
            queue.add(result);
        });
        thread.start();
        try {
            //这是得到的返回值
            int result = queue.take();
            System.out.println("method2计算结果为："+result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
    }
    
    // 方法3: 通过countdownlacth异步获取
    private static void method3() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        List<Integer> result = new ArrayList<Integer>(1);
        Thread thread = new Thread(() -> {
            result.add(sum());
            countDownLatch.countDown();
        });
        thread.start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 确保  拿到result 并输出
        System.out.println("method3计算结果为："+result.get(0));
    }
    
    // 方法4: 通过线程池future获取
    private static void method4() {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future<Integer> future = pool.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        });
        try {
            Integer result = future.get();
            System.out.println("method4计算结果为："+result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }
    
    // 方法5: 通过lock-condition异步获取
    private static void method5() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        List<Integer> result = new ArrayList<Integer>(1);
        Thread thread = new Thread(() -> {
            lock.lock();
            try {
                result.add(sum());
                condition.signal();
            } finally {
                lock.unlock();
            }
        });
        thread.start();
        try {
            lock.lock();
            try {
                condition.await();
                System.out.println("method5计算结果为："+result.get(0));
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // 方法6: 通过CyclicBarrier异步获取
    private static void method6() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        List<Integer> result = new ArrayList<Integer>(1);
        Thread thread = new Thread(() -> {
            result.add(sum());
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("method6计算结果为："+result.get(0));
    }
    
    private static int sum() {
        return fibo(36);
    }
    
    private static int fibo(int a) {
        if ( a < 2) 
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}

class Between {
    private long startMillis;
    
    Between() {
        startMillis = System.currentTimeMillis();
    }
    
    long getMillis() {
        long now = System.currentTimeMillis();
        long time = now - startMillis;
        startMillis = now;
        System.out.println("使用时间："+ time + " ms");
        return time;
    }
}
