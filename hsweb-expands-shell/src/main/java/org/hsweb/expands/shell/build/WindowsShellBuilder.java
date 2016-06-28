package org.hsweb.expands.shell.build;

import org.hsweb.expands.shell.Shell;

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
