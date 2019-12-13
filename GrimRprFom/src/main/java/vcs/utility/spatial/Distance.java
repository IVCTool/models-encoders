/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

public class Distance extends XYZVector implements java.io.Serializable
{
	private static final long serialVersionUID = -3244645967224321706L;
	
	public Distance(double X, double Y, double Z)
	{
    super(X, Y, Z);
	}

	public Distance(double[] xyz)
	{
    super(xyz);
	}

	public Distance(XYZVector V)
	{
    super(V);
	}
	
	
	public Distance add(Distance other)
	{
		return new Distance(super.add(other));
	}

	public Distance subtract(Distance other)
	{
		return new Distance(super.subtract(other));
	}

	public Distance rotateBy(Rotator R)
	{
		return new Distance(multiplyBy(R));
	}

	public Distance rotateBy(EulerAngles Orientation)
	{
		return rotateBy(Orientation.getRotationMatrix());
	}

	public Velocity rotateBy(DerivRotator R)
	{
		return new Velocity(multiplyBy(R));
	}
  
	public Distance scaleBy(double scalar)
	{
		return new Distance(super.scaleBy(scalar));
	}


	
}