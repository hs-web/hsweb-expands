package org.hswebframework.expands.office.excel.wrapper;


import org.hswebframework.expands.office.excel.ExcelReaderWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 浩 on 2015-12-07 0007.
 */
public abstract class AbstractWrapper<T> implements ExcelReaderWrapper<T> {
    protected boolean shutdown;
    protected Map<String, String> headerNameMapper = new HashMap<>();

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public void shutdown() {
        shutdown = true;
    }


    @Override
    public void wrapperDone(T instance) {

    }

    protected String headerMapper(String old) {
        String newHeader = headerNameMapper.get(old);
        if (newHeader == null) return old;
        else return newHeader;
    }

    public Map<String, String> getHeaderNameMapper() {
        return headerNameMapper;
    }

    public void setHeaderNameMapper(Map<String, String> headerNameMapper) {
        this.headerNameMapper = headerNameMapper;
    }
}
