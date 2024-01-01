package com.anton.springeshop.service;

import com.anton.springeshop.dao.ProductRepository;
import com.anton.springeshop.domain.Bucket;
import com.anton.springeshop.domain.Product;
import com.anton.springeshop.domain.Role;
import com.anton.springeshop.domain.User;
import com.anton.springeshop.dto.ProductDto;
import com.anton.springeshop.dto.UserDto;
import com.anton.springeshop.mapper.ProductMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductMapper mapper = ProductMapper.MAPPER;

	private final ProductRepository productRepository;
	private final UserService userService;
	private final BucketService bucketService;
	//private final SimpMessagingTemplate template;

	public ProductServiceImpl(ProductRepository productRepository,
							  UserService userService,
							  BucketService bucketService)
		//					  SimpMessagingTemplate template)
	{
		this.productRepository = productRepository;
		this.userService = userService;
		this.bucketService = bucketService;
	//	this.template = template;
	}

	@Override
	public List<ProductDto> getAll() {
		return mapper.fromProductList(productRepository.findAll());
	}

	@Override
	@javax.transaction.Transactional
	public void addToUserBucket(Long productId, String username) {
		User user = userService.findByName(username);
		if(user == null){
			throw new RuntimeException("User not found. " + username);
		}

		Bucket bucket = user.getBucket();
		if(bucket == null){
			Bucket newBucket = bucketService.createBucket(user, Collections.singletonList(productId));
			user.setBucket(newBucket);
			userService.save(user);
		}
		else {
			bucketService.addProducts(bucket, Collections.singletonList(productId));
		}
	}

	@Override
	@Transactional
	public void addProduct(ProductDto dto) {
		//Product product = mapper.toProduct(dto);
		Product product = Product.builder()
				.price(dto.getPrice())
				.title(dto.getTitle())
				.build();
		productRepository.save(product);
		//Product savedProduct = productRepository.save(product);
//
	}

	@Override
	public ProductDto getById(Long id) {
		Product product = productRepository.findById(id).orElse(new Product());
		return ProductMapper.MAPPER.fromProduct(product);
	}


//	@Override
//	@javax.transaction.Transactional
//	public boolean save(ProductDto dto) {
//		Product product = mapper.toProduct(dto);
//		if(!Objects.equals(product.getProduct(), productRepository.getMatchingPassword())){ // проверем пароли
//			throw new RuntimeException("Password is not equal");
//		}
//		User user = User.builder()
//				.name(userDto.getUsername())
//				.password(passwordEncoder.encode(userDto.getPassword()))
//				.email(userDto.getEmail())
//				.role(Role.CLIENT)
//				.build();
//		userRepository.save(user);
//		return true;
//	}

}
