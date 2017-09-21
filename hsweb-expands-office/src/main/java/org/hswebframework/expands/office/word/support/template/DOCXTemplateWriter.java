package org.hswebframework.expands.office.word.support.template;

import org.apache.poi.xwpf.usermodel.*;
import org.hswebframework.expands.office.word.config.WordReaderCallBack;
import org.hswebframework.expands.office.word.support.template.expression.ExpressionRunner;

import java.io.OutputStream;
import java.util.List;

/**
 * 基于POI docx的文档模板写出
 * Created by 浩 on 2015-12-18 0018.
 */
public class DOCXTemplateWriter implements WordReaderCallBack {

    /**
     * 文档输出流
     */
    private OutputStream outputStream;

    /**
     * 模板表达式执行器
     */
    private ExpressionRunner runner;

    /**
     * 带参数的构造方法，参数不能为null
     *
     * @param outputStream 写出模板后输出流
     * @param runner       模板表达式执行器
     */
    public DOCXTemplateWriter(OutputStream outputStream, ExpressionRunner runner) {
        this.outputStream = outputStream;
        this.runner = runner;
    }

    @Override
    public void onParagraph(Object par) {
        XWPFParagraph paragraph = ((XWPFParagraph) par);
        String text = getParagraphText(paragraph);
        runner.pushExpression(text, paragraph);
    }

    @Override
    public void onTable(Object t) {
        XWPFTable table = ((XWPFTable) t);
        for (XWPFTableRow xwpfTableRow : table.getRows()) {
            for (XWPFTableCell xwpfTableCell : xwpfTableRow.getTableCells()) {
                for (XWPFParagraph xwpfParagraph : xwpfTableCell.getParagraphs()) {
                    String text = getParagraphText(xwpfParagraph);
                    runner.pushExpression(text, xwpfTableCell, xwpfParagraph);
                }
            }
        }
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

    @Override
    public void done(Object document) throws Exception {
        try {
            //执行表达式
            runner.run();
        } catch (Throwable throwable) {
            throw new Exception(throwable);
        }
        XWPFDocument docx = ((XWPFDocument) document);
        docx.write(outputStream);
    }
}
