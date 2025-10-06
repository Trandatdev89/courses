package com.project01.skillineserver.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService{

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveData(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> T getData(String key) {
        return (T)redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public void saveDataOnTime(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value,timeout, unit);
    }

    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
