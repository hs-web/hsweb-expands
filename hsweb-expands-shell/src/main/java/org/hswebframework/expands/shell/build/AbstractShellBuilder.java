package org.hswebframework.expands.shell.build;

import org.hswebframework.expands.shell.ShellBuilder;
import org.hswebframwork.utils.StringUtils;
import org.hswebframwork.utils.file.FileUtils;

import java.io.File;

public abstract class AbstractShellBuilder implements ShellBuilder {
    protected String createFile(String text) throws Exception {
        String tmp = System.getProperty("java.io.tmpdir").concat("/org/hsweb/shell/");
        String os = System.getProperty("os.name");
        File file = new File(tmp);
        file.mkdirs();
        String timeStr = StringUtils.concat("shell_", Math.abs(text.hashCode()));
        String shellFileName = timeStr + (os.equalsIgnoreCase("windows") ? ".bat" : ".sh");
        String encode = (os.equalsIgnoreCase("windows") ? "gbk" : "utf-8");
        FileUtils.writeString2File(text, shellFileName = new File(file, shellFileName).getAbsolutePath(), encode);
        return shellFileName;
    }
}
