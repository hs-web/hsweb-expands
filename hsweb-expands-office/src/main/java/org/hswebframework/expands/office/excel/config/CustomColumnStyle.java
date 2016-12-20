package org.hswebframework.expands.office.excel.config;

/**
 * 自定义列样式
 * Created by 浩 on 2015-12-07 0007.
 */
public class CustomColumnStyle {
    /**
     * 列宽度
     */
    private int width = 5000;

    public CustomColumnStyle() {
    }

    public CustomColumnStyle(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
