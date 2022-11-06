package com.ecommerce.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.ecommerce.DBConnection.DBConnection;
import com.ecommerce.model.User;
import com.ecommerce.service.AdminServiceImpl;
import com.ecommerce.service.UserServiceImpl;

public class Login {
	

	String username;
	String password;
	int choice;
	Connection conn = null;
	PreparedStatement pst=null;
	ResultSet rs=null;
	Scanner sc = null;
	
	public void printUserLoginMenu() {
		System.out.println();
		System.out.println("--------------------------------------->> WELCOME TO USER LOGIN <<---------------------------------");
		System.out.println();
		System.out.println("\t\t1. NEW USER? SIGN UP");
		System.out.println("\t\t2. EXISTING YOUR? - LOGIN");
		System.out.println("\t\t3. EXIT");
		System.out.println("-------------------------------------------------------------------------------------------------------");
		
	}
	
	public void printAdminLoginMenu() {
		System.out.println();
		System.out.println("--------------------------------------->> WELCOME TO ADMIN LOGIN <<---------------------------------");
		System.out.println();
		System.out.println("\t\t1. Login");
		System.out.println("\t\t2. EXIT");
		System.out.println("-------------------------------------------------------------------------------------------------------");
		
	}
	
	public void mainLoginMenu() {
		
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------------------------------");
		System.out.println("************************************>> WELCOME TO MY ELECTRONICS <<************************************");
		System.out.println("-------------------------------------------------------------------------------------------------------");
		System.out.println("\t\t1. Admin");
		System.out.println("\t\t2. User");
		System.out.println("\t\t3. EXIT");
		System.out.println("-------------------------------------------------------------------------------------------------------");
		
	}
	
	
	
	public void doLogin() {

		
		
		sc = new Scanner(System.in);
		
		
		
		do {
			this.mainLoginMenu();
			System.out.println("Enter your choice");
			choice=sc.nextInt();
			
			switch (choice) {
			
			case 1 :
					this.doAdminLogin();
					break;
			case 2:
					this.doUserLogin();
					break;
			case 3:
					System.out.println("Program Exited..");
					System.exit(0);
			default:
				System.out.println("Invalid choice");
				this.doLogin();
			}
			
			
		}
		while(choice<=4);
	}
	
	public void doUserLogin() {
		
		do {
		
			this.printUserLoginMenu();
		
		sc = new Scanner(System.in);
		System.out.println("Enter your choice : ");
		choice = sc.nextInt();
		
		switch (choice) {
		
		case 1:
			
			UserServiceImpl userServiceImpl = new UserServiceImpl();
			userServiceImpl.addUser();
			
			break;

		case 2:
			try {
				System.out.println("Enter username (Contact No) : ");
				username = sc.next();
				System.out.println();
				System.out.print("Enter password: ");
				password = sc.next();
				
				String query = "select contact_no from users where contact_no=? and password=? and user_type=?";
				conn = DBConnection.getConnection();
				
				pst = conn.prepareStatement(query);
				pst.setString(1, username);
				pst.setString(2, password);
				pst.setString(3, "U");
				
				rs = pst.executeQuery();
				
				if(rs.next()) {
						
						String userSession = rs.getString("contact_no");
						new UserServiceImpl().doOperation(userSession);
						break;
				}
				else {
					System.out.println("Invalid user credemtials.");
				}	
				
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case 3:
			System.out.println("Program Exited..");
			System.exit(0);
			break;
		default:
			System.out.println("Invalid choice");
			this.doUserLogin();
		}
		}
		while(choice<=4);
		
	}
	
	public void doAdminLogin() {
		

		sc = new Scanner(System.in);
		
		do {
		this.printAdminLoginMenu();	
		System.out.println("Enter your choice : ");
		choice = sc.nextInt();
		
		switch (choice) {
		
		case 1:
			try {
				System.out.println("Enter username (Contact No) : ");
				username = sc.next();
				System.out.println();
				System.out.print("Enter password: ");
				password = sc.next();
				
				String query = "select contact_no from users where contact_no=? and password=? and user_type=?";
				conn = DBConnection.getConnection();
				
				pst = conn.prepareStatement(query);
				pst.setString(1, username);
				pst.setString(2, password);
				pst.setString(3, "A");
				
				rs = pst.executeQuery();
				
				if(rs.next()) {
					
					User user = new User();
					user.setUserSession(rs.getString("contact_no"));
					new AdminServiceImpl().doOperation();
					break;
					
				}
				else {
					System.out.println("Invalid user credemtials.");
				}
				
				/*if(rs.next()!=false) {*/
				
				
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case 2:
			System.out.println("Program Exited..");
			System.exit(0);
			break;
		default:
			System.out.println("Invalid choice");
	}		this.doAdminLogin();
	}while(choice<=3);
	}
		
}
