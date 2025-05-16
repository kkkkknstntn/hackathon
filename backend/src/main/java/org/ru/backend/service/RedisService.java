package org.ru.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Получение всех значений из Redis по переданному ключу.
     *
     * @param key Redis ключ для поиска.
     * @return Сет уникальных значений.
     */
    public Set<String> getValuesByKey(String key) {
        try {
            // Проверяем, существует ли ключ в Redis
            if (redisTemplate.hasKey(key)) {
                return redisTemplate.opsForSet().members(key);  // Получаем элементы из множества
            }
            return null;  // Если ключ не существует, возвращаем null
        } catch (Exception e) {
            throw new RuntimeException("Error fetching data from Redis", e);
        }
    }
}
