package org.ru.backend.service;

import java.util.Map;
import java.util.Set;

public interface RedisService {
    Set<String> getValuesByKey(String key);
    Map<String, Map<String, Object>> getErrorStats();
}