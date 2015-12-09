package org.amoeba.netty.server.http;

/**
 *
 */
public interface Executor {

    public Object execute(AmoebaHttpRequest request);
}
