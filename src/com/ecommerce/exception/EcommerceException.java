package com.ecommerce.exception;

public class EcommerceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;

	public EcommerceException(String message) {

		super(message);

	}

}
