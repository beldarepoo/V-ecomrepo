package com.ecommerce.service;



import java.util.List;


import com.ecommerce.model.Product;

public interface UserService {
	
	public void addUser();
	public int updateUser(String contactNo);
	public void getUser(String contactNo);
	public void addProductToCart(String contactNo);
	public void checkout(String contactNo);
	public void getOrderDetails(String contactNo);
	public List<Product> getAllProducts();
	public boolean doPayment();
	public long doOrder(List<Product> products, double netAmount);
	
	
}
