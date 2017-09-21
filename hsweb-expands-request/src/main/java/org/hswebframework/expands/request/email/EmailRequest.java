package org.hswebframework.expands.request.email;

import java.util.Properties;

public interface EmailRequest {
    EmailRequest setting(String property, Object value);

    EmailRequest settings(Properties properties);

    EmailSender connect();

}
