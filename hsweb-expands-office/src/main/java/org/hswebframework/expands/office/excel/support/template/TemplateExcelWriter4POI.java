package org.hswebframework.expands.office.excel.support.template;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hswebframework.expands.office.excel.config.ExcelReaderCallBack;
import org.hswebframework.expands.office.excel.support.template.expression.ExpressionRunner;
import org.hswebframework.utils.StringUtils;

import java.io.OutputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于POI的模板导出
 * Created by 浩 on 2015-12-15 0015.
 */
public class TemplateExcelWriter4POI implements ExcelReaderCallBack {

    /**
     * 导出数据
     */
    protected Object data;
    /**
     * 导出输出流
     */
    protected OutputStream out;

    /**
     * 表达式匹配: ${} 则认为是一个表达式
     */
    private static final Pattern EXPRESSION_PATTERN = StringUtils.compileRegex("(?<=\\$\\{)(.+?)(?=\\})");

    /**
     * 表达式运行器,用于处理excel中的表达式
     */
    private ExpressionRunner runner;

    /**
     * 带参构造方法,所有参数不能为null
     *
     * @param data 要导出的数据
     * @param out  导出输出流
     * @throws Exception 当参数为null时,抛出NullPointerException异常
     */
    public TemplateExcelWriter4POI(Object data, OutputStream out, ExpressionRunner runner) throws Exception {
        if (data == null || out == null) {
            throw new NullPointerException("data or OutputStream can not be null!");
        }
        this.data = data;
        this.out = out;
        this.runner = runner;
    }

    @Override
    public void onCell(CellContent content) throws Exception {
        Cell cell = ((Cell) content.getCellProxy());
        if (cell == null) return;
        Object value = content.getValue();
        if (value instanceof String) {
            String temp = value.toString();
            //匹配是否有表达式
            Matcher matcher = EXPRESSION_PATTERN.matcher(temp);
            while (matcher.find()) {
                String group = matcher.group();
                runner.pushExpression(cell, group);
            }
        }
    }

    protected void putValue2Cell(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number) {
            cell.setCellValue(StringUtils.toDouble(value));
        } else if (value instanceof Date) {
            cell.setCellValue(((Date) value));
        } else {
            cell.setCellValue(value.toString());
        }
    }

    protected Object getValueFromExpression(String expression) {
        try {
            return BeanUtils.getProperty(data, expression);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void sheetDone(Object sheet) throws Exception {
        try {
            runner.getHelper().setSheet(((Sheet) sheet));
            runner.run();
            runner.reset();
        } catch (Throwable throwable) {
            throw new Exception(throwable);
        }
    }

    @Override
    public void done(Object workBook) throws Exception {
        Workbook wb = ((Workbook) workBook);
        wb.write(out);
    }
}
