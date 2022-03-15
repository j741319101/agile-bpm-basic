package com.dstz.org.core.manager.impl;

import java.util.*;

import javax.annotation.Resource;

import com.dstz.org.core.dao.GroupDao;
import com.dstz.sys.util.ContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.cache.ICache;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.org.api.context.ICurrentContext;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.core.constant.RelationTypeConstant;
import com.dstz.org.core.dao.OrgRelationDao;
import com.dstz.org.core.manager.OrgRelationManager;
import com.dstz.org.core.model.OrgRelation;
import com.github.pagehelper.Page;

import cn.hutool.core.util.ArrayUtil;
/**
 * 用户组织关系 Manager处理实现类
 * @author Jeff
 * @email for_office@qq.com
 * @time 2018-12-16 01:07:59
 */
@Service("orgRelationManager")
public class OrgRelationManagerImpl extends BaseManager<String, OrgRelation> implements OrgRelationManager{
	protected  final Logger log = LoggerFactory.getLogger(this.getClass());
	@Resource
	OrgRelationDao orgRelationDao;
	@Resource
	GroupDao groupDao;

	public OrgRelationManagerImpl() {
	}

	public List<OrgRelation> getPostByUserId(String userId) {
		return this.orgRelationDao.getUserRelation(userId, RelationTypeConstant.POST_USER.getKey());
	}

	public List<OrgRelation> getUserRelation(String userId, String relationType) {
		return this.orgRelationDao.getUserRelation(userId, relationType);
	}

	@Override
	public void removeByUserId(String userId) {
		this.orgRelationDao.removeByUserId(userId);
	}

	@Override
	public List<OrgRelation> getGroupPost(String groupId) {
		return orgRelationDao.getGroupPost(groupId);
	}

	@Override
	public void removeGroupPostByGroupId(String groupId) {
		this.orgRelationDao.removeGroupPostByGroupId(groupId);
	}

	@Override
	public void updateUserGroupRelationIsMaster(String id) {
		OrgRelation relation = orgRelationDao.get(id);
		if(relation == null || StringUtil.isEmpty(relation.getUserId())) return ;

		List<String> relationList = Arrays.asList(RelationTypeConstant.GROUP_USER.getKey(),RelationTypeConstant.POST_USER.getKey());
		//查询出用户 与 岗位，组织的所有关系，置为 非主版本
		List<OrgRelation>  userGroupRelations = orgRelationDao.getRelationsByParam(relationList, relation.getUserId(), null, null);
		userGroupRelations.forEach(rel ->{
			rel.setIsMaster(0);
			orgRelationDao.update(rel);
		});
		//切换是否主版本
		relation.setIsMaster(relation.getIsMaster() == 0 ? 1:0);
		orgRelationDao.update(relation);
	}

	public void removeByUserId(String userId, List<String> relationTypes) {
		ContextUtil.clearCurrentUser();
		this.orgRelationDao.removeByUserId(userId, relationTypes);
	}

	public void removeRelationByGroupId(String groupType, String groupId) {
		this.orgRelationDao.removeRelationByGroupId(groupType, groupId);
	}

	public void updateUserGroupRelationIsMaster(String groupId, String userId, String groupType) {
		RelationTypeConstant relationType = RelationTypeConstant.getUserRelationTypeByGroupType(groupType);
		if (StringUtils.isEmpty(groupId)) {
			throw new BusinessMessage("组织id不能为空");
		} else if (StringUtils.isEmpty(userId)) {
			throw new BusinessMessage("用户id不能为空");
		} else if (null == relationType) {
			throw new BusinessMessage("组织类型不正确");
		} else {
			QueryFilter filter = new DefaultQueryFilter(true);
			filter.addFilter("relation.type_", relationType.getKey(), QueryOP.EQUAL);
			filter.addFilter("relation.user_id_", userId, QueryOP.EQUAL);
			filter.addFilter("relation.group_id_", groupId, QueryOP.EQUAL);
			List<OrgRelation> lstOrgRelation = this.query(filter);
			OrgRelation orgRelation;
			if (lstOrgRelation.size() > 0) {
				orgRelation = (OrgRelation)lstOrgRelation.get(0);
				orgRelation.setIsMaster(1);
				this.orgRelationDao.update(orgRelation);

				for(int i = 1; i < lstOrgRelation.size(); ++i) {
					this.orgRelationDao.remove(((OrgRelation)lstOrgRelation.get(i)).getId());
				}
			} else {
				orgRelation = new OrgRelation();
				orgRelation.setIsMaster(1);
				orgRelation.setType(relationType.getKey());
				orgRelation.setUserId(userId);
				orgRelation.setGroupId(groupId);
				this.orgRelationDao.create(orgRelation);
			}

			filter = new DefaultQueryFilter(true);
			filter.addFilter("relation.type_", relationType.getKey(), QueryOP.EQUAL);
			filter.addFilter("relation.user_id_", userId, QueryOP.EQUAL);
			filter.addFilter("relation.is_master_", "1", QueryOP.EQUAL);
			filter.addFilter("relation.group_id_", groupId, QueryOP.NOT_EQUAL);
			lstOrgRelation = this.query(filter);
			if (lstOrgRelation.size() > 0) {
				Iterator var9 = lstOrgRelation.iterator();

				while(var9.hasNext()) {
					OrgRelation orgRel = (OrgRelation)var9.next();
					this.orgRelationDao.remove(orgRel.getId());
				}
			}

		}
	}

	public void changeStatus(String id, int status) {
		OrgRelation relation = (OrgRelation)this.orgRelationDao.get(id);
		if (relation != null) {
			relation.setStatus(status);
			this.orgRelationDao.update(relation);
			String userId = relation.getUserId();
			if (!StringUtil.isEmpty(userId)) {
				ICache<IGroup> iCache = (ICache)AppUtil.getBean(ICache.class);
				String userKey = "current_org" + relation.getUserId();
				if (iCache != null) {
					iCache.delByKey(userKey);
				}

			}
		}
	}

	public void saveUserGroupRelation(String groupId, String[] roleIds, String[] userIds) {
		String[] var4 = userIds;
		int var5 = userIds.length;

		for(int var6 = 0; var6 < var5; ++var6) {
			String userId = var4[var6];
			if (!StringUtil.isEmpty(userId)) {
				OrgRelation orgRelation = new OrgRelation(groupId, userId, RelationTypeConstant.GROUP_USER.getKey());
				if (ArrayUtil.isNotEmpty(roleIds)) {
					String[] var9 = roleIds;
					int var10 = roleIds.length;

					for(int var11 = 0; var11 < var10; ++var11) {
						String roleId = var9[var11];
						orgRelation.setRoleId(roleId);
						orgRelation.setId((String)null);
						orgRelation.setType(RelationTypeConstant.POST_USER.getKey());
						if (!this.checkRelationIsExist(orgRelation)) {
							this.create(orgRelation);
						} else {
							this.log.warn("关系重复添加，已跳过  {}", JSON.toJSONString(orgRelation));
						}
					}
				} else if (!this.checkRelationIsExist(orgRelation)) {
					this.create(orgRelation);
				} else {
					this.log.warn("关系重复添加，已跳过  {}", JSON.toJSONString(orgRelation));
				}
			}
		}

	}

	private boolean checkRelationIsExist(OrgRelation orgRelation) {
		int count = this.orgRelationDao.getCountByRelation(orgRelation);
		return count > 0;
	}

	public int saveRoleUsers(String roleId, String[] userIds) {
		List<String> relationTypes = new ArrayList();
		List<OrgRelation> lstOrgRelation = this.orgRelationDao.getRelationsByParam(relationTypes, "", "", roleId);
		Map<String, OrgRelation> mapOrgRelation = new HashMap();
		lstOrgRelation.forEach((orgRelationx) -> {
			OrgRelation var10000 = (OrgRelation)mapOrgRelation.put(orgRelationx.getUserId() + "-" + orgRelationx.getRoleId(), orgRelationx);
		});
		int i = 0;
		if (null != userIds) {
			String[] var7 = userIds;
			int var8 = userIds.length;

			for(int var9 = 0; var9 < var8; ++var9) {
				String userId = var7[var9];
				OrgRelation orgRelation = new OrgRelation(roleId, userId, RelationTypeConstant.USER_ROLE.getKey());
				if (this.checkRelationIsExist(orgRelation)) {
					mapOrgRelation.remove(orgRelation.getUserId() + "-" + orgRelation.getRoleId());
				} else {
					++i;
					this.create(orgRelation);
				}
			}
		}

		mapOrgRelation.values().forEach((orgRelationx) -> {
			this.orgRelationDao.remove(orgRelationx.getId());
		});
		return i;
	}

	public List<OrgRelation> getUserRole(String userId) {
		return this.orgRelationDao.getUserRole(userId);
	}

	public OrgRelation getPost(String id) {
		return this.orgRelationDao.getPost(id);
	}

	@Override
	public void removeCheck(String groupId, String roleId) {

	}

	public void removeCheck(String groupId) {
		QueryFilter filter = new DefaultQueryFilter();
		if (StringUtil.isNotEmpty(groupId)) {
			filter.addFilter("relation.group_id_", groupId, QueryOP.EQUAL);
		}

		Page<OrgRelation> relationList = (Page)this.query(filter);
		if (!relationList.isEmpty()) {
			StringBuilder sb = new StringBuilder("请先移除以下关系：<br>");
			Iterator var5 = relationList.iterator();

			while(var5.hasNext()) {
				OrgRelation relation = (OrgRelation)var5.next();
				this.getRelationNotes(relation, sb);
			}

			if (relationList.getTotal() > 10L) {
				sb.append("......<br>");
			}

			sb.append(" 共[").append(relationList.getTotal()).append("]条，待移除关系");
			throw new BusinessMessage(sb.toString());
		}
	}

	private void getRelationNotes(OrgRelation relation, StringBuilder sb) {
		if (relation.getType().equals(RelationTypeConstant.POST_USER.getKey())) {
			sb.append("岗位 [").append(relation.getPostName()).append("] 下用户：").append(relation.getUserName());
		} else if (relation.getType().equals(RelationTypeConstant.GROUP_USER.getKey())) {
			sb.append("组织 [").append(relation.getGroupName()).append("] 下用户：").append(relation.getUserName());
		} else if (relation.getType().equals(RelationTypeConstant.USER_ROLE.getKey())) {
			sb.append("角色 [").append(relation.getRoleName()).append("] 下用户：").append(relation.getUserName());
		}

		sb.append("<br>");
	}

	public void removeAllRelation(String relationType) {
		this.orgRelationDao.removeAllRelation(relationType);
	}

	public void modifyUserOrg(List<OrgRelation> relations) {
		Iterator var2 = relations.iterator();

		while(var2.hasNext()) {
			OrgRelation relation = (OrgRelation)var2.next();
			List<String> relationTypes = new ArrayList();
			relationTypes.add(relation.getType());
			List<OrgRelation> lstOrgRelation = this.orgRelationDao.getRelationsByParam(relationTypes, relation.getUserId(), relation.getOldGroupId(), relation.getRoleId());
			OrgRelation relationTemp = (OrgRelation)lstOrgRelation.stream().filter((orgRelation) -> {
				return 1 == orgRelation.getIsMaster();
			}).findFirst().orElse((OrgRelation)null);
			if (null != relationTemp) {
				relation.setIsMaster(1);
			}

			this.orgRelationDao.deleteRelationByUserIdAndType(relation.getUserId(), relation.getType(), relation.getOldGroupId());
			lstOrgRelation = this.orgRelationDao.getRelationsByParam(relationTypes, relation.getUserId(), relation.getGroupId(), relation.getRoleId());
			if (lstOrgRelation.isEmpty()) {
				this.create(relation);
			} else if (null != relationTemp) {
				relationTemp = (OrgRelation)lstOrgRelation.get(0);
				relationTemp.setIsMaster(1);
				this.orgRelationDao.updateByPrimaryKeySelective(relationTemp);
			}
		}

	}

	public void modifyAllUserGroup(String path, String oldGroupId, String newGroupId, String type) {
		QueryFilter filter = new DefaultQueryFilter();
		if (StringUtils.isNotEmpty(path)) {
			filter.addFilter("tgroup.path_", path, QueryOP.RIGHT_LIKE);
		} else {
			filter.addFilter("relation.group_id_", oldGroupId, QueryOP.EQUAL);
		}

		filter.addFilter("relation.type_", type, QueryOP.IN);
		List<OrgRelation> lstOrgRelation = this.orgRelationDao.query(filter);
		Iterator var7 = lstOrgRelation.iterator();

		while(true) {
			while(var7.hasNext()) {
				OrgRelation orgRelation = (OrgRelation)var7.next();
				orgRelation.setGroupId(newGroupId);
				List<String> relationTypes = new ArrayList();
				relationTypes.add(orgRelation.getType());
				List<OrgRelation> temp = this.orgRelationDao.getRelationsByParam(relationTypes, orgRelation.getUserId(), orgRelation.getGroupId(), orgRelation.getRoleId());
				if (temp != null && !temp.isEmpty()) {
					this.orgRelationDao.remove(orgRelation.getId());
					if (1 == orgRelation.getIsMaster()) {
						OrgRelation orgRelationTemp = (OrgRelation)temp.get(0);
						orgRelationTemp.setIsMaster(1);
						this.orgRelationDao.updateByPrimaryKeySelective(orgRelationTemp);
					}
				} else {
					this.orgRelationDao.updateGroupId(orgRelation);
				}
			}

			return;
		}
	}

	public void updateGroupIdByUserId(List<OrgRelation> relations, String relationType) {
		relations.forEach((relation) -> {
			relation.setType(relationType);
			this.orgRelationDao.updateGroupIdByUserId(relation);
		});
	}

	public void batchAdd(List<OrgRelation> relations) {
		relations.forEach((orgRelation) -> {
			if (!this.checkRelationIsExist(orgRelation)) {
				this.create(orgRelation);
			}

		});
	}

	public void update(OrgRelation entity) {
		this.orgRelationDao.updateByPrimaryKeySelective(entity);
	}

	public void batchRemove(List<OrgRelation> relations) {
		relations.forEach((orgRelation) -> {
			if (StringUtils.isNotEmpty(orgRelation.getId())) {
				this.orgRelationDao.remove(orgRelation.getId());
			} else {
				this.orgRelationDao.deleteRelationByUserIdAndType(orgRelation.getUserId(), orgRelation.getType(), orgRelation.getGroupId());
			}

		});
	}

	public void create(OrgRelation entity) {
		if (StringUtils.isEmpty(entity.getGroupId())) {
			throw new BusinessMessage("添加组关系 组id不能为空");
		} else if (StringUtils.isEmpty(entity.getUserId())) {
			throw new BusinessMessage("添加组关系 用户id不能为空");
		} else {
			this.orgRelationDao.create(entity);
		}
	}

}
