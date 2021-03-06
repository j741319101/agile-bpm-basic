package com.dstz.sys.util;

import org.springframework.stereotype.Component;

import com.dstz.base.api.context.ICurrentContext;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;

/**
 * 系统环境信息 用于 base层获取 当前用户、等。不依赖 orgApi
 * 
 * @author Jeff
 */
@Component
public class CurrentContext implements ICurrentContext {
	public CurrentContext() {
	}

	public String getCurrentUserId() {
		return ContextUtil.getCurrentUserId();
	}

	public String getCurrentUserName() {
		IUser user = ContextUtil.getCurrentUser();
		return user != null ? user.getFullname() : null;
	}

	public String getCurrentGroupId() {
		return ContextUtil.getCurrentGroupId();
	}

	public String getCurrentGroupName() {
		IGroup group = ContextUtil.getCurrentGroup();
		return group != null ? group.getGroupName() : null;
	}

}
