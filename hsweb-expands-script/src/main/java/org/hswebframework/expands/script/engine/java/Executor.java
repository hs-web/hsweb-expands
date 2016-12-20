package org.hswebframework.expands.script.engine.java;

import java.util.Map;

/**
 * Created by 浩 on 2016-01-25 0025.
 */
public interface Executor {
    Object execute(Map<String, Object> var) throws Exception;
}
