package com.soho.pos.utils;

import javax.persistence.Query;
import java.util.Map;

public class SqlUtils {
    /**
     * 设置查询所需的参数
     *
     * @param query
     * @param maps
     * @return
     */
    public static Query getQueryWithParameters(Query query, Map<String, Object> maps) {
        for (String key : maps.keySet()) {
            query.setParameter(key, maps.get(key));
        }
        return query;
    }
}
