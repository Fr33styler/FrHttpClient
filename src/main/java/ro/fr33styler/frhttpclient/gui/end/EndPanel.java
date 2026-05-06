package ro.fr33styler.frhttpclient.gui.end;

import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel {

    private final JTextField statusCodeField;
    private final JTextArea responseLogArea;
    private final KeyValuePanel headersKeyValuePanel;
    private final KeyValuePanel paramsKeyValuePanel;
    private final JTextArea bodyLogArea;

    public EndPanel(JFrame frame, CardLayout cardLayout) {
        super(cardLayout);
        JPanel responsePanel = new JPanel();
        responsePanel.setLayout(new BoxLayout(responsePanel, BoxLayout.Y_AXIS));

        JPanel statusPanel = new JPanel();
        statusPanel.add(new JLabel("Status code: "));

        statusCodeField = new JTextField("", 10);
        statusCodeField.setEditable(false);
        statusPanel.add(statusCodeField);
        responsePanel.add(statusPanel);

        JScrollPane responseScrollablePane = new JScrollPane();
        responseScrollablePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        responseScrollablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        responseLogArea = new JTextArea(20, 80);
        responseLogArea.setEditable(false);
        responseLogArea.setLineWrap(true);
        responseLogArea.setWrapStyleWord(true);
        responseScrollablePane.getViewport().setView(responseLogArea);
        responsePanel.add(responseScrollablePane);

        add(responsePanel, "response");

        headersKeyValuePanel = new KeyValuePanel(frame);
        add(headersKeyValuePanel, "headers");

        paramsKeyValuePanel = new KeyValuePanel(frame);
        add(paramsKeyValuePanel, "params");

        JScrollPane bodyScrollablePane = new JScrollPane();
        bodyScrollablePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        bodyScrollablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        bodyLogArea = new JTextArea(20, 80);
        bodyScrollablePane.getViewport().setView(bodyLogArea);
        add(bodyScrollablePane, "body");
    }

    public JTextField getResponseStatusCodeField() {
        return statusCodeField;
    }

    public JTextArea getResponseLogArea() {
        return responseLogArea;
    }

    public KeyValuePanel getHeadersKeyValuePanel() {
        return headersKeyValuePanel;
    }

    public KeyValuePanel getParamsKeyValuePanel() {
        return paramsKeyValuePanel;
    }

    public JTextArea getBodyLogArea() {
        return bodyLogArea;
    }

}