package com.dstz.org.core.manager.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.dstz.base.api.constant.EventEnum;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.core.event.CommonEvent;
import com.dstz.base.core.util.AppUtil;
import com.dstz.org.api.constant.UserTypeConstant;
import com.dstz.org.core.manager.GroupManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dstz.base.api.constant.StringConstants;
import com.dstz.base.core.cache.ICache;
import com.dstz.base.core.encrypt.EncryptUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.org.api.context.ICurrentContext;
import com.dstz.org.core.constant.RelationTypeConstant;
import com.dstz.org.core.dao.UserDao;
import com.dstz.org.core.manager.OrgRelationManager;
import com.dstz.org.core.manager.UserManager;
import com.dstz.org.core.model.OrgRelation;
import com.dstz.org.core.model.User;

import cn.hutool.core.collection.CollectionUtil;

/**
 * <pre>
 * 描述：用户表 处理实现类
 * </pre>
 */
@Service("userManager")
public class UserManagerImpl extends BaseManager<String, User> implements UserManager {

	public static final String LOGIN_USER_CACHE_KEY = "agilebpm:loginUser:";
	@Resource
	GroupManager groupManager;
	@Autowired
	ICurrentContext currentContext;
	@Resource
    UserDao userDao;
    @Resource
    OrgRelationManager orgRelationMananger;
    @Autowired
    ICache icache;

    public User getByAccount(String account) {
        return this.userDao.getByAccount(account);
    }

    @Override
    public boolean isUserExist(User user) {
        return userDao.isUserExist(user)>0;
    }

	@Override
	public List<User> getUserListByRelation(String relId, String type) {
		if(type.equals(RelationTypeConstant.POST_USER.getKey())) {
			String [] postId = relId.split(StringConstants.DASH);
			if(postId.length != 2) {
				return Collections.emptyList();
			}
			return userDao.getUserListByPost(postId[1],postId[0]);
		}
		
		return userDao.getUserListByRelation(relId,type);
	}

	/**
	 * 通过ID 获取会带上用户关系信息
	 */
	@Override
	public User get(String entityId) {
		
		User user =  super.get(entityId);
		if(user!= null) {
			user.setOrgRelationList(orgRelationMananger.getUserRelation(entityId, null));
		}
		return user;
	}
	
	@Override
	public void remove(String entityId) {
		orgRelationMananger.removeByUserId(entityId);
		super.remove(entityId);
	}

	/*@Override
	public void saveUserInfo(User user) {
		if(StringUtil.isEmpty(user.getId())) {
			if(StringUtil.isEmpty(user.getPassword())) {
				user.setPassword("1");
			}
            user.setPassword(EncryptUtil.encryptSha256(user.getPassword()));
			this.create(user);
		}else {
			this.update(user);
			orgRelationMananger.removeByUserId(user.getId());
		}
		
		List<OrgRelation> orgRelationList = user.getOrgRelationList();
		if(CollectionUtil.isEmpty(orgRelationList)) return;
		
		orgRelationList.forEach( rel ->{
			rel.setUserId(user.getId());
			orgRelationMananger.create(rel);
		});
		 
		//删除组织缓存
	    icache.delByKey(ICurrentContext.CURRENT_ORG .concat(user.getId()));
	    icache.delByKey(LOGIN_USER_CACHE_KEY.concat(user.getAccount()));
	}*/



	public UserManagerImpl() {
	}




	public void saveUserInfo(User user) {
		List<OrgRelation> orgRelationList = user.getOrgRelationList();
		if (StringUtil.isEmpty(user.getId())) {
			String account = user.getAccount();
			String email = user.getEmail();
			String phone = user.getMobile();
			if (StringUtils.isNotEmpty(account) && null != this.getByAccount(account)) {
				throw new BusinessMessage(String.format("账号:%s已存在", account));
			}

			if (StringUtil.isEmpty(user.getPassword())) {
				user.setPassword("111111");
			}

			user.setPassword(EncryptUtil.encryptSha256(user.getPassword()));
//			user.setPassword(SM3Util.SM3EncodePws(user.getPassword()));
			user.setStatus(0);
			user.setActiveStatus(0);
			if (StringUtils.isEmpty(user.getType())) {
				user.setType(UserTypeConstant.NORMAL.key());
			}

			if (UserTypeConstant.MANAGER.key().equals(user.getType())) {
				user.setStatus(1);
				user.setActiveStatus(1);
			}

			if (null == user.getSn()) {
				user.setSn(0);
			}

			this.create(user);
		} else {
			user.setAccount((String)null);
			if (StringUtil.isNotEmpty(user.getPassword())) {
				user.setPassword(EncryptUtil.encryptSha256(user.getPassword()));
				user.setPassword(user.getPassword());
			}
			user.setUpdateBy(this.currentContext.getCurrentUserId());
			user.setUpdateTime(new Date());
			this.updateByPrimaryKeySelective(user);
			if (!CollectionUtil.isEmpty(orgRelationList)) {
				List<String> relationTypes = new ArrayList();
				relationTypes.add(RelationTypeConstant.GROUP_USER.getKey());
				this.orgRelationMananger.removeByUserId(user.getId(), relationTypes);
			}
		}
		if (!CollectionUtil.isEmpty(orgRelationList)) {
			orgRelationList.forEach((rel) -> {
				if (RelationTypeConstant.GROUP_USER.getKey().equals(rel.getType())) {
					rel.setUserId(user.getId());
					this.orgRelationMananger.create(rel);
				}
			});
		}

		//删除组织缓存
		this.clearUserCache(user);
	}

	public void create(User entity) {
		AppUtil.publishEvent(new CommonEvent(EventEnum.ADD_USER));
		this.dao.create(entity);
	}

	public List<User> getUserListByGroupPath(String path) {
		return this.userDao.getUserListByGroupPath(path + "%");
	}

	public void removeOutSystemUser() {
		this.userDao.removeOutSystemUser();
	}

	public User getByOpneid(String openid) {
		return this.userDao.getByOpenid(openid);
	}

	public int updateByPrimaryKeySelective(User record) {
		User entity = this.get(record.getId());
		this.clearUserCache(entity);
		return this.userDao.updateByPrimaryKeySelective(record);
	}

	public String getIdByAccount(String account) {
		return this.userDao.getIdByAccount(account);
	}

	public void update(User entity) {
		super.update(entity);
		this.clearUserCache(entity);
	}

	public Integer getAllEnableUserNum() {
		return this.userDao.getAllEnableUserNum();
	}

	public List<User> getUsersByOrgPath(String orgPath) {
		if (StringUtils.isNotEmpty(orgPath)) {
			orgPath = orgPath.concat("%");
		}

		return this.userDao.getUsersByOrgPath(orgPath);
	}

	public void clearUserCache(User entity) {
		this.icache.delByKey(LOGIN_USER_CACHE_KEY.concat(entity.getAccount()));
		this.icache.delByKey("current_org".concat(entity.getId()));
	}

}
