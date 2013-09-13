package net.ryanhagan.pather.views;

import net.ryanhagan.pather.math.Vector2d;
import net.ryanhagan.pather.models.Train;

import java.awt.*;


public class TrainLineDrawer implements TrainDrawer {

    final int LENGTH = 16;
	final int HEIGHT = 6;
	private Graphics2D graphicsContext;

    public TrainLineDrawer() {
    }

    public void setGraphicsContext(Graphics2D graphics)
    {
    	graphicsContext = graphics;
    }

	public void draw(Train train, Point position, Vector2d direction)
	{
        Rectangle trainBox = new Rectangle(position.x - LENGTH/2, position.y - HEIGHT /2, LENGTH, HEIGHT);

        graphicsContext.setColor(Color.red);
        graphicsContext.rotate(Math.atan2(direction.y,direction.x), position.x, position.y);
        graphicsContext.fill(trainBox);
	}
}