package com.dstz.base.api.aop.annotion;


import com.dstz.base.api.handler.OperateLogHandler;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {
    boolean writeResponse() default true;

    boolean writeRequest() default true;

    Class<? extends OperateLogHandler> handler() default OperateLogHandler.class;
}
