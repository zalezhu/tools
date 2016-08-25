package com.zale.shortlink.exception;

/**
 * @author luwei
 *
 */
public class RecordNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6374339786082466324L;
	
	public RecordNotFoundException(String message){
		super(message);
	}

}
