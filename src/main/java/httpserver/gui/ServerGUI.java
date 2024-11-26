package httpserver.gui;

import httpserver.HttpServerDriver;
import httpserver.core.ServerListnerThread;
import httpserver.config.Config;
import httpserver.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ServerGUI implements GuiLogger{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerGUI.class);

    private JFrame frame;
    private CustomButton startButton, configureButton;
    private JTextArea logArea;
    ServerListnerThread serverThread;

    public ServerGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("HTTP Server GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        frame.add(logScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        frame.add(buttonPanel, BorderLayout.NORTH);

        startButton = new CustomButton("Start Server");
        startButton.addActionListener(e -> runServer());
        buttonPanel.add(startButton, BorderLayout.CENTER);

        configureButton = new CustomButton("Configure JSON");
        configureButton.setPreferredSize(new Dimension(100, 30));
        configureButton.addActionListener(e -> openConfigMenu());
        buttonPanel.add(configureButton, BorderLayout.EAST);

        frame.setVisible(true);
    }

    public void runServer(){
        try {
            ConfigManager.getInstance().loadConfigFile("src/main/resources/http.json");
            Config conf = ConfigManager.getInstance().getCurrentConfig();

            appendLog("Using Port: " + conf.getPort());
            appendLog("Using Webroot: " + conf.getWebroot());

            serverThread = new ServerListnerThread(conf.getPort(), conf.getWebroot(), this);
            serverThread.start();

            appendLog("Server started successfully!");
            startButton.setEnabled(false);
        } catch (IOException e) {
            appendLog("Error starting server: " + e.getMessage());
            LOGGER.error("Error starting server", e);
        } catch (Exception e) {
            appendLog("Unexpected error: " + e.getMessage());
            LOGGER.error("Unexpected error", e);
        }
    }

    private void openConfigMenu() {
        String filePath = JOptionPane.showInputDialog(
                frame,
                "Enter the path to the configuration JSON file:",
                "Configure Server",
                JOptionPane.PLAIN_MESSAGE
        );

        if (filePath != null && !filePath.trim().isEmpty()) {
            try {
                ConfigManager.getInstance().loadConfigFile(filePath.trim());
                Config conf = ConfigManager.getInstance().getCurrentConfig();
                appendLog("Configuration updated successfully!");
                appendLog("Using Port: " + conf.getPort());
                appendLog("Using Webroot: " + conf.getWebroot());
            } catch (Exception e) {
                appendLog("Error loading configuration file: " + e.getMessage());
                LOGGER.error("Error loading configuration file", e);
            }
        } else {
            appendLog("Configuration update canceled.");
        }
    }

    @Override
    public void appendLog(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    @Override
    public void appendLog(String message, Exception e) {
        logArea.append(message + "\n" + e + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

}
