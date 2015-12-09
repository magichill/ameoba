package org.amoeba.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.amoeba.netty.server.http.AbstractExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class HttpServer {
    private final static Logger logger = LoggerFactory.getLogger("amoeba");

    private final int                port;

    public static ApplicationContext ctx       = null;

    public static List<String> executorNameList = new LinkedList<String>();

    public HttpServer(){
        port = 8081;
    }

    public HttpServer(final int port){
        this.port = port;
    }

    public HttpServer(final int port,ApplicationContext ctx){
        this.port = port;
        HttpServer.ctx = ctx;
        String[] executorNames = ctx.getBeanNamesForType(AbstractExecutor.class);
        for (String beanName : executorNames) {
            executorNameList.add(beanName.toLowerCase());
            logger.debug("Spring loaded bean name " +  beanName.toLowerCase());
        }
    }

    public void run(final int eventLoopThreads) throws Exception {
        // Configure the server.
        //NioEventLoopGroup是用来处理I/O操作的多线程事件循环器，netty提供了许多不同的EventLoopGroup的实现用来处理不通过传输协议。
        //服务端应用有2个NioEventLoopGroup会被使用。第一个叫做“boss”，用来接收进来的连接。第二个叫做“worker”，用来处理已经被接收的连接
        //一旦boss接收到连接，就会把连接信息注册到woker上，用来处理已经被接收的连接。
        EventLoopGroup bossGroup = new NioEventLoopGroup();// bossGroup线程池用来接受客户端的连接请求
        EventLoopGroup workerGroup = new NioEventLoopGroup(eventLoopThreads);// //workerGroup线程池用来处理boss线程池里面的连接的数据
        try {
            //ServerBootstrap是一个启动NIO服务的辅助启动类。你可以在这个服务中直接使用Channel，但是这会是一个复杂的处理过程，在很多情况下并不需要。
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);// 最大排队数量
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new HttpServerInitializer());

            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
