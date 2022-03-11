package com.dstz.org.api.model.system;


import java.util.List;

public interface IClient {
    String getId();

    String getName();

    String getSecretKey();

    List<String> getAuthority();
}