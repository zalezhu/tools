package com.zale.shortlink.exception;

/**
 * 
 * @author Zale
 *
 */
public class ErrorArgument {
    private String field;
    private Object value;
    private String message;

    public ErrorArgument() { }

    public ErrorArgument(String field, Object value, String message) {
        this.field = field;
        this.value = value;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
