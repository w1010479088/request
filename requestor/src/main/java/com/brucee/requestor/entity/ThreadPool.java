package com.brucee.requestor.entity;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private ExecutorService executors = Executors.newFixedThreadPool(4);
    private Handler handler = new Handler(Looper.getMainLooper());

    private ThreadPool() {
    }

    private static class HOLDER {
        private static ThreadPool INSTANCE = new ThreadPool();
    }

    public static void work(Runnable action) {
        HOLDER.INSTANCE.executors.execute(action);
    }

    public static void main(Runnable action) {
        HOLDER.INSTANCE.handler.post(action);
    }

    public static void main(Runnable action, long delayMillis) {
        HOLDER.INSTANCE.handler.postDelayed(action, delayMillis);
    }
}
