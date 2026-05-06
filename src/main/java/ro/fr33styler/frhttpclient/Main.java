package ro.fr33styler.frhttpclient;

import ro.fr33styler.frhttpclient.gui.Gui;
import ro.fr33styler.frhttpclient.snapshot.GsonUtil;
import ro.fr33styler.frhttpclient.snapshot.Snapshots;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        System.setProperty("sun.java2d.noddraw", "true");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JFrame frame = new JFrame("FrHttpClient");

        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Properties properties = new Properties();
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException ignored) {}

        Snapshots snapshots = GsonUtil.load();
        Gui gui = new Gui(frame, snapshots, properties);

        frame.add(gui.getTopPanel(), BorderLayout.PAGE_START);
        frame.add(gui.getCenterPanel(), BorderLayout.CENTER);
        frame.add(gui.getEndPanel(), BorderLayout.PAGE_END);

        frame.pack();
        frame.setVisible(true);
    }

}
