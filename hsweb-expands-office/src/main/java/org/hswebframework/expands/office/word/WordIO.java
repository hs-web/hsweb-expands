package org.hswebframework.expands.office.word;


import org.hswebframework.expands.office.word.api.poi.POIWordApi;
import org.hswebframework.expands.office.word.support.template.DOCXTemplateWriter;
import org.hswebframework.expands.office.word.support.template.expression.GroovyExpressionRunner;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by 浩 on 2015-12-18 0018.
 */
public class WordIO {

    /**
     * 根据模板写出一个word文档,此方法目前仅支持2007版本以上的word模板（.docx）
     * 模板表达式使用Groovy引擎
     *
     * @param inputStream  模板输入流
     * @param outputStream 模板输出流
     * @param vars         模板变量
     * @throws Exception 异常信息
     */
    public static void writeTemplate(InputStream inputStream, OutputStream outputStream, Map<String, Object> vars) throws Exception {
        GroovyExpressionRunner runner = new GroovyExpressionRunner();
        runner.setVar(vars);
        DOCXTemplateWriter writer = new DOCXTemplateWriter(outputStream, runner);
        POIWordApi.getDocxInstance().read(inputStream, writer);
    }
}
