package ro.fr33styler.frhttpclient.snapshot;

import java.util.LinkedHashMap;
import java.util.Map;

public class Snapshots {

    private final Map<String, Snapshot> snapshots = new LinkedHashMap<>();

    public Map<String, Snapshot> getSnapshots() {
        return snapshots;
    }

}
