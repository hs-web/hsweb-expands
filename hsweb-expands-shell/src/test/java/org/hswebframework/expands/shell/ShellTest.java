package org.hswebframework.expands.shell;

import org.hswebframework.utils.file.FileUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by zhouhao on 16-6-28.
 */
public class ShellTest {

    @Test
    public void testPing() throws Exception {
        int[] count = new int[1];
        Shell.build("ping", "www.baidu.com")
                .onProcess((line, helper) -> {
                    if (++count[0] > 10) helper.kill();
                    System.out.println(line);
                })
                .exec();
    }

    @Test
    public void testLs() throws Exception {
        Shell.build("ls")
                .onProcess((line, helper) -> System.out.println(line))
                .exec();
    }

    @Test
    public void testShellFile() throws Exception {
        Shell.build("bash", FileUtils.getResourceAsFile("test.sh").getAbsolutePath())
                .onProcess((line, helper) -> System.out.println(line))
                .exec();
    }

    @Test
    public void testText() throws Exception {
        Shell.buildText("echo helloShell \n ls").dir("/")
                .onProcess((line, helper) -> System.out.println(line))
                .exec();
    }

    @Test
    public void testJavac() throws Exception {
        String code = "public class Test{" +
                "public static void main(String args[]){" +
                "   for(int i=0;i<args.length;i++){System.out.print(args[i]+\\\"\\\\t\\\");}" +
                "   System.out.println();" +
                "}" +
                "};";
        Shell.buildText("\necho \"" + code + "\">Test.java \n" +
                "/usr/lib/jvm/jdk1.8.0_77/bin/javac -encoding utf-8 Test.java\n" +
                "/usr/lib/jvm/jdk1.8.0_77/bin/java Test arg1 arg2")
                .dir("target")
                .onProcess((line, helper) -> System.out.println(line))
                .onError((line, helper) -> System.out.println(line))
                .exec();
    }

    @Test
    public void testDocker() {
        for (int i = 0; i < 20; i++) {
            int n = i;
            new Thread(() -> {
                try {
                    Shell.build("docker run --rm --name mysql-test-" + n + " -e MYSQL_ROOT_PASSWORD=test mysql")
                            .onProcess((line, helper) -> System.out.println(line))
                            .onError((line, helper) -> System.out.println(line))
                            .exec();
                } catch (IOException e) {
                }
            }).start();
        }
    }

    public static void main(String[] args) throws Exception {
        Shell.buildText("ifconfig | awk '/HWaddr/{print $5}'")
                .onProcess((line, helper) -> System.out.println(line))
                .exec();
    }

}