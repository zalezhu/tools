package cn.com.cardinfo.cmdcall.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Zale on 16/8/20.
 */
public class Result<T> implements Serializable{
    private static final long serialVersionUID = -4124758062575957532L;
    private boolean hasError;
    private String error;
    private T obj;
    private Date startTime;
    private Date endTime;
    private String exeBy;

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getExeBy() {
        return exeBy;
    }

    public void setExeBy(String exeBy) {
        this.exeBy = exeBy;
    }
}
