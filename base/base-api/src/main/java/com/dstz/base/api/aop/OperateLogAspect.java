package com.dstz.base.api.aop;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;

/**
 * todo 日志记录切面，记录操作日志（服务启动时需要注入带有日志相关表的数据源）
 *
 * @author guolihao
 * @date 2020/9/18 13:30
 */
//@Aspect
//@Component
//public class OperateLogAspect {
//
//    private Logger LOGGER = LoggerFactory.getLogger(OperateLogAspect.class);
//
//    /**
//     * 是否启用mq
//     */
//    @Value("${ecloud.log.mq.enabled:false}")
//    private boolean mqEnabled;
//    /**
//     * 数据源别名
//     */
//    @Value("${ecloud.log.dataSource.dbAlias:}")
//    private String dbAlias;
//    /**
//     * 数据源的类型：oracle,mysql,dmsql
//     */
//    @Value("${ecloud.log.dataSource.dbType:}")
//    private String dbType;
//
//    @Resource
//    private LogOperateManager logOperateManager;
//
//    @Autowired(required = false)
//    private JmsProducer jmsProducer;
//
//    @Resource
//    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
//
//    @Value("${ecloud.log.thirdSysLog.isOpen:false}")
//    private boolean isOpenThirdSysLog;
//
//    @Value("${ecloud.log.thirdSysLog.ip:}")
//    private String thirdSysLogIp;
//
//    @Value("${ecloud.log.thirdSysLog.port:}")
//    private int thirdSysLogPort;
//
//    @Pointcut("@annotation(cn.gwssi.ecloudframework.base.api.aop.annotion.OperateLog)")
//    public void operateLogPoint() {
//    }
//
//    @AfterReturning(value = "@annotation(operateLog)", returning = "result")
//    public void doRecordLogAfter(JoinPoint pjp, OperateLog operateLog, Object result) {
//        try {
//            LogOperate logOperate = new LogOperate();
//            logOperate.setId(IdUtil.getSuid());
//            //操作用户
//            IUser currentUser = ContextUtil.getCurrentUser();
//            if(Objects.nonNull(currentUser)){
//                logOperate.setAccount(currentUser.getFullname());
//                logOperate.setUserId(currentUser.getUserId());
//            }
//            //写入请求信息
//            writeRequest(logOperate, pjp, operateLog);
//            //操作时间
//            logOperate.setOperateTime(new Date());
//            //返回结果
//            if (result instanceof ResultMsg) {
//                ResultMsg resultMsg = (ResultMsg) result;
//                if (Objects.nonNull(resultMsg)) {
//                    //记录备份日志
//                    String method = pjp.getSignature().getName();
//                    if(method.startsWith("exportLog")){
//                        String fileName = resultMsg.getData().toString();
//                        logOperate.setBackupFileName(fileName.substring(0,fileName.indexOf(".")));
//                        logOperate.setBackupFileType(fileName.substring(fileName.indexOf(".")+1));
//                        logOperate.setLogType(2);
//                    }
//                    if (resultMsg.getIsOk()) {
//                        logOperate.setResult(1);
//                    } else {
//                        logOperate.setResult(0);
//                    }
//                }
//            }
//            //写入返回信息
//            if (operateLog.writeResponse()) {
//                String responseResult = JSONObject.toJSONString(result);
//                logOperate.setResponseResultData(responseResult);
//            }
//            logOperate.setCreateTime(new Date());
//            //写入特殊处理接口参数
//            writeHandler(logOperate,pjp);
//            //记录日志
//            threadPoolTaskExecutor.execute(() -> recordLopOperate(logOperate));
//        }catch (Exception ex){
//            LOGGER.error("日志记录切面，记录操作日志异常：",ex);
//            //TODO
//        }
//    }
//
//    /**
//     * 特殊接口处理，通过handler返回值来保存配置项
//     *
//     * @param logOperate 日志记录信息
//     * @param pjp 切面拦截方法上下文
//     * @author guolihao
//     * @date 2020/11/16 16:45
//     */
//    private void writeHandler(LogOperate logOperate, JoinPoint pjp) throws IOException {
//        Object target = pjp.getTarget();
//        if(target instanceof OperateLogHandler){
//            OperateLogHandler handler = (OperateLogHandler) target;
//            Object[] objects = pjp.getArgs();
//            if(objects.length>0){
//                Object config = handler.getLogConfig(objects);
//                if(config instanceof Map){
//                    Map map = (Map) config;
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//                    String outJson = objectMapper.writeValueAsString(map);
//                    ObjectReader objectReader = objectMapper.readerForUpdating(logOperate);
//                    objectReader.readValue(outJson);
//                }
//            }
//        }
//    }
//
//    /**
//     * 记录操作日志
//     *
//     * @param logOperate 操作日志记录信息
//     * @return
//     * @author guolihao
//     * @date 2020/9/28 17:19
//     */
//    private void recordLopOperate(LogOperate logOperate) {
//        if (mqEnabled) {
//            jmsProducer.sendToQueue(new DefaultJmsDTO("log", logOperate));
//        } else {
//            DbContextHolder.setDataSource(dbAlias, dbType);
//            logOperateManager.create(logOperate);
//        }
//
//        //todo 发送给日志系统
//        if (isOpenThirdSysLog){
//            sendThirdSysLog(logOperate);
//        }
//    }
//
//    private void sendThirdSysLog(LogOperate logOperate) {
//        System.out.println("发送日志到第三方==== "+ JSON.toJSONString(logOperate) + isOpenThirdSysLog + thirdSysLogIp+ thirdSysLogPort);
//        SyslogIF syslog = Syslog.getInstance(SyslogConstants.UDP);// 协议
//        syslog.getConfig().setHost(thirdSysLogIp);// 接收服务器
//        syslog.getConfig().setPort(thirdSysLogPort);// 端口
//        syslog.getConfig().setMaxMessageLength(1024000);
//        threadPoolTaskExecutor.execute(()->{
//            try {
//                JSONObject jsonObject = JSONObject.parseObject(logOperate.toString());
//                jsonObject.put("requestTime",String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS",logOperate.getOperateTime()));
//                jsonObject.put("application","电子公文");
//                System.out.print("开始发送日志："+jsonObject.toString());
//                long startMilli=System.currentTimeMillis();// 当前时间对应的毫秒数
//                syslog.log(0, URLDecoder.decode(jsonObject.toString(), "utf-8"));
//                long endMilli=System.currentTimeMillis();
//                System.out.println("发送日志成功，总耗时为："+(endMilli-startMilli)+"毫秒");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    /**
//     * 写入请求信息
//     *
//     * @param logOperate 操作日志记录实体类
//     * @param pjp        切面拦截方法上下文
//     * @param operateLog 日志记录切面注解
//     * @return LogOperate
//     * @author guolihao
//     * @date 2020/9/21 11:57
//     */
//    private LogOperate writeRequest(LogOperate logOperate, JoinPoint pjp, OperateLog operateLog) {
//        HttpServletRequest request = RequestContext.getHttpServletRequest();
//        if (request != null) {
//            //IP，访问路径
//            String path = request.getServletPath();
//            //流程访问路径处理
//            if(path.contains("/model")){
//                String model = path.substring(path.indexOf("/model"),("/model").length());
//                String last = path.substring(path.lastIndexOf("/"));
//                path = model+last;
//            }
//            String ip = RequestUtil.getIpAddr(request);
//            logOperate.setIp(ip);
//            logOperate.setPath(path);
//            //请求参数
//            String paramMap = getParams(pjp,request);
//            //头信息
//            if (operateLog.writeRequest()) {
//                Enumeration headerNames = request.getHeaderNames();
//                Map headerMap = Maps.newHashMap();
//                while (headerNames.hasMoreElements()) {
//                    String key = (String) headerNames.nextElement();
//                    headerMap.put(key, request.getHeader(key));
//                }
//                logOperate.setRequestHead(JSONObject.toJSONString(headerMap));
//                logOperate.setRequestParam(paramMap);
//            }
//        }
//        return logOperate;
//    }
//
//    /**
//     * 获取入参
//     *
//     * @param pjp 切面拦截方法上下文
//     * @param request request
//     * @return
//     * @author guolihao
//     * @date 2020/11/16 16:48
//     */
//    private String getParams(JoinPoint pjp, HttpServletRequest request) {
//        String paramMap = JSONObject.toJSONString(request.getParameterMap());
//        if (StringUtils.isEmpty(paramMap) || ("{}").equals(paramMap)) {
//            paramMap = "";
//            for (Object o : pjp.getArgs()) {
//                if (o instanceof ServletRequest || o instanceof ServletResponse) {
//                    continue;
//                }
//                //数据导入参数处理
//                if(o instanceof MultipartFile){
//                    paramMap = ((MultipartFile) o).getOriginalFilename();
//                    break;
//                }
//                paramMap += JSONObject.toJSONString(o);
//            }
//        }
//        return paramMap;
//    }
//}
