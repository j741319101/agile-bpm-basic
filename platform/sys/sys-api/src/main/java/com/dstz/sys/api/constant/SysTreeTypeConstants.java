package com.dstz.sys.api.constant;


public enum SysTreeTypeConstants {
    FLOW("flow", "流程分类"),
    DICT("dict", "数据字典");

    private String key;
    private String label;

    private SysTreeTypeConstants(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public String key() {
        return this.key;
    }

    public String label() {
        return this.label;
    }

    public static SysTreeTypeConstants getByKey(String key) {
        SysTreeTypeConstants[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            SysTreeTypeConstants rights = var1[var3];
            if (rights.key.equals(key)) {
                return rights;
            }
        }

        throw new RuntimeException(String.format(" key [%s] 对应RightsObjectConstants 不存在的权限常亮定义，请核查！", key));
    }
}