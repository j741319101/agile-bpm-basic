package com.dstz.sys.rest.platform;


import com.dstz.sys.api.platform.ISysPropertiesPlatFormService;
import com.dstz.sys.api.service.PropertyService;
import com.dstz.sys.core.manager.SysPropertiesManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class SysPropertiesPlatFormServiceImpl implements ISysPropertiesPlatFormService {
    @Resource
    private PropertyService propertyService;
    @Resource
    private SysPropertiesManager sysPropertiesManager;

    public SysPropertiesPlatFormServiceImpl() {
    }

    public String getByAlias(String alias) {
        return this.propertyService.getByAlias(alias);
    }

    public Map<String, Map<String, String>> reloadProperty() {
        return this.sysPropertiesManager.reloadProperty();
    }
}