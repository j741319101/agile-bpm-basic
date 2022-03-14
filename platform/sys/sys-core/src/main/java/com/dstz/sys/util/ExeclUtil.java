/*    */ package com.dstz.sys.util;
/*    */ import java.io.OutputStream;
import java.util.Iterator;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.poi.hssf.usermodel.HSSFCell;
/*    */ import org.apache.poi.hssf.usermodel.HSSFRow;
/*    */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*    */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*    */ 
/*    */ public class ExeclUtil {
/* 12 */   static int sheetsize = 5000;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void ListtoExecl(List<Map> data, OutputStream out, LinkedHashMap<String, String> fields) throws Exception {
/* 23 */     HSSFWorkbook workbook = new HSSFWorkbook();
/*    */     
/* 25 */     if (data == null || data.size() == 0) {
/* 26 */       throw new Exception("导入的数据为空");
/*    */     }
/*    */     
/* 29 */     int pages = data.size() / sheetsize;
/* 30 */     if (data.size() % sheetsize > 0) {
/* 31 */       pages++;
/*    */     }
/*    */     
/* 34 */     String[] egtitles = new String[fields.size()];
/* 35 */     String[] cntitles = new String[fields.size()];
/* 36 */     Iterator<String> it = fields.keySet().iterator();
/* 37 */     int count = 0;
/* 38 */     while (it.hasNext()) {
/* 39 */       String egtitle = it.next();
/* 40 */       String cntitle = fields.get(egtitle);
/* 41 */       egtitles[count] = egtitle;
/* 42 */       cntitles[count] = cntitle;
/* 43 */       count++;
/*    */     } 
/*    */     
/* 46 */     for (int i = 0; i < pages; i++) {
/* 47 */       int rownum = 0;
/*    */       
/* 49 */       int startIndex = i * sheetsize;
/* 50 */       int endIndex = ((i + 1) * sheetsize - 1 > data.size()) ? data.size() : ((i + 1) * sheetsize - 1);
/*    */ 
/*    */       
/* 53 */       HSSFSheet sheet = workbook.createSheet();
/* 54 */       HSSFRow row = sheet.createRow(rownum);
/*    */ 
/*    */       
/* 57 */       for (int f = 0; f < cntitles.length; f++) {
/* 58 */         HSSFCell cell = row.createCell(f);
/* 59 */         cell.setCellValue(cntitles[f]);
/*    */       } 
/* 61 */       rownum++;
/*    */       
/* 63 */       for (int j = startIndex; j < endIndex; j++) {
/* 64 */         row = sheet.createRow(rownum);
/* 65 */         Map<String, Object> item = data.get(j);
/* 66 */         for (int h = 0; h < cntitles.length; h++) {
/* 67 */           Object o = item.get(egtitles[h]);
/* 68 */           String value = (o == null) ? "" : o.toString();
/* 69 */           HSSFCell cell = row.createCell(h);
/* 70 */           cell.setCellValue(value);
/*    */         } 
/* 72 */         rownum++;
/*    */       } 
/*    */     } 
/*    */     
/* 76 */     workbook.write(out);
///* 77 */     workbook.close();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/util/ExeclUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */