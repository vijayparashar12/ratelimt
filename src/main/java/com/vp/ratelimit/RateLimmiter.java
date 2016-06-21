package com.vp.ratelimit;

import java.util.concurrent.atomic.AtomicInteger;

import com.vp.config.RateLimtSettings;

/**
 * @author vijay
 *	This implementation of Ratelimmiter in done using atomic counter and synchronized block,
 *  every time access method is called counter is incremented and compared with given threshold.
 *  if window has expired it will recreate the window and then check the rate limit process.
 */
public final class RateLimmiter {
    private AtomicInteger counter;
    private final int threshold;
    private final int window;
    private long windowStart;
    private long windowEnd;

    public RateLimmiter(int threshold, int window) {
        this.threshold = threshold;
        this.window = window;
        counter = new AtomicInteger();
        createWindow();
    }

    public RateLimmiter(RateLimtSettings settings) {
        this(settings.getThreshold(), settings.getWindow());
    }

    private void createWindow() {
        windowStart = System.currentTimeMillis();
        windowEnd = windowStart + window * 1000;
        counter.set(0);
    }

    public boolean access() {
        boolean access = false;
        long now = System.currentTimeMillis();
        // This Synchronized block will protect create window method for being accessed by two threads.
        synchronized (this) {
            if (windowStart <= now && windowEnd >= now) {
                int currentCount = counter.incrementAndGet();
                if (currentCount <= threshold) {
                    access = true;
                }
            } else {
                createWindow();
                access = access();
            }
        }
        return access;
    }

    /*public static void main(String[] args) {
        RateLimmiter rateLimmiter = new RateLimmiter(1, 10);
        Task t1 = new Task(rateLimmiter);
        Task t2 = new Task(rateLimmiter);
        Task t3 = new Task(rateLimmiter);
        Thread th1 = new Thread(t1);
        th1.start();
        Thread th2 = new Thread(t2);
        th2.start();
        Thread th3 = new Thread(t3);
        th3.start();
    }

    static class Task implements Runnable {
        private RateLimmiter rateLimmiter;

        public Task(RateLimmiter rateLimmiter) {
            this.rateLimmiter = rateLimmiter;
        }

        @Override
        public void run() {
            int count = 0;
            while (count < 30) {
                System.out.println(rateLimmiter.access() + ":" + count);
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

}
