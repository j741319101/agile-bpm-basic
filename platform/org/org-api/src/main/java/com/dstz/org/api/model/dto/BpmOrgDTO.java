package com.dstz.org.api.model.dto;


public class BpmOrgDTO {
    private static final long serialVersionUID = -700694295167942753L;
    String orgId;
    String orgName;
    String orgType;
    String orgCode;
    Integer orgSn;
    String parentOrgId;
    String parentOrgName;
    String parentOrgType;
    String parentOrgCode;
    Integer parentOrgSn;

    public BpmOrgDTO() {
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgType() {
        return this.orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getOrgSn() {
        return this.orgSn;
    }

    public void setOrgSn(Integer orgSn) {
        this.orgSn = orgSn;
    }

    public String getParentOrgId() {
        return this.parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getParentOrgName() {
        return this.parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public String getParentOrgType() {
        return this.parentOrgType;
    }

    public void setParentOrgType(String parentOrgType) {
        this.parentOrgType = parentOrgType;
    }

    public String getParentOrgCode() {
        return this.parentOrgCode;
    }

    public void setParentOrgCode(String parentOrgCode) {
        this.parentOrgCode = parentOrgCode;
    }

    public Integer getParentOrgSn() {
        return this.parentOrgSn;
    }

    public void setParentOrgSn(Integer parentOrgSn) {
        this.parentOrgSn = parentOrgSn;
    }
}

