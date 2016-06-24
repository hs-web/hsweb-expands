package org.hsweb.expands.office.excel.api.poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hsweb.expands.office.excel.ExcelApi;
import org.hsweb.expands.office.excel.api.poi.callback.POIExcelWriterProcessor;
import org.hsweb.expands.office.excel.config.ExcelReaderCallBack;
import org.hsweb.expands.office.excel.config.ExcelWriterCallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * POI的excel读取实现
 * Created by 浩 on 2015-12-07 0007.
 */
public class POIExcelApi implements ExcelApi {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final POIExcelApi instance = new POIExcelApi();

    private POIExcelApi() {
    }

    public static POIExcelApi getInstance() {
        return instance;
    }

    @Override
    public void read(InputStream inputStream, ExcelReaderCallBack callBack) throws Exception {
        Workbook wbs = WorkbookFactory.create(inputStream);
        //获取sheets
        int sheetSize = wbs.getNumberOfSheets();
        for (int x = 0; x < sheetSize; x++) {
            Sheet sheet = wbs.getSheetAt(x);
            // 得到总行数
            int rowNum = sheet.getLastRowNum();
            Row row = sheet.getRow(0);
            int colNum = row.getPhysicalNumberOfCells();
            for (int i = 0; i <= rowNum; i++) {
                row = sheet.getRow(i);
                if (row == null) continue;
                for (int j = 0; j < colNum; j++) {
                    if (callBack.isShutdown()) {
                        return;
                    }
                    Cell cell = row.getCell(j);
                    //创建单元格数据
                    ExcelReaderCallBack.CellContent cellContent = new ExcelReaderCallBack.CellContent();
                    cellContent.setCellProxy(cell);
                    cellContent.setFirst(j == 0);
                    cellContent.setLast(j == colNum - 1);
                    cellContent.setSheet(x);
                    cellContent.setRow(i);
                    cellContent.setColumn(j);
                    Object value = row == null ? null : cell2Object(cell);
                    cellContent.setValue(value);
                    //调用回掉
                    callBack.onCell(cellContent);
                }
            }
            callBack.sheetDone(sheet);
        }
        callBack.done(wbs);
    }


    /**
     * 将单元格数据转为java对象
     *
     * @param cell 单元格数据
     * @return 对应的java对象
     */
    protected Object cell2Object(Cell cell) {
        if (cell == null)
            return "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                double value = cell.getNumericCellValue();
                if (String.valueOf(value).endsWith(".0") || String.valueOf(value).endsWith(".00")) return new Double(value).intValue();
                return cell.getNumericCellValue();
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            default:
                return "";
        }
    }

    @Override
    public void write(OutputStream outputStream, ExcelWriterCallBack callBack, ExcelWriterCallBack... moreSheet) throws Exception {
        logger.info("create workbook");
        //支持2007写出
        Workbook workbook = new XSSFWorkbook();
        //合并所有需要写出的sheet
        List<ExcelWriterCallBack> allSheet = new ArrayList<>(Arrays.asList(moreSheet));
        allSheet.add(0, callBack);
        int index = 0;
        logger.info("start write sheet,size :" + allSheet.size());
        for (ExcelWriterCallBack writerCallBack : allSheet) {
            //创建处理器
            POIExcelWriterProcessor processor = new POIExcelWriterProcessor(outputStream, workbook);
            processor.setSheetIndex(index++);
            //调用回掉进行渲染
            writerCallBack.render(processor);
        }
    }

}
