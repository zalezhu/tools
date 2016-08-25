package com.zale.shortlink.exception;

/**
 * 
 * @author Zale
 *
 */
public class HttpClientException extends RuntimeException{
	private static final long serialVersionUID = -6392735144015814801L;

	public HttpClientException(String message){
		super(message);
	}

	public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
