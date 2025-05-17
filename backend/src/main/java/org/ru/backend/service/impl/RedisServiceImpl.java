package org.ru.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.ru.backend.service.RedisService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final JedisPool jedisPool; // Используем JedisPool для управления подключениями к Redis

    /**
     * Получение всех значений по ключу из Redis.
     * @param key Ключ в Redis.
     * @return Множество значений по ключу.
     */
    public Set<String> getValuesByKey(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.smembers(key);
        }
    }

    /**
     * Получение статистики по ошибкам из Redis.
     * Статистика включает:
     * 1. Список всех ошибок.
     * 2. Для каждой ошибки — количество встреч и список пакетов.
     * @return Статистика по ошибкам, отсортированная по количеству встреч.
     */
    public Map<String, Map<String, Object>> getErrorStats() {
        Map<String, Map<String, Object>> errorStats = new HashMap<>();

        // Получаем все уникальные ошибки
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> errors = jedis.smembers("unique_errors");

            // Для каждой ошибки получаем статистику по её встречам и связанным с ней пакетам
            for (String error : errors) {
                String errorKey = "error:" + error;

                // Получаем все пакеты, связанные с ошибкой
                Set<String> packages = jedis.smembers(errorKey + ":packages");

                // Получаем количество встреч этой ошибки
                String countStr = jedis.hget(errorKey, "count");
                int count = countStr != null ? Integer.parseInt(countStr) : 0;

                // Формируем статистику по ошибке
                Map<String, Object> errorStat = new HashMap<>();
                errorStat.put("count", count);  // Количество встреч ошибки
                errorStat.put("packages", packages);  // Список пакетов, где эта ошибка встретилась

                // Добавляем статистику по ошибке в общую карту
                errorStats.put(error, errorStat);
            }
        }

        // Сортируем ошибки по количеству встреч (count) в порядке убывания
        return errorStats.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Integer.compare((int) entry2.getValue().get("count"), (int) entry1.getValue().get("count")))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // Если есть одинаковые ключи, выбираем первое (на случай, если есть дубликаты)
                        LinkedHashMap::new // Для сохранения порядка
                ));
    }
}
