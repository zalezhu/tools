package com.zale.shortlink.exception;
/**
 * 
 * @author Zale
 *
 */
public class BaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -8506674167793694961L;
	private int errorCode;

	public BaseRuntimeException(){
		
	}
	public BaseRuntimeException( String message){
		super(message);
	}
	public BaseRuntimeException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
