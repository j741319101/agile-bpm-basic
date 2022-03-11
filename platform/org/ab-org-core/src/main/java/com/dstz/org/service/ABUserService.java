package com.dstz.org.service;


import java.util.*;

import javax.annotation.Resource;

import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.api.query.Direction;
import com.dstz.base.api.query.FieldSort;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.api.response.impl.PageResultDto;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.db.model.query.DefaultFieldSort;
import com.dstz.base.db.model.query.DefaultPage;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.org.api.context.ICurrentContext;
import com.dstz.org.api.model.dto.BpmUserDTO;
import com.dstz.org.api.model.dto.UserQueryDTO;
import com.dstz.org.core.manager.*;
import com.dstz.org.core.model.Group;
import com.dstz.org.core.model.Post;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.BeanCopierUtils;
import com.dstz.org.api.constant.GroupTypeConstant;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.model.IUserRole;
import com.dstz.org.api.model.dto.UserDTO;
import com.dstz.org.api.model.dto.UserRoleDTO;
import com.dstz.org.api.service.UserService;
import com.dstz.org.core.constant.RelationTypeConstant;
import com.dstz.org.core.model.OrgRelation;
import com.dstz.org.core.model.User;

import cn.hutool.core.collection.CollectionUtil;


@SuppressWarnings("unchecked")
@Service(value = "userService")
public class ABUserService implements UserService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	PostManager postManager;
	@Resource
	ICurrentContext iCurrentContext;
	@Resource
	BpmUserManager bpmUserManager;
    @Resource
    UserManager userManager;
    @Resource
    GroupManager groupManager;
    @Resource
    OrgRelationManager orgRelationMananger;
    

    @Override
    public IUser getUserById(String userId) {
    	IUser user = userManager.get(userId);
        return BeanCopierUtils.transformBean(user, UserDTO.class);
    }

    @Override
    public IUser getUserByAccount(String account) {
    	IUser user =  userManager.getByAccount(account);
    	return BeanCopierUtils.transformBean(user, UserDTO.class);
    }

//    @Override
//    public List<IUser> getUserListByGroup(String groupType, String groupId) {
//        //此处可以根据不同的groupType去调用真实的实现：如角色下的人，组织下的人
//    	RelationTypeConstant relationType = RelationTypeConstant.getUserRelationTypeByGroupType(groupType);
//    	if(relationType == null) {
//    		throw new BusinessException(groupType + "查找不到对应用户的类型！");
//    	}
//
//    	List<User> userList  = userManager.getUserListByRelation(groupId,relationType.getKey());
//
//        if(CollectionUtil.isNotEmpty(userList)) {
//        	return (List)BeanCopierUtils.transformList(userList, UserDTO.class);
//        }
//
//        return Collections.emptyList();
//    }

	public ABUserService() {
	}

	public UserDTO getUserDetailInfo(User user) {
		UserDTO userDTO = (UserDTO)BeanCopierUtils.transformBean(user, UserDTO.class);
		if (null != user) {
			user.getOrgRelationList().stream().filter((orgRelation) -> {
				return RelationTypeConstant.POST_USER.getKey().equals(orgRelation.getType()) && null != orgRelation.getIsMaster() && 1 == orgRelation.getIsMaster();
			}).findFirst().ifPresent((postRelation) -> {
				userDTO.setPostId(postRelation.getPostId());
				userDTO.setPostName(postRelation.getPostName());
				Post post = (Post)this.postManager.get(postRelation.getGroupId());
				if (null != post) {
					userDTO.setPostCode(post.getCode());
				}

			});
			user.getOrgRelationList().stream().filter((orgRelation) -> {
				return RelationTypeConstant.GROUP_USER.getKey().equals(orgRelation.getType()) && null != orgRelation.getIsMaster() && 1 == orgRelation.getIsMaster();
			}).findFirst().ifPresent((orgRelation) -> {
				userDTO.setOrgId(orgRelation.getGroupId());
				userDTO.setOrgName(orgRelation.getGroupName());
				Group group = (Group)this.groupManager.get(orgRelation.getGroupId());
				if (null != group) {
					userDTO.setOrgCode(group.getCode());
				}

			});
			List<OrgRelation> lstOrgRelation = this.orgRelationMananger.getUserRole(user.getUserId());
			List<IUserRole> lstUserRoleDTO = new ArrayList();
			lstOrgRelation.forEach((temp) -> {
				lstUserRoleDTO.add(new UserRoleDTO(temp.getRoleId(), temp.getRoleName(), temp.getRoleAlias()));
			});
			userDTO.setRoles(lstUserRoleDTO);
		}

		return userDTO;
	}

	public List<? extends IUser> getUserListByGroup(String groupType, String groupId) {
		RelationTypeConstant relationType = RelationTypeConstant.getUserRelationTypeByGroupType(groupType);
		if (relationType == null) {
			throw new BusinessException(groupType + "查找不到对应用户的类型！");
		} else {
			List<User> userList = null;
			if (RelationTypeConstant.GROUP_USER.getKey().equals(relationType.getKey())) {
				userList = this.bpmUserManager.getUserListByOrgId(groupId);
			} else if (RelationTypeConstant.POST_USER.getKey().equals(relationType.getKey())) {
				userList = this.bpmUserManager.getUserListByPostId(groupId);
			} else {
				userList = this.bpmUserManager.getUserListByRelation(groupId, relationType.getKey());
			}

			return CollectionUtil.isNotEmpty(userList) ? BeanCopierUtils.transformList(userList, UserDTO.class) : Collections.emptyList();
		}
	}

	public List<? extends IUserRole> getUserRole(String userId) {
		List<OrgRelation> orgRelationList = this.orgRelationMananger.getUserRole(userId);
		List<UserRoleDTO> userRoleList = new ArrayList();
		Iterator var4 = orgRelationList.iterator();

		while(var4.hasNext()) {
			OrgRelation orgRelation = (OrgRelation)var4.next();
			UserRoleDTO userRole = new UserRoleDTO(orgRelation.getRoleId(), orgRelation.getUserId(), orgRelation.getUserName(), orgRelation.getRoleName());
			userRole.setAlias(orgRelation.getRoleAlias());
			userRoleList.add(userRole);
		}

		return userRoleList;
	}

	public List<? extends IUser> getAllUser() {
		QueryFilter query = new DefaultQueryFilter(true);
		query.addFilter("tuser.status_", 1, QueryOP.EQUAL);
		List<User> userList = this.userManager.query(query);
		return BeanCopierUtils.transformList(userList, UserDTO.class);
	}

	public List<? extends IUser> getUsersByUsername(String username) {
		QueryFilter query = new DefaultQueryFilter(true);
		query.addFilter("tuser.status_", 1, QueryOP.EQUAL);
		if (StringUtils.isNotEmpty(username)) {
			query.addFilter("tuser.fullname_", username, QueryOP.LIKE);
		}

		List<User> userList = this.userManager.query(query);
		return BeanCopierUtils.transformList(userList, UserDTO.class);
	}

	public List<? extends IUser> getUserListByGroupPath(String path) {
		List<User> userList = this.userManager.getUserListByGroupPath(path);
		return BeanCopierUtils.transformList(userList, UserDTO.class);
	}

	public IUser getUserByOpenid(String openid) {
		IUser user = this.userManager.getByOpneid(openid);
		return (IUser)BeanCopierUtils.transformBean(user, UserDTO.class);
	}

	public void updateUserOpenId(String account, String openid) {
		User user = this.userManager.getByAccount(account);
		if (user == null) {
			throw new BusinessMessage("账户不存在:" + account);
		} else {
			user.setOpenid(openid);
			this.userManager.update(user);
		}
	}

	public Map<String, ? extends IUser> getUserByIds(String userIds) {
		QueryFilter filter = new DefaultQueryFilter(true);
		filter.addFilter("tuser.id_", userIds, QueryOP.IN);
		List<User> lstUser = this.userManager.query(filter);
		Map<String, IUser> mapUser = new HashMap();
		lstUser.forEach((user) -> {
			IUser var10000 = (IUser)mapUser.put(user.getId(), BeanCopierUtils.transformBean(user, UserDTO.class));
		});
		return mapUser;
	}

	public List<? extends IUser> getUsersByOrgIds(String groupIds) {
		QueryFilter filter = new DefaultQueryFilter(true);
		Map<String, Object> params = new HashMap();
		params.put("orgIds", groupIds.split(","));
		filter.addParams(params);
		filter.addFilter("tuser.status_", "1", QueryOP.EQUAL);
		List<User> lstUser = this.userManager.query(filter);
		return BeanCopierUtils.transformList(lstUser, UserDTO.class);
	}

	public List<? extends IUser> getUsersByRoleIds(String groupIds) {
		QueryFilter filter = new DefaultQueryFilter(true);
		Map<String, Object> params = new HashMap();
		params.put("roleIds", groupIds.split(","));
		filter.addFilter("tuser.status_", "1", QueryOP.EQUAL);
		filter.addParams(params);
		List<User> lstUser = this.userManager.query(filter);
		return BeanCopierUtils.transformList(lstUser, UserDTO.class);
	}

	public List<? extends IUser> getUsersByPostIds(String groupIds) {
		QueryFilter filter = new DefaultQueryFilter(true);
		Map<String, Object> params = new HashMap();
		params.put("postIds", groupIds.split(","));
		filter.addFilter("tuser.status_", "1", QueryOP.EQUAL);
		filter.addParams(params);
		List<User> lstUser = this.bpmUserManager.query(filter);
		return BeanCopierUtils.transformList(lstUser, UserDTO.class);
	}

	public Integer countEnabledUser() {
		return this.userManager.getAllEnableUserNum();
	}

	public List<? extends IUser> getUsersByUserName(String userName, String postId, Integer offset, Integer limit) {
		QueryFilter filter = new DefaultQueryFilter();
		Map<String, Object> params = new HashMap();
		params.put("postId", postId);
		filter.addParams(params);
		if (StringUtils.isNotEmpty(userName)) {
			filter.addFilter("tuser.fullname_", userName, QueryOP.LEFT_LIKE);
		}

		RowBounds rowBounds = new RowBounds(offset, limit);
		DefaultPage page = new DefaultPage(rowBounds);
		filter.setPage(page);
		List<User> lstUser = this.userManager.query(filter);
		return BeanCopierUtils.transformList(lstUser, UserDTO.class);
	}

	public List<? extends IUser> getUsersByOrgPath(String orgPath) {
		List<User> lstUser = this.userManager.getUsersByOrgPath(orgPath);
		return BeanCopierUtils.transformList(lstUser, UserDTO.class);
	}

	public List<? extends IUser> getUserByAccounts(String accounts) {
		QueryFilter filter = new DefaultQueryFilter(true);
		filter.addFilter("tuser.account_", accounts, QueryOP.IN);
		List<User> lstUser = this.userManager.query(filter);
		return BeanCopierUtils.transformList(lstUser, UserDTO.class);
	}

	public List<? extends IUser> getUsersByGroup(String orgIds, String postIds) {
		QueryFilter filter = new DefaultQueryFilter(true);
		Map<String, Object> params = new HashMap();
		params.put("orgIds", orgIds.split(","));
		if (StringUtils.isNotEmpty(postIds)) {
			params.put("postIds", postIds.split(","));
		}

		filter.addParams(params);
		List<User> lstUser = this.userManager.query(filter);
		return BeanCopierUtils.transformList(lstUser, UserDTO.class);
	}

	public List<? extends IUser> getUsersByMobiles(String mobiles) {
		QueryFilter filter = new DefaultQueryFilter(true);
		filter.addFilter("tuser.mobile_", mobiles, QueryOP.IN);
		List<User> lstUser = this.userManager.query(filter);
		return BeanCopierUtils.transformList(lstUser, UserDTO.class);
	}

	public PageResultDto<? extends IUser> getUsersByUserQuery(UserQueryDTO userQuery) {
		DefaultQueryFilter filter = new DefaultQueryFilter(userQuery.getNoPage());
		if (!userQuery.getNoPage()) {
			filter.setPage(new DefaultPage(new RowBounds(userQuery.getOffset(), userQuery.getLimit())));
		}

		if (null != userQuery.getLstQueryConf()) {
			userQuery.getLstQueryConf().forEach((queryConf) -> {
				filter.addFilter(queryConf.getName(), queryConf.getValue(), QueryOP.getByVal(queryConf.getType()));
			});
		}

		String sort = userQuery.getSort();
		String order = userQuery.getOrder();
		if (StringUtil.isNotEmpty(sort)) {
			String[] sorts = sort.split(",");
			String[] orders = new String[0];
			if (StringUtils.isNotEmpty(order)) {
				orders = order.split(",");
			}

			List<FieldSort> fieldSorts = new ArrayList();

			for(int i = 0; i < sorts.length; ++i) {
				String sortTemp = sorts[i];
				String orderTemp = Direction.ASC.name();
				if (orders.length > i) {
					orderTemp = orders[i];
				}

				fieldSorts.add(new DefaultFieldSort(sortTemp, Direction.fromString(orderTemp)));
			}

			filter.setFieldSortList(fieldSorts);
		}

		String teamCustomIds;
		Boolean orgHasChild;
		if (StringUtils.isNotEmpty(userQuery.getOrgIds())) {
			teamCustomIds = userQuery.getOrgIds();
			filter.getParams().put("orgIds", teamCustomIds.split(","));
			orgHasChild = userQuery.getOrgHasChild();
			if (null == orgHasChild) {
				orgHasChild = false;
			}

			filter.getParams().put("orgHasChild", orgHasChild);
		}

		if (StringUtils.isNotEmpty(userQuery.getOrgCodes())) {
			teamCustomIds = userQuery.getOrgCodes();
			filter.getParams().put("orgCodes", teamCustomIds.split(","));
			orgHasChild = userQuery.getOrgHasChild();
			if (null == orgHasChild) {
				orgHasChild = false;
			}

			filter.getParams().put("orgHasChild", orgHasChild);
		}

		if (StringUtils.isNotEmpty(userQuery.getOrgPath())) {
			filter.getParams().put("orgPath", userQuery.getOrgPath().concat("%"));
		}

		if (StringUtils.isNotEmpty(userQuery.getRoleIds())) {
			teamCustomIds = userQuery.getRoleIds();
			filter.getParams().put("roleIds", teamCustomIds.split(","));
		}

		if (StringUtils.isNotEmpty(userQuery.getRoleCodes())) {
			teamCustomIds = userQuery.getRoleCodes();
			filter.getParams().put("roleCodes", teamCustomIds.split(","));
		}

		if (StringUtils.isNotEmpty(userQuery.getPostIds())) {
			teamCustomIds = userQuery.getPostIds();
			filter.getParams().put("postIds", teamCustomIds.split(","));
		}

		if (StringUtils.isNotEmpty(userQuery.getPostCodes())) {
			teamCustomIds = userQuery.getPostCodes();
			filter.getParams().put("postCodes", teamCustomIds.split(","));
		}

		if (StringUtils.isNotEmpty(userQuery.getResultType())) {
			filter.getParams().put("resultType", userQuery.getResultType());
		}

		if (StringUtils.isNotEmpty(userQuery.getTeamIds())) {
			teamCustomIds = userQuery.getTeamIds();
			filter.getParams().put("teamIds", teamCustomIds.split(","));
		}

		if (StringUtils.isNotEmpty(userQuery.getTeamCustomIds())) {
			teamCustomIds = userQuery.getTeamCustomIds();
			filter.getParams().put("teamCustomIds", teamCustomIds.split(","));
		}

		if (null != userQuery.getUserSelectHistory() && userQuery.getUserSelectHistory()) {
			filter.getParams().put("teamHistory", true);
			filter.getParams().put("currentUserId", this.iCurrentContext.getCurrentUserId());
		}

		List<User> lstUser = this.bpmUserManager.query(filter);
		PageResult result = new PageResult(lstUser);
		result.setRows(BeanCopierUtils.transformList(result.getRows(), UserDTO.class));
		return (PageResultDto)BeanCopierUtils.transformBean(result, PageResultDto.class);
	}

	public String addUser(UserDTO userDTO) {
		if (null != this.iCurrentContext.getCurrentUserId()) {
			User user = (User)BeanCopierUtils.transformBean(userDTO, User.class);
			List<OrgRelation> lstOrgRelation = BeanCopierUtils.transformList(userDTO.getOrgRelationList(), OrgRelation.class);
			user.setOrgRelationList(lstOrgRelation);
			user.setId((String)null);
			this.userManager.saveUserInfo(user);
			return user.getId();
		} else {
			return null;
		}
	}

	public Integer editUser(UserDTO userDTO) {
		try {
			User user;
			if (null == this.iCurrentContext.getCurrentUserId()) {
				user = new User();
				user.setUserId("0");
				user.setFullname("系统");
				this.iCurrentContext.setCurrentUser(user);
			}

			user = (User)BeanCopierUtils.transformBean(userDTO, User.class);
			if (null != userDTO.getOrgRelationList()) {
				List<OrgRelation> lstOrgRelation = BeanCopierUtils.transformList(userDTO.getOrgRelationList(), OrgRelation.class);
				user.setOrgRelationList(lstOrgRelation);
			}

			if (StringUtils.isNotEmpty(user.getId())) {
				this.userManager.saveUserInfo(user);
				return 1;
			} else {
				throw new BusinessMessage("ID必填");
			}
		} catch (Exception var4) {
			this.logger.error("修改用户信息出错", var4);
			return 0;
		}
	}

	public boolean isAdmin(IUser user) {
		return this.iCurrentContext.isAdmin(user);
	}

	public List<BpmUserDTO> getUserOrgInfos(String userIds) {
		return this.bpmUserManager.getUserOrgInfos(userIds);
	}

	public Map<String, String> getUserMapByUserIds(Set<String> userIdSet) {
		Map<String, String> mapUser = new HashMap();
		if (CollectionUtil.isNotEmpty(userIdSet)) {
			QueryFilter filter = new DefaultQueryFilter();
			filter.addFilter("tuser.id_", new ArrayList(userIdSet), QueryOP.IN);
			filter.addFilter("tuser.STATUS_", "1", QueryOP.EQUAL);
			filter.getParams().put("resultType", "onlyUserUserName");
			List<User> lstUser = this.userManager.query(filter);
			if (CollectionUtil.isNotEmpty(lstUser)) {
				lstUser.forEach((temp) -> {
					mapUser.put(temp.getUserId(), temp.getFullname());
				});
			}
		}

		return mapUser;
	}


}
