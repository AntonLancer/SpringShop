package com.anton.springeshop.service;

import com.anton.springeshop.domain.Bucket;
import com.anton.springeshop.domain.User;
import com.anton.springeshop.dto.BucketDto;

import java.util.List;

public interface BucketService {
	Bucket createBucket(User user, List<Long> productIds);

	void addProducts(Bucket bucket, List<Long> productIds);

	BucketDto getBucketByUser(String name);

//	void commitBucketToOrder(String username);
}
