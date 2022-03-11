package com.dstz.org.core.manager.impl;


import com.dstz.base.api.query.QueryFilter;
import com.dstz.org.api.model.dto.BpmOrgDTO;
import com.dstz.org.api.model.dto.BpmUserDTO;
import com.dstz.org.core.dao.BpmUserDao;
import com.dstz.org.core.manager.BpmUserManager;
import com.dstz.org.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BpmUserManagerImpl implements BpmUserManager {
    @Autowired
    BpmUserDao userDao;

    public BpmUserManagerImpl() {
    }

    public List<User> getUserListByOrgId(String groupId) {
        return this.userDao.getUserListByOrgId(groupId);
    }

    public List<User> getUserListByRelation(String groupId, String type) {
        return this.userDao.getUserListByRelation(groupId, type);
    }

    public List<User> query(QueryFilter filter) {
        return this.userDao.query(filter);
    }

    public List<BpmUserDTO> getUserOrgInfos(String userIds) {
        String[] arrUserId = userIds.split(",");
        return this.userDao.getUserOrgInfos(arrUserId);
    }

    public List<BpmOrgDTO> getOrgInfos(String orgIds) {
        String[] arrOrgId = orgIds.split(",");
        return this.userDao.getOrgInfos(arrOrgId);
    }

    public List<User> getUserListByPostId(String postId) {
        return this.userDao.getUserListByPostId(postId);
    }
}