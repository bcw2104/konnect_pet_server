package com.konnect.pet.repository.query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.konnect.pet.entity.Properties;
import com.konnect.pet.repository.PropertiesRepository;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PropertiesQueryRepository {

	private final PropertiesRepository propertiesRepository;

	public Map<String,String> getPropertyMapByKeys(String... keys){
		List<String> keyList = Arrays.asList(keys);

		List<Properties> properties = propertiesRepository.fintByKeys(keyList);

		Map<String, String> propertyMap = new HashMap<String, String>();

		properties.forEach(ele -> {
			propertyMap.put(ele.getPKey(), ele.getPValue());
		});

		return propertyMap;
	}
}
