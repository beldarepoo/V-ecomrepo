package com.ecommerce.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import com.ecommerce.DBConnection.DBConnection;
import com.ecommerce.model.Product;

public class EcomUtil {
	
	static Connection conn =null;
	static PreparedStatement pst=null;
	static ResultSet rs=null;
	
	public static boolean isUserExisted(String contactNo) {
		
	String query = "select contact_no from users where contact_no=?";
			
			try {
				
				conn = DBConnection.getConnection();
				pst = conn.prepareStatement(query);
				pst.setString(1, contactNo);
				rs =pst.executeQuery();
				
				if(rs.next()==true) {
					
					return true;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return false;
			
		}
	
	public static long idGenerator() {
		
		Random r = new Random( System.currentTimeMillis() );
	     return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
	}
	
	public static long checkProductQuantity(long productID) {
		
		long pqty = 0;
		try {

			String query ="select quantity from products where id=?";
			conn=DBConnection.getConnection();
			pst=conn.prepareStatement(query);
			pst.setLong(1, productID);
			rs=pst.executeQuery();
			
			while(rs.next()) {
				
				pqty=rs.getInt("quantity");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pqty;
		
	}

public static long getMaxProductID() {
		
		long maxid = 0;
		try {

			String query ="select max(id) as maxid from products";
			conn=DBConnection.getConnection();
			pst=conn.prepareStatement(query);
			rs=pst.executeQuery();
			
			while(rs.next()) {
				
				maxid=rs.getInt("maxid");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return maxid;
		
	}


public static int getTotalCartItem(String contactNo) {
	
	int count = 0;
	try {

		String query ="select count(*) as count from cart_item where user_id=?";
		conn=DBConnection.getConnection();
		pst=conn.prepareStatement(query);
		pst.setString(1, contactNo);
		rs=pst.executeQuery();
		
		while(rs.next()) {
			
			count=rs.getInt("count");
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return count;
	
}

	
}
