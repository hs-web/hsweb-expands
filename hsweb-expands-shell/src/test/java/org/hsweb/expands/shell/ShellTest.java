package org.hsweb.expands.shell;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zhouhao on 16-6-28.
 */
public class ShellTest {

    @Test
    public void testExec() throws Exception {
        int[] count = new int[1];
        Shell.build("ping www.baidu.com")
                .onProcess((line, helper) -> {
                    System.out.println(line);
                    count[0]++;
                    //读取到输入10行后退出
                    if (count[0] > 10) helper.shutdown();
                })
                .exec();
    }
}