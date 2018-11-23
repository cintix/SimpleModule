/*
 */
package dk.cintix.servermodule.net;

import java.nio.channels.SocketChannel;

/**
 *
 * @author migo
 */
public class ServerResponse {

    private final SocketChannel channel;

    public ServerResponse(SocketChannel channel) {
        this.channel = channel;
    }
}
