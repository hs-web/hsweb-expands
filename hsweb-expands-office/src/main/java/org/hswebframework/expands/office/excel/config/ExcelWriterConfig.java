package org.hswebframework.expands.office.excel.config;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 浩 on 2015-12-07 0007.
 */
public class ExcelWriterConfig {

    /**
     * 表格名称
     */
    private String sheetName;

    /**
     * 写出的起始位置,
     */
    private int startWith = 0;

    /**
     * 合并相同列
     */
    private List<String> mergeColumns = new LinkedList<>();

    /**
     * 合并指定单元格
     */
    private List<Merge> merges = new LinkedList<>();

    /**
     * 导出的表头信息
     */
    private List<Header> headers = new LinkedList<>();

    /**
     * 导出的数据
     */
    private List<Object> datas = new LinkedList<>();

    public ExcelWriterConfig addHeader(String header, String field) {
        this.addHeader(new Header(header, field));
        return this;
    }

    public ExcelWriterConfig addHeader(Header header) {
        header.setSort(headers.size());
        headers.add(header);
        return this;
    }

    public ExcelWriterConfig addData(Object data) {
        datas.add(data);
        return this;
    }

    public ExcelWriterConfig addAll(List<Object> data) {
        datas.addAll(data);
        return this;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getStartWith() {
        return startWith;
    }

    public void setStartWith(int startWith) {
        this.startWith = startWith;
    }


    public ExcelWriterConfig mergeColumn(String... column) {
        mergeColumns.addAll(Arrays.asList(column));
        return this;
    }

    public List<String> getMergeColumns() {
        return mergeColumns;
    }

    public void setMergeColumns(List<String> mergeColumns) {
        this.mergeColumns = mergeColumns;
    }

    public List<Merge> getMerges() {
        return merges;
    }

    public void setMerges(List<Merge> merges) {
        this.merges = merges;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        for (int i = 0; i < headers.size(); i++) {
            Header header = headers.get(i);
            if (header.getSort() == -1) {
                header.setSort(i);
            }
        }
        headers.sort(Header::compareTo);
        this.headers = headers;
    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }

    public void addMerge(int rowFrom, int colFrom, int rowTo, int colTo) {
        addMerge(new Merge(rowFrom, colFrom, rowTo, colTo));
    }

    public void addMerge(Merge merge) {
        if (!merges.contains(merge)) ;
        merges.add(merge);
    }


    private CustomCellStyle cellStyle = new CustomCellStyle();

    public ExcelWriterConfig() {
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        CustomCellStyle.Border border = new CustomCellStyle.Border((short) 1, HSSFColor.BLACK.index);
        cellStyle.setBorderLeft(border);
        cellStyle.setBorderRight(border);
        cellStyle.setBorderBottom(border);
        cellStyle.setBorderTop(border);
    }

    /**
     * 在写出开始前,设置了跳过写出的行时调用。
     * 如果配置中设置了startWith,则在渲染被跳过的单元格时,将调用此回掉来获取自定义的值
     *
     * @param row    当前行
     * @param column 当前列
     * @return 自定义值
     */
    public Object startBefore(int row, int column) {
        return "";
    }

    /**
     * 获取一个单元格的自定义样式
     *
     * @param row    行,如果为-1,代表为表头行
     * @param column 列
     * @param header 表头
     * @param value  单元格值
     * @return 自定义样式
     */
    public CustomCellStyle getCellStyle(int row, int column, String header, Object value) {
        cellStyle.setFormat("");
        if (value == null) {
            cellStyle.setDataType("string");
        } else {
            if (value instanceof Integer) {
                cellStyle.setDataType("int");
            } else if (value instanceof Number) {
                cellStyle.setDataType("double");
            } else if (value instanceof Date) {
                cellStyle.setDataType("date");
                cellStyle.setFormat("yyyy-MM-dd");
            } else {
                cellStyle.setDataType("string");
            }
        }
        cellStyle.setValue(value);
        return cellStyle;
    }

    /**
     * 获取自定义列的样式
     *
     * @param column 列号
     * @param header 表头
     * @return 自定义列样式
     */
    public CustomColumnStyle getColumnStyle(int column, String header) {
        return null;
    }

    /**
     * 获取自定义行样式
     *
     * @param row    行号，为-1时代表是表头行
     * @param header 表头
     * @return 自定义行样式
     */
    public CustomRowStyle getRowStyle(int row, String header) {
        return null;
    }

    /**
     * 合并信息
     */
    public class Merge {
        private int rowFrom;

        private int colFrom;

        private int rowTo;

        private int colTo;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Merge) {
                Merge m = (Merge) obj;
                return m.getColFrom() == this.getColFrom() && m.getColTo() == this.getColTo() && m.getRowFrom() == this.getRowFrom() && m.getRowTo() == this.getRowTo();
            }
            return super.equals(obj);
        }

        public Merge(int rowFrom, int colFrom, int rowTo, int colTo) {
            this.rowFrom = rowFrom;
            this.colFrom = colFrom;
            this.rowTo = rowTo;
            this.colTo = colTo;
        }

        public int getRowFrom() {
            return rowFrom;
        }

        public void setRowFrom(int rowFrom) {
            this.rowFrom = rowFrom;
        }

        public int getColFrom() {
            return colFrom;
        }

        public void setColFrom(int colFrom) {
            this.colFrom = colFrom;
        }

        public int getRowTo() {
            return rowTo;
        }

        public void setRowTo(int rowTo) {
            this.rowTo = rowTo;
        }

        public int getColTo() {
            return colTo;
        }

        public void setColTo(int colTo) {
            this.colTo = colTo;
        }

        @Override
        public String toString() {
            return "Merge{" +
                    "rowFrom=" + rowFrom +
                    ", colFrom=" + colFrom +
                    ", rowTo=" + rowTo +
                    ", colTo=" + colTo +
                    '}';
        }
    }
}
