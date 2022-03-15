package com.dstz.sys.core.manager;


import com.dstz.base.manager.Manager;
import com.dstz.sys.core.model.SysConnectRecord;

import java.util.List;

public interface SysConnectRecordManager extends Manager<String, SysConnectRecord > {
    List<SysConnectRecord> getByTargetId(String var1, String var2);

    void bulkCreate(List<SysConnectRecord> var1);

    void removeBySourceId(String var1, String var2);
}
