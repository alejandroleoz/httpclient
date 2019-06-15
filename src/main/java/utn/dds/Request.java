package utn.dds;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class Request {

    private String url;
    private Method method = Method.GET;
    private String contentType = "text/plain";
    private String accept = "*/*";
    private Map<String, Serializable> parameters = Collections.emptyMap();
    private Map<String, String> headers = Collections.emptyMap();
    private String payload = null;

    public enum Method {
        GET,
        POST,
        PUT,
        DELETE,
        HEAD,
        OPTIONS,
        TRACE
    }

    public Request(String url) {
        this.url = url;
    }

    public Request withMethod(Method method) {
        this.method = method;
        return this;
    }

    public Request withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Request withAcceptHeader(String accept) {
        this.accept = accept;
        return this;
    }

    public Request withParameters(Map<String, Serializable> parameters) {
        this.parameters = parameters;
        return this;
    }

    public Request withHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Request withPayload(String payload) {
        this.payload = payload;
        return this;
    }

    public Response execute(HTTPClient client) throws IOException {
        return client.doRequest(this);
    }

    public String getUrl() {
        return url;
    }

    public Method getMethod() {
        return method;
    }

    public String getContentType() {
        return contentType;
    }

    public String getAccept() {
        return accept;
    }

    public Map<String, Serializable> getParameters() {
        return parameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getPayload() {
        return payload;
    }

    public boolean requiresPayload() {
        return Objects.nonNull(payload) &&
                (Method.POST.equals(method) || Method.PUT.equals(method));
    }
}
