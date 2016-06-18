package org.hsweb.expands.script.engine.java;

import java.net.URL;
import java.net.URLClassLoader;

public class DynamicClassLoader extends URLClassLoader {


    public DynamicClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

}