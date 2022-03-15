package com.dstz.sys.api.model;


import com.dstz.base.core.model.BaseModel;
import org.hibernate.validator.constraints.NotEmpty;

public class SysConnectRecordDTO extends BaseModel {
    @NotEmpty(
            message = "关联类型不能为空"
    )
    protected String type;
    @NotEmpty(
            message = "关联源头ID不能为空"
    )
    protected String sourceId;
    @NotEmpty(
            message = "关联目标ID不能为空"
    )
    protected String targetId;
    @NotEmpty(
            message = "关联提示信息不能为空"
    )
    protected String notice;

    public SysConnectRecordDTO() {
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
