package com.zale.shortlink.exception;

/**
 * 
 * @author Zale
 *
 */
public class NotSupportException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5237912446641917664L;

	public NotSupportException(String message){
		super(message);
	}
}
