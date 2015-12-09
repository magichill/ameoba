package org.amoeba.netty.server.http;


import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class AmoebaHttpRequest {

    private HttpRequest httpRequest;

    private String method;

    private String uri;

    private Map<String, List<String>> params;

    private String content;

    private HttpContent httpContnet;

    public AmoebaHttpRequest(HttpRequest request, String channelId) {
        if (request == null || channelId == null) throw new IllegalArgumentException();
        this.httpRequest = request;
        this.uri = request.getUri();
        this.method = request.getMethod().toString();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
        this.params = queryStringDecoder.parameters();
        AbstractExecutor.requestThreadLocal.set(this);
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public String getContent() {
        return content;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, List<String>> getParams() {
        return params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HttpContent getHttpContnet() {
        return httpContnet;
    }

    public void setHttpContnet(HttpContent httpContnet) {
        this.httpContnet = httpContnet;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((params == null) ? 0 : params.hashCode());
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AmoebaHttpRequest other = (AmoebaHttpRequest) obj;
        if (content == null) {
            if (other.content != null) return false;
        } else if (!content.equals(other.content)) return false;
        if (method == null) {
            if (other.method != null) return false;
        } else if (!method.equals(other.method)) return false;
        if (params == null) {
            if (other.params != null) return false;
        } else if (!params.equals(other.params)) return false;
        if (uri == null) {
            if (other.uri != null) return false;
        } else if (!uri.equals(other.uri)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "AmoebaHttpRequest [method=" + method + ", uri=" + uri + ", params=" + params + ", content="
                + content + "]";
    }
}
