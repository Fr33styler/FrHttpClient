package ro.fr33styler.frhttpclient.snapshot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import ro.fr33styler.frhttpclient.Main;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GsonUtil {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Snapshots.class, new SnapshotsTypeAdapter())
            .setPrettyPrinting()
            .create();

    private static File getFolder() {
        return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParentFile();
    }

    public static void save(Snapshots snapshots) {
        File file = new File(getFolder(), "FrHttpClientSettings.json");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            JsonWriter writer = GSON.newJsonWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            GSON.toJson(snapshots, snapshots.getClass(), writer);
            writer.flush();
        } catch (IOException ignored) {}
    }

    public static Snapshots load() {
        File file = new File(getFolder(), "FrHttpClientSettings.json");
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return GSON.fromJson(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8), Snapshots.class);
        } catch (IOException ignored) {}
        return new Snapshots();
    }

}
