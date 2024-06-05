package com.zerobase.cms.order.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisClient {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T get(Long key, Class<T> classType){
        return get(key.toString(), classType);
    }

    private <T> T get(String key, Class<T> classType){
        String redisValue = (String) redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(redisValue)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(redisValue, classType);
            } catch (JsonProcessingException e) {
                log.info("parsing error: {}", e.getMessage());
                return null;
            }
        }
    }

    public void put (Long key, Cart cart){
        put(key.toString(), cart);
    }

    private void put (String key, Cart cart){
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(cart));
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.CART_CHANGE_FAIL);
        }
    }
}
