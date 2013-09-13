package net.ryanhagan.pather.math;


public class CubicPolynomial {

	float a,b,c,d;     /* a + b*x + c*x^2 + d*x^3 */

	public CubicPolynomial(float a, float b, float c, float d)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}


	/** evaluate cubic */
	public float eval(float u)
	{
		return (((d*u) + c)*u + b)*u + a;
	}

	public float tangent(float u)
	{
		return (((3*d)*u) + (2*c))*u + b;
	}

}
