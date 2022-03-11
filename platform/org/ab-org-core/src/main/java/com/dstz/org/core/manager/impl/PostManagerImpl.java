package com.dstz.org.core.manager.impl;


import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.id.IdUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.org.core.dao.PostDao;
import com.dstz.org.core.manager.OrgRelationManager;
import com.dstz.org.core.manager.PostManager;
import com.dstz.org.core.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostManagerImpl extends BaseManager<String, Post> implements PostManager {
    @Resource
    PostDao postDao;
    @Autowired
    OrgRelationManager orgRelationMananger;

    public PostManagerImpl() {
    }

    public void create(Post entity) {
        if (this.postDao.getByCode(entity.getCode(), (String)null) != null) {
            throw new BusinessMessage("岗位编码“" + entity.getCode() + "”已存在，请修改！");
        } else {
            entity.setId(IdUtil.getSuid());
            super.create(entity);
        }
    }

    public void update(Post entity) {
        if (this.postDao.getByCode(entity.getCode(), entity.getId()) != null) {
            throw new BusinessMessage("岗位编码“" + entity.getCode() + "”已存在，请修改！");
        } else {
            super.update(entity);
        }
    }

    public Post getByAlias(String alias) {
        return this.postDao.getByCode(alias, (String)null);
    }

    public void remove(String id) {
        this.orgRelationMananger.removeCheck(id);
        super.remove(id);
    }

    public List<Post> getByUserId(String userId) {
        if (StringUtil.isEmpty(userId)) {
            return Collections.emptyList();
        } else {
            QueryFilter filter = new DefaultQueryFilter(true);
            Map<String, Object> params = new HashMap();
            params.put("userId", userId);
            filter.addParams(params);
            return this.postDao.query(filter);
        }
    }

    public Post getMasterByUserId(String userId) {
        if (StringUtil.isEmpty(userId)) {
            return null;
        } else {
            QueryFilter filter = new DefaultQueryFilter(true);
            Map<String, Object> params = new HashMap();
            params.put("userId", userId);
            params.put("isMaster", "1");
            filter.addFilter("trelation.is_master_", 1, QueryOP.EQUAL);
            filter.addParams(params);
            List<Post> lstPost = this.postDao.query(filter);
            return null != lstPost && lstPost.size() > 0 ? (Post)lstPost.get(0) : null;
        }
    }
}