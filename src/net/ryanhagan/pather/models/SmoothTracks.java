package net.ryanhagan.pather.models;

import net.ryanhagan.pather.math.NaturalCubicSpline;
import net.ryanhagan.pather.math.Vector2d;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class SmoothTracks extends NaturalCubicSpline implements Tracks
{

    public SmoothTracks() {
    }

	public void addPointAt(Point location)
	{
		this.addControlPointAt(location);
	}

	public void movePointTo(Point thePointToMove, Point theNewLocation)
	{
		this.moveControlPointTo(thePointToMove, theNewLocation);
	}

    public ArrayList<Point> getPoints()
    {
    	return this.getControlPoints();
    }

    public ArrayList<Point2D.Float> getTrackSegments()
    {
    	return this.getSplineSegments();
    }

    public Point getScreenPointAt(float distanceFromStart)
    {
    	return this.getPointAt(distanceFromStart);
    }

    public Vector2d getForwardDirectionAt(float distanceFromStart)
    {
    	return this.getTangentVectorAt(distanceFromStart);
    }

    public float getLength()
    {
    	return this.getMaxPosition();
    }

}