package net.ryanhagan.pather;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.Toolkit;

public class DrawPanel extends JPanel
{

	private BufferedImage buffer;

    public DrawPanel()
    {
    	setIgnoreRepaint(true);
    	setFocusable(true);
    }

	public void initialize(int panelWidth, int panelHeight)
	{
		buffer = new BufferedImage(panelWidth,panelHeight,BufferedImage.TYPE_INT_RGB);
	}

	public Graphics2D getBackBuffer()
	{
		Graphics2D backBuffer = buffer.createGraphics();
		return backBuffer;
	}

	public void drawScreen()
	{
		Graphics2D screenBuffer = (Graphics2D)this.getGraphics();
		screenBuffer.drawImage(buffer,0,0,this);
		Toolkit.getDefaultToolkit().sync();
		screenBuffer.dispose();
	}

	public void clearDrawingArea(Graphics2D graphicsContext)
	{
		graphicsContext.setColor(Color.white);
		graphicsContext.fillRect(0,0,getWidth(),getHeight());
	}

}
