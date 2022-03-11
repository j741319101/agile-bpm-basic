package com.dstz.sys.api.model;

import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

/**
 * <pre>
 * 描述：默认的SysIdentity
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2019年5月26日
 * 版权:summer
 * </pre>
 */
public class DefaultIdentity implements SysIdentity<DefaultIdentity> {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String type;
	private Integer sn = -1;
	private static Integer order = 0;
	private String orgId;
	private String clazz = "DefaultIdentity";
	private String compareOrgId;

	public DefaultIdentity() {

	}

	/**
	 * 
	 * 创建一个新的实例 DefaultDefaultIdentity.
	 * 
	 * @param id
	 * @param name
	 * @param type
	 */
	public DefaultIdentity(String id, String name, String type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public DefaultIdentity(String id, String name, String type, String orgId) {
		this.id = id;
		this.name = name;
		this.type = this.getSysIdentityType(type);
		this.orgId = orgId;
	}

	public DefaultIdentity(String id, String name, String type, String orgId, Integer sn) {
		this.id = id;
		this.name = name;
		this.type = this.getSysIdentityType(type);
		this.sn = sn;
		this.orgId = orgId;
	}


	public DefaultIdentity(IGroup group) {
		this.id = group.getGroupId();
		this.name = group.getGroupName();
		this.type = group.getGroupType();
		this.sn = group.getSn() == null ? -1 : group.getSn();
		this.orgId = group.getGroupId();
	}
	public DefaultIdentity(IUser user) {
		this.id = user.getUserId();
		this.name = user.getFullname();
		this.type = TYPE_USER;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}


	public void setType(String type) {
		if (StringUtils.indexOf("user,role,group,org,post", type) == -1) {
			throw new BusinessException("候选人类型错误，检查候选人类型");
		} else {
			this.type = type;
		}
	}

	@Override
	public void setOrgId(String orgId) {
		this.orgId=orgId;
	}

	@Override
	public String getOrgId() {
		return null;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode() + this.type.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SysIdentity)) {
			return false;
		} else if (!StringUtil.isEmpty(this.id) && !StringUtil.isEmpty(this.name)) {
			SysIdentity identity = (SysIdentity)obj;
			return this.type.equals(identity.getType()) && this.id.equals(identity.getId()) && this.orgId.equals(identity.getOrgId());
		} else {
			return false;
		}
	}

	public String toString() {
		return "DefaultIdentity{id='" + this.id + '\'' + ", name='" + this.name + '\'' + ", type='" + this.type + '\'' + ", sn=" + this.sn + ", orgId=" + this.orgId + '}';
	}


	public String getCompareOrgId() {
		return this.compareOrgId;
	}

	public void setCompareOrgId(String compareOrgId) {
		this.compareOrgId = compareOrgId;
	}

	public Integer getSn() {
		return this.sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public int compareTo(DefaultIdentity identity) {
		if (order == 0) {
			Environment environment = (Environment) AppUtil.getImplInstanceArray(Environment.class).get(0);
			String orderStr = environment.getProperty("ecloud.identity.order");
			if (StringUtils.equals(orderStr, "desc")) {
				order = -1;
			} else {
				order = 1;
			}
		}

		if (this.sn != null && identity.sn != null) {
			if (this.getSn() == identity.getSn()) {
				return 0;
			} else {
				return this.getSn() > identity.getSn() ? order : -order;
			}
		} else {
			return 0;
		}
	}

	public String getClazz() {
		return this.clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	private String getSysIdentityType(String type) {
		byte var3 = -1;
		switch(type.hashCode()) {
			case 110308:
				if (type.equals("org")) {
					var3 = 3;
				}
				break;
			case 3446944:
				if (type.equals("post")) {
					var3 = 2;
				}
				break;
			case 3506294:
				if (type.equals("role")) {
					var3 = 1;
				}
				break;
			case 3599307:
				if (type.equals("user")) {
					var3 = 0;
				}
		}

		switch(var3) {
			case 0:
				return "user";
			case 1:
				return "role";
			case 2:
				return "post";
			case 3:
				return "org";
			default:
				return "user";
		}
	}
}
