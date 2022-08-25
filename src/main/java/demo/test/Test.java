package demo.test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    public static void main(String[] args) {
        /*ThreadDemo td = new ThreadDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(td).start();
            //System.out.println(td.add());
        }*/
        CompareAndSwap cas = new CompareAndSwap();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                int expectedValue = cas.getValue();
                boolean result = cas.compareAndSet(expectedValue, new Random().nextInt());
                System.out.println(result + "====" + System.nanoTime());
            }).start();
        }
    }
}


class ThreadDemo implements Runnable {

    private AtomicInteger i = new AtomicInteger();

    @Override
    public void run() {
        try {
            //Thread.sleep(400);
            System.out.println(i.getAndIncrement());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class CompareAndSwap {
    private int value;


    public synchronized int getValue() {
        return value;
    }


    public synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return expectedValue == compareAndSwap(expectedValue, newValue);
    }


    private synchronized int compareAndSwap(int expectedValue, int newValue) {
        int oldValue = value;
        if (expectedValue == oldValue) {
            value = newValue;
        }
        return oldValue;
    }
}