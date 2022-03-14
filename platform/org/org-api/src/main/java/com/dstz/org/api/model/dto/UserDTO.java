package com.dstz.org.api.model.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dstz.org.api.model.IUser;
import com.dstz.org.api.model.IUserRole;


/**
 * <pre>
 * 描述：用户表 实体对象
 * </pre>
 */
public class UserDTO implements IUser{

    private static final long serialVersionUID = -700694295167942753L;

    /**	
     * id_
     */
    protected String id;

    /**
     * 姓名
     */
    protected String fullname;

    /**
     * 账号
     */
    protected String account;

    /**
     * 密码
     */
    protected String password;

    /**
     * 邮箱
     */
    protected String email;

    /**
     * 手机号码
     */
    protected String mobile;

    /**
     * 微信号
     */
    protected String weixin;

    /**
     * 创建时间
     */
    protected java.util.Date createTime;

    /**
     * 地址
     */
    protected String address;

    /**
     * 头像
     */
    protected String photo;

    /**
     * 性别：男，女，未知
     */
    protected String sex;

    /**
     * 来源
     */
    protected String from = "system";

    /**
     * 0:禁用，1正常
     */
    protected Integer status;


    /**
     * 组织ID，用于在组织下添加用户。
     */
    protected String groupId = "";
    public static final String FROM_SYSTEM = "system";
    public static final String FROM_HUMAN_RESOURCE = "humanResource";
    protected String telephone;
    protected String openid;
    protected Integer sn;
    protected String orgId;
    protected String orgName;
    protected String orgCode;
    protected String postId;
    protected String postName;
    protected String postCode;
    protected String fristLogin;
    protected List<IUserRole> roles;
    protected List<RelationDTO> orgRelationList;
    protected String type;
    protected List<String> managerGroupIdList;
    
	public void setId(String id) {
        this.id = id;
    }

    /**
     * 返回 id_
     *
     * @return
     */
    public String getId() {
        return this.id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * 返回 姓名
     *
     * @return
     */
    public String getFullname() {
        return this.fullname;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 返回 账号
     *
     * @return
     */
    public String getAccount() {
        return this.account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 返回 密码
     *
     * @return
     */
    public String getPassword() {
        return this.password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 返回 邮箱
     *
     * @return
     */
    public String getEmail() {
        return this.email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 返回 手机号码
     *
     * @return
     */
    public String getMobile() {
        return this.mobile;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    /**
     * 返回 微信号
     *
     * @return
     */
    public String getWeixin() {
        return this.weixin;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 返回 创建时间
     *
     * @return
     */
    public java.util.Date getCreateTime() {
        return this.createTime;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 返回 地址
     *
     * @return
     */
    public String getAddress() {
        return this.address;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * 返回 头像
     *
     * @return
     */
    public String getPhoto() {
        return this.photo;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 返回 性别：男，女，未知
     *
     * @return
     */
    public String getSex() {
        return this.sex;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 返回 来源
     *
     * @return
     */
    public String getFrom() {
        return this.from;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 返回 0:禁用，1正常
     *
     * @return
     */
    public Integer getStatus() {
        return this.status;
    }

    public String getUserId() {
        return this.id;
    }

    public void setUserId(String userId) {
        this.id = userId;

    }

    public void setAttributes(Map<String, String> map) {

    }

    public Map<String, String> getAttributes() {
        return null;
    }

    public String getAttrbuite(String key) {
        return null;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    public UserDTO() {
        this.from = "system";
    }

    public UserDTO(String account) {
        this.account = account;
    }

    public UserDTO fromHumanResource() {
        this.from = "humanResource";
        return this;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOpenid() {
        return this.openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getSn() {
        return this.sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getPostId() {
        return this.postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<IUserRole> getRoles() {
        return this.roles;
    }

    public void setRoles(List<IUserRole> roles) {
        this.roles = roles;
    }

    public List<RelationDTO> getOrgRelationList() {
        return this.orgRelationList;
    }

    public void setOrgRelationList(List<RelationDTO> orgRelationList) {
        this.orgRelationList = orgRelationList;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPostName() {
        return this.postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getManagerGroupIdList() {
        return this.managerGroupIdList;
    }

    public void setManagerGroupIdList(List<String> managerGroupIdList) {
        this.managerGroupIdList = managerGroupIdList;
    }

    public String getFristLogin() {
        return this.fristLogin;
    }

    public void setFristLogin(String fristLogin) {
        this.fristLogin = fristLogin;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}