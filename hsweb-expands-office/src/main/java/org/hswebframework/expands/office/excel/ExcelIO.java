package org.hswebframework.expands.office.excel;


import org.hswebframework.expands.office.excel.api.poi.POIExcelApi;
import org.hswebframework.expands.office.excel.config.ExcelWriterConfig;
import org.hswebframework.expands.office.excel.config.Header;
import org.hswebframework.expands.office.excel.support.CommonExcelReader;
import org.hswebframework.expands.office.excel.support.CommonExcelWriter;
import org.hswebframework.expands.office.excel.support.template.TemplateExcelWriter4POI;
import org.hswebframework.expands.office.excel.support.template.expression.CommonCellHelper;
import org.hswebframework.expands.office.excel.support.template.expression.ExpressionRunner;
import org.hswebframework.expands.office.excel.support.template.expression.GroovyExpressionRunner;
import org.hswebframework.expands.office.excel.wrapper.BeanWrapper;
import org.hswebframework.expands.office.excel.wrapper.HashMapWrapper;
import org.hswebframework.expands.office.excel.wrapper.MultitermSheetWrapper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Excel读写操作类
 * Created by 浩 on 2015-12-07 0007.
 */
public class ExcelIO {

    /**
     * 读取excel为map集合
     *
     * @param inputStream excel输入流
     * @return map对象集合
     * @throws Exception 读取异常
     */
    public static List<Map<String, Object>> read2Map(InputStream inputStream) throws Exception {
        return read(inputStream, new HashMapWrapper());
    }

    public static List<List<Map<String, Object>>> read2MulMap(InputStream inputStream) throws Exception {
        HashMapWrapper[] wrapper = {new HashMapWrapper()};
        return (List) read(inputStream, wrapper);
    }

    /**
     * 读取excel为javaBean
     *
     * @param inputStream  excel输入流
     * @param headerMapper 表头与字段映射配置
     * @param <T>          bean泛型
     * @param tClass       javaBean类型
     * @return bean集合
     * @throws Exception 读取异常
     */
    public static <T> List<T> read2Bean(InputStream inputStream, Map<String, String> headerMapper, Class<T> tClass) throws Exception {
        BeanWrapper wrapper = new BeanWrapper<T>();
        wrapper.setType(tClass);
        wrapper.setHeaderNameMapper(headerMapper);
        return read(inputStream, wrapper);
    }

    /**
     * 自定义包装器读取excel为集合
     *
     * @param inputStream excel输入流
     * @param wrapper     包装器
     * @param <T>         读取结果泛型
     * @return 读取结果集合
     * @throws Exception 读取异常
     */
    public static <T> List<T> read(InputStream inputStream, ExcelReaderWrapper<T> wrapper) throws Exception {
        CommonExcelReader reader = new CommonExcelReader();
        reader.setWrapper(wrapper);
        return reader.readExcel(inputStream);
    }

    /**
     * 读取多个sheet为多个list
     *
     * @param inputStream excel输入流
     * @param wrappers    包装器
     * @return 读取结果
     * @throws Exception
     */
    public static List<List> read(InputStream inputStream, ExcelReaderWrapper[] wrappers) throws Exception {
        CommonExcelReader reader = new CommonExcelReader();
        MultitermSheetWrapper wrapper = new MultitermSheetWrapper(wrappers);
        reader.setWrapper(wrapper);
        reader.readExcel(inputStream);
        return wrapper.getData();
    }


    /**
     * 写出简单格式excel,第一行为表头,依次为数据
     *
     * @param outputStream 输出流
     * @param headers      表头信息
     * @param dataList     数据集合
     * @throws Exception
     */
    public static void write(OutputStream outputStream, List<Header> headers, List<Object> dataList) throws Exception {
        ExcelWriterConfig config = new ExcelWriterConfig();
        config.setHeaders(headers);
        config.setDatas(dataList);
        write(outputStream, config);
    }

    public static void write(OutputStream outputStream, List<Header> headers, List<Object> dataList, String[] merge) throws Exception {
        ExcelWriterConfig config = new ExcelWriterConfig();
        config.setHeaders(headers);
        config.setDatas(dataList);
        config.setMergeColumns(Arrays.asList(merge));
        write(outputStream, config);
    }

    /**
     * 根据模板导出,基于POI导出
     *
     * @param inputStream  模板输入流
     * @param outputStream 结果输出流
     * @param var          定义的变量
     * @throws Exception 导出异常
     */
    public static void writeTemplate(InputStream inputStream, OutputStream outputStream, Map<String, Object> var) throws Exception {
        ExpressionRunner runner = new GroovyExpressionRunner();
        runner.setHelper(new CommonCellHelper());
        runner.setData(var);
        TemplateExcelWriter4POI templateExcelWriter4POI = new TemplateExcelWriter4POI(var, outputStream, runner);
        POIExcelApi.getInstance().read(inputStream, templateExcelWriter4POI);
    }

    /**
     * 自定义导出
     *
     * @param outputStream 输出流
     * @param config       导出配置
     * @param moreSheet    多个表格导出
     * @throws Exception 导出异常
     */
    public static void write(OutputStream outputStream, ExcelWriterConfig config, ExcelWriterConfig... moreSheet) throws Exception {
        CommonExcelWriter writer = new CommonExcelWriter();
        writer.write(outputStream, config, moreSheet);
    }

}
