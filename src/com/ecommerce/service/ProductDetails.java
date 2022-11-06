package com.ecommerce.service;

import java.math.BigInteger;
import java.util.List;

import com.ecommerce.model.Product;

public interface ProductDetails {
	
	public int addProduct(Product product);
	public void deleteProduct(BigInteger productID);
	public int updateProduct(BigInteger productID);
	public Product getProduct(BigInteger productID);
	public List<Product> getAllProducts();

}
