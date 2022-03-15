package com.dstz.sys.util;


import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.db.model.query.DefaultPage;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import org.apache.ibatis.session.RowBounds;

public class CustomDefaultQueryFilterUtil {
    public CustomDefaultQueryFilterUtil() {
    }

    public static QueryFilter setDefaultQueryFilter() {
        QueryFilter filter = new DefaultQueryFilter();
        DefaultPage page = new DefaultPage(new RowBounds());
        filter.setPage(page);
        return filter;
    }
}