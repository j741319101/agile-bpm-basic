/*    */ package com.dstz.sys.util;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DateUtil
/*    */ {
/*    */   public static String getCurrentTime() {
/* 17 */     return convertDate(new Date());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Map<String, String> getDiff(Date start, Date end) {
/* 25 */     long nd = 86400000L;
/* 26 */     long nh = 3600000L;
/* 27 */     long nm = 60000L;
/*    */ 
/*    */     
/* 30 */     long diff = end.getTime() - start.getTime();
/*    */     
/* 32 */     long day = diff / nd;
/*    */     
/* 34 */     long hour = diff % nd / nh;
/* 35 */     Map<String, String> diffMap = new HashMap<>();
/* 36 */     diffMap.put("day", day + "");
/* 37 */     diffMap.put("hour", hour + "");
/* 38 */     return diffMap;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getDelay(int delayMin) {
/* 47 */     long currentTime = System.currentTimeMillis();
/* 48 */     currentTime += (delayMin * 60 * 1000);
/* 49 */     Date date = new Date(currentTime);
/* 50 */     return convertDate(date);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String convertDate(Date date) {
/* 58 */     if (date == null || "".equals(date)) {
/* 59 */       return null;
/*    */     }
/* 61 */     SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
/* 62 */     String dateStr = date.toString();
/* 63 */     String formatStr = "";
/*    */     
/* 65 */     try { formatStr = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(formatter.parse(dateStr));
/* 66 */       String hour = dateStr.substring(11, 13);
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 71 */       return formatStr; } catch (Exception exception) { return formatStr; } finally { Exception exception = null; }
/*    */   
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Date convertStr(String str) {
/* 80 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 81 */     Date date = null;
/*    */     try {
/* 83 */       date = sdf.parse(str);
/* 84 */     } catch (ParseException e) {
/* 85 */       e.printStackTrace();
/*    */     } 
/* 87 */     return date;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/util/DateUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */