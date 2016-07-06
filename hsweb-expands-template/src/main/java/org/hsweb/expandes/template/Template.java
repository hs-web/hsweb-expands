package org.hsweb.expandes.template;

import org.hsweb.expandes.template.freemarker.FreemarkerTemplateRender;

/**
 * @author zhouhao
 */
public enum Template {
    freemarker {
        FreemarkerTemplateRender render = new FreemarkerTemplateRender(2, 3, 23);

        @Override
        public TemplateRender compile(String template) {
            return render.compile(template);
        }
    };

    public abstract TemplateRender compile(String template);

}
