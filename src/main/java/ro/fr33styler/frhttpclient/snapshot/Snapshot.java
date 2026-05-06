package ro.fr33styler.frhttpclient.snapshot;

import java.util.LinkedHashMap;
import java.util.Map;

public class Snapshot {

    private String method;
    private String url;
    private final Map<String, String> headers = new LinkedHashMap<>();
    private final Map<String, String> parameters = new LinkedHashMap<>();
    private String body;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
