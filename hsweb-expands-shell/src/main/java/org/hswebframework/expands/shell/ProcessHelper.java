package org.hswebframework.expands.shell;

import java.io.IOException;

/**
 */
public interface ProcessHelper {
    void kill();

    void sendMessage(byte[] msg) throws IOException;

    default void sendMessage(String msg) throws IOException {
        sendMessage(msg.getBytes());
    }
}
