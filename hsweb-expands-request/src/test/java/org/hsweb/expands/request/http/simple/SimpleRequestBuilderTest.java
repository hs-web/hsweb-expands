package org.hsweb.expands.request.http.simple;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.hsweb.commons.file.FileUtils;
import org.hsweb.expands.request.RequestBuilder;
import org.hsweb.expands.request.SimpleRequestBuilder;
import org.hsweb.expands.request.http.HttpRequest;
import org.hsweb.expands.request.http.Response;
import org.hsweb.expands.request.webservice.WebServiceRequest;
import org.hsweb.expands.request.webservice.WebServiceRequestInvoker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
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
        HttpRequest request = builder.http("192.168.2.195:8888/user");
        Response response = request.get();
        System.out.println(response.getCode());
        System.out.println(response.asString());
    }

    @Test
    public void testHttps() throws IOException {
        HttpRequest request = builder.https("https://www.aliyun.com/");
        Response response = request.get();
        System.out.println(response.asString());
    }


    @Test
    public void testFtp() throws IOException {
        builder.ftp("202.98.57.23", 21, "sjzx", "sjzx")
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
}