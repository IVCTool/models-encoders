/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

public class Acceleration extends XYZVector implements java.io.Serializable
{
	private static final long serialVersionUID = -3244645967224321706L;
	
	public Acceleration(double X, double Y, double Z)
	{
    super(X, Y, Z);
	}

	public Acceleration(double[] xyz)
	{
    super(xyz);
	}

	public Acceleration(XYZVector V)
	{
    super(V);
	}
	
	public Velocity velocityOverTime(Duration dur)
	{
		return new Velocity(scaleBy(dur.get()));
	}
  
	public Distance distanceOverTime(Duration dur)
	{
		return new Distance(scaleBy(dur.get() * dur.get() * 0.5));
	}
	
	public Acceleration add(Acceleration other)
	{
		return new Acceleration(super.add(other));
	}
	
	public Acceleration rotateBy(Rotator R)
	{
		return new Acceleration(multiplyBy(R));
	}
	
	public Velocity rotateBy(IntegRotator R)
	{
		return new Velocity(multiplyBy(R));
	}

	public Distance rotateBy(IntegIntegRotator R)
	{
		return new Distance(multiplyBy(R));
	}
}