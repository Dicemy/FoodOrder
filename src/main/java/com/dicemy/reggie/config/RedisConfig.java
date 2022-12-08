package com.dicemy.reggie.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//继承CachingConfigurerSupport的作用是对CachingConfigurerSupport进行强化，在一些地方使用到了CachingConfigurerSupport，如果直接删除extends CachingConfigurerSupport会报错
//查阅资料后发现，如果在一个类上标注@Configuration后，该类中的@Bean可以被类中别的@Bean直接依赖，这样继承CachingConfigurerSupport的目的可能是因为CachingConfigurerSupport中有用到RedisTemplate类的Bean。
//同时extends CachingConfigurerSupport后再@Configuration标注可以注入CachingConfigurerSupport类中的@Bean
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
