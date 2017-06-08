package org.hswebframework.expands.office.excel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Color;
import org.hswebframework.expands.office.excel.api.poi.POIExcelApi;
import org.hswebframework.expands.office.excel.api.poi.callback.CommonExcelWriterCallBack;
import org.hswebframework.expands.office.excel.config.*;
import org.hswebframework.utils.file.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by 浩 on 2015-12-07 0007.
 */
public class TestWriter {

    private List<Header> headers = new LinkedList<>();

    private List<Object> datas = new ArrayList<>();

    @Before
    public void initData() {
        CustomCellStyle cellStyle = new CustomCellStyle();
        cellStyle.setFontColor(HSSFColor.BLUE.index);
        //创建模拟数据
        headers.add(new Header("年级", "grade", cellStyle));
        headers.add(new Header("班级", "classes"));
        headers.add(new Header("性别", "sex"));
        headers.add(new Header("姓名", "name"));
        headers.add(new Header("年龄", "age"));
        headers.add(new Header("备注", "remark"));

        //创建模拟数据
        for (int i = 0; i < 20; i++) {
            final int t = i;
            datas.add(new HashMap<String, Object>() {
                {
                    put("grade", "一年级");
                    put("classes", "2班");
                    put("sex", "男");
                    put("name", "张三" + t);
                    put("age", t);
                    put("remark", "测试2");
                }
            });
        }
        for (int i = 0; i < 30; i++) {
            final int t = i;
            datas.add(new HashMap<String, Object>() {
                {
                    put("grade", "一年级");
                    put("classes", "2班");
                    put("sex", "女");
                    put("name", "张三" + t);
                    put("age", t);
                    put("remark", "测试2");
                }
            });
        }
        for (int i = 0; i < 10; i++) {
            final int t = i;
            datas.add(new HashMap<String, Object>() {
                {
                    put("grade", "一年级");
                    put("classes", "3班");
                    put("sex", "女");
                    put("name", "李四" + t);
                    put("age", t);
                    put("remark", "测试2");
                }
            });
        }

        for (int i = 0; i < 10; i++) {
            final int t = i;
            datas.add(new HashMap<String, Object>() {
                {
                    put("grade", "一年级");
                    put("classes", "3班");
                    put("sex", "女");
                    put("name", "李四__" + t);
                    put("age", t);
                    put("remark", "测试2");
                }
            });
        }

        for (int i = 0; i < 50; i++) {
            final int t = i;
            datas.add(new HashMap<String, Object>() {
                {
                    put("grade", "二年级");
                    put("classes", "3班");
                    put("sex", "男");
                    put("name", "李四__" + t);
                    put("age", t);
                    put("remark", "测试2");
                }
            });
        }


        for (int i = 0; i < 20; i++) {
            final int t = i;
            datas.add(new HashMap<String, Object>() {
                {
                    put("grade", "二年级");
                    put("classes", "1班");
                    put("name", "测试11" + t);
                    put("age", 12.3333);
                    put("remark", "测试2");
                }
            });
        }
    }


    /**
     * 简单粗暴的写出
     */
    @Test
    public void testWriteSimple() throws Exception {
        long t = System.currentTimeMillis();
        try (OutputStream outputStream = new FileOutputStream("target/test_1.xlsx")) {
            ExcelIO.write(outputStream, headers, datas);
            outputStream.flush();
        }
        System.out.println(System.currentTimeMillis() - t);
    }

    /**
     * 按模板方式写出
     */
    @Test
    public void testWriteTemplate() throws Exception {
        try (InputStream inputStream = FileUtils.getResourceAsStream("template22.xlsx")
             ; OutputStream outputStream = new FileOutputStream("target/test_template.xlsx")) {
            //定义变量
            Map<String, Object> var = new HashMap<>();
            var.put("标题", "实收汇总报表(2016-10-01至2016-10-31)");
            var.put("list", new ArrayList<Object>() {
                {
                    for (int i = 0; i < 10; i++) {
                        add(new HashMap<String, Object>() {
                            {
                                put("房间号码", "101");
                                put("客户名称", "测试");
                                put("金额", 20);
                                put("合计", 10000);
                            }
                        });
                    }
                }
            });
            //输出模板
            ExcelIO.writeTemplate(inputStream, outputStream, var);
            outputStream.flush();
        }
    }

    /**
     * 自定义导出,合并单元格等，有待优化
     */
    @Test
    public void testWriteCustom() throws Exception {
        try (OutputStream outputStream = new FileOutputStream("target/test.xlsx")) {
            ExcelWriterConfig config = new ExcelWriterConfig();
            config.setDatas(datas);
            config.setHeaders(headers);
            //1、自动合并年级和班级相同的列
            config.mergeColumn("grade", "classes", "sex");
            CommonExcelWriterCallBack ca = new CommonExcelWriterCallBack(config);
            POIExcelApi.getInstance().write(outputStream, ca);
            outputStream.flush();
        }
    }

    /**
     * 自定义导出,合并单元格等，有待优化
     */
    @Test
    public void testWriteMerge() throws Exception {
        try (OutputStream outputStream = new FileOutputStream("target/test.xlsx")) {
            ExcelWriterConfig config = new ExcelWriterConfig();
            config.setDatas(Arrays.asList(
                    new HashMap() {{
                        put("type", "规模");
                        put("name", "产量");
                    }}, new HashMap() {{
                        put("type", "规模");
                        put("name", "新能源产量");
                    }}, new HashMap() {{
                        put("type", "规模");
                        put("name", "新能源占比");
                    }}, new HashMap() {{
                        put("type", "达标情况");
                        put("name", "平均油耗实测值");
                    }}, new HashMap() {{
                        put("type", "达标情况");
                        put("name", "平均油耗目标值");
                    }}, new HashMap() {{
                        put("type", "达标情况");
                        put("name", "平均油耗达标值");
                    }}));
            config.setHeaders(Arrays.asList(
                    new Header("类型", "type"),
                    new Header("名称", "name")));
            config.mergeColumn("type");
            CommonExcelWriterCallBack ca = new CommonExcelWriterCallBack(config);
            POIExcelApi.getInstance().write(outputStream, ca);
            outputStream.flush();
        }
    }


    /**
     * 自定义导出样式，有待优化
     */
    @Test
    public void testWriteCustomStyle() throws Exception {
        try (OutputStream outputStream = new FileOutputStream("target/test_2.xlsx")) {
            ExcelWriterConfig config = new ExcelWriterConfig() {
                @Override
                public Object startBefore(int row, int column) {
                    //被跳过的行(代码[2、]处设置)填充此值
                    return "这是一个自动合并单元格并且自定义样式的示例";
                }

                @Override
                public CustomCellStyle getCellStyle(int row, int column, String header, Object value) {
                    CustomCellStyle style = super.getCellStyle(row, column, header, value);
                    //不为表头并且为姓名列
                    if (row > 0 && "姓名".equals(header)) {
                        //设置红色
                        style.setFontColor(HSSFColor.RED.index);
                    } else {
                        style.setFontColor(HSSFColor.BLACK.index);
                    }
                    return style;
                }

                @Override
                public CustomRowStyle getRowStyle(int row, String header) {
                    if (row == -1) {
                        //表头高度
                        return new CustomRowStyle(20);
                    }
                    if (row == 0) {
                        //第一行的高度
                        return new CustomRowStyle(50);
                    }
                    return null;
                }

                @Override
                public CustomColumnStyle getColumnStyle(int column, String header) {
                    //设置姓名列的宽度
                    if ("姓名".equals(header)) {
                        return new CustomColumnStyle(Short.MAX_VALUE);
                    }
                    return null;
                }
            };
            //设置表头和数据
            config.setHeaders(headers);
            config.setDatas(datas);
            //1、自动合并年级和班级相同的列
            config.mergeColumn("grade", "classes", "sex");
            //2、从第2行开始写出
            config.setStartWith(1);
            //3、合并第一行的第一列到第六列,因为设置了startWith起始行号为1,所以第一列为-1
            config.addMerge(-1, 0, 5, -1);

            //第二个sheet
            ExcelWriterConfig config2 = new ExcelWriterConfig();
            config2.setSheetName("第二个");
            //设置表头和数据
            config2.setHeaders(headers);
            config2.setDatas(datas);
            //写出
            ExcelIO.write(outputStream, config, config2);
            outputStream.flush();
        }
    }

}
