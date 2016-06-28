package org.hsweb.expands.shell.build;

import org.hsweb.expands.shell.Shell;

/**
 * Created by zhouhao on 16-6-28.
 */
public class LinuxShellBuilder extends AbstractShellBuilder {

    @Override
    public Shell buildTextShell(String text) throws Exception {
        if (!text.startsWith("#!")) {
            text = "#!/usr/bin/env bash\n" + text;
        }
        String file = createFile(text);

        return Shell.build("bash", file);
    }

}
