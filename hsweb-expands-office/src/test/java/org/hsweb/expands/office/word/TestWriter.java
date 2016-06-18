package org.hsweb.expands.office.word;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.hsweb.commons.file.FileUtils;
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

    public static void main(String[] args) throws Exception {
        String[] headers = {"姓名", "性别", "年龄", "班级"};
        List<Map<String, Object>> datas = new ArrayList<>();
        datas.add(new HashMap<String, Object>() {
            {
                put("姓名", "222");
                put("性别", "aaa");
            }
        });
        datas.add(new HashMap<String, Object>() {
            {
                put("姓名", "33");
                put("性别", "222");
            }
        });
        XWPFDocument document = new XWPFDocument();
        XWPFTable table = document.createTable(datas.size(), headers.length);
        table.setWidth(500);
        List<XWPFTableRow> rows = table.getRows();
        for (int i = 0; i < rows.size(); i++) {
            if(i==0)continue;
            XWPFTableRow row = rows.get(i);
            List<XWPFTableCell> cells = row.getTableCells();
            for (int x = 0; x < cells.size(); x++) {
                String header = headers[x];
                if (i ==1)
                    cells.get(x).setText(header);
                else  {
                    String text = String.valueOf(datas.get(i - 1).get(header));
                    cells.get(x).setText(text);
                }
            }
        }
        document.write(new FileOutputStream("/home/zhouhao/桌面/test2.docx"));
    }


}
