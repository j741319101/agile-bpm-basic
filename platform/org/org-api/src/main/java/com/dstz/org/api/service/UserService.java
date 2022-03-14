package com.dstz.org.api.service;

import com.dstz.org.api.model.IUser;
import com.dstz.org.api.model.IUserRole;
import com.dstz.org.api.model.dto.BpmUserDTO;
import com.dstz.org.api.model.dto.UserDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * 描述：用户服务接口类
 * </pre>
 */
public interface UserService {

    /**
     * 根据用户ID获取用户的对象。
     *
     * @param userId 用户ID
     * @return
     */
    IUser getUserById(String userId);


    /**
     * 根据用户帐号获取用户对象。
     *
     * @param account
     * @return
     */
    IUser getUserByAccount(String account);


    /**
     * 根据组织id和组织类型获取用户列表。
     *
     * @param groupId   组织列表
     * @param groupType 组织类型
     * @return
     */
    List<? extends IUser> getUserListByGroup(String groupType, String groupId);
    
    /**
     * 获取用户的角色关系
     * @param userId
     * @return
     */
	List<? extends IUserRole> getUserRole(String userId);

    List<? extends IUser> getAllUser();

    List<? extends IUser> getUsersByUsername(String var1);

    List<? extends IUser> getUserListByGroupPath(String var1);

    IUser getUserByOpenid(String var1);

    void updateUserOpenId(String var1, String var2);

    Map<String, ? extends IUser> getUserByIds(String var1);

    List<? extends IUser> getUsersByOrgIds(String var1);

    List<? extends IUser> getUsersByRoleIds(String var1);

    List<? extends IUser> getUsersByPostIds(String var1);

    Integer countEnabledUser();

    List<? extends IUser> getUsersByUserName(String var1, String var2, Integer var3, Integer var4);

    List<? extends IUser> getUsersByOrgPath(String var1);

    List<? extends IUser> getUserByAccounts(String var1);

    List<? extends IUser> getUsersByGroup(String var1, String var2);

    List<? extends IUser> getUsersByMobiles(String var1);

//    PageResultDto getUsersByUserQuery(UserQueryDTO var1);

    String addUser(UserDTO var1);

    Integer editUser(UserDTO var1);

    boolean isAdmin(IUser var1);

    List<BpmUserDTO> getUserOrgInfos(String var1);

    Map<String, String> getUserMapByUserIds(Set<String> var1);
}
