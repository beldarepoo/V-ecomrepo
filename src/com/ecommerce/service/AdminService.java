package com.ecommerce.service;


import com.ecommerce.model.Product;
import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

public interface AdminService {

	public Product checkProductQuantity(long productID);

	public void getAllUsers();

	public Order checkOrderDetails(long orderID);

}
