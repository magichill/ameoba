package org.amoeba.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.amoeba.netty.server.http.HttpRequestHandler;

/**
 *
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        p.addLast("logger", new LoggingHandler(LogLevel.INFO));
        p.addLast("codec", new HttpServerCodec());
        p.addLast(new LineBasedFrameDecoder(6148));
        p.addLast("handler", new HttpRequestHandler());
    }
}
