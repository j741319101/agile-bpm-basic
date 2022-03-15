package com.dstz.sys.api.service;


import com.dstz.sys.api.model.SysConnectRecordDTO;

import java.util.List;

public interface SysConnectRecordService {
    List<SysConnectRecordDTO> getByTargetId(String var1, String var2);

    void save(List<SysConnectRecordDTO> var1);

    void save(SysConnectRecordDTO var1);

    void removeBySourceId(String var1, String var2);

    void checkIsRelatedWithBusinessMessage(String var1, String var2);
}
