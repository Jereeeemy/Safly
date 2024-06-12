package applicationihm;

import org.jxmapviewer.viewer.GeoPosition;

public class Route {
    private final GeoPosition start;
    private final GeoPosition end;

    public Route(GeoPosition start, GeoPosition end) {
        this.start = start;
        this.end = end;
    }

    public GeoPosition getStart() {
        return start;
    }

    public GeoPosition getEnd() {
        return end;
    }
}