package com.konnect.pet.entity.redis;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@RedisHash(value = "rtk")
public class RefreshToken {

    @Id
    private Long id;

    private String refreshToken;
    
    @TimeToLive
    private Long timeout;
}