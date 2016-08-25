package com.zale.shortlink.exception;

import org.apache.commons.lang.BooleanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Zale
 *
 */
public class ArgumentInvalidException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -815501326491898031L;
	
	private List<ErrorArgument> errorArguments;

    public ArgumentInvalidException() { }

    public ArgumentInvalidException(ErrorArgument argument) {
        addErrorArgument(argument);
    }

    public ArgumentInvalidException(String field, Object value, String message) {
        addErrorArgument(field, value, message);
    }

    public ArgumentInvalidException addErrorArgument(String field, Object value, String message) {
        return addErrorArgument(new ErrorArgument(field, value, message));
    }

    public ArgumentInvalidException addErrorArgument(ErrorArgument argument) {
        if(argument == null || argument.getField() == null) return this;
        if(getErrorArguments() == null) {
            this.errorArguments = new ArrayList<>();
        }
        getErrorArguments().add(argument);
        return this;
    }

    @Override
  	public String getMessage() {
      	if(this.getErrorArguments().size() > 0) {
      		return this.getErrorArguments().get(0).getMessage();
      	}
  		return "";
  	}
    
    public boolean hasErrors() {
        return getErrorArguments() != null && getErrorArguments().size() > 0;
    }

    public List<ErrorArgument> getErrorArguments() {
        return errorArguments;
    }


    // modify by Yan lun

    public ArgumentInvalidException addErrorArgument(String field, Object value, Boolean validateResult ,String message) {
    	if(BooleanUtils.isTrue(validateResult)){
    		return addErrorArgument(new ErrorArgument(field, value, message));
    	}
    	return this;
    }
    
    public void throwExceptionCheck(){
    	if(hasErrors()){ throw this; }
    }
    
}
