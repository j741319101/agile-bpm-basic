package com.dstz.sys.util;

import java.util.Locale;

import com.dstz.base.core.util.StringUtil;
import com.dstz.org.api.context.ICurrentContext;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;

import cn.hutool.core.util.StrUtil;


/**
 * 获取上下文数据对象的工具类。
 * <pre>
 *
 * </pre>
 */
public class ContextUtil {
    private static ContextUtil contextUtil;
    private ICurrentContext currentContext;

    public ContextUtil() {
    }

    public void setCurrentContext(ICurrentContext _currentContext) {
        contextUtil = this;
        contextUtil.currentContext = _currentContext;
    }

    public static IUser getCurrentUser() {
        return contextUtil.currentContext.getCurrentUser();
    }

    public static String getCurrentUserId() {
        return contextUtil.currentContext.getCurrentUserId();
    }

    public static String getCurrentUserName() {
        IUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getFullname() : null;
    }

    public static String getCurrentUserAccount() {
        IUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getAccount() : null;
    }

    public static IGroup getCurrentGroup() {
        return contextUtil.currentContext.getCurrentGroup();
    }

    public static String getCurrentGroupId() {
        IGroup iGroup = getCurrentGroup();
        return iGroup != null ? iGroup.getGroupId() : "";
    }

    public static String getCurrentGroupName() {
        IGroup iGroup = getCurrentGroup();
        return iGroup != null ? iGroup.getGroupName() : "";
    }

    public static Locale getLocale() {
        return contextUtil.currentContext.getLocale();
    }

    public static void clearCurrentUser() {
        contextUtil.currentContext.clearCurrentUser();
    }

    public static void setCurrentUser(IUser user) {
        contextUtil.currentContext.setCurrentUser(user);
    }

    public static void setCurrentUserByAccount(String account) {
        contextUtil.currentContext.setCurrentUserByAccount(account);
    }

    public static void setCurrentOrg(IGroup group) {
        contextUtil.currentContext.cacheCurrentGroup(group);
    }

    public static void clearUserRedisCache(String userId) {
        if (StringUtil.isEmpty(userId)) {
            userId = getCurrentUserId();
        }

        contextUtil.currentContext.clearUserRedisCache(userId);
    }

    public static void setLocale(Locale locale) {
        contextUtil.currentContext.setLocale(locale);
    }

    public static void cleanLocale() {
        contextUtil.currentContext.clearLocale();
    }

    public static void clearAll() {
        cleanLocale();
        clearCurrentUser();
    }

    public static boolean isAdmin(IUser user) {
        return contextUtil.currentContext.isAdmin(user);
    }

    public static boolean currentUserIsAdmin() {
        IUser user = getCurrentUser();
        return isAdmin(user);
    }
}
