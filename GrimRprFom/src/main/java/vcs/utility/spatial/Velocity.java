/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

public class Velocity extends XYZVector implements java.io.Serializable
{
	private static final long serialVersionUID = -3244645967224321706L;
	
	public Velocity(double X, double Y, double Z)
	{
    super(X, Y, Z);
	}

	public Velocity(double[] xyz)
	{
    super(xyz);
	}

	public Velocity(XYZVector V)
	{
    super(V);
	}
	
	
	public Distance distanceOverTime(Duration dur)
	{
		return new Distance(scaleBy(dur.get()));
	}
  
	public Velocity add(Velocity other)
	{
		return new Velocity(super.add(other));
	}

	public Velocity subtract(Velocity other)
	{
		return new Velocity(super.subtract(other));
	}

	public Velocity rotateBy(Rotator R)
	{
		return new Velocity(multiplyBy(R));
	}
  
	public Acceleration rotateBy(DerivRotator R)
	{
		return new Acceleration(multiplyBy(R));
	}
	
	public Distance rotateBy(IntegRotator R)
	{
		return new Distance(multiplyBy(R));
	}
	
	public Velocity scaleBy(double dur)
	{
		return new Velocity(super.scaleBy(dur));
	}
	
}