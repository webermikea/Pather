package net.ryanhagan.pather.views;

import net.ryanhagan.pather.models.Tracks;

import java.awt.Point;
import java.awt.Graphics2D;

public interface TrainTrackDrawer {

	public void setGraphicsContext(Graphics2D graphics);
    public void draw(Tracks track);
    public void highlightTrackPoint(Point trackPoint);
    public void unHighlightTrackPoints();

}