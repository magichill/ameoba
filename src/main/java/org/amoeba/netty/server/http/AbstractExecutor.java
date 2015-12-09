package org.amoeba.netty.server.http;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 *
 */
public abstract class AbstractExecutor implements Executor {

    public static ThreadLocal<AmoebaHttpRequest> requestThreadLocal = new ThreadLocal<AmoebaHttpRequest>();

    public static ThreadLocal<AmoebaHttpResponse> responseThreadLocal = new ThreadLocal<AmoebaHttpResponse>();

    public static AmoebaHttpRequest getCurrentRequest() {
        return requestThreadLocal.get();
    }

    public static AmoebaHttpResponse getCurrentResponse() {
        return responseThreadLocal.get();
    }

    public FullHttpResponse createFullHttpResponse(String responseBody) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1,
                OK,
                Unpooled.wrappedBuffer(responseBody.getBytes()));
        return response;
    }

    public FullHttpResponse createFullHttpResponse(byte[] responseBody) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1,
                OK,
                Unpooled.wrappedBuffer(responseBody));
        return response;
    }

}
