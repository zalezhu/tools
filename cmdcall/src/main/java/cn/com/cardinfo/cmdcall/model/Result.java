package cn.com.cardinfo.cmdcall.model;

import java.io.Serializable;

/**
 * Created by Zale on 16/8/20.
 */
public class Result<T> implements Serializable{
    private static final long serialVersionUID = -4124758062575957532L;
    private boolean hasError;
    private String error;
    private T obj;

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
