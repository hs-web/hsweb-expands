package org.hsweb.expands.shell;

import org.hsweb.commons.file.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

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
                .onProcess(Callback.sout)
                .onError(Callback.sout).exec();
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
                "public static void main(String args[])throws Exception{" +
                "   for(int i=0;i<args.length;i++){System.out.println(args[i]+\\\"\\\\t\\\");}" +
                "     System.out.println(\\\"stdin:\\\"+System.in.read());" +
                "}" +
                "};";
        Shell.buildText("\necho \"" + code + "\">Test.java \n" +
                "/home/zhouhao/lib/jdk1.8.0_77/bin/javac -encoding utf-8 Test.java\n" +
                "/home/zhouhao/lib/jdk1.8.0_77/bin/java Test arg")
                .dir("target")
                .onProcess((line, helper) -> {
                    System.out.println(line);
                    try {
                        helper.sendMessage(new byte[]{100});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .onError((line, helper) -> System.out.println(line))
                .execAsyn(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void testDocker() throws IOException {
//        Shell.build("docker run --rm --name mysql-test -e MYSQL_ROOT_PASSWORD=test mysql")
//                .onProcess((line, helper) -> System.out.println(line))
//                .onError((line, helper) -> System.out.println(line))
//                .exec();

        Shell.build("docker", "exec", "-i", "mysql-test", "mysql", "-hlocalhost", "-uroot", "-ptest", "mysql", "</test.sql")
                .onProcess((line, helper) -> System.out.println(line))
                .onError((line, helper) -> System.out.println(line))
                .exec();

    }

    public static void main(String[] args) throws Exception {
//        Shell.buildText("ifconfig | awk '/HWaddr/{print $5}'")
//                .onProcess((line, helper) -> System.out.println(line))
//                .exec();

        Shell.build("php", "/home/zhouhao/桌面/io.php", "1")
                .onProcess((line, helper) -> System.out.println(line))
                .onError((line, helper) -> System.out.println(line))
                .exec();
    }

}