package com.dstz.org.api.model.dto;

public class PageDTO {
    private static final long serialVersionUID = -700694295167942753L;
    protected String offset;
    protected String limit;
    protected String noPage;
    protected String order;
    protected String sort;

    PageDTO() {
    }

    public PageDTO(String offset, String limit, String noPage, String order, String sort) {
        this.offset = offset;
        this.limit = limit;
        this.noPage = noPage;
        this.order = order;
        this.sort = sort;
    }

    public String getOffset() {
        return this.offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLimit() {
        return this.limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getNoPage() {
        return this.noPage;
    }

    public void setNoPage(String noPage) {
        this.noPage = noPage;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return this.sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
