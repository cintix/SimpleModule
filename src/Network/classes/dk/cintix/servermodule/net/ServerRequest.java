/*
 */
package dk.cintix.servermodule.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author migo
 */
public class ServerRequest {

    private final SocketChannel channel;
    private final Map<String, String> headers = new LinkedHashMap<>();
    private String data;
    private String path;
    private String method;
    private String postData;

    public ServerRequest(SocketChannel channel) throws IOException {
        this.channel = channel;
        processRequest();
    }

    private void processRequest() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
        channel.read(byteBuffer);
        data = new String(byteBuffer.array()).trim();
        if (data.isEmpty()) {
            return;
        }

        String[] lines = data.split("\n");
        if (lines != null) {
            String[] arguments = lines[0].split(" ");
            if (arguments != null && arguments.length > 1) {
                method = arguments[0].trim();
                path = arguments[1].trim();
            }

            if (lines.length > 1) {
                for (int index = 1; index < lines.length; index++) {

                    if (lines[index].isEmpty()) {
                        if (index < lines.length - 1) {
                            postData = lines[index++].trim();
                            break;
                        }
                    }

                    arguments = lines[index].split(":");
                    if (arguments != null && arguments.length > 1) {
                        headers.put(arguments[0], arguments[1]);
                    }

                }
            }
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    @Override
    public String toString() {
        return "ServerRequest{" + "channel=" + channel + ", headers=" + headers + ", data=" + data + ", path=" + path + ", method=" + method + ", postData=" + postData + '}';
    }

}
