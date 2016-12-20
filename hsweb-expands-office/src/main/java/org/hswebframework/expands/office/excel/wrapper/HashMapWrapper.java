package org.hswebframework.expands.office.excel.wrapper;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 浩 on 2015-12-07 0007.
 */
public class HashMapWrapper extends AbstractWrapper<Map<String, Object>> {

    private int nullHeaderSize = 1;

    @Override
    public Map<String, Object> newInstance() {
        return new LinkedHashMap();
    }

    @Override
    public void wrapper(Map<String, Object> instance, String header, Object value) {
        if (header == null || "".equals(header)) {
            header = "null." + nullHeaderSize++;
        }
        header=header.trim();
        instance.put(headerMapper(header), value);
    }

}
