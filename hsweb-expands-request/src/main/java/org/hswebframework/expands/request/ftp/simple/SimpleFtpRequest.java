package org.hswebframework.expands.request.ftp.simple;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.hswebframework.expands.request.ftp.FtpRequest;
import org.hswebframework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class SimpleFtpRequest implements FtpRequest {
    private Logger logger = LoggerFactory.getLogger(FtpRequest.class);
    private FTPClient ftp;
    private String    username, password;
    private String workDir = "/";

    public SimpleFtpRequest(String addr, int port, String username, String password) throws IOException {
        if (StringUtils.isNullOrEmpty(username)) {
            username = "Anonymous";
        }
        this.username = username;
        this.password = password;
        ftp = new FTPClient();
        ftp.connect(addr, port);
        ftp.setAutodetectUTF8(true);
        ftp.setListHiddenFiles(true);
        login();
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        int replyCode = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            ftp.disconnect();
            throw new IOException("建立ftp链接失败:" + replyCode);
        }
    }

    public FtpRequest setting(Consumer<FTPClient> clientConsumer) {
        clientConsumer.accept(ftp);
        return this;
    }

    @Override
    public FtpRequest encode(String encode) {
        ftp.setControlEncoding(encode);
        return this;
    }

    @Override
    public FtpRequest login() throws IOException {
        ftp.login(username, password);
        return this;
    }

    @Override
    public List<FTPFile> lsDir() throws IOException {
        return Arrays.asList(ftp.listDirectories());
    }

    @Override
    public List<FTPFile> lsDir(String path) throws IOException {
        return Arrays.asList(ftp.listDirectories(path));
    }


    @Override
    public boolean logout() throws IOException {
        return ftp.logout();
    }

    @Override
    public List<FTPFile> ls() throws IOException {
        return Arrays.asList(ftp.listFiles());
    }

    @Override
    public List<FTPFile> ls(String path) throws IOException {
        return Arrays.asList(ftp.listFiles(path));
    }

    @Override
    public boolean mkdir(String path) throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("mkdir : {}", path);
        }
        return ftp.makeDirectory(path);
    }

    @Override
    public boolean rename(String oldName, String newName) throws IOException {
        return ftp.rename(oldName, newName);
    }

    @Override
    public boolean rm(String name) throws IOException {
        return ftp.deleteFile(name);
    }

    @Override
    public FtpRequest cd(String path) throws IOException {
        workDir = path;
        if (logger.isInfoEnabled()) {
            logger.info("cd : {}", path);
        }
        ftp.changeWorkingDirectory(path);
        return this;
    }

    @Override
    public boolean upload(String fileName, InputStream inputStream) throws IOException {
        if (logger.isInfoEnabled()) {
            logger.info("upload file : {}", fileName);
        }
        return ftp.storeFile(fileName, inputStream);
    }

    @Override
    public boolean upload(File file) throws IOException {
        if (file.isFile()) {
            return upload(file.getName(), new FileInputStream(file));
        }
        if (file.isDirectory()) {
            String dir = file.getName();
            if (".".equals(dir)) {
                String[] tmp = file.getAbsolutePath().split("[" + File.separator + "]");
                dir = tmp[tmp.length - 2];
            }
            String tmp = workDir;
            mkdir(dir);
            cd(dir);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file1 = files[i];
                upload(file1);
            }
            cd(tmp);
        }
        return true;
    }

    @Override
    public void download(String name, OutputStream outputStream) throws IOException {
        ftp.retrieveFile(name, outputStream);
    }

//    public static void main(String[] args) throws IOException {
//        FtpRequest request = new AbstractFtpRequest("192.168.2.142", 2121, null, null);
//        request.encode("gbk");
//        request.login();
//        request.cd("/2");
//        request.ls().forEach(System.out::println);
//        // request.upload(new File("."));
//    }
}
