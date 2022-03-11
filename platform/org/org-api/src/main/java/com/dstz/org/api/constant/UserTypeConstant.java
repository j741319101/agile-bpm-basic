package com.dstz.org.api.constant;

public enum UserTypeConstant {
    NORMAL("1", "普通用户"),
    MANAGER("2", "管理员"),
    EXTERNAL("3", "外部人员");

    private String key;
    private String label;

    private UserTypeConstant(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public String key() {
        return this.key;
    }

    public String label() {
        return this.label;
    }

    public static UserTypeConstant fromStr(String key) {
        UserTypeConstant[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            UserTypeConstant e = var1[var3];
            if (key.equals(e.key)) {
                return e;
            }
        }

        return null;
    }
}
