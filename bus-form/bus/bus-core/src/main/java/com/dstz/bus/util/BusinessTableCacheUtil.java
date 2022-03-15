/*    */ package com.dstz.bus.util;
/*    */ 
/*    */ import com.dstz.bus.model.BusinessTable;
/*    */ import com.dstz.base.core.cache.ICache;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BusinessTableCacheUtil
/*    */ {
/* 20 */   private static String businessTableMap = "businessTableMap";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void put(BusinessTable businessTable) {
/* 27 */     Map<String, BusinessTable> map = (Map<String, BusinessTable>)((ICache)AppUtil.getBean(ICache.class)).getByKey(businessTableMap);
/* 28 */     if (map == null) {
/* 29 */       map = new HashMap<>();
/*    */     }
/* 31 */     map.put(businessTable.getKey(), businessTable);
/* 32 */     ((ICache)AppUtil.getBean(ICache.class)).add(businessTableMap, map);
/*    */   }
/*    */   
/*    */   public static BusinessTable get(String key) {
/* 36 */     Map<String, BusinessTable> map = (Map<String, BusinessTable>)((ICache)AppUtil.getBean(ICache.class)).getByKey(businessTableMap);
/* 37 */     if (map == null) {
/* 38 */       return null;
/*    */     }
/* 40 */     return map.get(key);
/*    */   }
/*    */   
/*    */   public static void clean() {
/* 44 */     Map<String, BusinessTable> map = (Map<String, BusinessTable>)((ICache)AppUtil.getBean(ICache.class)).getByKey(businessTableMap);
/* 45 */     if (map == null) {
/*    */       return;
/*    */     }
/* 48 */     map.clear();
/*    */   }
/*    */   
/*    */   public static void removeByKey(String key) {
/* 52 */     Map<String, BusinessTable> map = (Map<String, BusinessTable>)((ICache)AppUtil.getBean(ICache.class)).getByKey(businessTableMap);
/* 53 */     if (map == null) {
/*    */       return;
/*    */     }
/* 56 */     map.remove(key);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/util/BusinessTableCacheUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */