package net.ryanhagan.pather;

import net.ryanhagan.pather.math.Vector2d;
import net.ryanhagan.pather.models.SmoothTracks;
import net.ryanhagan.pather.models.Tracks;
import net.ryanhagan.pather.models.Train;
import net.ryanhagan.pather.views.TrainDrawer;
import net.ryanhagan.pather.views.TrainLineDrawer;
import net.ryanhagan.pather.views.TrainTrackDrawer;
import net.ryanhagan.pather.views.TrainTrackLineDrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;


public class PatherExe
{
	private final int SCREEN_WIDTH = 500;
	private final int SCREEN_HEIGHT = 500;

	// frames and canvas
    private JFrame window;
    private DrawPanel panel;

    // event handling
    private MouseInputHandler mouseListener;
    private KeyListener keyListener;

	// game objects
    private float trainPosition = 0.0f;
	private Tracks tracks = new SmoothTracks();
    private TrainTrackDrawer trackDrawer = new TrainTrackLineDrawer();
	private Train train = new Train();
    private TrainDrawer trainDrawer = new TrainLineDrawer();


    public static void main(String[] args)
    {
    	PatherExe game = new PatherExe();
    	game.initialize();
    	game.run();
    }

    public PatherExe()
    {
    	window = new JFrame();
    	panel = new DrawPanel();

    	window.setTitle("Pather");
    	window.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
    	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	window.getContentPane().add(panel);

    	mouseListener = new MouseInputHandler();
    	panel.addMouseListener(mouseListener);
    	panel.addMouseMotionListener(mouseListener);
        panel.addKeyListener(keyListener);

    	window.setVisible(true);
    }

    private void initialize()
    {
    	mouseListener.setTracks(tracks, trackDrawer);

        train.setDesiredSpeed(20);
        trainPosition = 0.0f;

    	panel.initialize(SCREEN_WIDTH,SCREEN_HEIGHT);
    }

    private void run()
    {
        long currentTime = System.currentTimeMillis();
		long previousTime = 0;

		while (true)
		{
			try
			{
                previousTime = currentTime;
                currentTime = System.currentTimeMillis();
				long elapsedTime = currentTime - previousTime;
                if ( elapsedTime > 25 ) elapsedTime = 25;

				updateObjectsSince(elapsedTime);
                drawObjects();

				Thread.sleep(10);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
    }

	private void updateObjectsSince(long elapsedTime)
	{
        if (tracks.getPoints().size() > 2)
        {
            // get current speed from train
            train.updateCurrentSpeed(elapsedTime);
            float currentSpeed = train.getSpeed();

            // given current speed, ask the track how far we'd go from start position using current speed and elapsed time
            trainPosition += currentSpeed / 10;
            if ( trainPosition > tracks.getLength() )
                trainPosition = 0.0f;
        }
	}

    private void drawObjects()
    {
        Graphics2D graphicsContext = panel.getBackBuffer();
        panel.clearDrawingArea(graphicsContext);
        draw(graphicsContext);
        panel.drawScreen();
        graphicsContext.dispose();

    }

	// This overridden method has to exist in order for the applet to
	// draw anything to the screen.
	private void draw(Graphics2D graphicsContext)
	{
		trackDrawer.setGraphicsContext(graphicsContext);
		trackDrawer.draw(tracks);

		trainDrawer.setGraphicsContext(graphicsContext);
        Point screenPoint = tracks.getScreenPointAt(trainPosition);
        Vector2d directionalVector = tracks.getForwardDirectionAt(trainPosition);
        trainDrawer.draw(train, screenPoint, directionalVector);
	}

}
