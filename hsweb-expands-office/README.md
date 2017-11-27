# office文档处理工具

### 引入依赖

```xml
    <!--pom.xml-->
     <dependency>
        <groupId>org.hswebframework</groupId>
        <artifactId>hsweb-expands-office</artifactId>
        <version>3.0.0-SNAPSHOT</version>
    </dependency>
    
      <repositories>
        <repository>
            <id>hsweb-nexus</id>
            <name>Nexus Release Repository</name>
            <url>http://nexus.hsweb.me/content/groups/public/</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
     </repositories>
```

## 读取excel

```java
      //读取为map
      try (InputStream in = FileUtils.getResourceAsStream("User.xlsx")) {
            List<Map<String, Object>> dataList = ExcelIO.read2Map(in);
            dataList.forEach(System.out::println);
        }

        //多sheet读取为map
      try (InputStream in = FileUtils.getResourceAsStream("User.xlsx")) {
             List<List<Map<String, Object>>> dataList = ExcelIO.read2MulMap(in);
             dataList.forEach(System.out::println);
         }
         
       //读取为bean
       try (InputStream in = Resources.getResourceAsStream("User.xlsx")) {
              //设置表头与字段映射,可通过反射获取
              Map<String, String> mapper = new HashMap<>();
              mapper.put("姓名", "name");
              mapper.put("年龄", "age");
              mapper.put("备注", "remark");
              //解析为User对象集合
              List<User> dataList = ExcelIO.read2Bean(in, mapper, User.class);
          }
```

## 写出excel

```java
        List<Header> headers = new LinkedList<>();
        List<Object> datas = new ArrayList<>();
        // 简单粗暴的写出
        try (OutputStream outputStream = new FileOutputStream("target/test_1.xlsx")) {
            ExcelIO.write(outputStream, headers, datas);
            outputStream.flush();
        }
        
        //按模板写出 
         try (InputStream inputStream = FileUtils.getResourceAsStream("template.xlsx")
             ; OutputStream outputStream = new FileOutputStream("target/test_template.xlsx")) {
            //定义变量
            Map<String, Object> var = new HashMap<>();
            var.put("title", "测试");
            var.put("list", .......);
            //输出模板
            ExcelIO.writeTemplate(inputStream, outputStream, var);
            outputStream.flush();
        }
```


## word模板写出

```java
     try (InputStream in = new FileInputStream(FileUtils.getResourceAsFile("docx/template.docx"));
         OutputStream out = new FileOutputStream("target/test.docx")) {
        //构造 模板所需的变量
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", "测试");
        vars.put("age", "16");
        WordIO.writeTemplate(in, out, vars);
        out.flush();
    }
```