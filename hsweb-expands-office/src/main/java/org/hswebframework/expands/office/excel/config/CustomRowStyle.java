package org.hswebframework.expands.office.excel.config;

/**
 * 自定义行样式
 * Created by 浩 on 2015-12-07 0007.
 */
public class CustomRowStyle {
    /**
     * 行高度
     */
    private double height = 20;

    public CustomRowStyle() {
    }

    public CustomRowStyle(double height) {
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
