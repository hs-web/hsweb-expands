package org.hswebframework.expands.office.word.support.template;

import org.apache.poi.xwpf.usermodel.*;
import org.hswebframework.expands.office.word.config.WordReaderCallBack;

import java.util.List;

/**
 * Created by zhouhao on 16-4-10.
 */
public abstract class DOCXStringReader implements WordReaderCallBack {
    public abstract void readLine(String text);

    @Override
    public void onParagraph(Object par) {
        XWPFParagraph paragraph = ((XWPFParagraph) par);
        String text = getParagraphText(paragraph);
        readLine(text);
    }

    @Override
    public void onTable(Object t) {
        XWPFTable table = ((XWPFTable) t);

        for (XWPFTableRow xwpfTableRow : table.getRows()) {
            for (XWPFTableCell xwpfTableCell : xwpfTableRow.getTableCells()) {
                StringBuilder builder = new StringBuilder();
                for (XWPFParagraph xwpfParagraph : xwpfTableCell.getParagraphs()) {
                    String text = getParagraphText(xwpfParagraph);
                    builder.append(text);
                }
                readLine(builder.toString());
            }
        }
    }

    @Override
    public void done(Object document) throws Exception {
    }

    protected String getParagraphText(XWPFParagraph paragraph) {
        return getRunText(paragraph.getRuns());
    }

    protected String getRunText(List<XWPFRun> xwpfRuns) {
        StringBuilder builder = new StringBuilder();
        for (XWPFRun run : xwpfRuns) {
            builder.append(run.toString());
        }
        return builder.toString();
    }
}
