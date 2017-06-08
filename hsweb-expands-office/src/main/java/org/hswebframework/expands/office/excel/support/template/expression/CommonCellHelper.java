package org.hswebframework.expands.office.excel.support.template.expression;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.hswebframework.utils.StringUtils;

import java.util.Date;

/**
 * 默认的模板单元格渲染辅助器
 * Created by 浩 on 2015-12-17 0017.
 */
public class CommonCellHelper implements CellHelper {

    /**
     * 表格实例
     */
    private Sheet sheet;

    /**
     * 当前行实例
     */
    private Row nowRow;


    @Override
    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
        nowRow=null;
    }

    @Override
    public void initCell(Object value, Cell cell) {
        initCellValue(cell, value);
    }

    @Override
    public void nextCell(Object value, Cell cell) {
        //模板列索引
        int nowColumn = cell.getColumnIndex();
        //数据列所有
        Cell realCell = nowRow.getCell(nowColumn);
        if (realCell == null) {
            realCell = nowRow.createCell(nowColumn);
        }
        //复制样式
        realCell.setCellStyle(cell.getCellStyle());
        //初始化单元格值
        initCellValue(realCell, value);
    }

    @Override
    public void nextRow(Cell cell) {
        if (nowRow == null) {
            //首次渲染行,则将表达式所在行替换为数据行
            nowRow = cell.getRow();
        } else {
            //创建下一行
            int rowNum = nowRow.getRowNum() + 1;
            //将最后一行移动到当前行,以实现插入行效果
            sheet.shiftRows(rowNum,sheet.getLastRowNum(),1,true,false);
            Row tmp = sheet.createRow(rowNum);
            tmp.setHeight(nowRow.getHeight());
            tmp.setHeightInPoints(nowRow.getHeightInPoints());
            nowRow = tmp;
        }
    }

    protected void initCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }
        if (value instanceof Number) {
            cell.setCellValue(StringUtils.toDouble(value));
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }
}
