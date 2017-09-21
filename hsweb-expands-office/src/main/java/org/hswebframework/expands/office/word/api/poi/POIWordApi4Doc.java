package org.hswebframework.expands.office.word.api.poi;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.hswebframework.expands.office.word.WordApi;
import org.hswebframework.expands.office.word.config.WordReaderCallBack;
import org.hswebframework.expands.office.word.config.WordWriterCallBack;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 基于POI的word 2007+版本读写操作
 * Created by 浩 on 2015-12-18 0018.
 */
public class POIWordApi4Doc implements WordApi {

    /**
     * 单例
     */
    private static final POIWordApi4Doc instance = new POIWordApi4Doc();

    public static POIWordApi4Doc getInstance() {
        return instance;
    }

    private POIWordApi4Doc() {
    }

    @Override
    public void read(InputStream inputStream, WordReaderCallBack callBack) throws Exception {
        HWPFDocument document = new HWPFDocument(inputStream);
        Range range = document.getRange();
        for (int i = range.getStartOffset(); i < range.getEndOffset(); i++) {
        }
        callBack.done(document);
    }

    @Override
    public void write(OutputStream outputStream, WordWriterCallBack callBack) throws Exception {

    }

}
