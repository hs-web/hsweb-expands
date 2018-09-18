package org.hswebframework.expands.compress;

import org.hswebframework.expands.compress.zip.ZIPReader;
import org.hswebframework.utils.RandomUtil;
import org.hswebframework.utils.file.FileUtils;
import org.junit.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 压缩解压文件测试
 *
 * @author zhouhao
 */
public class CompressTest {

    @org.junit.Test
    public void testUnzip() throws Exception {
        ZIPReader reader = Compress.unzip(FileUtils.getResourceAsFile("test.zip"));
        List<String> files = reader.ls();
        Assert.assertEquals(files.size(), 2);
        reader.unpack("test", new File("target/test"));
    }

    @org.junit.Test
    public void testZip() throws Exception {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            builder.append(RandomUtil.randomChar(1000));
        }
        Compress.zip()
                .addTextFile("测试.txt", "test")
                .addTextFile("/test/test2.txt", builder.toString())
                .level(9)
                .write(new FileOutputStream("target/test.zip"));
    }
}