package com.ecommerce.exception;

public class EcommerceException extends RuntimeException {
	
	String message;
	
	public EcommerceException(String message) {
		
		super(message);
		
	}

}
