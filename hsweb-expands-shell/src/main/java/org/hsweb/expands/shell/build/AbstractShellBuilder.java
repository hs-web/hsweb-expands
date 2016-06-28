package org.hsweb.expands.shell.build;

import org.hsweb.commons.MD5;
import org.hsweb.commons.file.FileUtils;
import org.hsweb.expands.shell.ShellBuilder;

import java.io.File;

/**
 * Created by zhouhao on 16-6-28.
 */
public abstract class AbstractShellBuilder implements ShellBuilder {
    protected String createFile(String text) throws Exception {
        String tmp = System.getProperty("java.io.tmpdir").concat("/org/hsweb/shell/");
        String os = System.getProperty("os.name");
        File file = new File(tmp);
        file.mkdirs();
        String timeStr = String.valueOf(MD5.defaultEncode(text));
        String shellFileName = timeStr + (os.equalsIgnoreCase("windows") ? ".bat" : ".sh");
        String encode = (os.equalsIgnoreCase("windows") ? "gbk" : "utf-8");
        FileUtils.writeString2File(text, shellFileName = new File(file, shellFileName).getAbsolutePath(), encode);
        return shellFileName;
    }
}
