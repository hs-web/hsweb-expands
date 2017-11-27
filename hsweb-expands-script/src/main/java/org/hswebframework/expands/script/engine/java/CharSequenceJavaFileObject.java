package org.hswebframework.expands.script.engine.java;


import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.File;

public class CharSequenceJavaFileObject  extends SimpleJavaFileObject {

    private CharSequence content;


    public CharSequenceJavaFileObject(String javaFilePath, String className, CharSequence content) throws Exception {
        super(new File(javaFilePath + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension).toURI(), JavaFileObject.Kind.SOURCE);
        this.content = content;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return content;
    }
}