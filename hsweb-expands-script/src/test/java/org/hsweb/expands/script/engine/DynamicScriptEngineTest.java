package org.hsweb.expands.script.engine;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DynamicScriptEngineTest {

    @Test
    public void testJava() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("java");
        engine.compile("111", "package test.myTest;" +
                "//动态编译执行" +
                "\npublic class Test{\n" +
                "public static void test(java.util.Map param){" +
                "    param.put(\"test\",\"1\");" +
                "   }" +
                "}");
        ExecuteResult result = engine.execute("111");
        Map<String, Object> param = new HashMap<>();
        ((Class) result.getResult()).getMethod("test", Map.class).invoke(null, param);
        Assert.assertEquals("1", param.get("test"));

        engine.compile("111", "package test.myTest;" +
                "//动态编译执行" +
                "\npublic class Test implements org.hsweb.expands.script.engine.java.Executor{\n" +
                "   public  Object execute(java.util.Map<String, Object> param){" +
                "     return param;" +
                "   }" +
                "}");
        result = engine.execute("111", new HashMap<String, Object>() {{
            put("test", "111");
        }});
        Assert.assertEquals("111", ((Map) result.getResult()).get("test"));
    }

    @Test
    public void testGroovy() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("groovy");
        engine.compile("test", "return 3+2-5;");
        ExecuteResult result = engine.execute("test");
        Assert.assertEquals(result.getResult(), 0);
    }

    @Test
    public void testSpel() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("spel");
        engine.compile("test", "3+2-5");
        ExecuteResult result = engine.execute("test");
        Assert.assertEquals(result.getResult(), 0);
    }

    @Test
    public void testOgnl() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("ognl");
        engine.compile("test", "3+2-5");
        ExecuteResult result = engine.execute("test");
        Assert.assertEquals(result.getResult(), 0);
    }

    @Test
    public void testJavascript() throws Exception {
        DynamicScriptEngine engine = DynamicScriptEngineFactory.getEngine("js");
        engine.compile("test", "return 3+2-5;");
        ExecuteResult result = engine.execute("test");
        Assert.assertEquals(result.getResult(), 0);
    }


}