package org.hswebframework.expands.office.excel.support.template.expression;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * 在模板导出时,表达式通过调用此接口来渲染单元格
 * Created by 浩 on 2015-12-16 0016.
 */
public interface CellHelper {

    /**
     * 设置表格
     *
     * @param sheet 表格实例
     */
    void setSheet(Sheet sheet);

    /**
     * 初始化一个单元格
     *
     * @param value 单元格的值
     * @param cell  单元格实例
     */
    void initCell(Object value, Cell cell);

    /**
     * 渲染下一个单元格,当模板进入循环时,通过调用此方法来进行循环渲染单元格
     *
     * @param value 渲染的值
     * @param cell  第一行(表达式所在行)实例
     */
    void nextCell(Object value, Cell cell);

    /**
     * 渲染下一行,当模板进入循环时,通过调用次方法来循环创建行
     *
     * @param cell 进入行渲染时的所在单元格
     */
    void nextRow(Cell cell);

}
