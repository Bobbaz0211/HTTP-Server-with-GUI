package httpserver.gui;

public interface GuiLogger {
    void appendLog(String message);
    void appendLog(String message, Exception e);
}
