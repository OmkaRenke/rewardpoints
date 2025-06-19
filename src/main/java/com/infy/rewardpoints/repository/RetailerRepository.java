package com.infy.rewardpoints.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.infy.rewardpoints.entity.Retailer;

public interface RetailerRepository extends CrudRepository<Retailer,Long> {
	
	 Optional<Retailer> findByEmail(String email);
}
