# 拓展工具,office文档处理,动态脚本等

[![Maven Central](https://img.shields.io/maven-central/v/org.hswebframework/hsweb-expands.svg?style=plastic)](http://search.maven.org/#search%7Cga%7C1%7Chsweb-expands)


hsweb-expands的使用指南!

说明
1.首先把项目down下来
git clone https://github.com/hs-web/hsweb-expands.git

2.然后运行mvn install时跳过Test
mvn install -DskipTests

或者
mvn install -Dmaven.test.skip=true

也可以修改pom文件
<plugin>    
    <groupId>org.apache.maven.plugin</groupId>    
    <artifactId>maven-compiler-plugin</artifactId>    
    <version>2.1</version>    
    <configuration>    
        <skip>true</skip>    
    </configuration>    
</plugin>    
<plugin>    
    <groupId>org.apache.maven.plugins</groupId>    
    <artifactId>maven-surefire-plugin</artifactId>    
    <version>2.5</version>    
    <configuration>    
        <skip>true</skip>    
    </configuration>    
</plugin>

3.BUILD SUCCESS后打开单元测试用例。找到你需要的功能点，运行测试。
4.进入相关方法查看理解源码。
