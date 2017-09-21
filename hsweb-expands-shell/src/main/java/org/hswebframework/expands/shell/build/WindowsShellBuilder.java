package org.hswebframework.expands.shell.build;

import org.hswebframework.expands.shell.Shell;

/**
 * Created by zhouhao on 16-6-28.
 */
public class WindowsShellBuilder extends AbstractShellBuilder {

    @Override
    public Shell buildTextShell(String text) throws Exception {
        String file = createFile(text);
        return Shell.build(file);
    }

}
