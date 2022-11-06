package com.ecommerce.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	
	static Connection conn=null;
	
	public static Connection  getConnection() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//System.out.println("Driver Loaded.....");
			
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomdb?useSSL=false","root","root");
			//System.out.println("Connection created successfully....");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
}
