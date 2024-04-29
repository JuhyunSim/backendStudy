package com.example.demo.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import redis.embedded.RedisServer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

@Configuration
public class LocalRedisConfig {
    @Value("${spring.data.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws Exception {
        redisPort = 6379;

        if (isArmMac()) {
            redisServer
                    = new RedisServer(Objects.requireNonNull(getRedisFileForArcMac()),
                    redisPort);
        }
        if (!isArmMac()) {
            redisServer = new RedisServer(redisPort);
        }

        redisServer.start();
    }

    private boolean isArmMac() {
        return Objects.equals(System.getProperty("os.arch"), "aarch64") &&
                Objects.equals(System.getProperty("os.name"), "Mac OS X");
    }

    private File getRedisFileForArcMac() throws Exception{
        return new ClassPathResource("/redis/redis-server-7.2.3-mac-arm64").getFile();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {redisServer.stop();}
    }
}
