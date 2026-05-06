package ro.fr33styler.frhttpclient.gui.top;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import ro.fr33styler.frhttpclient.gui.Gui;
import ro.fr33styler.frhttpclient.snapshot.GsonUtil;
import ro.fr33styler.frhttpclient.snapshot.Snapshot;
import ro.fr33styler.frhttpclient.snapshot.Snapshots;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;

public class TopPanel extends JPanel {

    private final JTextField urlInput;

    public TopPanel(Gui gui, Snapshots snapshots, Properties properties) {
        JComboBox<String> httpMethods = new JComboBox<>();
        httpMethods.addItem("GET");
        httpMethods.addItem("POST");
        httpMethods.addItem("PUT");
        httpMethods.addItem("DELETE");
        httpMethods.addItem("PATCH");

        add(httpMethods);

        add(new JLabel("URL:"));

        urlInput = new JTextField("", 20);
        add(urlInput);

        JTextField saveNameInput = new JTextField("", 8);
        add(saveNameInput);

        JButton saveRequest = new JButton("Save");
        add(saveRequest);

        JButton deleteRequest = new JButton("Delete");
        add(deleteRequest);

        JComboBox<String> loadRequest = new JComboBox<>();
        for (String key : snapshots.getSnapshots().keySet()) {
            loadRequest.addItem(key);
        }
        loadRequest.setSelectedItem(null);
        add(loadRequest);

        JButton sendRequest = new JButton("Send");

        add(sendRequest);

        saveRequest.addActionListener(action -> {
            Snapshot snapshot = new Snapshot();

            snapshot.setMethod((String) httpMethods.getSelectedItem());
            snapshot.setUrl(urlInput.getText());
            snapshot.getHeaders().putAll(gui.getEndPanel().getHeadersKeyValuePanel().getEntries());
            snapshot.getParameters().putAll(gui.getEndPanel().getParamsKeyValuePanel().getEntries());
            snapshot.setBody(gui.getEndPanel().getBodyLogArea().getText());
            snapshots.getSnapshots().put(saveNameInput.getText(), snapshot);
            GsonUtil.save(snapshots);

            loadRequest.removeItem(saveNameInput.getText());
            loadRequest.addItem(saveNameInput.getText());
        });

        deleteRequest.addActionListener(action -> {
            Object selected = loadRequest.getSelectedItem();
            if (selected == null) return;

            snapshots.getSnapshots().remove(selected.toString());
            loadRequest.removeItem(selected);

            GsonUtil.save(snapshots);
        });

        loadRequest.addActionListener(action -> {
            Snapshot snapshot = snapshots.getSnapshots().get((String) loadRequest.getSelectedItem());
            if (snapshot == null) return;

            httpMethods.setSelectedItem(snapshot.getMethod());
            urlInput.setText(snapshot.getUrl());
            gui.getEndPanel().getHeadersKeyValuePanel().setEntries(snapshot.getHeaders());
            gui.getEndPanel().getParamsKeyValuePanel().setEntries(snapshot.getParameters());
            gui.getEndPanel().getBodyLogArea().setText(snapshot.getBody());
        });

        sendRequest.addActionListener(action -> {
            sendRequest.setEnabled(false);
            String endpoint = urlInput.getText();

            StringJoiner joiner = new StringJoiner("&");
            gui.getEndPanel().getParamsKeyValuePanel().getEntries().forEach((key, value) -> {
                if (key.isBlank() || value.isBlank()) return;

                joiner.add(key + "=" + value);
            });

            if (joiner.length() > 0) {
                if (!endpoint.contains("?")) {
                    endpoint = endpoint.concat("?");
                }
                endpoint = endpoint.concat(joiner.toString());
            }

            handleHttpConnection(gui, (String) httpMethods.getSelectedItem(), endpoint, properties);

            sendRequest.setEnabled(true);
        });

    }

    private String tryPrettyPrint(String text) {
        if (text.contains("\n")) return text;
        try {
            InputSource src = new InputSource(new StringReader(text));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            Writer out = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(out));
            return out.toString();
        } catch (Exception exception) {
            return text;
        }
    }

    private void handleHttpConnection(Gui gui, String httpMethod, String endpoint, Properties properties) {
        try {
            HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

            String body = gui.getEndPanel().getBodyLogArea().getText();
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(endpoint));
            if (body.isBlank()) {
                requestBuilder.method(httpMethod, HttpRequest.BodyPublishers.noBody());
            } else {
                requestBuilder.method(httpMethod, HttpRequest.BodyPublishers.ofString(body));
            }

            requestBuilder.header("User-Agent", "FrHttpClient/" + properties.getProperty("version", "1.0"));
            for (Map.Entry<String, String> entry : gui.getEndPanel().getHeadersKeyValuePanel().getEntries().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.isBlank() || value.isBlank()) continue;

                requestBuilder.header(entry.getKey(), entry.getValue());
            }
            HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);
            HttpResponse<String> response = httpClient.send(requestBuilder.build(), bodyHandler);

            gui.getEndPanel().getResponseStatusCodeField().setText(String.valueOf(response.statusCode()));
            try {
                JsonElement jsonElement = JsonParser.parseString(response.body());
                gui.getEndPanel().getResponseLogArea().setText(GsonUtil.GSON.toJson(jsonElement));
            } catch (JsonParseException exception) {
                gui.getEndPanel().getResponseLogArea().setText(tryPrettyPrint(response.body()));
            }
        } catch (Exception exception) {
            gui.getEndPanel().getResponseLogArea().setText(exception.getMessage());
        }
    }

}
