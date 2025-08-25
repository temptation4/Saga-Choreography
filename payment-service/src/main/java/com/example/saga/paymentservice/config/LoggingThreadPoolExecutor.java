package com.example.saga.paymentservice.config;

// LoggingThreadPoolExecutor.java (Custom Executor with exception handling)

import java.util.concurrent.*;

public class LoggingThreadPoolExecutor extends ThreadPoolExecutor {

    public LoggingThreadPoolExecutor(int poolSize) {
        super(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t == null && r instanceof Future<?>) {
            try {
                ((Future<?>) r).get();
            } catch (CancellationException | ExecutionException e) {
                t = e.getCause();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            System.err.println("⚠️ Exception in background task: " + t.getMessage());
            t.printStackTrace();
        }
    }
}
