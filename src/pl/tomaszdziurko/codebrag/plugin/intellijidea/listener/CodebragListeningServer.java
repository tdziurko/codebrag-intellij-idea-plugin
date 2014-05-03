package pl.tomaszdziurko.codebrag.plugin.intellijidea.listener;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import pl.tomaszdziurko.codebrag.plugin.intellijidea.handler.OpenFileInProjectHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class CodebragListeningServer {

    private List<OpenFileInProjectHandler> handlers = new ArrayList<OpenFileInProjectHandler>();

    public CodebragListeningServer() {
        System.out.println("CodebragListeningServer constructor");
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8880), 0);
            server.createContext("/codebrag-plugin", new InternalHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
        }
    }

    public void registerHandler(OpenFileInProjectHandler handler) {
        handlers.add(handler);
    }

    private class InternalHandler implements HttpHandler {

        public void handle(HttpExchange t) throws IOException {
            String fileName = t.getRequestURI().getQuery();

            if (fileName == null || fileName.isEmpty()) {
                writeResponse(t, 400, "Codebrag Plugin: Missing request parameter defining file name");
            } else {
                for (OpenFileInProjectHandler handler : handlers) {
                    handler.handle(fileName);
                }
                writeResponse(t, 200, "Codebrag Plugin: Request processed successfully");
            }
        }

        private void writeResponse(HttpExchange httpExchange, int httpStatus, String message) throws IOException {
            httpExchange.sendResponseHeaders(httpStatus, message.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(message.getBytes());
            os.close();
        }
    }

}
