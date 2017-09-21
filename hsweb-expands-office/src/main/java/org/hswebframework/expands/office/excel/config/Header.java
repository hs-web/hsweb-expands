package org.hswebframework.expands.office.excel.config;

/**
 * Created by 浩 on 2015-12-07 0007.
 */
public class Header implements Comparable<Header> {
    private String title;

    private String field;

    private CustomCellStyle style;

    private int sort = -1;

    public Header() {
    }

    public Header(String title, String field) {
        this.title = title;
        this.field = field;
    }

    public Header(String title, String field, CustomCellStyle style) {
        this.title = title;
        this.field = field;
        this.style = style;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public int compareTo(Header o) {
        return ((Integer) sort).compareTo(o.getSort());
    }

    public CustomCellStyle getStyle() {
        return style;
    }

    public void setStyle(CustomCellStyle style) {
        this.style = style;
    }
}
