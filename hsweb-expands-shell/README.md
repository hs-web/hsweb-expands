## shell执行模块

```java
        //执行命令
       Shell.build("ping","www.baidu.com")
            .onProcess((line, helper) -> {
                    System.out.println(line);
                   // helper.kill(); 结束
                })
            .onError((line, helper) -> System.out.println(line))
            .exec();
        //执行多个命令 
      Shell.buildText("echo helloShell \n ls").dir("/")
                    .onProcess((line, helper) -> System.out.println(line))
                    .exec();
```