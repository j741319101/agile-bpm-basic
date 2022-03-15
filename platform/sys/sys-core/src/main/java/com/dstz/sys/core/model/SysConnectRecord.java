package com.dstz.sys.core.model;


import com.dstz.base.core.model.BaseModel;

public class SysConnectRecord extends BaseModel {
    protected String type;
    protected String sourceId;
    protected String targetId;
    protected String notice;

    public SysConnectRecord() {
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceId() {
        return this.sourceId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetId() {
        return this.targetId;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getNotice() {
        return this.notice;
    }
}