/*
 */
package dk.cintix.servermodule.server;

import java.io.IOException;
import java.lang.System.Logger;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 *
 * @author migo
 */
public class Server {

    private static final Logger LOG =  System.getLogger(Server.class.getName());

    private volatile boolean running = false;
    private int port = 80;
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
        
        while(!running) {
            
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
