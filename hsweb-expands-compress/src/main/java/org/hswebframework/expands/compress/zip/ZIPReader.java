package org.hswebframework.expands.compress.zip;

import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by zhouhao on 16-7-5.
 */
public class ZIPReader {

    private File zipFile;

    public ZIPReader(File inputStream) {
        zipFile = inputStream;
    }

    public List<String> ls() {
        List<String> name = new ArrayList<>();
        ZipUtil.iterate(zipFile, (inputStream, zipEntry) -> {
            name.add(zipEntry.getName());
        });
        return name;
    }

    public InputStream read(String entryName) {
        byte[] data = ZipUtil.unpackEntry(zipFile, entryName);
        Objects.requireNonNull(data, entryName);
        return new ByteArrayInputStream(data);
    }

    public void unpack(String entryName, File to) throws IOException {
        ZipUtil.unpackEntry(zipFile, entryName, to);
    }

    public void unpack(File to) throws IOException {
        ZipUtil.unpack(zipFile, to);
    }

    public void remove(String entryName) throws IOException {
        ZipUtil.removeEntry(zipFile, entryName);
    }

    public void replace(String entryName, InputStream inputStream) throws IOException {
        ZipUtil.replaceEntry(zipFile, entryName, IOUtils.toByteArray(inputStream));
    }

}
