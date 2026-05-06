package ro.fr33styler.frhttpclient.gui;

import ro.fr33styler.frhttpclient.gui.center.CenterPanel;
import ro.fr33styler.frhttpclient.gui.end.EndPanel;
import ro.fr33styler.frhttpclient.gui.top.TopPanel;
import ro.fr33styler.frhttpclient.snapshot.Snapshots;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class Gui {

    private final TopPanel topPanel;
    private final CenterPanel centerPanel;
    private final EndPanel endPanel;
    private final CardLayout cardLayout;

    public Gui(JFrame frame, Snapshots snapshots, Properties properties) {
        topPanel = new TopPanel(this, snapshots, properties);
        centerPanel = new CenterPanel();
        cardLayout = new CardLayout();
        endPanel = new EndPanel(frame, cardLayout);

        handleCardSwitch();
    }

    public TopPanel getTopPanel() {
        return topPanel;
    }

    public CenterPanel getCenterPanel() {
        return centerPanel;
    }

    public EndPanel getEndPanel() {
        return endPanel;
    }

    private void handleCardSwitch() {
        centerPanel.getResponse().addActionListener(action -> {
            centerPanel.getResponse().setSelected(true);
            centerPanel.getHeaders().setSelected(false);
            centerPanel.getParams().setSelected(false);
            centerPanel.getBody().setSelected(false);
            cardLayout.show(endPanel, "response");
        });

        centerPanel.getHeaders().addActionListener(action -> {
            centerPanel.getResponse().setSelected(false);
            centerPanel.getHeaders().setSelected(true);
            centerPanel.getParams().setSelected(false);
            centerPanel.getBody().setSelected(false);
            cardLayout.show(endPanel, "headers");
        });

        centerPanel.getParams().addActionListener(action -> {
            centerPanel.getResponse().setSelected(false);
            centerPanel.getHeaders().setSelected(false);
            centerPanel.getParams().setSelected(true);
            centerPanel.getBody().setSelected(false);
            cardLayout.show(endPanel, "params");
        });

        centerPanel.getBody().addActionListener(action -> {
            centerPanel.getResponse().setSelected(false);
            centerPanel.getHeaders().setSelected(false);
            centerPanel.getParams().setSelected(false);
            centerPanel.getBody().setSelected(true);
            cardLayout.show(endPanel, "body");
        });
    }

}
