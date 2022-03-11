package com.dstz.org.core.dao;


import com.dstz.base.dao.BaseDao;
import com.dstz.org.core.model.Post;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface PostDao extends BaseDao<String, Post> {
    Post getByCode(@Param("code") String var1, @Param("excludeId") String var2);

    int updateByPrimaryKeySelective(Post var1);
}