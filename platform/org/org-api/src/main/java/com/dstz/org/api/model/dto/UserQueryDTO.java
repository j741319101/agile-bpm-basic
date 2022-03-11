package com.dstz.org.api.model.dto;

import com.dstz.base.api.query.QueryOP;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserQueryDTO implements Serializable {
    List<QueryConfDTO> lstQueryConf = new ArrayList();
    Boolean noPage = true;
    Integer offset;
    Integer limit;
    String orgIds;
    String orgCodes;
    String orgPath;
    Boolean orgHasChild;
    String roleIds;
    String roleCodes;
    String postIds;
    String postCodes;
    String teamIds;
    String teamCustomIds;
    Boolean userSelectHistory;
    String resultType;
    String status = "1";
    String sort;
    String order;
    static final String TABLE_NAME_USER = "tuser";
    static final String RESULT_TYPE_ONLY_USER_ID = "onlyUserId";
    static final String RESULT_TYPE_ONLY_USER_ACCOUNT = "onlyUserAccount";
    static final String RESULT_TYPE_WEB_RESULT = "webResult";
    static final String USER_STATUS_ALIVE = "1";
    static final String USER_STATUS_NOT_ALIVE = "0";

    public UserQueryDTO() {
    }

    public UserQueryDTO page(int pageNo, int pageSize) {
        if (pageNo <= 0) {
            System.out.println("别闹，页数有负数或者零的吗");
            pageNo = 1;
        }

        if (pageSize <= 0) {
            System.out.println("别闹，能显示负数或者零条记录吗");
            pageNo = 10;
        }

        this.offset = (pageNo - 1) * pageSize;
        this.limit = pageSize;
        this.noPage = false;
        return this;
    }

    public UserQueryDTO pageOffset(int offset, int limit) {
        if (offset < 0) {
            System.out.println("别闹，偏移量有负数的吗");
            offset = 0;
        }

        if (limit <= 0) {
            System.out.println("别闹，能显示负数或者零条记录吗");
            limit = 10;
        }

        this.offset = offset;
        this.limit = limit;
        this.noPage = false;
        return this;
    }

    public UserQueryDTO queryByUserIds(List<String> lstUserId) {
        String userIds = "";
        if (null != lstUserId) {
            userIds = String.join(",", lstUserId);
        }

        return this.queryByUserIds(userIds);
    }

    public UserQueryDTO queryByUserIds(String userIds) {
        this.lstQueryConf.add(new QueryConfDTO("tuser.id_", QueryOP.IN.value(), userIds));
        return this;
    }

    public UserQueryDTO queryByUserName(String userName) {
        return this.queryByUserName(userName, QueryOP.LIKE);
    }

    public UserQueryDTO queryByUserName(String userName, QueryOP queryType) {
        this.lstQueryConf.add(new QueryConfDTO("tuser.fullname_", queryType.value(), userName));
        return this;
    }

    public UserQueryDTO queryByUserAccounts(List<String> lstUserAccount) {
        String userAccounts = "";
        if (null != lstUserAccount) {
            userAccounts = String.join(",", lstUserAccount);
        }

        return this.queryByUserAccounts(userAccounts);
    }

    public UserQueryDTO queryByUserAccounts(String userAccounts) {
        this.lstQueryConf.add(new QueryConfDTO("tuser.account_", QueryOP.IN.value(), userAccounts));
        return this;
    }

    public UserQueryDTO queryByOrgIds(List<String> orgIds) {
        return this.queryByOrgIds(orgIds, false);
    }

    public UserQueryDTO queryByOrgIds(String orgIds) {
        return this.queryByOrgIds(orgIds, false);
    }

    public UserQueryDTO queryByOrgIds(List<String> orgIds, boolean orgHasChild) {
        String temp = "";
        if (!StringUtils.isEmpty(orgIds)) {
            temp = String.join(",", orgIds);
        }

        return this.queryByOrgIds(temp, orgHasChild);
    }

    public UserQueryDTO queryByOrgIds(String orgIds, boolean orgHasChild) {
        this.orgIds = orgIds;
        this.orgHasChild = orgHasChild;
        return this;
    }

    public UserQueryDTO queryByOrgCodes(List<String> orgCodes) {
        return this.queryByOrgCodes(orgCodes, false);
    }

    public UserQueryDTO queryByOrgCodes(String orgCodes) {
        return this.queryByOrgCodes(orgCodes, false);
    }

    public UserQueryDTO queryByOrgCodes(List<String> orgCodes, boolean orgHasChild) {
        String temp = "";
        if (!StringUtils.isEmpty(orgCodes)) {
            temp = String.join(",", orgCodes);
        }

        return this.queryByOrgCodes(temp, orgHasChild);
    }

    public UserQueryDTO queryByOrgCodes(String orgCodes, boolean orgHasChild) {
        this.orgCodes = orgCodes;
        this.orgHasChild = orgHasChild;
        return this;
    }

    public UserQueryDTO queryByOrgPath(String orgPath) {
        this.orgPath = orgPath;
        return this;
    }

    public UserQueryDTO queryByRoleIds(List<String> roleIds) {
        if (!StringUtils.isEmpty(roleIds)) {
            this.roleIds = String.join(",", roleIds);
        }

        return this;
    }

    public UserQueryDTO queryByRoleIds(String roleIds) {
        this.roleIds = roleIds;
        return this;
    }

    public UserQueryDTO queryByRoleCodes(List<String> postCodes) {
        if (!StringUtils.isEmpty(postCodes)) {
            this.postCodes = String.join(",", postCodes);
        }

        return this;
    }

    public UserQueryDTO queryByRoleCodes(String roleCodes) {
        this.roleCodes = roleCodes;
        return this;
    }

    public UserQueryDTO queryByPostIds(List<String> postIds) {
        if (!StringUtils.isEmpty(postIds)) {
            this.postIds = String.join(",", postIds);
        }

        return this;
    }

    public UserQueryDTO queryByPostIds(String postIds) {
        this.postIds = postIds;
        return this;
    }

    public UserQueryDTO queryByPostCodes(List<String> postCodes) {
        if (!StringUtils.isEmpty(postCodes)) {
            this.postCodes = String.join(",", postCodes);
        }

        return this;
    }

    public UserQueryDTO queryByPostCodes(String postCodes) {
        this.postCodes = postCodes;
        return this;
    }

    public UserQueryDTO setResultTypeOnlyUserId() {
        this.resultType = "onlyUserId";
        return this;
    }

    public UserQueryDTO setResultTypeOnlyUserAccount() {
        this.resultType = "onlyUserAccount";
        return this;
    }

    public UserQueryDTO setResultTypeWebResult() {
        this.resultType = "webResult";
        return this;
    }

    public UserQueryDTO queryByTeamIds(List<String> teamIds) {
        if (!StringUtils.isEmpty(teamIds)) {
            this.teamIds = String.join(",", teamIds);
        }

        return this;
    }

    public UserQueryDTO queryByTeamIds(String teamIds) {
        this.teamIds = teamIds;
        return this;
    }

    public UserQueryDTO queryByTeamCustomIds(List<String> teamCustomIds) {
        if (!StringUtils.isEmpty(teamCustomIds)) {
            this.teamCustomIds = String.join(",", teamCustomIds);
        }

        return this;
    }

    public UserQueryDTO queryByTeamCustomIds(String teamCustomIds) {
        this.teamCustomIds = teamCustomIds;
        return this;
    }

    public UserQueryDTO queryByUserSelectHistory() {
        this.userSelectHistory = true;
        return this;
    }

    public UserQueryDTO queryAliveStatusUser() {
        this.status = "1";
        return this;
    }

    public UserQueryDTO queryNotAliveStatusUser() {
        this.status = "0";
        return this;
    }

    public UserQueryDTO queryAllStatusUser() {
        this.status = null;
        return this;
    }

    public UserQueryDTO queryOrder(String sort, String order) {
        this.sort = sort;
        this.order = order;
        return this;
    }

    public Boolean getNoPage() {
        return this.noPage;
    }

    public UserQueryDTO setNoPage(Boolean noPage) {
        this.noPage = noPage;
        return this;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public UserQueryDTO setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public UserQueryDTO setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public String getOrgIds() {
        return this.orgIds;
    }

    public UserQueryDTO setOrgIds(String orgIds) {
        this.orgIds = orgIds;
        return this;
    }

    public String getOrgPath() {
        return this.orgPath;
    }

    public UserQueryDTO setOrgPath(String orgPath) {
        this.orgPath = orgPath;
        return this;
    }

    public String getRoleIds() {
        return this.roleIds;
    }

    public UserQueryDTO setRoleIds(String roleIds) {
        this.roleIds = roleIds;
        return this;
    }

    public String getPostIds() {
        return this.postIds;
    }

    public UserQueryDTO setPostIds(String postIds) {
        this.postIds = postIds;
        return this;
    }

    public String getResultType() {
        return this.resultType;
    }

    public UserQueryDTO setResultType(String resultType) {
        this.resultType = resultType;
        return this;
    }

    public Boolean getOrgHasChild() {
        return this.orgHasChild;
    }

    public UserQueryDTO setOrgHasChild(Boolean orgHasChild) {
        this.orgHasChild = orgHasChild;
        return this;
    }

    public String getTeamIds() {
        return this.teamIds;
    }

    public UserQueryDTO setTeamIds(String teamIds) {
        this.teamIds = teamIds;
        return this;
    }

    public String getTeamCustomIds() {
        return this.teamCustomIds;
    }

    public UserQueryDTO setTeamCustomIds(String teamCustomIds) {
        this.teamCustomIds = teamCustomIds;
        return this;
    }

    public Boolean getUserSelectHistory() {
        return this.userSelectHistory;
    }

    public UserQueryDTO setUserSelectHistory(Boolean userSelectHistory) {
        this.userSelectHistory = userSelectHistory;
        return this;
    }

    public String getStatus() {
        return this.status;
    }

    public UserQueryDTO setStatus(String status) {
        this.status = status;
        return this;
    }

    public List<QueryConfDTO> getLstQueryConf() {
        return this.lstQueryConf;
    }

    public UserQueryDTO setLstQueryConf(List<QueryConfDTO> lstQueryConf) {
        this.lstQueryConf = lstQueryConf;
        return this;
    }

    public String getOrgCodes() {
        return this.orgCodes;
    }

    public UserQueryDTO setOrgCodes(String orgCodes) {
        this.orgCodes = orgCodes;
        return this;
    }

    public String getRoleCodes() {
        return this.roleCodes;
    }

    public UserQueryDTO setRoleCodes(String roleCodes) {
        this.roleCodes = roleCodes;
        return this;
    }

    public String getPostCodes() {
        return this.postCodes;
    }

    public UserQueryDTO setPostCodes(String postCodes) {
        this.postCodes = postCodes;
        return this;
    }

    public String getSort() {
        return this.sort;
    }

    public UserQueryDTO setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getOrder() {
        return this.order;
    }

    public UserQueryDTO setOrder(String order) {
        this.order = order;
        return this;
    }
}