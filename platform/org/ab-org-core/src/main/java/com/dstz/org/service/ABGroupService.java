package com.dstz.org.service;

import java.util.*;

import javax.annotation.Resource;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.api.response.impl.PageResultDto;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.db.model.query.DefaultPage;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.org.api.context.ICurrentContext;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.model.dto.BpmOrgDTO;
import com.dstz.org.api.model.dto.GroupQueryDTO;
import com.dstz.org.api.model.dto.QueryConfDTO;
import com.dstz.org.core.manager.*;
import com.dstz.org.core.model.Role;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dstz.base.core.util.BeanCopierUtils;
import com.dstz.org.api.constant.GroupTypeConstant;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.dto.GroupDTO;
import com.dstz.org.api.service.GroupService;
import com.dstz.org.core.model.OrgRelation;
import com.dstz.org.core.model.User;

import cn.hutool.core.collection.CollectionUtil;

/**
 * 用户与组关系的实现类：通过用户找组，通过组找人等
 */
@Service("defaultGroupService")
public class ABGroupService implements GroupService {
	private Logger log  = LoggerFactory.getLogger(getClass());
    @Resource
    UserManager userManager;

    @Resource
    GroupManager groupManager;
    
    @Autowired
    RoleManager roleManager;

    @Resource
    OrgRelationManager orgRelationManager;

    @Resource
    PostManager postManager;
    @Resource
    ICurrentContext iCurrentContext;
    @Resource
    BpmUserManager bpmUserManager;





    @Override
    public IGroup getMainGroup(String userId) {
        return groupManager.getMainGroup(userId);
    }


    public ABGroupService() {
    }

    public List<? extends IGroup> getGroupsByGroupTypeUserId(String groupType, String userId) {
        List listGroup = null;
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            listGroup = this.groupManager.getByUserId(userId);
        } else if (groupType.equals(GroupTypeConstant.ROLE.key())) {
            listGroup = this.roleManager.getByUserId(userId);
        } else if (groupType.equals(GroupTypeConstant.POST.key())) {
            listGroup = this.postManager.getByUserId(userId);
        }

        return listGroup != null ? BeanCopierUtils.transformList(listGroup, GroupDTO.class) : null;
    }

    public Map<String, List<? extends IGroup>> getAllGroupByAccount(String account) {
        User user = this.userManager.getByAccount(account);
        return user == null ? Collections.emptyMap() : this.getAllGroupByUserId(user.getId());
    }

    public Map<String, List<? extends IGroup>> getAllGroupByUserId(String userId) {
        Map<String, List<? extends IGroup>> listMap = new HashMap();
        List<? extends IGroup> listOrg = this.groupManager.getByUserId(userId);
        List listRole;
        if (CollectionUtil.isNotEmpty(listOrg)) {
            listRole = BeanCopierUtils.transformList(listOrg, GroupDTO.class);
            listMap.put(GroupTypeConstant.ORG.key(), listRole);
        }

        listRole = this.roleManager.getByUserId(userId);
        if (CollectionUtil.isNotEmpty(listRole)) {
            List<? extends IGroup> groupList  = BeanCopierUtils.transformList(listRole, GroupDTO.class);
            listMap.put(GroupTypeConstant.ROLE.key(), groupList);
        }

        List<OrgRelation> listOrgRel =  orgRelationManager.getPostByUserId(userId);
        if (CollectionUtil.isNotEmpty(listOrgRel)) {
            List<IGroup> userGroups = new ArrayList();
            listOrgRel.forEach((post) -> {
                userGroups.add(new GroupDTO(post.getPostId(), post.getPostName(), GroupTypeConstant.POST.key()));
            });
            listMap.put(GroupTypeConstant.POST.key(), userGroups);
        }

        return listMap;
    }

    /**
     * 根据用户ID获取所有组
     */
    public List<? extends IGroup> getGroupsByUserId(String userId) {
        List<IGroup> userGroups = new ArrayList();
        List<? extends IGroup> listOrg = this.groupManager.getByUserId(userId);
        if (CollectionUtil.isNotEmpty(listOrg)) {
            userGroups.addAll(listOrg);
        }

        List<? extends IGroup> listRole = this.roleManager.getByUserId(userId);
        if (CollectionUtil.isNotEmpty(listRole)) {
            userGroups.addAll(listRole);
        }

        List<? extends IGroup> listPost = this.postManager.getByUserId(userId);
        if (CollectionUtil.isNotEmpty(listPost)) {
            userGroups.addAll(listPost);
        }

        List<? extends IGroup> groupList = BeanCopierUtils.transformList(userGroups, GroupDTO.class);
        return groupList;
    }
    /**
     * 根据组类别和组ID获取组定义
     */
    public IGroup getById(String groupType, String groupId) {
        IGroup group = null;
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            group = (IGroup)this.groupManager.get(groupId);
        } else if (groupType.equals(GroupTypeConstant.ROLE.key())) {
            group = (IGroup)this.roleManager.get(groupId);
        } else if (groupType.equals(GroupTypeConstant.POST.key())) {
            group = (IGroup)this.postManager.get(groupId);
        }

        return group == null ? null : new GroupDTO(group);
    }
    /**
     * 根据组类别和组编码获取组定义
     */
    public IGroup getByCode(String groupType, String code) {
        IGroup group = null;
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            group = this.groupManager.getByCode(code);
        } else if (groupType.equals(GroupTypeConstant.ROLE.key())) {
            group = this.roleManager.getByAlias(code);
        } else if (groupType.equals(GroupTypeConstant.POST.key())) {
            group = this.postManager.getByAlias(code);
        }

        return group == null ? null : new GroupDTO((IGroup)group);
    }

    public List<? extends IGroup> getSiblingsGroups(String code) {
        List<IGroup> lstGroup = null;
        QueryFilter filter = new DefaultQueryFilter(true);
        IGroup group = this.groupManager.getByCode(code);
        if (null != group && null != group.getParentId()) {
            filter.addFilter("torg.parent_id_", group.getParentId(), QueryOP.EQUAL);
            filter.addFilter("torg.code_", code, QueryOP.NOT_EQUAL);
            return BeanCopierUtils.transformList(this.groupManager.query(filter), GroupDTO.class);
        } else {
            return null;
        }
    }

    public List<? extends IGroup> getRoleList(QueryFilter queryFilter) {
        List<Role> pageList = this.roleManager.query(queryFilter);
        return pageList;
    }

    public List<? extends IGroup> getSameLevel(String groupType, String groupId) {
        List<IGroup> lstGroup = null;
        QueryFilter filter = new DefaultQueryFilter(true);
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            IGroup group = (IGroup)this.groupManager.get(groupId);
            if (null != group && null != group.getParentId()) {
                filter.addFilter("torg.parent_id_", group.getParentId(), QueryOP.EQUAL);
                return BeanCopierUtils.transformList(this.groupManager.query(filter), GroupDTO.class);
            }
        }

        return null;
    }

    public List<? extends IGroup> getGroupListByType(String groupType) {
        List<? extends IGroup> groups = null;
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            groups = this.groupManager.getAll();
        } else if (groupType.equals(GroupTypeConstant.ROLE.key())) {
            groups = this.roleManager.getAll();
        } else if (groupType.equals(GroupTypeConstant.POST.key())) {
            groups = this.postManager.getAll();
        }

        return groups;
    }

    public List<? extends IGroup> getGroupsById(String groupType, String groupIds) {
        QueryFilter filter = new DefaultQueryFilter(true);
        List<? extends IGroup> lstIGroup = null;
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            filter.addFilter("torg.id_", groupIds, QueryOP.IN);
            lstIGroup = this.groupManager.query(filter);
        } else if (groupType.equals(GroupTypeConstant.ROLE.key())) {
            filter.addFilter("trole.id_", groupIds, QueryOP.IN);
            lstIGroup = this.roleManager.query(filter);
        } else if (groupType.equals(GroupTypeConstant.POST.key())) {
            filter.addFilter("tpost.id_", groupIds, QueryOP.IN);
            lstIGroup = this.postManager.query(filter);
        }

        return lstIGroup;
    }

    public List<? extends IGroup> getChildrenGroupsById(String groupType, String groupId) {
        QueryFilter filter = new DefaultQueryFilter(true);
        List<? extends IGroup> lstIGroup = null;
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            filter.addFilter("torg.parent_id_", groupId, QueryOP.EQUAL);
            lstIGroup = this.groupManager.query(filter);
        }

        return lstIGroup;
    }

    public IGroup getMasterGroupByUserId(String groupType, String userId) {
        IGroup group = null;
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            group = this.groupManager.getMainGroup(userId);
        } else if (groupType.equals(GroupTypeConstant.POST.key())) {
            group = this.postManager.getMasterByUserId(userId);
        }

        if (group != null) {
            group = new GroupDTO((IGroup)group);
        }

        return (IGroup)group;
    }

    public PageResultDto<? extends IGroup> getGroupsByGroupQuery(GroupQueryDTO groupQuery) {
        QueryFilter filter = new DefaultQueryFilter(true);
        if (!groupQuery.getNoPage()) {
            filter.setPage(new DefaultPage(new RowBounds(groupQuery.getOffset(), groupQuery.getLimit())));
        }

        String tableName = "";
        String type = groupQuery.getGroupType();
        if (type.equals(GroupTypeConstant.ORG.key())) {
            tableName = "torg";
        } else if (type.equals(GroupTypeConstant.POST.key())) {
            tableName = "tpost";
        } else if (type.equals(GroupTypeConstant.ROLE.key())) {
            tableName = "trole";
        }
        if (null != groupQuery.getLstQueryConf()) {
            for (QueryConfDTO queryConf : groupQuery.getLstQueryConf()) {
                filter.addFilter(tableName + "." + queryConf.getName(), queryConf.getValue(), QueryOP.getByVal(queryConf.getType()));
            }
        }

        String groupCodes;
        if (StringUtils.isNotEmpty(groupQuery.getUserId())) {
            groupCodes = groupQuery.getUserId();
            filter.getParams().put("userId", groupCodes);
        }

        List noHasChildOrgIds;
        if (CollectionUtil.isNotEmpty(groupQuery.getOrgIds())) {
            noHasChildOrgIds = groupQuery.getOrgIds();
            filter.getParams().put("orgIds", noHasChildOrgIds);
        }

        if (StringUtils.isNotEmpty(groupQuery.getGroupCodes())) {
            groupCodes = groupQuery.getGroupCodes();
            if (type.equals(GroupTypeConstant.ORG.key())) {
                filter.getParams().put("orgCodes", Arrays.asList(groupCodes.split(",")));
            } else if (type.equals(GroupTypeConstant.POST.key())) {
                filter.addFilter(tableName + ".code_", groupCodes, QueryOP.IN);
            } else if (type.equals(GroupTypeConstant.ROLE.key())) {
                filter.addFilter(tableName + ".alias_", groupCodes, QueryOP.IN);
            }
        }

        if (CollectionUtil.isNotEmpty(groupQuery.getNoHasChildOrgIds())) {
            noHasChildOrgIds = groupQuery.getNoHasChildOrgIds();
            filter.getParams().put("noHasChildOrgIds", noHasChildOrgIds);
        }

        if (null != groupQuery.getOrgHasChild()) {
            boolean orgHasChild = groupQuery.getOrgHasChild();
            filter.getParams().put("orgHasChild", orgHasChild);
        }

        if (StringUtils.isNotEmpty(groupQuery.getResultType())) {
            filter.getParams().put("resultType", groupQuery.getResultType());
        }

        if (StringUtils.isNotEmpty(groupQuery.getSort()) && StringUtils.isNotEmpty(groupQuery.getOrder())) {
            filter.addFieldSort(groupQuery.getSort(), groupQuery.getOrder());
        }

        List<? extends IGroup> list = Lists.newArrayList();
        if (StringUtils.isNotEmpty(type)) {
            if (type.equals(GroupTypeConstant.ORG.key())) {
                if (StringUtils.isNotEmpty(groupQuery.getGroupPath())) {
                    filter.addFilter("torg.path_", groupQuery.getGroupPath(), QueryOP.RIGHT_LIKE);
                }

                list = this.groupManager.query(filter);
            } else if (type.equals(GroupTypeConstant.POST.key())) {
                list = this.postManager.query(filter);
            } else if (type.equals(GroupTypeConstant.ROLE.key())) {
                list = this.roleManager.query(filter);
            }
        }

        PageResult result = new PageResult((List)list);
        result.setRows(BeanCopierUtils.transformList(result.getRows(), GroupDTO.class));
        return (PageResultDto)BeanCopierUtils.transformBean(result, PageResultDto.class);
    }

    public String getCurrentManagerOrgIds() {
        IUser user = this.iCurrentContext.getCurrentUser();
        List<String> lstOrgId = new ArrayList();
        if (null != user) {
            lstOrgId = user.getManagerGroupIdList();
        }

        return null == lstOrgId ? "" : String.join(",", (Iterable)lstOrgId);
    }

    public List<BpmOrgDTO> getOrgInfos(String orgIds) {
        return this.bpmUserManager.getOrgInfos(orgIds);
    }

}
