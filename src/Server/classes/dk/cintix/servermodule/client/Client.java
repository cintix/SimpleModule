/*
 */
package dk.cintix.servermodule.client;

import dk.cintix.servermodule.net.ServerRequest;
import dk.cintix.servermodule.net.ServerResponse;
import java.io.IOException;

/**
 *
 * @author migo
 */
public class Client {

    public static void handleRead(ServerRequest request) throws IOException {
        System.out.println("DEBUG REQUEST: " + request);
    }

    public static void handleWrite(ServerResponse response) {

    }
}
