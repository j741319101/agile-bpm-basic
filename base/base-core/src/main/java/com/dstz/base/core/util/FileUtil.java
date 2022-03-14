/*     */ package com.dstz.base.core.util;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import cn.hutool.core.io.IoUtil;
import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ public class FileUtil {
/*     */   public static void deleteFiles(String filePath) {
/*  15 */     File file = new File(filePath);
/*  16 */     if (!file.exists()) {
/*     */       return;
/*     */     }
/*  19 */     if (file.isDirectory()) {
/*  20 */       String[] files = file.list();
/*  21 */       if (files.length > 0) {
/*  22 */         for (String childName : files) {
/*  23 */           deleteFiles(filePath + "/" + childName);
/*     */         }
/*     */       }
/*     */     } 
/*  27 */     file.delete();
/*     */   }
/*     */   
/*     */   private static final int BUFFER_SIZE = 2048;
/*     */   
/*     */   public static void toZipDir(String srcDir, String outPath, boolean KeepDirStructure) throws Exception {
/*  33 */     OutputStream out = new FileOutputStream(outPath);
/*  34 */     ZipOutputStream zos = null;
/*     */     try {
/*  36 */       zos = new ZipOutputStream(out);
/*  37 */       File sourceFile = new File(srcDir);
/*  38 */       compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
/*  39 */     } catch (Exception e) {
/*  40 */       throw new RuntimeException("zip error from ZipUtils", e);
/*     */     } finally {
/*  42 */       if (zos != null) {
/*     */         try {
/*  44 */           zos.close();
/*  45 */         } catch (IOException e) {
/*  46 */           e.printStackTrace();
/*     */         } 
/*     */       }
/*  49 */       out.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void toZipFiles(File[] listFiles, OutputStream out, boolean KeepDirStructure) throws Exception {
/*  54 */     ZipOutputStream zos = new ZipOutputStream(out);
/*  55 */     if (listFiles == null || listFiles.length == 0) {
/*  56 */       if (KeepDirStructure) {
/*  57 */         zos.putNextEntry(new ZipEntry("/"));
/*  58 */         zos.closeEntry();
/*     */       } 
/*     */     } else {
/*  61 */       for (File file : listFiles) {
/*  62 */         if (KeepDirStructure) {
/*  63 */           compress(file, zos, "/" + file.getName(), KeepDirStructure);
/*     */         } else {
/*  65 */           compress(file, zos, file.getName(), KeepDirStructure);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void unZipFiles(File zipFile, String descDir) throws IOException {
/*  72 */     File pathFile = new File(descDir);
/*  73 */     if (!pathFile.exists()) {
/*  74 */       pathFile.mkdirs();
/*     */     }
/*  76 */     ZipFile zip = new ZipFile(zipFile, Charset.forName("UTF-8"));
/*  77 */     for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
/*  78 */       ZipEntry entry = entries.nextElement();
/*  79 */       String zipEntryName = entry.getName();
/*  80 */       InputStream in = zip.getInputStream(entry);
/*  81 */       String outPath = (descDir + "/" + zipEntryName).replaceAll("\\*", "/");
/*  82 */       File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
/*  83 */       if (!file.exists()) {
/*  84 */         file.mkdirs();
/*     */       }
/*  86 */       if ((new File(outPath)).isDirectory()) {
/*     */         continue;
/*     */       }
/*  89 */       OutputStream out = new FileOutputStream(outPath);
/*  90 */       byte[] buf1 = new byte[1024];
/*     */       int len;
/*  92 */       while ((len = in.read(buf1)) > 0) {
/*  93 */         out.write(buf1, 0, len);
/*     */       }
/*  95 */       in.close();
/*  96 */       out.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception {
/* 101 */     byte[] buf = new byte[2048];
/* 102 */     if (sourceFile.isFile()) {
/* 103 */       zos.putNextEntry(new ZipEntry(name));
/*     */       
/* 105 */       FileInputStream in = new FileInputStream(sourceFile); int len;
/* 106 */       while ((len = in.read(buf)) != -1) {
/* 107 */         zos.write(buf, 0, len);
/*     */       }
/* 109 */       zos.closeEntry();
/* 110 */       in.close();
/*     */     } else {
/* 112 */       File[] listFiles = sourceFile.listFiles();
/* 113 */       if (listFiles == null || listFiles.length == 0) {
/* 114 */         if (KeepDirStructure) {
/* 115 */           zos.putNextEntry(new ZipEntry(name + "/"));
/* 116 */           zos.closeEntry();
/*     */         } 
/*     */       } else {
/* 119 */         for (File file : listFiles) {
/* 120 */           if (KeepDirStructure) {
/* 121 */             compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
/*     */           } else {
/* 123 */             compress(file, zos, file.getName(), KeepDirStructure);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static File multipartFileToFile(MultipartFile file) throws Exception {
/* 131 */     File toFile = null;
/* 132 */     if (file.equals("") || file.getSize() <= 0L) {
/* 133 */       file = null;
/*     */     } else {
/* 135 */       InputStream ins = null;
/* 136 */       ins = file.getInputStream();
/* 137 */       toFile = new File(file.getOriginalFilename());
/* 138 */       inputStreamToFile(ins, toFile);
/* 139 */       ins.close();
/*     */     } 
/* 141 */     return toFile;
/*     */   }
/*     */   
/*     */   public static void inputStreamToFile(InputStream ins, File file) {
/*     */     try {
/* 146 */       OutputStream os = new FileOutputStream(file);
/* 147 */       int bytesRead = 0;
/* 148 */       byte[] buffer = new byte[8192];
/* 149 */       while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
/* 150 */         os.write(buffer, 0, bytesRead);
/*     */       }
/* 152 */       os.close();
/* 153 */       ins.close();
/* 154 */     } catch (Exception e) {
/* 155 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyFolder(String resource, String target) throws Exception {
/* 167 */     File resourceFile = new File(resource);
/* 168 */     if (!resourceFile.exists()) {
/* 169 */       throw new Exception("源目标路径：[" + resource + "] 不存在...");
/*     */     }
/* 171 */     File targetFile = new File(target);
/* 172 */     if (!targetFile.exists()) {
/* 173 */       targetFile.mkdirs();
/*     */     }
/* 175 */     File[] resourceFiles = resourceFile.listFiles();
/* 176 */     for (File file : resourceFiles) {
/* 177 */       File file1 = new File(targetFile.getAbsolutePath() + File.separator + file.getName());
/* 178 */       if (file.isFile()) {
/* 179 */         InputStream io = new FileInputStream(file);
/* 180 */         OutputStream out = new FileOutputStream(file1);
/* 181 */         IoUtil.copy(io, out);
/* 182 */         out.flush();
/* 183 */         io.close();
/* 184 */         out.close();
/*     */       } 
/* 186 */       if (file.isDirectory()) {
/* 187 */         if (!file1.exists()) {
/* 188 */           file1.mkdirs();
/*     */         }
/* 190 */         copyFolder(file.getAbsolutePath(), file1.getAbsolutePath());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void moveTargetFolderFromNewResource(String oldTarget, String lastResource, String mergeDir) throws Exception {
/* 202 */     File lastResourceFile = new File(lastResource);
/* 203 */     File targetFile = new File(oldTarget);
/* 204 */     if (!targetFile.exists()) {
/* 205 */       throw new Exception("目标路径：[" + oldTarget + "] 不存在...");
/*     */     }
/* 207 */     if (!lastResourceFile.exists()) {
/*     */       return;
/*     */     }
/* 210 */     File mergeFile = new File(mergeDir);
/* 211 */     if (!mergeFile.exists()) {
/* 212 */       mergeFile.mkdirs();
/*     */     }
/* 214 */     File[] targetFiles = targetFile.listFiles();
/* 215 */     for (File file : targetFiles) {
/* 216 */       File newFile = new File(lastResourceFile.getAbsolutePath() + File.separator + file.getName());
/* 217 */       File merge = new File(mergeFile.getAbsoluteFile() + File.separator + file.getName());
/* 218 */       if (file.isFile()) {
/* 219 */         InputStream io = new FileInputStream(newFile);
/* 220 */         OutputStream out = new FileOutputStream(merge);
/* 221 */         IoUtil.copy(io, out);
/* 222 */         out.flush();
/* 223 */         io.close();
/* 224 */         out.close();
/* 225 */         newFile.delete();
/*     */       } 
/* 227 */       if (file.isDirectory()) {
/* 228 */         moveTargetFolderFromNewResource(file.getAbsolutePath(), newFile.getAbsolutePath(), merge.getAbsolutePath());
/* 229 */         if ((newFile.list()).length == 0)
/* 230 */           newFile.delete(); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/FileUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */