package com.dstz.org.api.constant;


public enum CollectTypeConstant {
    COMMON_WORDS("commonWords", "常用语"),
    COLLECT("collect", "收藏菜单");

    private String key;
    private String label;

    private CollectTypeConstant(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public String key() {
        return this.key;
    }

    public String label() {
        return this.label;
    }

    public static CollectTypeConstant fromStr(String key) {
        CollectTypeConstant[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            CollectTypeConstant e = var1[var3];
            if (key.equals(e.key)) {
                return e;
            }
        }

        return null;
    }
}
