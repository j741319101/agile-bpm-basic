package com.dstz.org.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(
        description = "SSO用户信息"
)
public interface ISSOUser extends Serializable {
    @ApiModelProperty("用户ID")
    String getUserId();

    void setUserId(String var1);

    @ApiModelProperty("用户名")
    String getFullname();

    void setFullname(String var1);

    @ApiModelProperty("账户")
    String getAccount();

    void setAccount(String var1);
}
