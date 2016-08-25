package com.zale.shortlink.exception;

/**
 * 
 * @author Zale
 *
 */
public class RequestFailureException extends BaseRuntimeException {
	private static final long serialVersionUID = -4151691300664588624L;

	public RequestFailureException(String message) {
		super(message);
	}
}
