package ro.fr33styler.frhttpclient.gui.center;

import javax.swing.*;

public class CenterPanel extends JPanel {

    private final JToggleButton response;
    private final JToggleButton headers;
    private final JToggleButton params;
    private final JToggleButton body;

    public CenterPanel() {
        response = new JToggleButton("Response");
        response.setSelected(true);
        add(response);

        headers = new JToggleButton("Headers");
        add(headers);

        params = new JToggleButton("Params");
        add(params);

        body = new JToggleButton("Body");
        add(body);
    }

    public JToggleButton getResponse() {
        return response;
    }

    public JToggleButton getHeaders() {
        return headers;
    }

    public JToggleButton getParams() {
        return params;
    }

    public JToggleButton getBody() {
        return body;
    }

}
