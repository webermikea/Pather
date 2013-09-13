package net.ryanhagan.pather.views;

import net.ryanhagan.pather.math.Vector2d;
import net.ryanhagan.pather.models.Train;

import java.awt.*;

public interface TrainDrawer {

	public void setGraphicsContext(Graphics2D graphics);
    public void draw(Train train, Point position, Vector2d direction);

}