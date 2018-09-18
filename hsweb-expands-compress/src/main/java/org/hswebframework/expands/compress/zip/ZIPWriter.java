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
import java.util.zip.Deflater;
import java.util.zip.ZipOutputStream;

/**
 * Created by zhouhao on 16-7-5.
 */
public class ZIPWriter {

    private List<ZipFileSteam> zipFileDatas = new ArrayList<>();
    private List<ZipFileFile>  zipFileFiles = new ArrayList<>();

    private int compressLevel = -1;

    public ZIPWriter level(int compressLevel) {
        this.compressLevel = compressLevel;
        return this;
    }

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

        try (ZipOutputStream out = new ZipOutputStream(outputStream)) {
            out.setLevel(compressLevel);
            List<ZipEntrySource> zipEntrySources = zipFileDatas.isEmpty() ? new ArrayList<>() :
                    zipFileDatas.stream()
                            .map(ZipFileSteam::create)
                            .collect(Collectors.toList());

            for (ZipEntrySource zipEntrySource : zipEntrySources) {
                addEntry(zipEntrySource, out);
            }
            out.flush();
            out.finish();
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private void addEntry(ZipEntrySource entry, ZipOutputStream out) throws IOException {
        out.putNextEntry(entry.getEntry());
        InputStream in = entry.getInputStream();
        if (in != null) {
            try {
                IOUtils.copy(in, out);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
        out.closeEntry();
    }


    class ZipFileSteam {
        private String      name;
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
        private File   file;

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
