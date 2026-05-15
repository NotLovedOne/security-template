package org.example.practice.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(RedisService::class.java)

    fun set(key: String, value: String, ttl: Duration = Duration.ofMinutes(10)) {
        val json = objectMapper.writeValueAsString(value)
        redisTemplate.opsForValue().set(key, json, ttl)
    }

    fun get (key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }

    fun delete (key: String) : Boolean {
        return redisTemplate.delete(key)
    }

    private inline fun <reified T> getAs(key: String): T? = get(key)?.let { objectMapper.readValue(it, T::class.java) }
}