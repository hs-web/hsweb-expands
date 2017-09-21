package org.hswebframework.expands.office.excel.config;

/**
 * excel读取回掉
 * Created by 浩 on 2015-12-07 0007.
 */
public interface ExcelReaderCallBack {

    /**
     * 读取一个单元格时回掉
     *
     * @param content 读取到的单元格内容
     */
    void onCell(CellContent content) throws Exception;

    /**
     * 是否已经手动结束读取，如果已经结束，将立即终止读取。
     *
     * @return 是否已经结束
     */
    boolean isShutdown();

    /**
     * 终止读取
     */
    void shutdown();

    void sheetDone(Object sheet) throws Exception;

    void done(Object workBook) throws Exception;

    /**
     * 单元格内容
     */
    class CellContent {

        private Object cellProxy;

        /**
         * 当前所在表格
         */
        private int sheet;

        /**
         * 当前所在行
         */
        private int row;

        /**
         * 当前所在列
         */
        private int column;

        /**
         * 当前获取到的值
         */
        private Object value;

        /**
         * 是否为第一个单元格
         */
        private boolean first;

        /**
         * 是否为最后一个单元格
         */
        private boolean last;

        public Object getCellProxy() {
            return cellProxy;
        }

        public void setCellProxy(Object cellProxy) {
            this.cellProxy = cellProxy;
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

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public int getSheet() {
            return sheet;
        }

        public void setSheet(int sheet) {
            this.sheet = sheet;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        @Override
        public String toString() {
            return "CellContent{" +
                    "sheet=" + sheet +
                    ", row=" + row +
                    ", column=" + column +
                    ", value=" + value +
                    '}';
        }
    }
}
