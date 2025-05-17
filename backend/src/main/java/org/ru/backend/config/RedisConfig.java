package org.ru.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128); // Set max connections (optional)
        poolConfig.setMaxIdle(128);  // Set max idle connections (optional)
        poolConfig.setMinIdle(16);   // Set min idle connections (optional)

        // Disable JMX for Jedis Pool
        poolConfig.setJmxEnabled(false);

        // You can specify host and port for your Redis instance, by default, it's localhost and port 6379
        return new JedisPool(poolConfig, "localhost", 6379);
    }

    @Bean
    public Jedis jedis(JedisPool jedisPool) {
        return jedisPool.getResource(); // Use Jedis from the pool
    }
}
