/*
 * Admin service class - it includes all operation related to Admin.
 */

package com.ecommerce.service;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

import com.ecommerce.model.User;
import com.ecommerce.util.EcomUtil;
import com.ecommerce.util.Login;
import com.ecommerce.DBConnection.DBConnection;
import com.ecommerce.model.Product;
import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

public class AdminServiceImpl implements AdminService{
	
	Scanner sc = null;
	int choice;
	long productID;
	long orderID;
	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	
	public void printMainMenu() {
		
		System.out.println();
		System.out.println("-------------------------------->> WELCOME TO ADMIN DASHBOARD <<------------------------------------");
		System.out.println("\t\t******* Select your choice *******");
		System.out.println("\t\t1. Check Product Quantity");
		System.out.println("\t\t2. Show All Users");
		System.out.println("\t\t3. Check Order History");
		System.out.println("\t\t4. Logout");
		System.out.println("\t\t5. EXIT");
		System.out.println("-----------------------------------------------------------------------------------------------------");
	}
	
	/*
	 * This method use to perform operation based on admin choice
	 */
	public void doOperation() {
		
		sc = new Scanner(System.in);
		
		do {
			
		this.printMainMenu();
		System.out.println("Enter your choice : ");
		
		choice = sc.nextInt();
		
			switch (choice) {
			
				case 1: 
						System.out.println("Enter product ID : ");
						productID = sc.nextLong();
						long maxid=EcomUtil.getMaxProductID();
						
						if(productID>maxid) {
							
							System.out.println("Invalid product ID. Please enter valid ID.");
							break;
						}
						
						Product product = this.checkProductQuantity(productID);
						System.out.println("Product ID: "+product.getProductId());
						System.out.println("Product Name: "+product.getProductName());
						System.out.print("Product Quantity: "+product.getProductQuantity());
						break;
				case 2:
						this.getAllUsers();
						break;
				case 3:
						
						System.out.println("Enter user's Contact No: ");
						String contactNo = sc.next();
						boolean isExist =EcomUtil.isUserExisted(contactNo);
						
						if(isExist==true) {
							UserServiceImpl userService = new UserServiceImpl();
							userService.getOrderDetails(contactNo);
							break;
						}
						else {
							System.out.println("Invalid user. Please enter correct user's contact No.");
							break;
						}
						
						
				case 4:
						System.out.println("Admin successfully logout...");
						new Login().doAdminLogin();
						break;
				case 5:
						System.out.println("Program Exited...");
						System.exit(0);
						break;
				default:
						System.out.println("You have entered wrong choice");
						this.doOperation();
				}
			
		} while (choice<=5);
		
		
		
		
	}


	/* @param long productID
	 *  this method is used to check product quentity
	 * @return Product object
	 */
	@Override
	public Product checkProductQuantity(long productID) {
		
		Product product = new Product();
		String query  = "Select id, name, quantity from products where id=?";
		
		
		try {
			

			//get connection
			conn = DBConnection.getConnection();
			pst = conn.prepareStatement(query);
			pst.setLong(1, productID);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				
				product.setProductId(productID);
				product.setProductName(rs.getString("name"));
				product.setProductQuantity(rs.getInt("quantity"));
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return product;
	}


	@Override
	public Order checkOrderDetails(long orderID) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void getAllUsers() {
		
		String query = "select * from users where user_type=?";
		
		try {
			
			conn=DBConnection.getConnection();
			pst=conn.prepareStatement(query);
			pst.setString(1, "U");
			rs=pst.executeQuery();
			System.out.println();
			System.out.println("-----------------------------------------------------------------------------------------------------");
			System.out.println("\tID \t\t NAME \t\t\t CONTACT NO \t\t\t EMAIL ID");
			System.out.println();
			while (rs.next()) {
			System.out.println("\t"+rs.getInt("id")+"\t\t"+rs.getString("name")+"\t\t"+rs.getString("contact_no")+"\t\t\t"+rs.getString("email_id"));	
				
			}
			System.out.println();
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	
}
