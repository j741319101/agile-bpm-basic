package com.dstz.sys.core.manager.impl;


import com.dstz.base.manager.impl.BaseManager;
import com.dstz.sys.core.dao.SysConnectRecordDao;
import com.dstz.sys.core.manager.SysConnectRecordManager;
import com.dstz.sys.core.model.SysConnectRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("sysConnectRecordManager")
public class SysConnectRecordManagerImpl extends BaseManager<String, SysConnectRecord> implements SysConnectRecordManager {
    @Resource
    SysConnectRecordDao sysConnectRecordDao;

    public SysConnectRecordManagerImpl() {
    }

    public List<SysConnectRecord> getByTargetId(String id, String type) {
        return this.sysConnectRecordDao.getByTargetId(id, type);
    }

    public void bulkCreate(List<SysConnectRecord> list) {
        this.sysConnectRecordDao.bulkCreate(list);
    }

    public void removeBySourceId(String sourceId, String type) {
        this.sysConnectRecordDao.removeBySourceId(sourceId, type);
    }
}