package com.dstz.org.core.model;
import java.util.Date;
import java.math.BigDecimal;

import com.dstz.base.core.model.BaseModel;


/**
 * 用户组织关系 实体对象
 * @author Jeff
 * @email for_office@qq.com
 * @time 2018-12-16 01:07:59
 */
public class OrgRelation extends BaseModel{
	/**
	* 组ID
	*/
	protected  String groupId; 
	/**
	* 用户ID
	*/
	protected  String userId; 
	/**
	* 0:默认组织，1：从组织
	*/
	protected  Integer isMaster = 0; 
	
	/**
	* 0:默认组织，1：从组织
	*/
	protected  Integer status = 1; 
	
	/**
	* 角色ID
	*/
	protected  String roleId; 
	/**
	* 类型：groupUser,groupRole,userRole,groupUserRole
	*/
	protected  String type; 
	
	
	/**
	 * 前端字段
	 */
	protected String groupName;
	protected String userName;
	protected String userAccount;
	protected String roleName;
	protected String roleAlias;
	protected String hasChild;
	protected String photo;
	protected String sex;
	protected String isMasters;
	protected String mobile;
	protected String sn;
	protected String oldGroupId;
	protected String postId;
	protected String postName;
	protected String unitId;
	protected String unitName;
	protected Integer userStatus;
	protected Integer userActiveStatus;
	protected Date userCreateTime;
	protected String parentOrgName;
	/**
	 * 岗位使用的时候调用
	 * @return
	 */
	public String getPostName() {
		return String.format("%s-%s", this.getGroupName(),this.getRoleName());
	}
	/**
	 * post ID
	 * @return
	 */
	public String getPostId() {
		return String.format("%s-%s", this.getGroupId(),this.getRoleId());
	}


	public void setGroupId( String groupId) {
		this.groupId = groupId;
	}
	

	public OrgRelation() {
		 
	}
	
	public OrgRelation(String groupId, String userId, String roleId, String type) {
		super();
		this.groupId = groupId;
		this.userId = userId;
		this.roleId = roleId;
		this.type = type;
	}

	/**
	 * 返回 组ID
	 * @return
	 */
	public  String getGroupId() {
		return this.groupId;
	}
	
	
	
	
	public void setUserId( String userId) {
		this.userId = userId;
	}
	
	/**
	 * 返回 用户ID
	 * @return
	 */
	public  String getUserId() {
		return this.userId;
	}
	
	
	
	
	public void setIsMaster( Integer isMaster) {
		this.isMaster = isMaster;
	}
	
	/**
	 * 返回 0:默认组织，1：从组织
	 * @return
	 */
	public  Integer getIsMaster() {
		return this.isMaster;
	}
	
	
	
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getUserName() {
		return userName;
	}

	public Integer getStatus() {
		return status;
	}

	public String getRoleAlias() {
		return roleAlias;
	}


	public void setRoleAlias(String roleAlias) {
		this.roleAlias = roleAlias;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setRoleId( String roleId) {
		this.roleId = roleId;
	}
	
	/**
	 * 返回 角色ID
	 * @return
	 */
	public  String getRoleId() {
		return this.roleId;
	}
	
	
	
	
	public void setType( String type) {
		this.type = type;
	}
	
	/**
	 * 返回 类型：groupUser,groupRole,userRole,groupUserRole
	 * @return
	 */
	public  String getType() {
		return this.type;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}




	public OrgRelation(String groupId, String userId, String type) {
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

}