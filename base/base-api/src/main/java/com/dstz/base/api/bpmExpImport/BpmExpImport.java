package com.dstz.base.api.bpmExpImport;

public interface BpmExpImport {
  void bpmExport(String paramString1, String paramString2) throws Exception;
  
  void bpmImport(String paramString) throws Exception;
  
  String checkImport(String paramString) throws Exception;
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/bpmExpImport/BpmExpImport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */