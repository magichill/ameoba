package org.amoeba.netty.server.http;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 *
 */
public class AmoebaHttpResponse {

    private HttpResponseStatus status;

    private String result;

    public AmoebaHttpResponse(String buffer, HttpResponseStatus status) {
        this.result = buffer;
        if (buffer == null || buffer.isEmpty()) {
            this.result = new String("No content!");
        }
        this.status = status;
    }


    public HttpResponseStatus getStatus() {
        return status;
    }


    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }


    public String getBuffer() {
        return result;
    }


    public void setBuffer(String buffer) {
        this.result = buffer;
    }

}
