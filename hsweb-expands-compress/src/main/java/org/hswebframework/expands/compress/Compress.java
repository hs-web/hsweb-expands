package org.hswebframework.expands.compress;

import org.hswebframework.expands.compress.zip.ZIPReader;
import org.hswebframework.expands.compress.zip.ZIPWriter;

import java.io.File;

public class Compress {
    public static ZIPReader unzip(File file) {
        return new ZIPReader(file);
    }

    public static ZIPWriter zip() {
        return new ZIPWriter();
    }
}
