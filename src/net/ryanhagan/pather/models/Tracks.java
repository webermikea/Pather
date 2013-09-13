package net.ryanhagan.pather.models;

import net.ryanhagan.pather.math.Vector2d;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface Tracks {

	public void addPointAt(Point location);
	public void movePointTo(Point thePointToMove, Point theNewLocation);
    public ArrayList<Point> getPoints();
    public ArrayList<Point2D.Float> getTrackSegments();
    public Point getScreenPointAt(float distanceFromStart);
    public Vector2d getForwardDirectionAt(float distanceFromStart);
    public float getLength();

}