package org.hswebframework.expands.request.http.simple;


import org.apache.commons.codec.digest.DigestUtils;
import org.hswebframework.expands.request.RequestBuilder;
import org.hswebframework.expands.request.SimpleRequestBuilder;
import org.hswebframework.expands.request.http.HttpRequest;
import org.hswebframework.expands.request.http.HttpRequestGroup;
import org.hswebframework.expands.request.http.Response;
import org.hswebframework.expands.request.webservice.WebServiceRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * Created by zhouhao on 16-6-23.
 */
public class SimpleRequestBuilderTest {

    RequestBuilder builder;

    @Before
    public void setup() {
        builder = new SimpleRequestBuilder();
    }


    @Test
    public void testHttpDownload() throws Exception {
        Document document2 = Jsoup.parse(new URL("https://www.baidu.com"), 5000);
        System.out.println(document2);
        Document document = Jsoup.parse(new URL("http://tm.22.cn/check/search/?type=1&state=0&tid=0&keyword=%E8%B1%86%E8%85%90&direction=0&page=1"), 1000);
        Elements elements = document.select(".content-inq-con img");
        for (Element element : elements) {
            System.out.println("下载图片:" + element);
            String src = element.attr("src");
            File file = new File("target/" + System.currentTimeMillis() + ".jpg");
            builder.http(src).download().write(file);
            src = "/download/file/" + file.getName();
            element.attr("src", src);
            System.out.println("替换图片:" + element);
        }
    }

    @Test
    public void testHttp() throws IOException {
        HttpRequestGroup group = builder.http();

        String login = group.request("http://demo.hsweb.me/login")
                .param("username", "test")
                .param("password", "123456").post().asString();

        System.out.println(login);
        System.out.println(group.request("http://demo.hsweb.me/online").get().asString());

        System.out.println(group.request("http://demo.hsweb.me/user").get().asString());
    }

    @Test
    public void testHttps() throws IOException {
        HttpRequest request = builder.https("https://www.aliyun.com/");
        Response response = request.get();
        System.out.println(response.asString());
    }


    @Test
    public void testFtp() throws IOException {
        builder.ftp("192.168.2.142", 2121, "", "")
                .encode("gbk")
                .ls()
                .forEach(System.out::println);
    }

    @Test
    public void testEmail() throws Exception {
        // TODO: 16-9-29
        builder.email()
                .setting("host", "smtp.qq.com")
                .setting("username", "")
                .setting("password", "")
                .connect()
                .createMessage()
                .to("admin@hsweb.me")
                .content("test..", "text/html")
                .send();
    }

    @Test
    public void testWebService() throws Exception {
        WebServiceRequest request = builder.webService()
                .wsdl("/home/zhouhao/云文档/项目/apsp/接口文档/WSDL/查询密码验证.wsdl");
        System.out.println(request.interfaces());
        System.out.println(request.services());
        for (String s : request.interfaces()) {
            Method[] methods = request.methods(s);
            for (Method method : methods) {
                System.out.println(method);
            }
        }
    }

}