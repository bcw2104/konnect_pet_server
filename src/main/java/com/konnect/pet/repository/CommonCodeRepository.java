package com.konnect.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.konnect.pet.entity.CommonCode;
import com.konnect.pet.entity.embedded.CommonCodePair;

public interface CommonCodeRepository extends JpaRepository<CommonCode,CommonCodePair>{

	List<CommonCode> findByCodePair_CodeGroupOrderBySortOrderAsc(String codeGroup);
}
