package org.hswebframework.expands.office.excel.support;


import org.hswebframework.expands.office.excel.ExcelApi;
import org.hswebframework.expands.office.excel.ExcelWriter;
import org.hswebframework.expands.office.excel.api.poi.POIExcelApi;
import org.hswebframework.expands.office.excel.api.poi.callback.CommonExcelWriterCallBack;
import org.hswebframework.expands.office.excel.config.ExcelWriterConfig;

import java.io.OutputStream;

/**
 * Created by 浩 on 2015-12-07 0007.
 */
public class CommonExcelWriter implements ExcelWriter {
    private ExcelApi api = POIExcelApi.getInstance();

    public ExcelApi getApi() {
        return api;
    }

    public void setApi(ExcelApi api) {
        this.api = api;
    }

    @Override
    public void write(OutputStream outputStream, ExcelWriterConfig config, ExcelWriterConfig... moreSheet) throws Exception {
        CommonExcelWriterCallBack call = new CommonExcelWriterCallBack(config);
        CommonExcelWriterCallBack[] callBackArr = new CommonExcelWriterCallBack[moreSheet.length];
        for (int i = 0; i < moreSheet.length; i++) {
            callBackArr[i] = new CommonExcelWriterCallBack(moreSheet[i]);
        }
        getApi().write(outputStream, call, callBackArr);
    }
}
