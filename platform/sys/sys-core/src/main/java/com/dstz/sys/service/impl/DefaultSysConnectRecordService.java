package com.dstz.sys.service.impl;


import com.dstz.base.api.aop.annotion.ParamValidate;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.core.util.BeanCopierUtils;
import com.dstz.sys.api.model.SysConnectRecordDTO;
import com.dstz.sys.api.service.SysConnectRecordService;
import com.dstz.sys.core.manager.SysConnectRecordManager;
import com.dstz.sys.core.model.SysConnectRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DefaultSysConnectRecordService implements SysConnectRecordService {
    @Autowired
    SysConnectRecordManager connectRecordMananger;

    public DefaultSysConnectRecordService() {
    }

    public List<SysConnectRecordDTO> getByTargetId(String id, String type) {
        List<SysConnectRecord> list = this.connectRecordMananger.getByTargetId(id, type);
        return BeanCopierUtils.transformList(list, SysConnectRecordDTO.class);
    }

    @ParamValidate
    public void save(List<SysConnectRecordDTO> records) {
        List<SysConnectRecord> recordsList = BeanCopierUtils.transformList(records, SysConnectRecord.class);
        this.connectRecordMananger.bulkCreate(recordsList);
    }

    @ParamValidate
    public void save(SysConnectRecordDTO records) {
        this.connectRecordMananger.create(BeanCopierUtils.transformBean(records, SysConnectRecord.class));
    }

    public void removeBySourceId(String sourceId, String type) {
        this.connectRecordMananger.removeBySourceId(sourceId, type);
    }

    public void checkIsRelatedWithBusinessMessage(String targetId, String type) {
        List<SysConnectRecord> list = this.connectRecordMananger.getByTargetId(targetId, type);
        if (!list.isEmpty()) {
            Set<String> notices = new HashSet();
            StringBuilder sb = new StringBuilder();
            list.forEach((record) -> {
                if (!notices.contains(record.getNotice())) {
                    if (sb.length() > 0) {
                        sb.append("<br/>");
                    }

                    sb.append(record.getNotice());
                    notices.add(record.getNotice());
                }

            });
            throw new BusinessMessage(sb.toString());
        }
    }
}
