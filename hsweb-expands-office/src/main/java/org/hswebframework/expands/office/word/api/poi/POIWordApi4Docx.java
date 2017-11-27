package org.hswebframework.expands.office.word.api.poi;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.hswebframework.expands.office.word.WordApi;
import org.hswebframework.expands.office.word.config.WordReaderCallBack;
import org.hswebframework.expands.office.word.config.WordWriterCallBack;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 基于POI的word 2007+版本读写操作
 * Created by 浩 on 2015-12-18 0018.
 */
public class POIWordApi4Docx implements WordApi {

    /**
     * 单例
     */
    private static final POIWordApi4Docx instance = new POIWordApi4Docx();

    public static POIWordApi4Docx getInstance() {
        return instance;
    }

    private POIWordApi4Docx() {
    }

    @Override
    public void read(InputStream inputStream, WordReaderCallBack callBack) throws Exception {
        XWPFDocument document = new XWPFDocument(inputStream);
        document.getParagraphs().forEach(callBack::onParagraph);
        document.getTables().forEach(callBack::onTable);
        callBack.done(document);
    }

    @Override
    public void write(OutputStream outputStream, WordWriterCallBack callBack) throws Exception {

    }

}
