package org.hswebframework.expands.office.excel.wrapper;


import org.hswebframework.expands.office.excel.ExcelReaderWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouhao on 16-6-24.
 */
public class MultitermSheetWrapper extends AbstractWrapper<Object> {
    private ExcelReaderWrapper[] wrappers;

    protected int sheet = 0;
    private ExcelReaderWrapper nowWrapper;
    private List<List> data = new ArrayList<>();
    private List nowData = new ArrayList<>();


    public MultitermSheetWrapper(ExcelReaderWrapper[] wrappers) {
        this.wrappers = wrappers;
        nowWrapper = wrappers[0];
        data.add(nowData);
    }
    @Override
    public Object newInstance() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object newInstance(int i) throws Exception {
        if (sheet != i) {
            if (wrappers.length > i) {
                nowWrapper = wrappers[i];
            } else {
                nowWrapper = wrappers[0];
            }
            sheet = i;
            nowData = new ArrayList<>();
            data.add(nowData);
        }
        return nowWrapper.newInstance(i);
    }

    @Override
    public void wrapper(Object instance, String header, Object value) {
        nowWrapper.wrapper(instance, header, value);
    }

    @Override
    public boolean wrapperDone(Object instance) {
        nowData.add(instance);

        return true;
    }

    public List<List> getData() {
        return data;
    }
}
