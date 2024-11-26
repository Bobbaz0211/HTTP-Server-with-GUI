package httpserver;

import httpserver.gui.ServerGUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;

public class HttpServerDriver {
    private final static Logger LOGGER= LoggerFactory.getLogger(HttpServerDriver.class);
    ServerGUI gui = new ServerGUI();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServerGUI::new);
    }

}
