package org.hswebframework.expands.compress.zip;

import org.zeroturnaround.zip.ByteSource;
import org.zeroturnaround.zip.FileSource;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhouhao on 16-7-5.
 */
public class ZIPWriter {

    private List<ZipFileSteam> zipFileDatas = new ArrayList<>();
    private List<ZipFileFile> zipFileFiles = new ArrayList<>();

    public ZIPWriter addTextFile(String fileName, String text) {
        zipFileDatas.add(new ZipFileSteam(fileName, new ByteArrayInputStream(text.getBytes())));
        return this;
    }

    private void addDir(String prefix, String rootName, File dir) throws FileNotFoundException {
        for (File file : dir.listFiles()) {
            String fileName = file.getAbsolutePath();
            fileName = fileName.substring(fileName.indexOf(prefix) + prefix.length(), fileName.length());
            if (file.isDirectory()) {
                addDir(prefix, rootName, file);
                continue;
            }
            addFile(rootName + fileName, file);
        }
    }

    public ZIPWriter addDir(File dir) throws FileNotFoundException {
        addDir(dir.getAbsolutePath(), dir.getName(), dir);
        return this;
    }

    public ZIPWriter addFile(String fileName, File file) throws FileNotFoundException {
        zipFileFiles.add(new ZipFileFile(fileName, file));
        return this;
    }

    public ZIPWriter addFile(String fileName, InputStream inputStream) {
        zipFileDatas.add(new ZipFileSteam(fileName, inputStream));
        return this;
    }

    public void write(OutputStream outputStream) {
        List<ZipEntrySource> zipEntrySources = zipFileDatas.size() == 0 ? new ArrayList<>() : zipFileDatas.stream().map(zipFileData -> zipFileData.create()).collect(Collectors.toList());
        if (zipFileFiles.size() > 0) {
            zipEntrySources.addAll(zipFileFiles.stream().map(zipFileData -> zipFileData.create()).collect(Collectors.toList()));
        }
        ZipUtil.pack(zipEntrySources.toArray(new ZipEntrySource[zipEntrySources.size()]), outputStream);
    }

    class ZipFileSteam {
        private String name;
        private InputStream inputStream;

        public ZipFileSteam(String name, InputStream inputStream) {
            this.name = name;
            this.inputStream = inputStream;
        }

        public ZipEntrySource create() {
            ZipEntrySource zipEntrySource;
            try (InputStream in = inputStream) {
                zipEntrySource = new ByteSource(name, IOUtils.toByteArray(in));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return zipEntrySource;
        }
    }

    class ZipFileFile {
        private String name;
        private File file;

        public ZipFileFile(String name, File file) {
            this.name = name;
            this.file = file;
        }

        public ZipEntrySource create() {
            ZipEntrySource zipEntrySource;
            zipEntrySource = new FileSource(name, file);
            return zipEntrySource;
        }
    }

}
