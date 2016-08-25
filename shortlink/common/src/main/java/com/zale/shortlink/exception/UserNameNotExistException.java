package com.zale.shortlink.exception;

/**
 * 
 * @author Zale
 *
 */
public class UserNameNotExistException extends RuntimeException{
	private static final long serialVersionUID = -5859332024702774926L;

	public UserNameNotExistException(String msg) {
		super(msg);
	}

}
