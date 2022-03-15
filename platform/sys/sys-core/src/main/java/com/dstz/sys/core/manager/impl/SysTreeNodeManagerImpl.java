package com.dstz.sys.core.manager.impl;

import java.util.*;

import javax.annotation.Resource;

import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.core.id.IdUtil;
import com.dstz.base.core.util.HanyuPinyinHelper;
import com.dstz.sys.api.model.SysNodeOrderParam;
import com.dstz.sys.core.manager.SysTreeManager;
import com.dstz.sys.core.model.SysTree;
import com.dstz.sys.util.CustomDefaultQueryFilterUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dstz.base.api.constant.Direction;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.sys.core.dao.SysTreeNodeDao;
import com.dstz.sys.core.manager.SysTreeNodeManager;
import com.dstz.sys.core.model.SysTreeNode;

/**
 * 系统树节点 Manager处理实现类
 *
 * @author aschs
 * @email aschs@qq.com
 * @time 2018-03-13 20:02:33
 */
@Service("sysTreeNodeManager")
public class SysTreeNodeManagerImpl extends BaseManager<String, SysTreeNode> implements SysTreeNodeManager {
    @Resource
    SysTreeNodeDao sysTreeNodeDao;
    @Autowired
    SysTreeManager sysTreeManager;

    @Override
    public List<SysTreeNode> getByTreeId(String treeId) {
        QueryFilter filter = new DefaultQueryFilter();
        filter.addFilter("tree_id_", treeId, QueryOP.EQUAL);
        filter.addFieldSort("sn_", Direction.ASC.getKey());
        return this.query(filter);
    }

    @Override
    public SysTreeNode getByTreeIdAndKey(String treeId, String key) {
        QueryFilter filter = new DefaultQueryFilter();
        filter.addFilter("tree_id_", treeId, QueryOP.EQUAL);
        filter.addFilter("key_", key, QueryOP.EQUAL);
        return this.queryOne(filter);
    }

    @Override
    public List<SysTreeNode> getByParentId(String parentId) {
        QueryFilter filter = new DefaultQueryFilter();
        filter.addFilter("parent_id_", parentId, QueryOP.EQUAL);
        return this.query(filter);
    }

    @Override
    public List<SysTreeNode> getStartWithPath(String path) {
        QueryFilter filter = new DefaultQueryFilter();
        filter.addFilter("path_", path , QueryOP.RIGHT_LIKE);
        return this.query(filter);
    }

    @Override
    public void removeByTreeId(String treeId) {
        sysTreeNodeDao.removeByTreeId(treeId);
    }
    
    @Override
    public void removeByPath(String path) {
    	sysTreeNodeDao.removeByPath(path);
    }

    public int chageOrder(SysNodeOrderParam sysNodeOrderParam) {
        return this.sysTreeNodeDao.chageOrder(sysNodeOrderParam);
    }

    public List<SysTreeNode> getByParentKey(String parentKey) {
        QueryFilter filter = CustomDefaultQueryFilterUtil.setDefaultQueryFilter();
        filter.addParamsFilter("parentkey", parentKey);
        filter.addFieldSort("sn_", Direction.ASC.getKey());
        return this.query(filter);
    }

    public int getCountByStartWithPath(String path) {
        return this.sysTreeNodeDao.getCountByStartWithPath(path + "%");
    }

    public void importData(XSSFWorkbook workbook, Map<String, Integer> rowConf) {
        String sheetName = "系统树节点";
        XSSFSheet sheet = workbook.getSheet(sheetName);
        this.sysTreeNodeDao.removeAll();
        Map<String, String> mapSysTreeNode = new HashMap();

        for(int i = 1; i <= sheet.getLastRowNum(); ++i) {
            XSSFRow row = sheet.getRow(i);
            String msg = "";

            try {
                SysTreeNode sysTreeNode = new SysTreeNode();
                sysTreeNode.setId(this.getCellStringData(row.getCell((Integer)rowConf.get(sheetName + "_ID"))));
                if (StringUtils.isEmpty(sysTreeNode.getId())) {
                    sysTreeNode.setId(IdUtil.getSuid());
                }

                sysTreeNode.setKey(this.getCellStringData(row.getCell((Integer)rowConf.get(sheetName + "_别名"))));
                sysTreeNode.setName(this.getCellStringData(row.getCell((Integer)rowConf.get(sheetName + "_名称"))));
                if (StringUtils.isEmpty(sysTreeNode.getName())) {
                    throw new BusinessMessage("_名称-必填");
                }

                sysTreeNode.setDesc(this.getCellStringData(row.getCell((Integer)rowConf.get(sheetName + "_描述"))));
                String treeKey = this.getCellStringData(row.getCell((Integer)rowConf.get(sheetName + "_树别名")));
                SysTree sysTree = this.sysTreeManager.getByKey(treeKey);
                if (null == sysTree) {
                    throw new BusinessMessage("系统树[" + treeKey + "]不存在");
                }

                sysTreeNode.setTreeId(sysTree.getId());
                String sn = this.getCellStringData(row.getCell((Integer)rowConf.get(sheetName + "_排序")));
                if (StringUtils.isNotEmpty(sn)) {
                    sysTreeNode.setSn(Integer.parseInt(sn));
                }

                String parentNodeId = "";
                String parentOrgPath = this.getCellStringData(row.getCell((Integer)rowConf.get(sheetName + "_父节点")));
                if ("".equals(parentOrgPath)) {
                    parentNodeId = "0";
                    sysTreeNode.setPath(sysTreeNode.getId() + ".");
                } else {
                    parentNodeId = this.findSysTreeNodeId(mapSysTreeNode, parentOrgPath, sysTreeNode.getTreeId());
                    if (StringUtils.isEmpty(parentNodeId)) {
                        throw new BusinessException("父节点[" + parentOrgPath + "]不存在");
                    }

                    SysTreeNode temp = (SysTreeNode)this.get(parentNodeId);
                    sysTreeNode.setPath(temp.getPath() + sysTreeNode.getId() + ".");
                }

                sysTreeNode.setParentId(parentNodeId);
                sysTreeNode.setIcon(this.getCellStringData(row.getCell((Integer)rowConf.get(sheetName + "_图标"))));
                sysTreeNode.setAppName(this.getCellStringData(row.getCell((Integer)rowConf.get(sheetName + "_所属应用"))));
                this.sysTreeNodeDao.create(sysTreeNode);
                msg = "操作成功";
            } catch (BusinessMessage var16) {
                msg = "操作失败," + var16.getMessage();
            } catch (Exception var17) {
                var17.printStackTrace();
                msg = "操作失败,未知错误," + var17.getMessage();
            }

            XSSFCell cellResult = row.createCell((Integer)rowConf.get(sheetName + "_操作结果"));
            cellResult.setCellValue(msg);
        }

    }

    private String getCellStringData(Cell cell) {
        String value = "";
        if (cell != null) {
            cell.setCellType(CellType.STRING);
            value = cell.getStringCellValue();
            if (StringUtils.isNotBlank(value)) {
                value = value.trim();
            }
        }

        return value;
    }

    public String creatByTreeKey(String treeKey, String nodeName) {
        SysTree sysTree = this.sysTreeManager.getByKey(treeKey);
        if (null == sysTree) {
            throw new BusinessException("分类树[" + treeKey + "]不存在");
        } else {
            DefaultQueryFilter filter = new DefaultQueryFilter();
            filter.addFilter("name_", nodeName, QueryOP.EQUAL);
            filter.addFilter("tree_id_", sysTree.getId(), QueryOP.EQUAL);
            List<SysTreeNode> lstSysTreeNode = this.query(filter);
            if (null != lstSysTreeNode && lstSysTreeNode.size() > 0) {
                return ((SysTreeNode)lstSysTreeNode.get(0)).getId();
            } else {
                SysTreeNode sysTreeNode = new SysTreeNode();
                sysTreeNode.setId(IdUtil.getSuid());
                sysTreeNode.setName(nodeName);
                sysTreeNode.setKey(HanyuPinyinHelper.getPinyinString(nodeName));
                sysTreeNode.setPath(sysTreeNode.getId() + ".");
                sysTreeNode.setTreeId(sysTree.getId());
                sysTreeNode.setParentId("0");
                this.create(sysTreeNode);
                return sysTreeNode.getId();
            }
        }
    }

    public String findSysTreeNodeId(Map<String, String> mapSysTreeNode, String mainSysTreeNodePath, String treeId) {
        String sysTreeNodeId = (String)mapSysTreeNode.get(mainSysTreeNodePath);
        if (StringUtils.isEmpty(sysTreeNodeId)) {
            String[] arrName = mainSysTreeNodePath.split(">");
            List<Map<String, String>> lstData = new ArrayList();
            String[] var7 = arrName;
            int var8 = arrName.length;

            String parentId;
            for(int var9 = 0; var9 < var8; ++var9) {
                parentId = var7[var9];
                QueryFilter filter = new DefaultQueryFilter(true);
                filter.addFilter("name_", parentId, QueryOP.EQUAL);
                filter.addFilter("tree_id_", treeId, QueryOP.EQUAL);
                List<SysTreeNode> lstSysTreeNode = this.query(filter);
                if (lstSysTreeNode.size() <= 0) {
                    throw new BusinessMessage("分类树节点：" + parentId + "不存在");
                }

                Map<String, String> temp = new HashMap();
                Iterator var14 = lstSysTreeNode.iterator();

                while(var14.hasNext()) {
                    SysTreeNode sysTreeNode = (SysTreeNode)var14.next();
                    temp.put(sysTreeNode.getId(), sysTreeNode.getParentId());
                }

                lstData.add(temp);
            }

            Iterator var16 = ((Map)lstData.get(lstData.size() - 1)).entrySet().iterator();

            label52:
            while(var16.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var16.next();
                String key = (String)entry.getKey();
                parentId = (String)entry.getValue();
                if (lstData.size() == 1 && "0".equals(parentId)) {
                    sysTreeNodeId = key;
                    break;
                }

                for(int i = lstData.size() - 2; i >= 0; --i) {
                    Map<String, String> temp = (Map)lstData.get(i);
                    String parentIdTemp = (String)temp.get(parentId);
                    if (i == 0 && "0".equals(parentIdTemp)) {
                        sysTreeNodeId = key;
                        break label52;
                    }

                    if (StringUtils.isNotEmpty(parentIdTemp)) {
                        parentId = parentIdTemp;
                    }
                }
            }

            if (StringUtils.isEmpty(sysTreeNodeId)) {
                throw new BusinessMessage("分类树节点：" + mainSysTreeNodePath + "不存在");
            }

            mapSysTreeNode.put(mainSysTreeNodePath, sysTreeNodeId);
        }

        return sysTreeNodeId;
    }
}
