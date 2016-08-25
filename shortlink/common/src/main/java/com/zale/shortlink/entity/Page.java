package com.zale.shortlink.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @包名称：com.bcar.entity.dto
 * @创建人：YYX
 * @创建时间：2015/6/17 20:06
 */
public class Page<T> implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -9144490274090268099L;

	List<T> result;

    private Integer page;
    private Integer pageSize;
    private Integer total;

    public Page() {
        super();
    }

    public Page(Integer total) {
        super();
        this.total = total;
    }

    public Page(Integer page, Integer pageSize, Integer total) {
        super();
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
    }


    public Page(Integer page, Integer pageSize, List<T> result) {
        super();
        this.page = page;
        this.pageSize = pageSize;
        this.total = null == result ? 0 : result.size();
        this.result = result;
    }

    public Page(Integer page, Integer pageSize) {
        super();
        this.page = page;
        this.pageSize = pageSize;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
