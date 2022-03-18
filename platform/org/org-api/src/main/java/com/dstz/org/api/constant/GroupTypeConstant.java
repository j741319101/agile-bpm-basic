package com.dstz.org.api.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统支持的组类型
 */

public enum GroupTypeConstant {
    ORG("org", "组织", new String[]{"bpm", "org"}),
    POST("post", "岗位", new String[]{"bpm"}),
    ROLE("role", "角色", new String[]{"bpm"});

    private String key;
    private String label;
    private String[] tags;

    private GroupTypeConstant(String key, String label) {
        this.key = key;
        this.label = label;
    }

    private GroupTypeConstant(String key, String label, String[] tags) {
        this.key = key;
        this.label = label;
        this.tags = tags;
    }

    public String key() {
        return this.key;
    }

    public String label() {
        return this.label;
    }

    public static GroupTypeConstant fromStr(String key) {
        GroupTypeConstant[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            GroupTypeConstant e = var1[var3];
            if (key.equals(e.key)) {
                return e;
            }
        }

        return null;
    }

    public static GroupTypeConstant[] getValuesByTags(String tag) {
        List<GroupTypeConstant> groupTypeConstants = new ArrayList();
        GroupTypeConstant[] var2 = values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            GroupTypeConstant e = var2[var4];
            if (e.tags != null) {
                String[] var6 = e.tags;
                int var7 = var6.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String enumTag = var6[var8];
                    if (enumTag.equals(tag)) {
                        groupTypeConstants.add(e);
                        break;
                    }
                }
            }
        }

        return (GroupTypeConstant[])groupTypeConstants.toArray(new GroupTypeConstant[0]);
    }
}
