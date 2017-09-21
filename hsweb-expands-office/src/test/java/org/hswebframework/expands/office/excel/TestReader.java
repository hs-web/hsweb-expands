package org.hswebframework.expands.office.excel;

import org.hswebframework.expands.office.excel.config.ExcelReaderCallBack;
import org.hswebframework.expands.office.excel.support.CommonExcelReader;
import org.hswebframework.expands.office.excel.wrapper.HashMapWrapper;
import org.hswebframework.utils.StringUtils;
import org.hswebframework.utils.file.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 浩 on 2015-12-07 0007.
 */
public class TestReader {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 测试将excel表格转为map
     */
    @Test
    public void testRead2Map() throws Exception {
        try (InputStream in = FileUtils.getResourceAsStream("User.xlsx")) {
            List<Map<String, Object>> dataList = ExcelIO.read2Map(in);
            dataList.forEach(System.out::println);
        }
    }
    /**
     * 测试将excel表格转为多个map
     */
    @Test
    public void testRead2MulMap() throws Exception {
        try (InputStream in = FileUtils.getResourceAsStream("User.xlsx")) {
            List<List<Map<String, Object>>> dataList = ExcelIO.read2MulMap(in);
            dataList.forEach(System.out::println);
        }
    }
    /**
     * 有合并行的excel读取示例
     */
    @Test
    public void testReadMerge2Map() throws Exception {
        try (InputStream in = FileUtils.getResourceAsStream("merge.xlsx")) {
            List<Map<String, Object>> dataList = ExcelIO.read(in, new HashMapWrapper() {
                String lastProject = null;

                {
                    //添加表头映射，将中文表头映射为英文
                    headerNameMapper.put("项目", "product");
                    //..
                    //..
                }

                @Override
                public void wrapper(Map<String, Object> instance, String header, Object value) {
                    //解决：列[项目]，合并的单元格只有一行有值，其他为空白。
                    if ("项目".equals(header)) {
                        if (!StringUtils.isNullOrEmpty(value)) {
                            lastProject = String.valueOf(value);
                        }
                        value = lastProject;
                    }
                    super.wrapper(instance, header, value);
                }
            });
            Assert.assertEquals(dataList.size(), 12);
            for (Map<String, Object> map : dataList) {
                System.out.println(map);
            }
        }
    }

    /**
     * 测试将excel表格转为bean
     */
    @Test
    public void testRead2Bean() throws Exception {
        try (InputStream in = FileUtils.getResourceAsStream("User.xlsx")) {
            //设置表头与字段映射,可通过反射获取
            Map<String, String> mapper = new HashMap<>();
            mapper.put("姓名", "name");
            mapper.put("年龄", "age");
            mapper.put("备注", "remark");
            //解析为User对象集合
            List<User> dataList = ExcelIO.read2Bean(in, mapper, User.class);
            Assert.assertEquals(dataList.size(), 5);
            logger.info(dataList.toString());
        }
    }


    /**
     * 自定义方式读取一个excel
     *
     * @throws Exception
     */
    @Test
    public void testReadComplicated() throws Exception {
        try (InputStream in = FileUtils.getResourceAsStream("Test.xlsx")) {
            CommonExcelReader<Map<String, Object>> reader = new CommonExcelReader<Map<String, Object>>() {
                @Override
                protected boolean isHeader(ExcelReaderCallBack.CellContent content, List header) {
                    //第二行开始为表头
                    return content.getRow() == 1;
                }
            };
            //设置包装器
            reader.setWrapper(new HashMapWrapper());
            List<Map<String, Object>> list = reader.readExcel(in);

            for (Map<String, Object> map : list) {
                logger.info(map.toString());
            }
            Assert.assertEquals(list.size(), 33);
        }
    }

}
