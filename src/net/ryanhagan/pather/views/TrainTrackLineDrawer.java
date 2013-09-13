package net.ryanhagan.pather.views;

import net.ryanhagan.pather.models.Tracks;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Path2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class TrainTrackLineDrawer implements TrainTrackDrawer
{
	private Graphics2D graphicsContext;
	private Point highlightedPoint;


    public TrainTrackLineDrawer()
    {
    	highlightedPoint = null;
    }

    public void setGraphicsContext(Graphics2D graphics)
    {
    	graphicsContext = graphics;
    }

	public void draw(Tracks track)
	{
        if ( track.getLength() > 0)
        {
		    drawControlPoints(track.getPoints());
		    drawTracks(track.getTrackSegments());
        }
	}

	public void highlightTrackPoint(Point trackPoint)
	{
		highlightedPoint = trackPoint;
	}

	public void unHighlightTrackPoints()
	{
		highlightedPoint = null;
	}

	private void drawControlPoints(ArrayList<Point> points)
	{
		graphicsContext.setColor(Color.black);
		for ( Point currentPoint : points )
			if ( highlightedPoint != null && currentPoint.equals(highlightedPoint) )
				drawControlPoint(currentPoint, Color.red);
			else
				drawControlPoint(currentPoint, Color.black);
	}

	private void drawControlPoint(Point point, Color color)
	{
        int WIDTH = 4;
        int HEIGHT = 4;
		Color originalColor = graphicsContext.getColor();
		graphicsContext.setColor(color);
		graphicsContext.fillRect(point.x - WIDTH / 2, point.y - HEIGHT / 2, WIDTH, HEIGHT);
		graphicsContext.setColor(originalColor);
	}

	public void drawTracks(ArrayList<Point2D.Float> trackSegments)
	{
		graphicsContext.setColor(Color.black);
		graphicsContext.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        Path2D track = new Path2D.Float();
        track.moveTo(trackSegments.get(0).x,trackSegments.get(0).y);
        for (Point2D.Float nextTrackLocation : trackSegments)
            track.lineTo(nextTrackLocation.x, nextTrackLocation.y);
		graphicsContext.draw(track);
	}
}
