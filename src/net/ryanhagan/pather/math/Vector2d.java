package net.ryanhagan.pather.math;

public class Vector2d
{
	public float x;
	public float y;
	private float length;

	
	public Vector2d(float x, float y) 
	{
		//  init the vector to 0
		if ( x == 0 && y == 0 ) 
			this.zero();
		else {
			this.x = x;
			this.y = y;
		}
		this.calcLength();
	}

	public void Normalize()
	{
		if ( this.length != 0 ) 
		{
			this.x = this.x / this.length;
			this.y = this.y / this.length;
		}
		this.length = 1;
	}

	private void calcLength()
	{
		this.length = (float)Math.sqrt( (this.x * this.x) + (this.y * this.y) );
	}
	
	private void zero()
	{
		this.x = this.y = this.length = 0;
	}

    public double GetLength()
    {
        return 0;
    }
}