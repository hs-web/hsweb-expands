package org.hsweb.expands.office.word;


import org.hsweb.expands.office.word.config.WordReaderCallBack;
import org.hsweb.expands.office.word.config.WordWriterCallBack;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Word文档读写API
 * Created by 浩 on 2015-12-18 0018.
 */
public interface WordApi {

    /**
     * 基于回掉读
     *
     * @param inputStream word输入流
     * @param callBack    回掉
     * @throws Exception 读取异常
     */
    void read(InputStream inputStream, WordReaderCallBack callBack) throws Exception;

    /**
     * 基于回掉写
     *
     * @param outputStream word输出流
     * @param callBack     回掉
     * @throws Exception 写出异常
     */
    void write(OutputStream outputStream, WordWriterCallBack callBack) throws Exception;
}
