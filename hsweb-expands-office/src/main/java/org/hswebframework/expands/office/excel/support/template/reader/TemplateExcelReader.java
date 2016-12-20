package org.hswebframework.expands.office.excel.support.template.reader;

import org.hswebframework.expands.office.excel.config.AbstractExcelReaderCallBack;
import org.hswebframework.expands.office.excel.support.CommonExcelReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 浩 on 2016-02-01 0001.
 */
public class TemplateExcelReader<T> extends CommonExcelReader<T> {

    private InputStream template;

    private List<List<Expression>> templateInfo = new ArrayList<>();

    protected void parseConfig() throws Exception {
        api.read(template, new AbstractExcelReaderCallBack() {
            int nowRow = -1;
            List<Expression> tmp = null;

            @Override
            public void onCell(CellContent content) throws Exception {
                if (nowRow != content.getRow()) {
                    nowRow = content.getRow();
                    if (tmp != null)
                        templateInfo.add(tmp);
                    tmp = new ArrayList<>();
                }
                Expression expression = new Expression();
                expression.setText(String.valueOf(content.getValue()));
                expression.setColumn(content.getColumn());
                expression.setRow(content.getRow());
                tmp.add(expression);
            }

            @Override
            public void done(Object workBook) {
                //添加最后一行
                templateInfo.add(tmp);
            }
        });
    }

    public void setTemplate(InputStream template) {
        this.template = template;
    }

    public static class Expression {
        private String text;

        private int row;
        private int column;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        @Override
        public String toString() {
            return "{" +
                    "text='" + text + '\'' +
                    ", row=" + row +
                    ", column=" + column +
                    '}';
        }

        enum TYPE {
            M,
            F,
            E,
            LIST
        }
    }
}
