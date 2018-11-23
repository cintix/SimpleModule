/*
 */
package dk.cintix.servermodule.server;

import dk.cintix.servermodule.client.Client;
import dk.cintix.servermodule.net.ServerRequest;
import dk.cintix.servermodule.net.ServerResponse;
import java.io.IOException;
import java.lang.System.Logger;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author migo
 */
public class Server {

    private static final Logger LOG = System.getLogger(Server.class.getName());

    private volatile boolean running = false;
    private int port = 8081;
    private String host = "0.0.0.0";

    private SelectionKey selectionKey;
    private ServerSocketChannel serverSocket;
    private Selector selector;
    private InetSocketAddress hostAddr;

    public Server() throws IOException {
    }

    private Server(int port) throws IOException {
        this.port = port;
    }

    private void initialize() throws IOException {

        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        hostAddr = new InetSocketAddress(host, port);

        serverSocket.bind(hostAddr);
        serverSocket.configureBlocking(false);

        int socketOps = serverSocket.validOps();
        selectionKey = serverSocket.register(selector, socketOps, null);

    }

    public void start() throws IOException {
        running = true;
        initialize();
        LOG.log(Logger.Level.INFO, "Server starting on " + getHost() + " port " + port);

        while (running) {
            LOG.log(Logger.Level.INFO, "Waiting for imcoming connection");
            selector.select();

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();

            while (selectionKeyIterator.hasNext()) {
                SelectionKey clientSelectionKey = selectionKeyIterator.next();

                if (clientSelectionKey.isAcceptable()) {
                    SocketChannel clientChannel = serverSocket.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                }

                if (clientSelectionKey.isValid()) {
                    if (clientSelectionKey.isReadable()) {
                        Client.handleRead(new ServerRequest((SocketChannel) clientSelectionKey.channel()));
                    }
                    if (clientSelectionKey.isWritable()) {
                        Client.handleWrite(new ServerResponse((SocketChannel) clientSelectionKey.channel()));
                    }
                } else {
                    ((SocketChannel) clientSelectionKey.channel()).close();
                }

                selectionKeyIterator.remove();
            }

        }

    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public static void main(String[] args) {
        try {
            new Server().start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
