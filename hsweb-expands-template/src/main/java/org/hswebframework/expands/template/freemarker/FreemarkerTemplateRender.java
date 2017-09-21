package org.hswebframework.expands.template.freemarker;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.hswebframework.expands.template.TemplateRender;
import org.hswebframework.utils.StringUtils;

import java.io.StringWriter;

public class FreemarkerTemplateRender {
    //freemarker字符串模板加载器
    private final StringTemplateLoader loader = new StringTemplateLoader();
    //freemarker配置器
    private final Configuration freemarkerCfg;

    public FreemarkerTemplateRender(int major, int minor, int micro) {
        freemarkerCfg = new Configuration(new Version(major, minor, micro));
        freemarkerCfg.setTemplateLoader(loader);
    }

    public final TemplateRender compile(String templateStr) {
        StringTemplateLoader templateLoader = ((StringTemplateLoader) freemarkerCfg.getTemplateLoader());
        String name = StringUtils.concat("template.", templateStr.hashCode());
        templateLoader.putTemplate(name, templateStr);
        freemarkerCfg.setTemplateLoader(templateLoader);
        final String finalName = name;
        return vars -> {
            Template template = freemarkerCfg.getTemplate(finalName);
            StringWriter out = new StringWriter();
            template.process(vars, out);
            return out.getBuffer().toString();
        };
    }
}
