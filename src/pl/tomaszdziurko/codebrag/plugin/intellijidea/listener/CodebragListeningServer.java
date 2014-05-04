package pl.tomaszdziurko.codebrag.plugin.intellijidea.listener;

import com.intellij.openapi.diagnostic.Logger;
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

    private final Logger log = Logger.getInstance(CodebragListeningServer.class);

    public static final int LISTENER_PORT = 8880;
    public static final String LISTENER_CONTEXT_URL = "/codebrag-plugin";

    private List<OpenFileInProjectHandler> handlers = new ArrayList<OpenFileInProjectHandler>();

    public CodebragListeningServer() {
        log.info("Starting listening server on http://localhost:" + LISTENER_PORT + LISTENER_CONTEXT_URL);
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(LISTENER_PORT), 0);
            server.createContext(LISTENER_CONTEXT_URL, new InternalHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (Exception e) {
            log.error("Exception " + e.getMessage() + " cause: " + e.getCause());
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
