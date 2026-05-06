package ro.fr33styler.frhttpclient.snapshot;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

public class SnapshotsTypeAdapter extends TypeAdapter<Snapshots> {

    @Override
    public void write(JsonWriter out, Snapshots value) throws IOException {
        out.beginObject();
        for (Map.Entry<String, Snapshot> entry : value.getSnapshots().entrySet()) {
            out.name(entry.getKey());
            writeSession(out, entry.getValue());
        }
        out.endObject();
    }

    private void writeSession(JsonWriter out, Snapshot value) throws IOException {
        out.beginObject();

        out.name("method");
        out.value(value.getMethod());

        out.name("url");
        out.value(value.getUrl());

        out.name("headers");
        out.beginObject();
        for (Map.Entry<String, String> entry : value.getHeaders().entrySet()) {
            out.name(entry.getKey());
            out.value(entry.getValue());
        }
        out.endObject();

        out.name("params");
        out.beginObject();
        for (Map.Entry<String, String> entry : value.getParameters().entrySet()) {
            out.name(entry.getKey());
            out.value(entry.getValue());
        }
        out.endObject();

        out.name("body");
        out.value(value.getBody());

        out.endObject();
    }

    @Override
    public Snapshots read(JsonReader in) throws IOException {
        Snapshots snapshots = new Snapshots();

        in.beginObject();
        while (in.hasNext()) {
            snapshots.getSnapshots().put(in.nextName(), readSnapshot(in));
        }
        in.endObject();

        return snapshots;
    }

    private Snapshot readSnapshot(JsonReader in) throws IOException {
        Snapshot snapshot = new Snapshot();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "method" -> snapshot.setMethod(in.nextString());
                case "url" -> snapshot.setUrl(in.nextString());
                case "headers" -> {
                    in.beginObject();
                    while (in.hasNext()) {
                        snapshot.getHeaders().put(in.nextName(), in.nextString());
                    }
                    in.endObject();
                }
                case "params" -> {
                    in.beginObject();
                    while (in.hasNext()) {
                        snapshot.getParameters().put(in.nextName(), in.nextString());
                    }
                    in.endObject();
                }
                case "body" -> snapshot.setBody(in.nextString());
            }
        }
        in.endObject();

        return snapshot;
    }


}
