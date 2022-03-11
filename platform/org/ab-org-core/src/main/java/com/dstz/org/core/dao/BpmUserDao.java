package com.dstz.org.core.dao;


import com.dstz.base.api.query.QueryFilter;
import com.dstz.org.api.model.dto.BpmOrgDTO;
import com.dstz.org.api.model.dto.BpmUserDTO;
import com.dstz.org.core.model.User;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@MapperScan
public interface BpmUserDao {
    List<User> getUserListByOrgId(@Param("groupId") String var1);

    List<User> getUserListByRelation(@Param("groupId") String var1, @Param("type") String var2);

    List<User> query(QueryFilter var1);

    List<BpmUserDTO> getUserOrgInfos(@Param("userIds") String[] var1);

    List<BpmOrgDTO> getOrgInfos(@Param("orgIds") String[] var1);

    List<User> getUserListByPostId(@Param("postId") String var1);
}
