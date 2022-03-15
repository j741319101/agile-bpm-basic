package com.dstz.sys.core.dao;

import com.dstz.base.dao.BaseDao;
import com.dstz.sys.core.model.SysConnectRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysConnectRecordDao extends BaseDao<String, SysConnectRecord> {
    void removeBySourceId(@Param("sourceId") String sourceId, @Param("type") String type);

    void bulkCreate(List<SysConnectRecord> sysConnectRecords);

    List<SysConnectRecord> getByTargetId(@Param("targetId") String targetId, @Param("type") String type);
}
