package org.hswebframework.expands.office.word.support.template.expression;

import java.util.Map;

/**
 * word 模板表达式运行器
 * Created by 浩 on 2015-12-18 0018.
 */
public interface ExpressionRunner {
    /**
     * 推送一个段落表达式信息.
     *
     * @param expression 表达式内容
     * @param paragraph  段落实例
     */
    void pushExpression(String expression, Object paragraph);

    /**
     * 推送一个表格的信息
     *
     * @param expression 表达式内容
     * @param cell       单元格实例
     * @param paragraph  单元格段落实例
     */
    void pushExpression(String expression, Object cell, Object paragraph);

    /**
     * 设置渲染变量
     *
     * @param var
     */
    void setVar(Map<String, Object> var);

    /**
     * 执行
     *
     * @throws Throwable
     */
    void run() throws Throwable;
}
