package com.dstz.base.api.request;

/**
 * TODO 请求头
 */
public class RequestHead {
    private String sourceSystem;
    private String operator;
    private String memo;
    private String ip;
    private Boolean isEncryptData;
    private String secreKey;
    private String traceId;

    public RequestHead() {
    }

    public String getSourceSystem() {
        return this.sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getIsEncryptData() {
        return this.isEncryptData;
    }

    public void setIsEncryptData(Boolean isEncryptData) {
        this.isEncryptData = isEncryptData;
    }

    public String getSecreKey() {
        return this.secreKey;
    }

    public void setSecreKey(String secreKey) {
        this.secreKey = secreKey;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}

