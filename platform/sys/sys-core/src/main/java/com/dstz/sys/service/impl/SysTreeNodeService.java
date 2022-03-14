package com.dstz.sys.service.impl;

import com.dstz.sys.api.model.ISysTreeNode;
import com.dstz.sys.api.service.ISysTreeNodeService;
import com.dstz.sys.core.manager.SysTreeManager;
import com.dstz.sys.core.manager.SysTreeNodeManager;
import com.dstz.sys.core.model.SysTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
@Service
public class SysTreeNodeService implements ISysTreeNodeService {
    @Autowired
    SysTreeNodeManager sysTreeNodeManager;
    @Autowired
    SysTreeManager sysTreeManager;
    @Override
    public ISysTreeNode getById(String id) {
        return sysTreeNodeManager.get(id);
    }
    public List<? extends ISysTreeNode> getTreeNodesByType(String treeKey) {
        SysTree tree = this.sysTreeManager.getByKey(treeKey);
        return tree == null ? Collections.emptyList() : this.sysTreeNodeManager.getByTreeId(tree.getId());
    }

    public List<? extends ISysTreeNode> getTreeNodesByNodeId(String nodeId) {
        ISysTreeNode node = (ISysTreeNode)this.sysTreeNodeManager.get(nodeId);
        return this.sysTreeNodeManager.getStartWithPath(node.getPath());
    }

    public String creatByTreeKey(String treeKey, String nodeName) {
        return this.sysTreeNodeManager.creatByTreeKey(treeKey, nodeName);
    }
}
