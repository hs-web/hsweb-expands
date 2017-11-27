package org.hswebframework.expands.request.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * http请求下载器,用户文件下载等操作
 * Created by zhouhao on 16-6-23.
 */
public interface HttpDownloader<R> {

    HttpDownloader<R> get() throws IOException;

    HttpDownloader<R> post() throws IOException;

    R write(File file) throws IOException;

    R write(OutputStream outputStream) throws IOException;

}
