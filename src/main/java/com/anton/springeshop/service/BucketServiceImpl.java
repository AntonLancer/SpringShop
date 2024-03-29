package com.anton.springeshop.service;

import com.anton.springeshop.dao.BucketRepository;
import com.anton.springeshop.dao.ProductRepository;
import com.anton.springeshop.domain.*;
import com.anton.springeshop.domain.*;
import com.anton.springeshop.dto.BucketDto;
import com.anton.springeshop.dto.BucketDetailDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BucketServiceImpl implements BucketService {

	private final BucketRepository bucketRepository;
	private final ProductRepository productRepository;
	private final UserService userService;
//	private final OrderService orderService;

	public BucketServiceImpl(BucketRepository bucketRepository,
							 ProductRepository productRepository,
							 UserService userService)
	//						 OrderService orderService)
	{
		this.bucketRepository = bucketRepository;
		this.productRepository = productRepository;
		this.userService = userService;
//		this.orderService = orderService;
	}

	@Override
	@javax.transaction.Transactional
	public Bucket createBucket(User user, List<Long> productIds) {
		Bucket bucket = new Bucket();
		bucket.setUser(user);
		List<Product> productList = getCollectRefProductsByIds(productIds);
		bucket.setProducts(productList);
		return bucketRepository.save(bucket);
	}

	private List<Product> getCollectRefProductsByIds(List<Long> productIds) {
		return productIds.stream()
				.map(productRepository::getOne)
				.collect(Collectors.toList());
	}

	@Override
	@javax.transaction.Transactional
	public void addProducts(Bucket bucket, List<Long> productIds) {
		List<Product> products = bucket.getProducts();
		List<Product> newProductsList = products == null ? new ArrayList<>() : new ArrayList<>(products);
		newProductsList.addAll(getCollectRefProductsByIds(productIds));
		bucket.setProducts(newProductsList);
		bucketRepository.save(bucket);
	}

	@Override
	public BucketDto getBucketByUser(String name) { // расчитывание количества добавленных товаров в корзину
		User user = userService.findByName(name);
		if(user == null || user.getBucket() == null){
			return new BucketDto();
		}

		BucketDto bucketDto = new BucketDto();
		Map<Long, BucketDetailDto> mapByProductId = new HashMap<>();

		List<Product> products = user.getBucket().getProducts();
		for (Product product : products) {
			BucketDetailDto detail = mapByProductId.get(product.getId());
			if(detail == null){
				mapByProductId.put(product.getId(), new BucketDetailDto(product));
			}
			else {
				detail.setAmount(detail.getAmount() + 1.0);
				detail.setSum(detail.getSum() + product.getPrice());
			}
		}

		bucketDto.setBucketDetails(new ArrayList<>(mapByProductId.values()));
		bucketDto.aggregate();

		return bucketDto;
	}
}