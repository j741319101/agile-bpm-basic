package com.dstz.sys.core.manager;

import java.util.List;
import java.util.Map;

import com.dstz.base.manager.Manager;
import com.dstz.sys.api.model.SysNodeOrderParam;
import com.dstz.sys.core.model.SysTreeNode;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 系统树节点 Manager处理接口
 *
 * @author aschs
 * @email aschs@qq.com
 * @time 2018-03-13 20:02:33
 */
public interface SysTreeNodeManager extends Manager<String, SysTreeNode> {

    /**
     * <pre>
     * 根据树id获取节点
     * 根据sn字段升序
     * </pre>
     *
     * @param treeId
     * @return
     */
    List<SysTreeNode> getByTreeId(String treeId);

    /**
     * <pre>
     * 获取指定树下的指定节点
     * </pre>
     *
     * @param treeId
     * @param key
     * @return
     */
    SysTreeNode getByTreeIdAndKey(String treeId, String key);

    /**
     * <pre>
     * 根据父节点获取其子节点
     * 不会进行递归查询，只获取第一层
     * </pre>
     *
     * @param parentId
     * @return
     */
    List<SysTreeNode> getByParentId(String parentId);

    /**
     * <pre>
     * 获取以path开始的路径
     * </pre>
     *
     * @param path
     * @return
     */
    List<SysTreeNode> getStartWithPath(String path);

    /**
     * <pre>
     * 根据树id删除节点
     * </pre>
     *
     * @param treeId
     */
    void removeByTreeId(String treeId);
    
    /**
     * <pre>
     * 删除path下的全部节点
     * </pre>
     *
     * @param path
     */
	void removeByPath(String path);

    List<SysTreeNode> getByParentKey(String var1);

    int chageOrder(SysNodeOrderParam var1);

    int getCountByStartWithPath(String var1);

    void importData(XSSFWorkbook var1, Map<String, Integer> var2);

    String findSysTreeNodeId(Map<String, String> var1, String var2, String var3);

    String creatByTreeKey(String var1, String var2);
}
