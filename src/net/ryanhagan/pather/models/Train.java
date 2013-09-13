package net.ryanhagan.pather.models;

public class Train
{
    private float speed;
    private float desiredSpeed;

    public Train()
   	{
   		speed = 0.0f;
	}

	public void setDesiredSpeed(float desiredSpeed)
	{
		this.desiredSpeed = desiredSpeed;
	}

    public void updateCurrentSpeed(long elapsedTime)
    {
        float numSeconds = (float)elapsedTime / 1000.0f;
        float percentage = numSeconds / 2.0f;
        speed += (desiredSpeed - speed) * percentage;
        if ( speed > desiredSpeed ) speed = desiredSpeed;
    }

    public float getSpeed()
    {
        return speed;
    }
}