package com.dstz.sys.api.platform;

import java.util.Map;

public interface ISysPropertiesPlatFormService {
    String getByAlias(String var1);

    Map<String, Map<String, String>> reloadProperty();
}
