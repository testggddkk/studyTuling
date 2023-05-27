package com.jvm;

import redis.clients.jedis.Jedis;

public class JedisDistributedLockDemo {
    private static final String LOCK_KEY = "my-distributed-lock";
    private static final int LOCK_TIMEOUT = 5000; // 锁超时时间为5秒

    private Jedis jedis;

    public JedisDistributedLockDemo() {
        jedis = new Jedis("localhost", 6379);
    }

    public boolean acquireLock() {
        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                // 使用 SETNX 命令尝试设置锁键
                Long setnx = jedis.setnx(LOCK_KEY, "1");
                if (setnx==1L) {
                    return true; // 获取到了锁
                }

                // 检查是否超时
                long currentTime = System.currentTimeMillis();
                if (currentTime - startTime >= LOCK_TIMEOUT) {
                    return false; // 获取锁超时
                }

                // 等待一段时间后重试
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return false;
    }

    public void releaseLock() {
        jedis.del(LOCK_KEY);
    }

    public void performTaskWithLock() {
        boolean acquiredLock = acquireLock();

        if (acquiredLock) {
            try {
                // 执行业务逻辑
                System.out.println("Lock acquired. Performing task...");
                performTask();
            } finally {
                // 释放锁
                releaseLock();
                System.out.println("Lock released.");
            }
        } else {
            System.out.println("Failed to acquire lock.");
        }
    }

    public void performTask() {
        // 执行你的业务逻辑
        // ...
    }

    public static void main(String[] args) {
        JedisDistributedLockDemo lockDemo = new JedisDistributedLockDemo();
        lockDemo.performTaskWithLock();
    }
}
