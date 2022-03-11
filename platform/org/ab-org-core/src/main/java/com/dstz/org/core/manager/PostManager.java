package com.dstz.org.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.org.core.model.Post;

import java.util.List;

public interface PostManager extends Manager<String, Post> {
    Post getByAlias(String var1);

    List<Post> getByUserId(String var1);

    Post getMasterByUserId(String var1);
}