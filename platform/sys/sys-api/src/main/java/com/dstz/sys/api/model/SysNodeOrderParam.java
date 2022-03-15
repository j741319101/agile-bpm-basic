package com.dstz.sys.api.model;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class SysNodeOrderParam implements Serializable {
    @NotNull(
            message = "ID不能为空!"
    )
    private String id;
    private int sn;

    public SysNodeOrderParam() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSn() {
        return this.sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }
}