package org.hswebframework.expands.office.excel.support;


import org.hswebframework.expands.office.excel.ExcelReaderWrapper;

/**
 * 通用的excel读取器,使用指定的包装器，将excel数据包装为对象，因此必须指定一个包装器。
 * Created by 浩 on 2015-12-07 0007.
 */
public class CommonExcelReader<T> extends AbstractExcelReader<T> {

    protected ExcelReaderWrapper<T> wrapper = null;

    @Override
    public ExcelReaderWrapper<T> getWrapper() {
        return wrapper;
    }

    public void setWrapper(ExcelReaderWrapper<T> wrapper) {
        this.wrapper = wrapper;
    }
}
