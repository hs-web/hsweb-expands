package org.hswebframework.expands.script.engine;


import java.util.Map;

/**
 * 动态脚本执行引擎
 * Created by 浩 on 2015-10-27 0027.
 */
public interface DynamicScriptEngine {

    /**
     * 引擎初始化
     *
     * @param contents 初始化内容
     * @throws Exception 异常
     */
    void init(String... contents) throws Exception;

    /**
     * 编译脚本
     *
     * @param id   脚本id
     * @param code 脚本内容
     * @return 编译是否成功
     * @throws Exception 异常欣喜
     */
    boolean compile(String id, String code) throws Exception;

    ScriptContext getContext(String id);

    boolean compiled(String id);

    boolean remove(String id);

    /**
     * 执行编译好的脚本
     *
     * @param id    编译后的id
     * @param param 执行参数
     * @return 执行结果
     */
    ExecuteResult execute(String id, Map<String, Object> param);

    ExecuteResult execute(String id);

    void addListener(ScriptListener scriptListener);

    void addGlobalVariable(Map<String, Object> vars);
}
