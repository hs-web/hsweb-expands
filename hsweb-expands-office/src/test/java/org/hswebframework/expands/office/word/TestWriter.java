package org.hswebframework.expands.office.word;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.hswebframework.utils.file.FileUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 浩 on 2015-12-18 0018.
 */
public class TestWriter {

    @Test
    public void testWriteTemplate2() throws Exception {
        try (InputStream in = new FileInputStream("/home/zhouhao/文档/保证书模板.docx");
             OutputStream out = new FileOutputStream("target/保证书模板.docx")) {
            //构造 模板所需的变量
            Map<String, Object> vars = new HashMap<>();
            vars.put("公司", "测试");
            vars.put("地址", "重庆市****");
            WordIO.writeTemplate(in, out, vars);
            out.flush();
        }
    }

    @Test
    public void testWriteTemplate() throws Exception {
        try (InputStream in = new FileInputStream(FileUtils.getResourceAsFile("docx/test.docx"));
             OutputStream out = new FileOutputStream("target/test.docx")) {
            //构造 模板所需的变量
            Map<String, Object> vars = new HashMap<>();
            vars.put("name", "姓名");
            vars.put("list", new ArrayList<Object>() {
                {
                    add(new HashMap<String, Object>() {
                        {
                            put("name", "张三");
                            put("sex", true);
                            put("age", 10);
                            put("remark", "测试");
                        }
                    });
                    add(new HashMap<String, Object>() {
                        {
                            put("name", "李四");
                            put("sex", false);
                            put("age", 10);
                            put("remark", "测试2");
                        }
                    });
                }
            });
            WordIO.writeTemplate(in, out, vars);
            out.flush();
        }
    }

}
