package org.amoeba.netty.server.http;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import org.amoeba.netty.server.HttpServer;
import org.amoeba.netty.server.HttpServerHandler;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 *
 */
public class HttpRequestHandler extends HttpServerHandler {

    public static final String CONTENT_TYPE_HTML = "text/html; charset=UTF-8";
    public static final String CONTENT_TYPE_JSON = "text/json; charset=UTF-8";
    public static final String CONTENT_TYPE_STREAM = "application/octet-stream";
    public static final String CONTENT_TYPE_XML = "text/xml; charset=UTF-8";
    public static final String CONTENT_TYPE_TEXT = "text/plain; charset=UTF-8";

    @Override
    public FullHttpResponse getHttpResponse() {
        FullHttpResponse response = null;
        HttpRequest req = AbstractExecutor.getCurrentRequest().getHttpRequest();
        try {
            String service = sanitizeUri(req.getUri());
            Executor commond = (Executor) HttpServer.ctx.getBean(service);
            Object result = commond.execute(AbstractExecutor.getCurrentRequest());
            if (result == null) {
                return new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            }

            if (result instanceof String) {
                String content = (String) result;
                response = new DefaultFullHttpResponse(
                        HTTP_1_1,
                        OK,
                        Unpooled.wrappedBuffer(content.getBytes()));
                response.headers().set(CONTENT_TYPE, CONTENT_TYPE_JSON);
            } else if (result instanceof byte[]) {
                byte[] content = (byte[]) result;
                response = new DefaultFullHttpResponse(
                        HTTP_1_1,
                        OK,
                        Unpooled.wrappedBuffer(content));
                response.headers().set(CONTENT_TYPE, CONTENT_TYPE_STREAM);
            } else if (result instanceof io.netty.handler.codec.http.FullHttpResponse) {
                return (FullHttpResponse) result;

            } else {
                return new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            }

        } catch (NoSuchBeanDefinitionException e) {
            response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
        } catch (Throwable t) {
            response = new DefaultFullHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR);
            t.printStackTrace();
        }
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

    public static String sanitizeUri(String uri) throws URISyntaxException {
        // Decode the path.
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }

        if (!uri.startsWith("/")) {
            return null;
        }

        URI uriObject = new URI(uri);
        uri = uriObject.getPath();

        while (uri != null && uri.startsWith("/")) {
            uri = uri.substring(1);
        }

        // if(!isAcronym(uri)){
        // return uri;
        // }
        // Convert first Letter to lowcase
        // uri = StringUtils.uncapitalize(uri);

        return uri.toLowerCase();
    }

    public static boolean isAcronym(String word) {
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }

}

