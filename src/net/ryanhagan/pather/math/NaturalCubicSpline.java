package net.ryanhagan.pather.math;

import java.lang.Math;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.util.ArrayList;



@SuppressWarnings("restriction")
public class NaturalCubicSpline {

	final int STEPS = 24;

	private ArrayList<Float> 			xCoordinates = new ArrayList<Float>();
	private ArrayList<Float> 			yCoordinates = new ArrayList<Float>();

	private ArrayList<CubicPolynomial> 	xCubicPolynomials = new ArrayList<CubicPolynomial>();
	private ArrayList<CubicPolynomial> 	yCubicPolynomials = new ArrayList<CubicPolynomial>();

	private ArrayList<Point> 			points = new ArrayList<Point>();
	private ArrayList<Point2D.Float> 	spline = new ArrayList<Point2D.Float>();

	private ArrayList<Float> 			splineSegmentLengths = new ArrayList<Float>();

	private float						length;
	private int							granularity;

// Constructor
	public NaturalCubicSpline()
	{
		length = 0.0f;
		granularity = STEPS;
	}

    public NaturalCubicSpline(int granularity)
	{
		length = 0.0f;
		this.granularity = granularity;
	}

    public void addControlPointAt(Point theNewLocation)
	{
		points.add(theNewLocation);
		xCoordinates.add((float)theNewLocation.x);
		yCoordinates.add((float)theNewLocation.y);
		regenerateSpline();
	}
	
	public void moveControlPointTo(Point theControlPoint, Point theNewLocation)
	{
		int controlPointIndex = 0;
    	for (Point controlPoint : points)
    	{
    		if ( controlPoint.equals(theControlPoint) )
    			points.set(controlPointIndex, theNewLocation);
    		controlPointIndex++;
    	}
		regenerateSpline();
	}

	public void deleteControlPointAt(Point theControlPoint) 
	{
		points.remove(theControlPoint);
		regenerateSpline();
	}

	public Point getPointAt(float theDistanceOnTheSpline)
	{
		if (points.size() == 0) return new Point(0,0);
		if (spline.size() == 0) return points.get(points.size() - 1);
		if (theDistanceOnTheSpline < 0.0f) return points.get(0);
		if (theDistanceOnTheSpline > length) return points.get(points.size() - 1);
		Point screenPoint;

		float distanceAlongSpline = theDistanceOnTheSpline;
		if (distanceAlongSpline > this.length) distanceAlongSpline = this.length;
		int splineSegmentIndex = 0;
		while ( (distanceAlongSpline -= splineSegmentLengths.get(splineSegmentIndex++)) > 0.0f ) {}
		splineSegmentIndex--;
		distanceAlongSpline += splineSegmentLengths.get(splineSegmentIndex);
		float distanceAlongSplineSegment = distanceAlongSpline / splineSegmentLengths.get(splineSegmentIndex);

		screenPoint = new Point(Math.round(xCubicPolynomials.get(splineSegmentIndex).eval(distanceAlongSplineSegment)), 
								Math.round(yCubicPolynomials.get(splineSegmentIndex).eval(distanceAlongSplineSegment)));
		return screenPoint;
	}

	public Vector2d getTangentVectorAt(float theDistanceOnTheSpline)
	{
		Vector2d tangentVector = new Vector2d(0f, 0f);
		if ( xCubicPolynomials.size() == 0 || spline.size() == 0 || theDistanceOnTheSpline < 0.0f ) return tangentVector;

		float distanceAlongSpline = theDistanceOnTheSpline;
		if (distanceAlongSpline > this.length) distanceAlongSpline = this.length;
		int splineSegmentIndex = 0;
		while ( (distanceAlongSpline -= splineSegmentLengths.get(splineSegmentIndex++)) > 0.0f ) {}
		splineSegmentIndex--;
		distanceAlongSpline += splineSegmentLengths.get(splineSegmentIndex);
		float distanceAlongSplineSegment = distanceAlongSpline / splineSegmentLengths.get(splineSegmentIndex);
		
		tangentVector = new Vector2d(xCubicPolynomials.get(splineSegmentIndex).tangent(distanceAlongSplineSegment),
									 yCubicPolynomials.get(splineSegmentIndex).tangent(distanceAlongSplineSegment));
		tangentVector.Normalize();
		return tangentVector;
	}

	public ArrayList<Point> getControlPoints()
	{
		return points;
	}

	public ArrayList<Point2D.Float> getSplineSegments()
	{
		return spline;
	}

	public float getMaxPosition()
	{
		return length;
	}

	private void regenerateSpline()
	{
		calculatePolynomialsFromCoordinates(xCoordinates, xCubicPolynomials);
		calculatePolynomialsFromCoordinates(yCoordinates, yCubicPolynomials);

		spline.clear();
		splineSegmentLengths.clear();
		if ( xCubicPolynomials.size() > 0 )
		{
			createSplineFromPolynomials();

			length = 0.0f;
			for(int i = 1; i < spline.size(); i++)
			{
				length += (float)Math.sqrt( Math.pow(spline.get(i).x - spline.get(i-1).x, 2) +
											Math.pow(spline.get(i).y - spline.get(i-1).y, 2) );
			}
		}
	}

	private void calculatePolynomialsFromCoordinates(ArrayList<Float> coordinates, ArrayList<CubicPolynomial> polynomials)
	{
		polynomials.clear();

		if(coordinates.size() < 3) return;

		int i;
		int n = coordinates.size();
		float[] a = new float[n]; a[0] = 0; for ( i = 1; i < n; i++ ) { a[i] = 1; }
		float[] b = new float[n]; b[0] = b[n-1] = 2; for ( i = 0; i < n-1; i++ ) { b[i] = 4; }
		float[] c = new float[n]; c[n-1] = 0; for ( i = 0; i < n-1; i++ ) { c[i] = 1; }
		float[] d = new float[n];
			d[0] = 3 * (coordinates.get(1) - coordinates.get(0));
			d[n-1] = 3 * (coordinates.get(n-1) - coordinates.get(n-2));
			for ( i = 1; i < n-1; i++ ) { d[i] = 3 * (coordinates.get(i+1) - coordinates.get(i-1)); }
		float[] x = new float[n];
		tridiagonalSolve(a, b, c, d, x, n);

		/* now compute the coefficients of the cubics */
		for ( i = 0; i < n-1; i++) 
		{
			polynomials.add( new CubicPolynomial(coordinates.get(i), x[i], 3*(coordinates.get(i+1) - coordinates.get(i)) - 2*x[i] - x[i+1],
			       2*(coordinates.get(i) - coordinates.get(i+1)) + x[i] + x[i+1]) );
		}
	}

	/*
	 * Please see http://en.wikipedia.org/wiki/Tridiagonal_matrix_algorithm
	 * for an explanation of how to solve the tridiagonal matrix and why
	 * it's important for natural cubic splines.
	 */
	private void tridiagonalSolve(float[] a, float[] b, float[] c, float[] d, float[] x, int n) 
	{
		/* Modify the coefficients. */
		c[0] /= b[0];	/* Division by zero risk. */
		d[0] /= b[0];	/* Division by zero would imply a singular matrix. */
		for (int i = 1; i < n; i++){
			float id = 1 / (b[i] - c[i-1] * a[i]);  /* Division by zero risk. */
			c[i] *= id;	                         /* Last value calculated is redundant. */
			d[i] = (d[i] - d[i-1] * a[i]) * id;
		}
	 
		/* Now back substitute. */
		x[n - 1] = d[n - 1];
		for (int i = n - 2; i >= 0; i--)
			x[i] = d[i] - c[i] * x[i + 1];
	}

	private void createSplineFromPolynomials() 
	{
		spline.add(new Point2D.Float(xCubicPolynomials.get(0).eval(0), yCubicPolynomials.get(0).eval(0)));
		for(int x = 0; x < xCubicPolynomials.size(); x++)
		{
			float segmentLengthDistance = 0.0f;
			for(int steps = 1; steps <= granularity; steps++)
			{
				float u = (float)steps / (float)granularity;
				spline.add(new Point2D.Float(xCubicPolynomials.get(x).eval(u), yCubicPolynomials.get(x).eval(u)));
				int index = x * granularity + (steps - 1);
				segmentLengthDistance += (float)Math.sqrt( Math.pow(spline.get(index).x - spline.get(index+1).x, 2) +
															Math.pow(spline.get(index).y - spline.get(index+1).y, 2) );
			}
			splineSegmentLengths.add(segmentLengthDistance);
		}
	}

}
