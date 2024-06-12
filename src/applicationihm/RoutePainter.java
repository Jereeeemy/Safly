package applicationihm;

import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

public class RoutePainter implements Painter<JXMapViewer> {
    private final List<Route> routes;

    public RoutePainter(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int width, int height) {
        g = (Graphics2D) g.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the color and stroke for the routes
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2));

        for (Route route : routes) {
            GeoPosition start = route.getStart();
            GeoPosition end = route.getEnd();

            Point2D startPt = map.getTileFactory().geoToPixel(start, map.getZoom());
            Point2D endPt = map.getTileFactory().geoToPixel(end, map.getZoom());

            Path2D path = new Path2D.Double();
            path.moveTo(startPt.getX(), startPt.getY());
            path.lineTo(endPt.getX(), endPt.getY());

            g.draw(path);
        }

        g.dispose();
    }
}