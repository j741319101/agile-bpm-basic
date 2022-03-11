package com.dstz.base.api.constant;


import com.dstz.base.api.exception.BusinessException;

public enum EventEnum {
    ADD_USER("add_user", "org_custom", "用户模块-添加用户");

    private String key;
    private String module;
    private String desc;

    private EventEnum(String key, String module, String desc) {
        this.key = key;
        this.module = module;
        this.desc = desc;
    }

    public String getKey() {
        return this.key;
    }

    public String getDesc() {
        return this.desc;
    }

    public boolean equalsWithKey(String key) {
        return this.key.equals(key);
    }

    public static EventEnum getByKey(String key) {
        EventEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            EventEnum type = var1[var3];
            if (type.getKey().equals(key)) {
                return type;
            }
        }

        throw new BusinessException(String.format("找不到key为[%s]的模块", key));
    }

    public boolean equalsWithModule(String module) {
        return this.module.equals(module);
    }
}