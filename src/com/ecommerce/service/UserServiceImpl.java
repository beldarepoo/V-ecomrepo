/*
 * User Service class- It includes all operations related to USer.
 */

package com.ecommerce.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ecommerce.DBConnection.DBConnection;
import com.ecommerce.exception.EcommerceException;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.util.EcomUtil;
import com.ecommerce.util.Login;

public class UserServiceImpl implements UserService {
	
	Scanner sc = null;
	Connection conn = null;
	PreparedStatement pst,pst2,pst3 = null;
	ResultSet rs,rs2= null;
	static String contactNo;
	
	// this method is used to show menu to user
	public void showUserMenu() {
		
		System.out.println();
		System.out.println("---------------------------------------------->> WELCOME USER TO My ELECTRONICS <<--------------------------------------------------------");
		System.out.println();
		System.out.println("\t\t******* Select your choice *******");
		System.out.println();
		System.out.println("\t\t1. Show All Products");
		System.out.println("\t\t2. Add Product To Cart");
		System.out.println("\t\t3. Checkout");
		System.out.println("\t\t4. Order History");
		System.out.println("\t\t5. Show Profile");
		System.out.println("\t\t6. Logout");
		System.out.println("\t\t7. EXIT");
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	
	public void doOperation(String userSession) {
		
		sc = new Scanner(System.in);
		contactNo=userSession;
		
		int choice;
		
		do {
			
		this.showUserMenu();
		System.out.println("Enter your choice : ");
		
		choice = sc.nextInt();
		
			switch (choice) {
			
				//case 1: to show all products to user
				case 1: 
						List<Product> products = this.getAllProducts();
						
						System.out.println();
						System.out.println("--------------------------------------------------->> PRODUCT LIST <<--------------------------------------------------------------------");
						System.out.println();
						System.out.println("\tID\t\t\t NAME \t\t\t\t\t DESCRIPTION\t\t\t\tQUANTITY \t\tPRICE");
						System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
						System.out.println();
						
						for(Product pList : products) {
							
							System.out.println("\t"+pList.getProductId()+" \t\t"+pList.getProductName()+"\t\t\t"+pList.getProductDescription()+"\t\t\t"+pList.getProductQuantity()+"\t\t\t"+pList.getProductPrice());
							System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
						}
						
						
						break;
				//case 2: to add product to cart
				case 2:
						this.addProductToCart(contactNo);
						
						break;
				//case 3. to get all products from cart
				case 3:
						this.checkout(contactNo);
				//case 4. to get user profile details
				case 4:
						
						this.getOrderDetails(contactNo);
						break;
				//case 5. to update user profile
				case 5:
						this.getUser(contactNo);
						break;
				case 6:
						System.out.println("User successfully logout.");
						new Login().doUserLogin();
						break;
				//case 7: to exit 
				case 7: 
					System.out.println("Program Exited...");
					System.exit(0);
				default:
						System.out.println("You have entered wrong choice");
						this.doOperation(contactNo);
				}
			
		} while (choice<=7);
		
		
		
	}

	// to register new user
	@Override
	public void addUser() {
		sc = new Scanner(System.in);
		User user = new User();
		int result=0;
		
		
		System.out.println("Enter contact No: ");
		String contactNo=sc.nextLine();
		user.setContactNo(contactNo);
		
		boolean isExist =EcomUtil.isUserExisted(contactNo);
		
		if(isExist==false) {
		
			sc = new Scanner(System.in);
			System.out.println("Enter name: ");
			user.setUserName(sc.nextLine());
			System.out.println("Enter EmailId: ");
			user.setEmailId(sc.nextLine());
			System.out.println("Enter password: ");
			user.setPassword(sc.nextLine());
			user.setUserType("U");
		
		try {
			
			String query = "insert into users(name,contact_no,email_id,password,user_type) values(?,?,?,?,?)";
			conn = DBConnection.getConnection();
			pst = conn.prepareStatement(query);
			pst.setString(1, user.getUserName());
			pst.setString(2, user.getContactNo());
			pst.setString(3, user.getEmailId());
			pst.setString(4, user.getPassword());
			pst.setString(5, user.getUserType());
			
			result =pst.executeUpdate();

			if(result>0) {
				
				System.out.println("User Successfully Registred.");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		}
		else {
			
			System.out.println("User already exist. Please try with diffrent contact No");
			
		}
		
		
	}

	//to update user profile details
	@Override
	public int updateUser(String contacctNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	//to get user profile details
	@Override
	public void getUser(String contactNo) {
		
		String query = "select * from users where contact_no=?";
		
		try {
			
			conn = DBConnection.getConnection();
			pst=conn.prepareStatement(query);
			pst.setString(1, contactNo);
			rs=pst.executeQuery();
			
			System.out.println("---------------------------USER PROFILE-----------------------------");
			while(rs.next()) {
				System.out.println("\t\tName: "+rs.getString("name"));
				System.out.println("\t\tContact No: "+rs.getString("contact_no"));
				System.out.println("\t\tEmail Id: "+rs.getString("email_id"));
			}
			System.out.println("--------------------------------------------------------------------");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}

	//to add product to cart
	@Override
	public void addProductToCart(String contactNo) {
		
		int result=0;
		int choice;
		long productID;
		int productQty;
		String ch;
		
		/*
		 * System.out.println("Enter user ID(Contact No): "); contactNo = sc.next();
		 */
		
		//boolean isValidUser=EcomUtil.isValidUser(contactNo);
		
		//if (isValidUser) {
			
			System.out.println("Enter product ID: ");
			productID = sc.nextLong();
			
			long maxid=EcomUtil.getMaxProductID();
			
			if(productID>maxid) {
				
				System.out.println("Invalid product ID. Please enter valid ID.");
				this.addProductToCart(contactNo);
			}
			
			System.out.println("Enter product QTY: ");
			productQty = sc.nextInt();
			
			long pqty= EcomUtil.checkProductQuantity(productID);
			
			if(pqty<=0 ) {
				
				System.out.println("Out of stock");
				
			}else if(productQty>pqty){
				
				System.out.println("Availabe products quantity is "+pqty+".Please select available quantity.");
				this.addProductToCart(contactNo);
			}
			else {
				
		String insertProduct = "insert into cart_item(product_id,quantity,user_id) values(?,?,?);";
		
		try {
			
			conn = DBConnection.getConnection();
			pst2 = conn.prepareStatement(insertProduct);
			pst2.setLong(1, productID);
			pst2.setInt(2, productQty);
			pst2.setString(3, contactNo);
			result = pst2.executeUpdate();
			
			if(result>0) {
				System.out.println("Product sucessfully added to cart.");
			}
			else {
				throw new EcommerceException("Product not added");
			}
			
			System.out.println("Do You Want Order Anything Else (Y/N):");
			ch = sc.next();
			if (ch.equalsIgnoreCase("Y")) {
				this.addProductToCart(contactNo);
			} else if (ch.equalsIgnoreCase("N")) {
				this.checkout(contactNo);
			
			} else {
				
				System.out.println("Invalid choice");
		
		}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		
			}
		/*
		 * } else {
		 * 
		 * 
		 * System.out.
		 * println("User is not valid.. please try with valid user ID (Contact No)...."
		 * ); this.addProductToCart(contactNo); }
		 */
		
			
		//return result;
		
	}

	

	//to get all products from database
	@Override
	public List<Product> getAllProducts() {
		
		
		List<Product> productList = new ArrayList<>();
		String query = "select * from products";
		
		try {
			
			conn = DBConnection.getConnection();
			pst = conn.prepareStatement(query);
			rs =pst.executeQuery();
			
			while (rs.next()) {
				
				Product product = new Product();
				
				product.setProductId(rs.getLong("id"));
				product.setProductName(rs.getString("name"));
				product.setProductDescription(rs.getString("desc"));
				product.setProductQuantity(rs.getInt("quantity"));
				product.setProductPrice(rs.getDouble("price"));
				
				productList.add(product);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return productList;
	}


	@Override
	public void checkout(String contactNo) {

		double grandTotal=0;
		long orderID = 0;
		String ch = null;
		
		int count=EcomUtil.getTotalCartItem(contactNo);
		
		if(count<=0) {
			System.out.println("Cart is empty.");
			this.doOperation(contactNo);
		}
		else {
			
			
			List<Product> pList= new ArrayList<>();
			
			String query = "select c.id,p.name,c.quantity,p.price from products p,cart_item c where p.id=c.product_id and c.user_id=?;";
			
			try {
				
				conn = DBConnection.getConnection();
				pst=conn.prepareStatement(query);
				pst.setString(1, contactNo);
				rs=pst.executeQuery();
				
				
				
				System.out.println("--------------------------List of Products in Cart-------------------------");
				System.out.println();
				System.out.println("ID\t\t NAME\t\t     QUANTITY\t PRICE \t\t TOTAL");
				System.out.println();
				while (rs.next()) {
					
					Product product = new Product();
					product.setProductId(rs.getLong("id"));
					product.setProductQuantity(rs.getInt("Quantity"));
					pList.add(product);
					
					grandTotal+=rs.getInt("quantity")*rs.getDouble("price");
					System.out.println(""+rs.getLong("id")+"\t"+rs.getString("name")+"\t"+rs.getInt("Quantity")+"\t"+rs.getDouble("price")+"\t\t"+(rs.getInt("Quantity")*rs.getDouble("price")));
					
				}
				double netAmount=((100-5)*grandTotal)/100;
				
				System.out.println("----------------------------------------------------------------------------");
				System.out.println("Grand Total : Rs "+grandTotal+"\t");
				System.out.println("Congratulation.. 5% discount is applied.");
				System.out.println("Net payable amount: Rs "+netAmount);
				System.out.println("----------------------------------------------------------------------------");
				System.out.println();
				System.out.println("Proceed for payment...");
				
				System.out.println("Do you want to pay..? (Y/N)");
				ch = sc.next();
				
				if (ch.equalsIgnoreCase("Y")) {
					
					orderID=this.doOrder(pList, netAmount);
					
					if(orderID!=0) {
						
						System.out.println("*****Order successfully placed. Your Order Id is:"+orderID+"*****\n ***Thank you for shopping with My Electronics***");
						this.doOperation(contactNo);
						
				}
				else{
					
					System.out.println("Order Failed. Please retry.");
					this.doOperation(contactNo);
				}
					
				}
				else{
					
					System.out.println("Order Failed. Please retry.");
					this.doOperation(contactNo);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		
		
	}


	@Override
	public boolean doPayment() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public long doOrder(List<Product> products, double netAmount) {
		
		
		int result,result2 =0;
		String query = "insert into order_details(user_id,order_id,total,payment_status) values(?,?,?,?)";
		String query2 = "insert into order_items(order_id,product_id,quantity) values(?,?,?)";
		String query3 = "delete from cart_item where user_id=?";
		String query4 = "update products set quentity=? where id=?";
		
		long orderId=EcomUtil.idGenerator();
	
	try {
		
		conn=DBConnection.getConnection();
		pst = conn.prepareStatement(query);
		
		pst.setString(1, contactNo);
		pst.setLong(2, orderId);
		pst.setDouble(3, netAmount);
		pst.setString(4, "Paid");
		
		result=pst.executeUpdate();
		
		if(result>0) {
			
			pst2 = conn.prepareStatement(query2);
			
			
			for (Product p : products) {
				  
				  pst2.setLong(1, orderId); 
				  pst2.setLong(2, p.getProductId()); 
				  pst2.setInt(3, p.getProductQuantity()); 
				  
				 result2= pst2.executeUpdate(); 
				  
			}
			
			if(result2>0) {
			
				
				pst3=conn.prepareStatement(query3);
				pst3.setString(1, contactNo);
				int res=pst3.executeUpdate();
				
				if(res>0) {
					
					return orderId;
				}
		}
		
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
		return orderId;
	}


	@Override
	public void getOrderDetails(String contactNo) {
		
		/*
		 * int choice; //String contactNo="123456789"; long productID; int productQty;
		 */
		
		String query="select o.order_id,o.created_at,o.total,p.name,oi.quantity from order_details o,order_items oi, products p where o.order_id=oi.order_id and oi.product_id=p.id  and o.user_id=? ";
		String query2 = "select distinct(o.order_id),o.created_at,o.total from order_details o,order_items oi, products p where o.order_id=oi.order_id and oi.product_id=p.id  and o.user_id=?  group by o.order_id";
		String query3 = "select * from order_details where user_id=?";
		try {
			
			conn=DBConnection.getConnection();
			/*
			 * pst=conn.prepareStatement(query); pst.setString(1, contactNo);
			 * rs=pst.executeQuery();
			 */
			
			System.out.println("--------------------------------------ORDER HISTORY------------------------------------");
			
			pst2=conn.prepareStatement(query3);
			pst2.setString(1, contactNo);
			rs2=pst2.executeQuery();
			
			while(rs2.next()) {
				System.out.println("Order ID: "+rs2.getInt("order_id")+"\t\t Total Amount: "+Math.round(rs2.getDouble("total"))+"\t\t Order Date: "+rs2.getDate("created_at")+"\t\t Payment Status: "+rs2.getString("payment_status"));
			}
			
			System.out.println("----------------------------------------------------------------------------------------");
			/*
			 * System.out.println("\tProduct Name \t\t\t\t    Product QTY");
			 * System.out.println();
			 * 
			 * while(rs.next()) {
			 * 
			 * System.out.println("\t"+rs.getString("name")+"\t\t\t"+rs.getInt("quantity"));
			 * } System.out.println(
			 * "----------------------------------------------------------------------------------------"
			 * );
			 */
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}