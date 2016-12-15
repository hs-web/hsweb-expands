package org.hsweb.expands.script.engine;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DynamicScriptEngineTest {

    @Test
    public void testJava() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("java");
        engine.compile("111", "package test.myTest;" +
                "\npublic class Test implements org.hsweb.expands.script.engine.java.Executor{\n" +
                "   public  Object execute(java.util.Map<String, Object> param){" +
                "       param.put(\"test\",\"测试\");" +
                "       return param;" +
                "   }" +
                "}");
        Map<String, Object> map = new HashMap<>();
        engine.execute("111", map);
        System.out.println(map);


//        Assert.assertEquals("111", (result.getResult()));
    }

    @Test
    public void testGroovy() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("groovy");
        engine.compile("test", "return 3+2-5;");
        ExecuteResult result = engine.execute("test");
        Assert.assertEquals(result.getIfSuccess(), 0);
    }

    @Test
    public void testSpel() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("spel");
        engine.compile("test", "#test[aa]");
        ExecuteResult result = engine.execute("test", new HashMap() {{
            put("test", new HashMap() {{
                put("aa", new Date());
            }});
        }});
        System.out.println(result.getIfSuccess());
    }

    @Test
    public void testOgnl() throws Exception {
        //ognl引擎
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("ognl");
        //参数
        Map<String, Object> vars = new HashMap<>();
        vars.put("_parameter", "hehe");
        //编译
        engine.compile("test", "_parameter=='hehe'?'1111':'222'");
        //执行
        ExecuteResult result = engine.execute("test", vars);
        Assert.assertEquals(result.getIfSuccess(), "1111");
    }

    @Test
    public void testJavascript() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("js");

        engine.addGlobalVariable(Collections.singletonMap("logger", LoggerFactory.getLogger("org.hsweb.script.javascript")));

        engine.compile("test", "logger.info('test')");
        engine.execute("test").getIfSuccess();
    }


}