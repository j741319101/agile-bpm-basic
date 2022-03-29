package com.dstz.bus.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONArray;
import java.util.Map;

import com.dstz.base.core.util.StringUtil;
import com.dstz.base.core.util.ThreadMapUtil;
import com.dstz.bus.api.constant.RightsType;
import com.dstz.bus.api.service.IBusinessPermissionService;
import com.dstz.bus.manager.BusinessObjectManager;
import com.dstz.bus.manager.BusinessPermissionManager;
import com.dstz.bus.model.BusinessPermission;
import com.dstz.bus.model.permission.AbstractPermission;
import com.dstz.bus.model.permission.BusColumnPermission;
import com.dstz.bus.model.permission.BusObjPermission;
import com.dstz.bus.model.permission.BusTablePermission;
import com.dstz.sys.api.permission.PermissionCalculatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BusinessPermissionService implements IBusinessPermissionService
{
	@Autowired
	BusinessPermissionManager businessPermissionManager;
	@Autowired
	BusinessObjectManager businessObjectManager;

	public BusinessPermission getByObjTypeAndObjVal(String defId, String objType, String objVal, String defalutBoKeys, boolean calculate) {
		BusinessPermission businessPermission = null;
		if (StringUtil.isNotEmpty(defalutBoKeys)) {
			businessPermission = this.businessPermissionManager.getByObjTypeAndObjVal(defId, objType, objVal, defalutBoKeys);
		} else {
			businessPermission = this.businessPermissionManager.getByObjTypeAndObjVal(defId, objType, objVal);
		}
		if (businessPermission == null) {
			return new BusinessPermission();
		}

		if (calculate) {
			calculateResult(businessPermission);
		}
		return businessPermission;
	}


	private void calculateResult(BusinessPermission businessPermission) {
		for (Map.Entry<String, BusObjPermission> entry : (Iterable<Map.Entry<String, BusObjPermission>>)businessPermission.getBusObjMap().entrySet()) {
			BusObjPermission busObjPermission = entry.getValue();
			calculateResult((AbstractPermission)busObjPermission);
			for (Map.Entry<String, BusTablePermission> etry : (Iterable<Map.Entry<String, BusTablePermission>>)busObjPermission.getTableMap().entrySet()) {
				BusTablePermission busTablePermission = etry.getValue();

				if (CollectionUtil.isEmpty(busTablePermission.getRights())) {
					busTablePermission.setResult(busObjPermission.getResult());
				} else {
					calculateResult((AbstractPermission)busTablePermission);
				}

				for (Map.Entry<String, BusColumnPermission> ery : (Iterable<Map.Entry<String, BusColumnPermission>>)busTablePermission.getColumnMap().entrySet()) {
					BusColumnPermission busColumnPermission = ery.getValue();

					if (MapUtil.isEmpty(busColumnPermission.getRights())) {
						busColumnPermission.setResult(busTablePermission.getResult()); continue;
					}
					calculateResult((AbstractPermission)busColumnPermission);
				}
			}
		}
	}










	private void calculateResult(AbstractPermission permission) {
		for (RightsType rightsType : RightsType.values()) {
			JSONArray jsonArray = (JSONArray)permission.getRights().get(rightsType.getKey());
			boolean b = PermissionCalculatorFactory.haveRights(jsonArray);
			if (b) {
				permission.setResult(rightsType.getKey());

				break;
			}
		}
		if (StringUtil.isEmpty(permission.getResult())) {
			permission.setResult(RightsType.values()[(RightsType.values()).length - 1].getKey());
		}


		ThreadMapUtil.remove("com.dstz.sys.permission.impl.GroupPermission");
	}
}
