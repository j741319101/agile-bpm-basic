package com.dstz.org.api.model;


import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(
        description = "关系定义"
)
public interface IRelation extends Serializable {
    String getGroupId();

    String getUserId();

    Integer getIsMaster();

    String getType();

    String getHasChild();
}