# 动态脚本模块
### 支持动态编译执行java,groovy,javascript,spel,ognl

```java
     DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("groovy");//java,js,spel,ognl
     engine.compile("test", "return 3+2-5;");
     ExecuteResult result = engine.execute("test");
     Object res=result.getResult();
     // res==0
```