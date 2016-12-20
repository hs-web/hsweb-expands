package org.hswebframework.expands.script.engine;


import org.hswebframework.expands.script.engine.SpEL.SpElEngine;
import org.hswebframework.expands.script.engine.groovy.GroovyEngine;
import org.hswebframework.expands.script.engine.java.JavaEngine;
import org.hswebframework.expands.script.engine.js.JavaScriptEngine;
import org.hswebframework.expands.script.engine.ognl.OgnlEngine;
import org.hswebframework.expands.script.engine.python.PythonScriptEngine;
import org.hswebframework.expands.script.engine.ruby.RubyScriptEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 浩 on 2015-10-27 0027.
 */
public final class DynamicScriptEngineFactory {
    private static final Map<String, DynamicScriptEngine> map = new HashMap<>();

    static {
        JavaScriptEngine engine = new JavaScriptEngine();
        map.put("js", engine);
        map.put("javascript", engine);
        map.put("groovy", new GroovyEngine());
        map.put("ruby", new RubyScriptEngine());
        map.put("python", new PythonScriptEngine());
        try {
            map.put("java", new JavaEngine());
        } catch (Exception e) {

        }
        try {
            Class.forName("org.springframework.expression.ExpressionParser");
            map.put("spel", new SpElEngine());
        } catch (ClassNotFoundException e) {
        }
        try {
            Class.forName("ognl.Ognl");
            map.put("ognl", new OgnlEngine());
        } catch (ClassNotFoundException e) {
        }
    }

    public static final DynamicScriptEngine getEngine(String type) {
        return map.get(type);
    }

}
