package com.dstz.org.api.model.dto;


import com.dstz.base.api.query.QueryOP;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupQueryDTO implements Serializable {
    List<QueryConfDTO> lstQueryConf = new ArrayList();
    Boolean noPage = true;
    Integer offset;
    Integer limit;
    String userId;
    List<String> orgIds = new ArrayList();
    List<String> types = new ArrayList();
    Boolean orgHasChild;
    String groupType;
    String groupPath;
    String resultType;
    String sort;
    String order;
    List<String> noHasChildOrgIds = new ArrayList();
    String groupCodes;
    static final String RESULT_TYPE_ONLY_GROUP_ID = "onlyGroupId";
    static final String RESULT_TYPE_ONLY_GROUP_NAME = "onlyGroupName";
    static final String RESULT_TYPE_WITH_USER_NUM = "withUserNum";

    public GroupQueryDTO() {
    }

    public GroupQueryDTO page(int pageNo, int pageSize) {
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

    public GroupQueryDTO pageOffset(int offset, int limit) {
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

    public GroupQueryDTO queryByUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public GroupQueryDTO queryByGroupIds(List<String> lstGroupId) {
        String groupIds = "";
        if (null != lstGroupId) {
            groupIds = String.join(",", lstGroupId);
        }

        return this.queryByGroupIds(groupIds);
    }

    public GroupQueryDTO queryByGroupTypes(List<String> listGroupType) {
        String groupTypes = "";
        if (null != listGroupType) {
            groupTypes = String.join(",", listGroupType);
        }

        return this.queryByGroupTypes(groupTypes);
    }

    public GroupQueryDTO queryByGroupIds(String groupIds) {
        this.lstQueryConf.add(new QueryConfDTO("id_", QueryOP.IN.value(), groupIds));
        return this;
    }

    public GroupQueryDTO queryByParentGroupIds(String groupIds) {
        this.lstQueryConf.add(new QueryConfDTO("parent_id_", QueryOP.IN.value(), groupIds));
        return this;
    }

    public GroupQueryDTO queryByGroupTypes(String groupTypes) {
        this.lstQueryConf.add(new QueryConfDTO("type_", QueryOP.IN.value(), groupTypes));
        return this;
    }

    public GroupQueryDTO queryByGroupName(String groupName) {
        return this.queryByGroupName(groupName, QueryOP.LIKE);
    }

    public GroupQueryDTO queryByGroupName(String groupName, QueryOP queryType) {
        this.lstQueryConf.add(new QueryConfDTO("name_", queryType.value(), groupName));
        return this;
    }

    public GroupQueryDTO queryByGroupPath(String groupPath) {
        this.groupPath = groupPath;
        return this;
    }

    public GroupQueryDTO queryOrderBy(String sort, String order) {
        this.sort = sort;
        if (!StringUtils.isEmpty(sort)) {
            if (StringUtils.isEmpty(sort)) {
                sort = "asc";
            }

            this.order = order;
        }

        return this;
    }

    public GroupQueryDTO queryByGroupCodes(String groupCodes) {
        this.groupCodes = groupCodes;
        return this;
    }

    public GroupQueryDTO queryByGroupCodes(List<String> listGroupCodes) {
        String groupCodes = "";
        if (null != listGroupCodes) {
            groupCodes = String.join(",", listGroupCodes);
        }

        this.groupCodes = groupCodes;
        return this;
    }

    public GroupQueryDTO setResultTypeOnlyGroupId() {
        this.resultType = "onlyGroupId";
        return this;
    }

    public GroupQueryDTO setResultTypeOnlyGroupName() {
        this.resultType = "onlyGroupName";
        return this;
    }

    public GroupQueryDTO setResultTypeWithUserNum() {
        this.resultType = "withUserNum";
        return this;
    }

    public Boolean getNoPage() {
        return this.noPage;
    }

    public GroupQueryDTO setNoPage(Boolean noPage) {
        this.noPage = noPage;
        return this;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public GroupQueryDTO setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public GroupQueryDTO setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public String getUserId() {
        return this.userId;
    }

    public GroupQueryDTO setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getGroupType() {
        return this.groupType;
    }

    public GroupQueryDTO setGroupType(String groupType) {
        this.groupType = groupType;
        return this;
    }

    public String getGroupPath() {
        return this.groupPath;
    }

    public GroupQueryDTO setGroupPath(String groupPath) {
        this.groupPath = groupPath;
        return this;
    }

    public String getResultType() {
        return this.resultType;
    }

    public GroupQueryDTO setResultType(String resultType) {
        this.resultType = resultType;
        return this;
    }

    public List<QueryConfDTO> getLstQueryConf() {
        return this.lstQueryConf;
    }

    public GroupQueryDTO setLstQueryConf(List<QueryConfDTO> lstQueryConf) {
        this.lstQueryConf = lstQueryConf;
        return this;
    }

    public String getSort() {
        return this.sort;
    }

    public GroupQueryDTO setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getOrder() {
        return this.order;
    }

    public GroupQueryDTO setOrder(String order) {
        this.order = order;
        return this;
    }

    public List<String> getOrgIds() {
        return this.orgIds;
    }

    public void setOrgIds(List<String> orgIds) {
        this.orgIds = orgIds;
    }

    public Boolean getOrgHasChild() {
        return this.orgHasChild;
    }

    public void setOrgHasChild(Boolean orgHasChild) {
        this.orgHasChild = orgHasChild;
    }

    public List<String> getTypes() {
        return this.types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getNoHasChildOrgIds() {
        return this.noHasChildOrgIds;
    }

    public void setNoHasChildOrgIds(List<String> noHasChildOrgIds) {
        this.noHasChildOrgIds = noHasChildOrgIds;
    }

    public String getGroupCodes() {
        return this.groupCodes;
    }

    public void setGroupCodes(String groupCodes) {
        this.groupCodes = groupCodes;
    }
}
