package org.hswebframework.expands.office.word;


import org.hswebframework.expands.office.word.support.template.DOCXTemplateReader;
import org.hswebframework.utils.file.FileUtils;

import java.io.InputStream;

/**
 * Created by zhouhao on 16-6-6.
 */
public class TestReader {

    public static void main(String[] args) throws Exception {
        try (InputStream template = FileUtils.getResourceAsStream("docx/template.docx");
             InputStream data = FileUtils.getResourceAsStream("docx/template_data.docx")
        ) {
            DOCXTemplateReader reader = new DOCXTemplateReader(template, data);
            System.out.println(reader.read());
        }

    }
}
