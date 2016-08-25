package com.zale.shortlink.exception;

/**
 * 
 * @author Zale
 *
 */
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -2982727599045489500L;

	public ResourceNotFoundException(String message) {
        super(message);
    }

}
