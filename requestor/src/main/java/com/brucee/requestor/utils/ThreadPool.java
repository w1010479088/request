package com.brucee.requestor.utils;

import android.os.Handler;
import android.os.Looper;

import com.brucee.requestor.entity.RequestBridge;

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
        HOLDER.INSTANCE.executors.execute(() -> {
            try {
                action.run();
            } catch (Exception ex) {
                log(ex.getMessage());
            }
        });
    }

    public static void main(Runnable action) {
        HOLDER.INSTANCE.handler.post(() -> {
            try {
                action.run();
            } catch (Exception ex) {
                log(ex.getMessage());
            }
        });
    }

    public static void main(Runnable action, long delayMillis) {
        HOLDER.INSTANCE.handler.postDelayed(() -> {
            try {
                action.run();
            } catch (Exception ex) {
                log(ex.getMessage());
            }
        }, delayMillis);
    }

    private static void log(String msg) {
        RequestBridge.logger.log(msg);
    }
}
