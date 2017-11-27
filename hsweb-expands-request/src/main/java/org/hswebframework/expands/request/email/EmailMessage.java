package org.hswebframework.expands.request.email;

import java.io.InputStream;

public interface EmailMessage {
    EmailMessage from(String from);

    EmailMessage to(String to, String... more);

    EmailMessage subject(String sub);

    EmailMessage content(String content, String contentType);

    EmailMessage addImage(String id, InputStream inputStream);

    EmailMessage addAttach(String name, InputStream inputStream);

    EmailResponse send();
}
