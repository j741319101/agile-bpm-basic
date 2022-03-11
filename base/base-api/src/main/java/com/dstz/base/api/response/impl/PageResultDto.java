package com.dstz.base.api.response.impl;


import java.util.List;

public class PageResultDto<T> extends BaseResult {
    private static final long serialVersionUID = 1L;
    private Integer pageSize = 0;
    private Integer page = 1;
    private Integer total = 0;
    private List rows = null;

    public PageResultDto() {
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getRows() {
        return this.rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
