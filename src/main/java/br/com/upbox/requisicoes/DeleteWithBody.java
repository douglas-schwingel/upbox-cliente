package br.com.upbox.requisicoes;

import com.mongodb.annotations.NotThreadSafe;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

@NotThreadSafe
class DeleteWithBody extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "DELETE";

    public String getMethod() {
        return METHOD_NAME;
    }

    public DeleteWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public DeleteWithBody(final URI uri) {
        super();
        setURI(uri);
    }

    public DeleteWithBody() {
        super();
    }
}
