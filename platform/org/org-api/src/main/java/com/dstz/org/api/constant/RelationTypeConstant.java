package com.dstz.org.api.constant;


public enum RelationTypeConstant {
    GROUP_USER("groupUser", "机构用户"),
    USER_ROLE("userRole", "角色用户"),
    POST_USER("postUser", "岗位用户"),
    GROUP_MANAGER("groupManager", "机构管理员");

    private String key;
    private String label;

    private RelationTypeConstant(String key, String label) {
        this.setKey(key);
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static RelationTypeConstant getUserRelationTypeByGroupType(String groupType) {
        GroupTypeConstant type = GroupTypeConstant.fromStr(groupType);
        if (null != type) {
            switch(type) {
                case ORG:
                    return GROUP_USER;
                case POST:
                    return POST_USER;
                case ROLE:
                    return USER_ROLE;
            }
        }

        return null;
    }
}