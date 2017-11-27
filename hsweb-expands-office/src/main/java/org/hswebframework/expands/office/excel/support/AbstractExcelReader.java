package org.hswebframework.expands.office.excel.support;


import org.hswebframework.expands.office.excel.ExcelApi;
import org.hswebframework.expands.office.excel.ExcelReader;
import org.hswebframework.expands.office.excel.ExcelReaderWrapper;
import org.hswebframework.expands.office.excel.api.poi.POIExcelApi;
import org.hswebframework.expands.office.excel.config.AbstractExcelReaderCallBack;
import org.hswebframework.expands.office.excel.config.ExcelReaderCallBack;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 抽象读取器,实现基本的读取功能，将excel解析为一行一行的数据并调用包装其进行包装
 * Created by 浩 on 2015-12-07 0007.
 */
public abstract class AbstractExcelReader<T> implements ExcelReader<T> {

    protected ExcelApi api = POIExcelApi.getInstance();

    public abstract ExcelReaderWrapper<T> getWrapper();

    @Override
    public List<T> readExcel(InputStream inputStream) throws Exception {
        final List<T> dataList = new ArrayList<>();
        //回掉
        ExcelReaderCallBack callBack = new AbstractExcelReaderCallBack() {
            List<String> header = new LinkedList<>();//表头信息

            //行缓存,一行的数据缓存起来,读完一样进行对象包装后,清空,进行下一行读取
            List<ExcelReaderCallBack.CellContent> temp = new LinkedList<>();
            private int sheet = 0;

            @Override
            public void onCell(ExcelReaderCallBack.CellContent content) throws Exception {
                //下一个sheet,重置
                if (content.getSheet() != sheet) {
                    header.clear();
                    temp.clear();
                    sheet = content.getSheet();
                }
                //已经被手动终止
                if (getWrapper().isShutdown()) {
                    shutdown();
                    return;
                }
                boolean isHeader = isHeader(content, header);
                if (isHeader) {
                    //如果该行为表头
                    header.add(String.valueOf(content.getValue()));
                } else if (header.size() != 0) { //有表头才读取
                    getWrapper().setup(header, sheet);
                    if (getWrapper().isShutdown()) {
                        shutdown();
                        return;
                    }
                    temp.add(content);
                    getWrapper().setup(header, sheet);
                    if (getWrapper().isShutdown()) {
                        shutdown();
                        return;
                    }
                    //如果是最后一列，则代表本行已经读取完毕,调用包装器进行本行对象的实例化。
                    if (content.isLast()) {
                        dataList.add(wrapperRow(header, temp, sheet));
                        temp.clear();
                    }
                }
            }
        };
        api.read(inputStream, callBack);
        return dataList;
    }

    /**
     * 包装一个对象
     *
     * @param headers  表头信息
     * @param contents 一行的数据
     * @return 包装结果
     * @throws Exception
     */
    protected T wrapperRow(List<String> headers, List<ExcelReaderCallBack.CellContent> contents, int sheet) throws Exception {
        T instance = getWrapper().newInstance(sheet);//创建实例
        for (int i = 0, len = contents.size(); i < len; i++) {
            String header = null;
            if (headers.size() >= i) {
                header = headers.get(i);
            }
            //包装属性
            getWrapper().wrapper(instance, header, contents.get(i).getValue());
        }
        getWrapper().wrapperDone(instance);
        return instance;
    }

    /**
     * 判断一个单元格是否为表头,默认判断条件为：表格的第一行就是表头
     *
     * @param content 单元格数据
     * @param header  已有的表头
     * @return 是否为表头
     */
    protected boolean isHeader(ExcelReaderCallBack.CellContent content, List<String> header) {
        if (content.getRow() == 0) return true;
        return false;
    }
}
