package com.dstz.bus.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dstz.base.api.exception.BusinessException;
import com.dstz.form.api.constant.FormStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dstz.base.core.executor.ExecutorFactory;
import com.dstz.bus.api.constant.BusTableRelType;
import com.dstz.bus.api.model.IBusinessData;
import com.dstz.bus.api.model.IBusinessObject;
import com.dstz.bus.api.model.IBusinessPermission;
import com.dstz.bus.api.service.IBusinessDataService;
import com.dstz.bus.executor.assemblyval.AssemblyValExecuteChain;
import com.dstz.bus.executor.assemblyval.AssemblyValParam;
import com.dstz.bus.executor.parseval.ParseValExecuteChain;
import com.dstz.bus.executor.parseval.ParseValParam;
import com.dstz.bus.manager.BusinessObjectManager;
import com.dstz.bus.model.BusTableRel;
import com.dstz.bus.model.BusinessData;
import com.dstz.bus.model.BusinessObject;
import com.dstz.bus.model.permission.BusObjPermission;

@Service
public class BusinessDataService implements IBusinessDataService {
	@Autowired
	BusinessObjectManager businessObjectManager;

	@Override
	public void saveFormDefData(JSONObject data, IBusinessPermission businessPermission) {
		for (Entry<String, Object> entry : data.entrySet()) {
			String boKey = entry.getKey();
			JSONObject boData = (JSONObject) entry.getValue();
			BusinessData businessData = (BusinessData) parseBusinessData(boData, boKey);
			businessData.getBusTableRel().getBusObj().setPermission((BusObjPermission) businessPermission.getBusObj(boKey));
			BusinessDataPersistenceServiceFactory.saveData(businessData);
		}
	}

	@Override
	public JSONObject getFormDefData(IBusinessObject businessObject, Object id) {
		BusinessData businessData = (BusinessData) loadData((BusinessObject) businessObject, id, true);
		JSONObject data = new JSONObject();

		assemblyFormDefData(data, businessData);
		return data;
	}

	/**
	 * <pre>
	 * ??????formDefData?????????
	 * ??????businessData??????????????????
	 * </pre>
	 * 
	 * @param businessData
	 */
	private void initFormDefData(BusinessData businessData) {
		BusTableRel busTableRel = businessData.getBusTableRel();
		businessData.setDbData(busTableRel.getTable().initDbData());

		// ???????????????
		for (BusTableRel rel : busTableRel.getChildren()) {
			if (BusTableRelType.ONE_TO_ONE.equalsWithKey(rel.getType())) {// ???????????????????????????
				BusinessData childData = new BusinessData();
				childData.setBusTableRel(rel);
				initFormDefData(childData);
				businessData.addChildren(childData);
			}
		}

	}

	@Override
	public JSONObject assemblyFormDefData(IBusinessData businessData) {
		JSONObject data = new JSONObject();
		assemblyFormDefData(data, businessData);
		return data;
	}

	/**
	 * <pre>
	 * ??????formdefdata??????data,????????????????????????
	 * </pre>
	 * 
	 * @param data
	 * @param businessData
	 */
	private void assemblyFormDefData(JSONObject data, IBusinessData ibusinessData) {
		BusinessData businessData = (BusinessData) ibusinessData;

		AssemblyValParam param = new AssemblyValParam(data, businessData);
		ExecutorFactory.execute(AssemblyValExecuteChain.class, param);

		// ????????????
		for (Entry<String, List<BusinessData>> entry : businessData.getChildren().entrySet()) {
			String tableKey = entry.getKey();
			List<BusinessData> children = entry.getValue();
			if (BusTableRelType.ONE_TO_ONE.equalsWithKey(children.get(0).getBusTableRel().getType())) {
				JSONObject cData = new JSONObject();
				if (!children.isEmpty()) {
					cData = new JSONObject(children.get(0).getData());
				}
				assemblyFormDefData(cData, children.get(0));
				data.put(tableKey, cData);
			} else {// ????????????????????????
				JSONArray dataList = new JSONArray();
				for (BusinessData bd : children) {
					JSONObject cData = new JSONObject(bd.getData());
					assemblyFormDefData(cData, bd);
					dataList.add(cData);
				}
				data.put(tableKey + "List", dataList);
			}
		}
	}

	/**
	 * <pre>
	 * ??????FormDefData ??????data ??????businessData
	 * </pre>
	 * 
	 * @param object
	 * @param relation
	 * @param parent
	 * @return
	 */
	private BusinessData analysisFormDefData(Object object, BusTableRel relation) {
		BusinessData businessData = new BusinessData();
		businessData.setBusTableRel(relation);
		// ???????????????????????????
		if (object instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) object;
			Map<String, Object> bdData = new HashMap<>();// businessData????????????
			for (Entry<String, Object> enty : jsonObject.entrySet()) {
				// ???????????? ??? ?????? ???????????????
				if (!(enty.getValue() instanceof JSONArray) && !(enty.getValue() instanceof JSONObject)) {
					ParseValParam param = new ParseValParam(enty.getKey(), enty.getValue(), bdData, relation);
					ExecutorFactory.execute(ParseValExecuteChain.class, param);
				}

				// ???????????? ???????????????????????????
				if (enty.getValue() instanceof JSONArray) {
					String tableKey = enty.getKey().substring(0, enty.getKey().length() - 4);// ???????????????List
					BusTableRel rel = relation.find(tableKey);
					for (Object obj : (JSONArray) enty.getValue()) {// ??????
						businessData.addChildren(analysisFormDefData(obj, rel));
					}
				}
				// ???????????? ???????????????????????????
				if (enty.getValue() instanceof JSONObject) {
					BusTableRel rel = relation.find(enty.getKey());
					businessData.addChildren(analysisFormDefData(enty.getValue(), rel));
				}
			}
			businessData.setData(bdData);
		}
		return businessData;
	}

	@Override
	public void removeData(IBusinessObject businessObject, Object id) {
		BusinessDataPersistenceServiceFactory.removeData((BusinessObject) businessObject, id);
	}

	@Override
	public void saveData(IBusinessData businessData) {
		BusinessDataPersistenceServiceFactory.saveData((BusinessData) businessData);
	}

	@Override
	public IBusinessData loadData(IBusinessObject businessObject, Object id) {
		return loadData(businessObject, id, false);
	}

	@Override
	public IBusinessData loadData(IBusinessObject businessObject, Object id, boolean init) {
		BusinessData businessData = BusinessDataPersistenceServiceFactory.loadData((BusinessObject) businessObject, id);
		if (id == null && init) {
			initFormDefData(businessData);
		}
		return businessData;
	}

	@Override
	public IBusinessData parseBusinessData(JSONObject jsonObject, String boKey) {
		BusinessObject businessObject = businessObjectManager.getFilledByKey(boKey);
		return analysisFormDefData(jsonObject, businessObject.getRelation());
	}

	public void saveNewFormDefData(JSONObject data, IBusinessPermission businessPermission) throws BusinessException {
		Iterator var3 = data.entrySet().iterator();

		while(var3.hasNext()) {
			Entry<String, Object> entry = (Entry)var3.next();
			String boKey = (String)entry.getKey();
			JSONObject boData = (JSONObject)entry.getValue();
			BusinessData businessData = (BusinessData)this.parseBusinessData(boData, boKey);
			Object pk = businessData.getPk();
			if (pk != null && !pk.toString().equals("")) {
				throw new BusinessException("?????????????????????????????????", FormStatusCode.PARAM_ILLEGAL);
			}

			businessData.getBusTableRel().getBusObj().setPermission((BusObjPermission)businessPermission.getBusObj(boKey));
			BusinessDataPersistenceServiceFactory.saveData(businessData);
		}

	}


	public JSONObject getFormDefData(IBusinessObject businessObject) {
		BusinessData businessData = (BusinessData)this.loadData((BusinessObject)businessObject, true);
		JSONObject data = new JSONObject();
		this.assemblyFormDefData(data, businessData);
		return data;
	}

	public void saveData(IBusinessData businessData, JSONObject instData) {
		BusinessDataPersistenceServiceFactory.saveData((BusinessData)businessData, instData);
	}


}
