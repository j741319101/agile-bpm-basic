package com.dstz.base.api.response.impl;

import com.dstz.base.api.constant.BaseStatusCode;
import com.dstz.base.api.constant.IStatusCode;
import com.dstz.base.api.constant.StatusCode;
import com.dstz.base.api.exception.BusinessException;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回结果
 * @param <E>
 * @author Administrator
 */

@ApiModel(
        description = "标准的返回结果包装类"
)
public class ResultMsg<E> extends BaseResult {
    private static final long serialVersionUID = 7420095794453471L;
    /** @deprecated */
    @Deprecated
    public static final int SUCCESS = 1;
    /** @deprecated */
    @Deprecated
    public static final int FAIL = 0;
    /** @deprecated */
    @Deprecated
    public static final int ERROR = -1;
    /** @deprecated */
    @Deprecated
    public static final int TIMEOUT = 2;
    @ApiModelProperty("结果数据")
    private E data;

    public ResultMsg() {
    }

    public ResultMsg(E result) {
        this.setOk(Boolean.TRUE);
        this.setCode(BaseStatusCode.SUCCESS.getCode());
        this.setData(result);
    }

    public ResultMsg(IStatusCode code, String msg) {
        this.setOk(BaseStatusCode.SUCCESS.getCode().equals(code.getCode()));
        this.setCode(code.getCode());
        this.setMsg(msg);
    }

    /** @deprecated */
    @Deprecated
    public ResultMsg(int code, String msg) {
        this.setOk(code == 1);
        this.setMsg(msg);
    }

    public E getData() {
        return this.data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public static <T> T getSuccessResult(ResultMsg<T> result) {
        if (result != null && result.getIsOk()) {
            return result.getData();
        } else {
            throw new BusinessException(new StatusCode(result.getCode(), result.getMsg()));
        }
    }

    public ResultMsg<E> addMapParam(String key, Object val) {
        if (this.data == null) {
            Map map = new HashMap();
            this.data =(E) map;
        }

        if (!(this.data instanceof Map)) {
            throw new RuntimeException("设置参数异常！当前返回结果非map对象，无法使用 addMapParam方法获取数据");
        } else {
            Map map = (Map)this.data;
            map.put(key, val);
            return this;
        }
    }

    public Object getMapParam(String key) {
        if (!(this.data instanceof Map)) {
            throw new RuntimeException("获取参数异常！当前返回结果非map对象，无法使用 addMapParam方法获取数据");
        } else {
            Map map = (Map)this.data;
            return map.get(key);
        }
    }

    public static <E> ResultMsg<E> ERROR(String msg) {
        ResultMsg<E> result = new ResultMsg();
        result.setOk(Boolean.FALSE);
        result.setMsg(msg);
        return result;
    }

    public static <E> ResultMsg<E> SUCCESS() {
        ResultMsg<E> result = new ResultMsg();
        result.setOk(Boolean.TRUE);
        return result;
    }

    public static <E> ResultMsg<E> SUCCESS(E data) {
        ResultMsg<E> result = new ResultMsg();
        result.setOk(Boolean.TRUE);
        result.setData(data);
        return result;
    }
}

