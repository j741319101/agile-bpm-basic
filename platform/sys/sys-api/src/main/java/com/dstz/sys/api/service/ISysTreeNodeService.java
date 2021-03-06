package com.dstz.sys.api.service;

import com.dstz.sys.api.model.ISysTreeNode;

import java.util.List;

/**
 * <pre>
 * 描述：SysTreeNodeService接口
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年3月28日 下午3:31:25
 * 版权:summer
 * </pre>
 */
public interface ISysTreeNodeService {
    /**
     * <pre>
     * 根据id获取对象
     * </pre>
     *
     * @param id
     * @return
     */
    ISysTreeNode getById(String id);

    List<? extends ISysTreeNode> getTreeNodesByType(String var1);

    List<? extends ISysTreeNode> getTreeNodesByNodeId(String var1);

    String creatByTreeKey(String var1, String var2);
}
