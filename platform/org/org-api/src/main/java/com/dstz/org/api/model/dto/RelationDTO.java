package com.dstz.org.api.model.dto;

import java.util.Date;

public class RelationDTO {
    private static final long serialVersionUID = -700694295167942753L;
    protected String groupId;
    protected String userId;
    protected Integer isMaster;
    protected Integer status;
    protected String type;
    protected String hasChild;
    protected String groupCode;
    protected String groupName;
    protected String userName;
    protected String userAccount;
    protected String roleName;
    protected String roleAlias;
    protected String photo;
    protected String sex;
    protected String isMasters;
    protected String mobile;
    protected String sn;
    protected String oldGroupId;
    protected String roleId;
    protected String postId;
    protected String postCode;
    protected String postName;
    protected String unitId;
    protected String unitName;
    protected Integer userStatus;
    protected Integer userActiveStatus;
    protected Date userCreateTime;
    protected String parentOrgName;

    public RelationDTO() {
    }

    public RelationDTO(String groupId, String userId, String type) {
        this.groupId = groupId;
        this.userId = userId;
        this.type = type;
    }

    public String getUnitId() {
        return this.unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setIsMaster(Integer isMaster) {
        this.isMaster = isMaster;
    }

    public Integer getIsMaster() {
        return this.isMaster;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserName() {
        return this.userName;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getRoleAlias() {
        return this.roleAlias;
    }

    public void setRoleAlias(String roleAlias) {
        this.roleAlias = roleAlias;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public String getUserAccount() {
        return this.userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getIsMasters() {
        return this.isMasters;
    }

    public void setIsMasters(String isMasters) {
        this.isMasters = isMasters;
    }

    public String getOldGroupId() {
        return this.oldGroupId;
    }

    public void setOldGroupId(String oldGroupId) {
        this.oldGroupId = oldGroupId;
    }

    public String getPostId() {
        return this.postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return this.postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Integer getUserStatus() {
        return this.userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getUserActiveStatus() {
        return this.userActiveStatus;
    }

    public void setUserActiveStatus(Integer userActiveStatus) {
        this.userActiveStatus = userActiveStatus;
    }

    public Date getUserCreateTime() {
        return this.userCreateTime;
    }

    public void setUserCreateTime(Date userCreateTime) {
        this.userCreateTime = userCreateTime;
    }

    public String getHasChild() {
        return this.hasChild;
    }

    public void setHasChild(String hasChild) {
        this.hasChild = hasChild;
    }

    public String getParentOrgName() {
        return this.parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public String getGroupCode() {
        return this.groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
