package httpserver.core;

import httpserver.gui.GuiLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListnerThread extends Thread {
    private final static Logger LOGGER= LoggerFactory.getLogger(ServerListnerThread.class);

    private int port;
    private String webroot;
    private ServerSocket serverSocket;
    private GuiLogger guiLog;

    public ServerListnerThread(int port, String wRoot, GuiLogger guiLog) throws IOException {
        this.port = port;
        this.webroot = wRoot;
        this.serverSocket = new ServerSocket(this.port);
        this.guiLog = guiLog;
    }

    @Override
    public void run(){

        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                LOGGER.info(" * Connection accepted: " + socket.getInetAddress());
                guiLog.appendLog(" * Connection accepted: " + socket.getInetAddress());

                HttpConnectionWorker worker = new HttpConnectionWorker(socket);
                worker.start();
            }

        } catch (IOException e) {
            LOGGER.error("Problem with initialising socket", e);
            guiLog.appendLog("Problem with initialising socket", e);
        }finally {
            if (serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                }
            }
        }

    }
}
