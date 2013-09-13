package net.ryanhagan.pather;

import net.ryanhagan.pather.models.Tracks;
import net.ryanhagan.pather.views.TrainTrackDrawer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class MouseInputHandler implements MouseListener, MouseMotionListener
{
	private Tracks track;
	private TrainTrackDrawer trackDrawer;
	private boolean dragging;
	private Point trackPointBeingHoveredOver;
	private Point currentlySelectedPoint;

    public MouseInputHandler()
    {
    	dragging = false;
    	currentlySelectedPoint = null;
    	trackPointBeingHoveredOver = null;
    }

    public void setTracks(Tracks currentTrack, TrainTrackDrawer currentTrackDrawer)
    {
    	track = currentTrack;
    	trackDrawer = currentTrackDrawer;
    	dragging = false;
    }

    public void mousePressed(MouseEvent e)
    {
    	Point mouseLocation = new Point(e.getX(), e.getY());

		if ( trackPointBeingHoveredOver == null )
			track.addPointAt(mouseLocation);

		else
			beginMovingTrackPoint(trackPointBeingHoveredOver);
    }

	private void beginMovingTrackPoint(Point mouseLocation)
	{
		dragging = true;
	}

    public void mouseReleased(MouseEvent e)
    {
    	trackPointBeingHoveredOver = null;
    	dragging = false;
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mouseClicked(MouseEvent e)
    {
    }

	public void mouseDragged(MouseEvent e)
	{
    	Point mouseLocation = new Point(e.getX(), e.getY());

    	if ( dragging )
    		track.movePointTo(trackPointBeingHoveredOver, mouseLocation);
    }

    public void mouseMoved(MouseEvent e)
    {
    	Point mouseLocation = new Point(e.getX(), e.getY());

    	ArrayList<Point> trackPoints = track.getPoints();
		trackDrawer.unHighlightTrackPoints();
		trackPointBeingHoveredOver = null;
    	for (Point trackPointLocation : trackPoints)
    	{
    		if ( mouseLocation.distance(trackPointLocation) <= 10 )
    		{
				trackPointBeingHoveredOver = trackPointLocation;
				trackDrawer.highlightTrackPoint(trackPointBeingHoveredOver);
    		}
    	}
    }

}