package org.hswebframework.expands.office.excel;


import org.hswebframework.expands.office.excel.config.ExcelReaderCallBack;
import org.hswebframework.expands.office.excel.config.ExcelWriterCallBack;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * excel操作的API接口
 * Created by 浩 on 2015-12-07 0007.
 */
public interface ExcelApi {

    /**
     * 基于回掉的excel读取
     *
     * @param inputStream excel文件输入流
     * @param callBack    excel读取回掉接口
     * @throws Exception 读取异常
     */
    void read(InputStream inputStream, ExcelReaderCallBack callBack) throws Exception;

    /**
     * 基于回掉的excel写出
     *
     * @param outputStream excel输出流
     * @param callBack     回掉
     * @param moreSheet    多个表格写出
     * @throws Exception 写出异常
     */
    void write(OutputStream outputStream, ExcelWriterCallBack callBack, ExcelWriterCallBack... moreSheet) throws Exception;
}
