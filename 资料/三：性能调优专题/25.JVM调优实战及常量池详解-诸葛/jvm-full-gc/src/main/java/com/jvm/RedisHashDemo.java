package com.jvm;

import redis.clients.jedis.Jedis;
import java.util.Map;
import java.util.Set;

public class RedisHashDemo {
    public static void main(String[] args) {
        // 创建Jedis实例
        Jedis jedis = new Jedis("localhost");

        // 设置Hash字段和值
        jedis.hset("myHash", "field1", "value1");
        jedis.hset("myHash", "field2", "value2");
        jedis.hset("myHash", "field3", "value3");

        // 获取Hash字段的值
        String fieldValue1 = jedis.hget("myHash", "field1");
        System.out.println("field1: " + fieldValue1);

        // 获取Hash的所有字段和值
        Map<String, String> hashValues = jedis.hgetAll("myHash");
        for (Map.Entry<String, String> entry : hashValues.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // 删除Hash中的字段
        jedis.hdel("myHash", "field2");

        // 获取Hash的所有字段
        Set<String> myHash = jedis.hkeys("myHash");
        System.out.println("Fields in myHash:");
        for (String hash : myHash) {
            System.out.println(hash);
        }

        // 关闭Jedis连接
        jedis.close();
    }
}
