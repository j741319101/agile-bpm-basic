package com.dstz.org.core.model;


import com.dstz.base.core.model.BaseModel;
import com.dstz.org.api.constant.GroupTypeConstant;
import com.dstz.org.api.model.IGroup;

public class Post extends BaseModel implements IGroup {
    private static final long serialVersionUID = -700694295167942753L;
    protected String name;
    protected String code;
    protected String type;
    protected String desc;
    protected Integer isCivilServant;
    protected String level;
    protected String orgId;
    protected Integer userNum;

    public Post() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getIsCivilServant() {
        return this.isCivilServant;
    }

    public void setIsCivilServant(Integer isCivilServant) {
        this.isCivilServant = isCivilServant;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGroupId() {
        return this.id;
    }

    public String getGroupName() {
        return this.name;
    }

    public String getGroupCode() {
        return this.code;
    }

    public String getGroupType() {
        return GroupTypeConstant.POST.key();
    }

    public Integer getGroupLevel() {
        return null;
    }

    public String getParentId() {
        return null;
    }

    public Integer getSn() {
        return null;
    }

    public String getPath() {
        return null;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Integer getUserNum() {
        return this.userNum;
    }

    public String getSimple() {
        return null;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }
}