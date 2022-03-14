package com.dstz.bus.dao;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.dstz.base.dao.BaseDao;
import com.dstz.bus.model.BusinessPermission;

import java.util.Set;

/**
 * bo权限 DAO接口
 * 
 * @author aschs
 * @email aschs@qq.com
 * @time 2018-04-25 19:00:07
 */
@MapperScan
public interface BusinessPermissionDao extends BaseDao<String, BusinessPermission> {
	BusinessPermission getByObjTypeAndObjVal(@Param("objType") String objType, @Param("objVal") String objVal);
	BusinessPermission getByObjTypeAndObjVal(@Param("defId") String defId, @Param("objType") String objType, @Param("objVal") String objVal);

	int removeByBpmDefKey(@Param("defId") String defId, @Param("objType") String objType, @Param("boKey") String boKey);

	int removeNotInBpmNode(@Param("defId") String defId, @Param("boKey") String boKey, @Param("nodeIds") Set<String> nodeIds);

	int removeByDefId(@Param("defId") String defId);
}
