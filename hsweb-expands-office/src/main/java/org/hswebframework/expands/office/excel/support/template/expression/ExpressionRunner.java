package org.hswebframework.expands.office.excel.support.template.expression;

import org.apache.poi.ss.usermodel.Cell;

import java.util.Map;

/**
 * 表达式执行器
 * Created by 浩 on 2015-12-16 0016.
 */
public interface ExpressionRunner {
    /**
     * 设置单元格渲染辅助器
     *
     * @param helper 单元格渲染辅助器
     */
    void setHelper(CellHelper helper);

    /**
     * 获取单元格渲染辅助器
     *
     * @return 单元格渲染辅助器
     */
    CellHelper getHelper();

    /**
     * 设置数据
     *
     * @param data 数据
     */
    void setData(Map<String, Object> data);

    /**
     * 追加一条表达式
     *
     * @param cell       单元格实例
     * @param expression 表达式内容
     */
    void pushExpression(Cell cell, String expression);

    /**
     * 执行
     *
     * @throws Throwable
     */
    void run() throws Throwable;

    /**
     * 重置
     */
    void reset();
}
