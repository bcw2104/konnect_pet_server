package com.konnect.pet.repository.redis;

import org.springframework.data.repository.CrudRepository;

import com.konnect.pet.entity.redis.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long>{

}
