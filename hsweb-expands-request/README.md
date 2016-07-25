## 模拟请求模块:(http,ftp)

## http

```java
     String html= new SimpleRequestBuilder()
      .http("http://[host]:[port]/")
      .header("key","value")
      .resultAsJsonString() //  ->  header("Accept", "application/json");
      .get()
      .asString();
      
      //下载文件
      new SimpleRequestBuilder()
       .http("http://[host]:[port]/")
       .download()
       .write(outputstream);
```

## ftp

```java
    new SimpleRequestBuilder()
        .ftp("127.0.0.1",21,"username","password")
        .encode("gbk")
        .ls()
        .forEach(System.out::println);
```