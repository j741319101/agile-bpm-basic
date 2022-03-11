package com.dstz.org.core.manager;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.org.api.model.dto.BpmOrgDTO;
import com.dstz.org.api.model.dto.BpmUserDTO;
import com.dstz.org.core.model.User;

import java.util.List;

public interface BpmUserManager {
    List<User> getUserListByOrgId(String var1);

    List<User> getUserListByRelation(String var1, String var2);

    List<User> query(QueryFilter var1);

    List<BpmUserDTO> getUserOrgInfos(String var1);

    List<BpmOrgDTO> getOrgInfos(String var1);

    List<User> getUserListByPostId(String var1);
}
