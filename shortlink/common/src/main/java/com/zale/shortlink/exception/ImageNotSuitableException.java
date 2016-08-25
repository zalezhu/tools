package com.zale.shortlink.exception;

/**
 * @author Zale
 *
 */
public class ImageNotSuitableException  extends RuntimeException{
	private static final long serialVersionUID = 4152571041195909332L;
	public ImageNotSuitableException(String message){
		super(message);
	}
}
