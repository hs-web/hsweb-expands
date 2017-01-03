package org.hswebframework.expands.office.excel.api.poi.callback;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hswebframework.expands.office.excel.config.ExcelWriterProcessor;

import java.io.OutputStream;

/**
 * POI，excel文档写出处理器
 * Created by 浩 on 2015-12-16 0016.
 */
public class POIExcelWriterProcessor implements ExcelWriterProcessor {

    /**
     * 文档输出流
     */
    private OutputStream outputStream;
    /**
     * 工作簿对象
     */
    private Workbook     workbook;
    /**
     * 表格对象
     */
    private Sheet        sheet;
    /**
     * 表格索引
     */
    private int sheetIndex = 0;
    /**
     * 是否已经开始处理
     */
    private boolean started;
    /**
     * 是否已经结束
     */
    private boolean done;

    /**
     * 当前进行渲染的行
     */
    private Row nowRow;

    /**
     * 当前进行渲染的列
     */
    private Cell nowCell;

    private boolean autoWrite;

    /**
     * 带参数的构造方法，参数不能为空
     *
     * @param outputStream 文档输出流
     * @param workbook     工作簿实例
     */
    public POIExcelWriterProcessor(OutputStream outputStream, Workbook workbook, boolean autoWrite) {
        this.outputStream = outputStream;
        this.workbook = workbook;
        this.autoWrite = autoWrite;
    }

    @Override
    public <S> S start() throws Exception {
        return start("表格" + sheetIndex);
    }

    @Override
    public <S> S start(String sheetName) throws Exception {
        if (started) {
            //禁止重复启动
            throw new NullPointerException("processor is stared!");
        }
        sheet = workbook.createSheet(sheetName);
        started = true;
        return (S) sheet;
    }

    @Override
    public <R> R nextRow() {
        int rowNum = nowRow != null ? nowRow.getRowNum() + 1 : 0;
        nowRow = sheet.createRow(rowNum);
        nowCell = null;
        return (R) nowRow;
    }

    @Override
    public <C> C nextCell() {
        int cellNum = nowCell != null ? nowCell.getColumnIndex() + 1 : 0;
        nowCell = nowRow.createCell(cellNum);
        return (C) nowCell;
    }

    @Override
    public void done() throws Exception {
        if (done) {
            //禁止重复结束
            throw new NullPointerException("processor is done");
        }
        if (autoWrite)
            workbook.write(outputStream);
        done = true;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }
}
