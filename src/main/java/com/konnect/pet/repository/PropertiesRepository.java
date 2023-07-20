package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.Properties;
import com.konnect.pet.entity.User;

public interface PropertiesRepository extends JpaRepository<Properties, String>{

	@Query("select p from Properties p where p.pKey = :key")
	Optional<Properties> findByKey(@Param("key") String key);

	@Query("select p from Properties p where p.pKey in :keys")
	List<Properties> fintByKeys(@Param("keys") List<String> keys);

	@Query("select p.pValue from Properties p where p.pKey = :key")
	Optional<String> findValueByKey(@Param("key") String key);

	@Query("select p from Properties p where p.pKeyGroup = :keyGroup")
	List<Properties> findByKeyGroup(@Param("keyGroup") String keyGroup);
}
