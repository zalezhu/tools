package com.zale.shortlink.exception;

/**
 * 
 * @author Zale
 *
 */
public class NoPermissionException extends BaseRuntimeException{
	private static final long serialVersionUID = 9175120163269832284L;

	public NoPermissionException(String message){
		super(ExceptionCode.NoPermissionException, message);
	}
}
