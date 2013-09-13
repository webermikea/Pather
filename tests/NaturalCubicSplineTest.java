import net.ryanhagan.pather.math.NaturalCubicSpline;
import net.ryanhagan.pather.math.Vector2d;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.*;



public class NaturalCubicSplineTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBasicInstantiation() 
	{
		NaturalCubicSpline spline = new NaturalCubicSpline();
		assertNotNull(spline);
	}
	
	@Test
	public void testAddingOnePoint()
	{
		NaturalCubicSpline spline = new NaturalCubicSpline();
		spline.addControlPointAt( new Point(0,0) );
		assertTrue( spline.getControlPoints().size() == 1 );
	}
	
	@Test
	public void testAddingTwoSimplePoints()
	{
		NaturalCubicSpline spline = new NaturalCubicSpline(1);
		spline.addControlPointAt( new Point(0,0) );
		spline.addControlPointAt( new Point(0,1) );
		assertTrue( spline.getControlPoints().size() == 2 );
		assertEquals( "Spline length", 0, spline.getSplineSegments().size());
	}
	
	@Test
	public void testAddingThreeSimplePoints()
	{
		NaturalCubicSpline spline = new NaturalCubicSpline(1);
		spline.addControlPointAt( new Point(0,0) );
		spline.addControlPointAt( new Point(0,1) );
		spline.addControlPointAt( new Point(0,2) );
		assertTrue( spline.getControlPoints().size() == 3 );
		assertEquals( "Spline length", 3, spline.getSplineSegments().size());
	}

	@Test
	public void testAddingThreeSimplePointsWithGranularity()
	{
		NaturalCubicSpline spline = new NaturalCubicSpline(2);
		spline.addControlPointAt( new Point(0,0) );
		spline.addControlPointAt( new Point(0,1) );
		spline.addControlPointAt( new Point(0,2) );
		assertTrue( spline.getControlPoints().size() == 3 );
		assertEquals( "Spline length", 5, spline.getSplineSegments().size());
	}

	@Test
	public void testMovingTheOnlyControlPoint()
	{
		NaturalCubicSpline spline = new NaturalCubicSpline();
		Point originalPoint = new Point(0,0);
		Point destinationPoint = new Point(1,1);
		spline.addControlPointAt( originalPoint );
		spline.moveControlPointTo(originalPoint, destinationPoint);
		assertEquals( "Number of points", 1, spline.getControlPoints().size());
		assertEquals( "Control point", destinationPoint, spline.getControlPoints().get(0));
	}

	@Test
	public void testDeletingControlPointThatExists()
	{
		NaturalCubicSpline spline = new NaturalCubicSpline(1);
		spline.addControlPointAt( new Point(0,0) );
		spline.addControlPointAt( new Point(0,1) );
		spline.addControlPointAt( new Point(0,2) );
		assertTrue( spline.getControlPoints().size() == 3 );
		spline.deleteControlPointAt( new Point(0,1) );
		assertTrue( spline.getControlPoints().size() == 2 );
	}
	
	@Test
	public void testDeletingControlPointThatDoesNotExist()
	{
		NaturalCubicSpline spline = new NaturalCubicSpline(1);
		spline.addControlPointAt( new Point(0,0) );
		spline.addControlPointAt( new Point(0,1) );
		spline.addControlPointAt( new Point(0,2) );
		assertTrue( spline.getControlPoints().size() == 3 );
		spline.deleteControlPointAt( new Point(5,5) );
		assertTrue( spline.getControlPoints().size() == 3 );
	}
	
	@Test
	public void testGettingPointAlongSimpleSpline()
	{
		NaturalCubicSpline spline = new NaturalCubicSpline(1);
		spline.addControlPointAt( new Point(0,0) );
		spline.addControlPointAt( new Point(0,1) );
		spline.addControlPointAt( new Point(0,2) );
		assertEquals( new Point(0,1), spline.getPointAt(1) );
	}
		
	@Test
	public void testGettingPointAlongNonSimpleSpline()
	{
		NaturalCubicSpline spline = new NaturalCubicSpline(10);
		spline.addControlPointAt( new Point(0,0) );
		spline.addControlPointAt( new Point(0,10) );
		spline.addControlPointAt( new Point(0,20) );
		spline.addControlPointAt( new Point(0,30) );
		assertEquals( new Point(0,27), spline.getPointAt(27) );
	}
		
	@Test
	public void testGettingTangentVectorFromSimpleSpline()
	{
		NaturalCubicSpline spline = new NaturalCubicSpline(5);
		spline.addControlPointAt( new Point(0,0) );
		spline.addControlPointAt( new Point(0,5) );
		spline.addControlPointAt( new Point(0,10) );
		spline.addControlPointAt( new Point(0,15) );
		spline.addControlPointAt( new Point(0,20) );
		Vector2d tangentVector = spline.getTangentVectorAt( 7 );
		assertEquals( 1, tangentVector.GetLength(), 0.5 );
	}

	@Test
	public void testGettingTangentVectorFromCurvedSpline()
	{
		NaturalCubicSpline spline = new NaturalCubicSpline(1);
		spline.addControlPointAt( new Point(0,0) );
		spline.addControlPointAt( new Point(4,2) );
		spline.addControlPointAt( new Point(8,2) );
		spline.addControlPointAt( new Point(12,0) );
		Vector2d tangentVector = spline.getTangentVectorAt(6);
		assertEquals( 1, tangentVector.GetLength(), 0.5 );
	}

	@Test
	public void testGettingTangentVectorFromSimpleSplineWithBoundaryCase()
	{
		NaturalCubicSpline spline = new NaturalCubicSpline(10);
		spline.addControlPointAt( new Point(0,0) );
		spline.addControlPointAt( new Point(0,10) );
		spline.addControlPointAt( new Point(0,20) );
		spline.addControlPointAt( new Point(0,30) );
		Vector2d tangentVector = spline.getTangentVectorAt( 30 );
		assertEquals( 1, tangentVector.GetLength(), 0.5 );
	}

}
