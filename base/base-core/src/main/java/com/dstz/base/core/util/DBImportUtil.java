/*     */ package com.dstz.base.core.util;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStreamReader;
import java.sql.PreparedStatement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
import java.util.List;
/*     */ import java.util.Map;
/*     */ import com.dstz.base.core.util.AppUtil;
import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ 
/*     */ public class DBImportUtil {
/*     */   public static void importTable(String filePath, String tableName) throws Exception {
/*  16 */     importTable(filePath, tableName, "ID_", null);
/*     */   }
/*     */   
/*     */   public static void importTable(String filePath, String tableName, String keyColumn) throws Exception {
/*  20 */     importTable(filePath, tableName, keyColumn, null);
/*     */   }
/*     */   
/*     */   public static void importTable(String filePath, String tableName, Map<String, Object> coverValue) throws Exception {
/*  24 */     importTable(filePath, tableName, "ID_", coverValue);
/*     */   }
/*     */   
/*     */   public static void importTable(String filePath, String tableName, String keyColumn, Map<String, Object> coverValue) throws Exception {
/*  28 */     File file = new File(filePath + "/" + tableName + ".txt");
/*  29 */     if (!file.exists()) {
/*     */       return;
/*     */     }
/*  32 */     JdbcTemplate jdbcTemplate = AppUtil.<JdbcTemplate>getBean(JdbcTemplate.class);
/*  33 */     BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
/*  34 */     StringBuffer buffer = new StringBuffer();
/*  35 */     String deleteSql = "DELETE FROM " + tableName + " WHERE " + keyColumn + "  = ? ";
/*  36 */     String line = "";
/*  37 */     while ((line = in.readLine()) != null) {
/*  38 */       buffer.append(line);
/*     */     }
/*  40 */     in.close();
/*  41 */     String bpmDef = buffer.toString();
/*  42 */     JSONArray jsonArray = JSON.parseArray(bpmDef);
/*  43 */     List<Map> params = jsonArray.toJavaList(Map.class);
/*  44 */     StringBuffer sbInsert = new StringBuffer();
/*  45 */     StringBuffer sbUpdate = new StringBuffer();
/*  46 */     StringBuffer value = new StringBuffer();
/*  47 */     StringBuffer keyV = new StringBuffer();
/*  48 */     Map<Integer, String> blobIndex = new HashMap<>();
/*  49 */     params.forEach(map -> {
/*     */           boolean init = false;
/*     */           List<Object> param = new ArrayList();
/*     */           if (sbInsert.length() == 0) {
/*     */             sbInsert.append("INSERT INTO " + tableName + " ( ");
/*     */             sbUpdate.append("UPDATE " + tableName + " SET ");
/*     */             init = true;
/*     */           } 
/*     */           for (Object key : map.keySet()) {
/*     */             if (init) {
/*     */               if (StringUtils.startsWith(key.toString(), "BLOB#")) {
/*     */                 blobIndex.put(Integer.valueOf(param.size()), map.get(key).toString());
/*     */                 sbInsert.append(key.toString().split("#")[1] + ",");
/*     */                 sbUpdate.append(key.toString().split("#")[1] + " = ? ,");
/*     */               } else {
/*     */                 sbInsert.append(key + ",");
/*     */                 sbUpdate.append(key + " = ? ,");
/*     */               } 
/*     */               value.append("?,");
/*     */             } 
/*     */             if (coverValue != null && coverValue.containsKey(key.toString())) {
/*     */               param.add(coverValue.get(key.toString()));
/*     */             } else {
/*     */               param.add(map.get(key));
/*     */             } 
/*     */             if (StringUtils.equalsIgnoreCase(keyColumn, key.toString())) {
/*     */               try {
/*     */                 jdbcTemplate.update(deleteSql, new Object[] { map.get(key) });
/*  77 */               } catch (Exception e) {
/*     */                 keyV.setLength(0);
/*     */                 keyV.append(map.get(key));
/*     */               } 
/*     */             }
/*     */           } 
/*     */           if (init) {
/*     */             sbInsert.delete(sbInsert.length() - 1, sbInsert.length()).append(")").append("values (").append(value.substring(0, value.length() - 1)).append(")");
/*     */             sbUpdate.delete(sbUpdate.length() - 1, sbUpdate.length()).append(" WHERE ").append(keyColumn).append(" = ?");
/*     */           } 
/*     */           String sql = null;
/*     */           if (keyV.length() > 0) {
/*     */             param.add(keyV.toString());
/*     */             keyV.setLength(0);
/*     */             sql = sbUpdate.toString();
/*     */           } else {
/*     */             sql = sbInsert.toString();
/*     */           } 
/*     */
/*     */           jdbcTemplate.update(sql);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void cleanTable(String filePath, String tableName, String keyColumn) throws Exception {
/* 116 */     File file = new File(filePath + "/" + tableName + ".txt");
/* 117 */     if (!file.exists()) {
/*     */       return;
/*     */     }
/* 120 */     JdbcTemplate jdbcTemplate = AppUtil.<JdbcTemplate>getBean(JdbcTemplate.class);
/* 121 */     BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
/* 122 */     StringBuffer buffer = new StringBuffer();
/* 123 */     String deleteSql = "DELETE FROM " + tableName + " WHERE " + keyColumn + "  = ? ";
/* 124 */     String line = "";
/* 125 */     while ((line = in.readLine()) != null) {
/* 126 */       buffer.append(line);
/*     */     }
/* 128 */     in.close();
/* 129 */     String bpmDef = buffer.toString();
/* 130 */     JSONArray jsonArray = JSON.parseArray(bpmDef);
/* 131 */     List<Map> params = jsonArray.toJavaList(Map.class);
/* 132 */     params.forEach(map -> {
/*     */           for (Object key : map.keySet()) {
/*     */             if (StringUtils.equalsIgnoreCase(keyColumn, key.toString())) {
/*     */               jdbcTemplate.update(deleteSql, new Object[] { map.get(key) });
/*     */             }
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public static String checkTable(String filePath, String tableName, String keyColumn, String showColumn) throws Exception {
/* 142 */     File file = new File(filePath + "/" + tableName + ".txt");
/* 143 */     if (!file.exists()) {
/* 144 */       return "";
/*     */     }
/* 146 */     JdbcTemplate jdbcTemplate = AppUtil.<JdbcTemplate>getBean(JdbcTemplate.class);
/* 147 */     BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
/* 148 */     StringBuffer buffer = new StringBuffer();
/*     */     
/* 150 */     StringBuffer selectSql = (new StringBuffer("SELECT ")).append(showColumn).append(" FROM ").append(tableName).append(" WHERE ").append(keyColumn + " in ( ");
/* 151 */     String line = "";
/* 152 */     while ((line = in.readLine()) != null) {
/* 153 */       buffer.append(line);
/*     */     }
/* 155 */     in.close();
/* 156 */     String bpmDef = buffer.toString();
/* 157 */     JSONArray jsonArray = JSON.parseArray(bpmDef);
/* 158 */     List<Map> params = jsonArray.toJavaList(Map.class);
/* 159 */     List<Object> param = new ArrayList();
/* 160 */     params.forEach(map -> {
/*     */           Object value = map.get(keyColumn);
/*     */           if (value != null) {
/*     */             param.add(value);
/*     */             selectSql.append("?,");
/*     */           } 
/*     */         });
/* 167 */     if (param.size() > 0) {
/* 168 */       selectSql.delete(selectSql.length() - 1, selectSql.length()).append(")");
/* 169 */       List<Map<String, Object>> exists = jdbcTemplate.queryForList(selectSql.toString(), param.toArray());
/* 170 */       if (exists.size() > 0) {
/* 171 */         StringBuffer showExist = (new StringBuffer(tableName)).append(" 已存在：");
/* 172 */         exists.forEach(value -> showExist.append(value.get(showColumn)).append(","));
/* 173 */         return showExist.delete(showExist.length() - 1, showExist.length()).toString();
/*     */       } 
/*     */     } 
/* 176 */     return "";
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/DBImportUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */