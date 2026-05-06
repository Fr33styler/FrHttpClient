package ro.fr33styler.frhttpclient.gui.end;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class KeyValuePanel extends JPanel {

    private static final int PADDED_MINIMUM = 12;

    private int padded = 12;
    private final JFrame frame;
    private final JPanel scrollablePanel;

    public KeyValuePanel(JFrame frame) {
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("KEY"));
        topPanel.add(new JLabel("         VALUE"));
        topPanel.add(new JLabel(""));
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 90, 0));
        add(topPanel);

        scrollablePanel = new JPanel();
        scrollablePanel.setLayout(new BoxLayout(scrollablePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollablePane = new JScrollPane(scrollablePanel);
        scrollablePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollablePane.getViewport().setPreferredSize(new Dimension(0, 220));
        add(scrollablePane);

        for (int i = 0; i < padded; i++) {
            scrollablePanel.add(new JPanel());
        }

        JPanel addPanel = new JPanel();

        JButton addButton = new JButton("Add");
        addButton.addActionListener(action -> {
            addEntry();
            this.frame.revalidate();
            this.frame.repaint();
        });
        addPanel.add(addButton);

        add(addPanel);
    }

    private int getKeyValueComponentCount() {
        return scrollablePanel.getComponentCount() - padded;
    }

    public Map<String, String> getEntries() {
        Map<String, String> entries = new LinkedHashMap<>();
        forEachKeyValueComponent((key, value) -> entries.put(key.getText(), value.getText()));
        return entries;
    }

    public void setEntries(Map<String, String> entries) {
        while (getKeyValueComponentCount() > entries.size()) {
            removeEntry();
        }
        while (getKeyValueComponentCount() < entries.size()) {
            addEntry();
        }
        frame.revalidate();
        frame.repaint();

        Iterator<Map.Entry<String, String>> iterator = entries.entrySet().iterator();
        forEachKeyValueComponent((key, value) -> {
            if (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                key.setText(entry.getKey());
                value.setText(entry.getValue());
            }
        });
    }

    public void forEachKeyValueComponent(BiConsumer<JTextField, JTextField> consumer) {
        for (int i = 0; i < getKeyValueComponentCount(); i++) {
            Component component = scrollablePanel.getComponent(i);
            if (!(component instanceof JPanel)) continue;

            JPanel panel = (JPanel) component;
            if (panel.getComponentCount() < 2) continue;

            Component keyComponent = panel.getComponent(0);
            Component valueComponent = panel.getComponent(1);
            if (!(keyComponent instanceof JTextField) || !(valueComponent instanceof JTextField)) continue;

            consumer.accept((JTextField) keyComponent, (JTextField) valueComponent);
        }
    }

    private void addEntry() {
        JPanel panel = new JPanel();
        panel.add(new JTextField("", 10));
        panel.add(new JTextField("", 10));

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(action -> {
            scrollablePanel.remove(panel);
            if (scrollablePanel.getComponentCount() < PADDED_MINIMUM && padded < PADDED_MINIMUM) {
                scrollablePanel.add(new JPanel());
                padded++;
            }
            frame.revalidate();
            frame.repaint();
        });
        panel.add(removeButton);

        int componentCount = scrollablePanel.getComponentCount();
        scrollablePanel.add(panel, componentCount - padded);

        if (padded > 0) {
            scrollablePanel.remove(componentCount - 1);
            padded--;
        }
    }

    private void removeEntry() {
        scrollablePanel.remove(getKeyValueComponentCount() - 1);
        if (scrollablePanel.getComponentCount() < PADDED_MINIMUM && padded < PADDED_MINIMUM) {
            scrollablePanel.add(new JPanel());
            padded++;
        }
    }

}
