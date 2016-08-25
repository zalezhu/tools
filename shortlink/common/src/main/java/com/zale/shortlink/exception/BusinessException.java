package com.zale.shortlink.exception;

/**
 * 
 * @author Zale
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 2892641419700853889L;

	public BusinessException(String message){
        super(message);
    }

}



