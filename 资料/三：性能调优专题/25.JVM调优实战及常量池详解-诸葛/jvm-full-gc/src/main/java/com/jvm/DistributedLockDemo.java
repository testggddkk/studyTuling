package com.jvm;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;

public class DistributedLockDemo {
    private static final String LOCK_KEY = "my-distributed-lock";
    private static final Duration LOCK_TIMEOUT = Duration.ofSeconds(5);

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> redisCommands;

    public DistributedLockDemo() {
        redisClient = RedisClient.create("redis://localhost");
        connection = redisClient.connect();
        redisCommands = connection.sync();
    }

    public void acquireLock() {
        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                Boolean setnx = redisCommands.setnx(LOCK_KEY, "1");
                if (setnx) {
                    return;
                }

                long currentTime = System.currentTimeMillis();
                if (currentTime - startTime >= LOCK_TIMEOUT.toMillis()) {
                    throw new RuntimeException("Failed to acquire lock.");
                }

                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (RedisConnectionException e) {
            throw new RuntimeException("Failed to acquire lock due to Redis connection issue.", e);
        }
    }

    public void releaseLock() {
        try {
            redisCommands.del(LOCK_KEY);
        } catch (RedisConnectionException e) {
            throw new RuntimeException("Failed to release lock due to Redis connection issue.", e);
        } finally {
            connection.close();
            redisClient.shutdown();
        }
    }

    public void performTaskWithLock() {
        acquireLock();

        try {
            System.out.println("Lock acquired. Performing task...");
            performTask();
        } finally {
            releaseLock();
            System.out.println("Lock released.");
        }
    }

    public void performTask() {
        // 执行业务逻辑
        // ...
    }

    public static void main(String[] args) {
        DistributedLockDemo lockDemo = new DistributedLockDemo();
        lockDemo.performTaskWithLock();
    }
}
