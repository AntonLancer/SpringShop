package com.anton.springeshop.controllers;

import com.anton.springeshop.dto.ProductDto;
import com.anton.springeshop.dto.UserDto;
import com.anton.springeshop.service.ProductService;
import com.anton.springeshop.service.SessionObjectHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	private final SessionObjectHolder sessionObjectHolder;

	public ProductController(ProductService productService, SessionObjectHolder sessionObjectHolder) {
		this.productService = productService;
		this.sessionObjectHolder = sessionObjectHolder;
	}

	@GetMapping
	public String list(Model model){
		sessionObjectHolder.addClick();
		List<ProductDto> list = productService.getAll();
		model.addAttribute("products", list);
		return "products";
	}

	@GetMapping("/{id}/bucket")
	public String addBucket(@PathVariable Long id, Principal principal){
		sessionObjectHolder.addClick();
		if(principal == null){
			return "redirect:/products";
		}
		productService.addToUserBucket(id, principal.getName());
		return "redirect:/products";
	}

	@PostMapping("/new") //если пользователь создался корректно,то
	public String saveUser(ProductDto dto, Model model){
		productService.addProduct(dto);
		//model.addAttribute("products", dto);
		return "redirect:/products";
	}

//	@PostMapping
//	public ResponseEntity<Void> addProduct(ProductDto dto){
//		productService.addProduct(dto);
//		return ResponseEntity.status(HttpStatus.CREATED).build();
//	}


//	@GetMapping("/{id}")
//	@ResponseBody
//	public ProductDto getById(@PathVariable Long id){
//		return productService.getById(id);
//	}

}
