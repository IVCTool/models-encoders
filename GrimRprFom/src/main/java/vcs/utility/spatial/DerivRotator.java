/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;


public class DerivRotator extends XYZMatrix implements java.io.Serializable
{
	private static final long serialVersionUID = 8772427006678313174L;


  public DerivRotator(double e00, double e01, double e02, 
                      double e10, double e11, double e12,
                      double e20, double e21, double e22)
  {
    super(e00, e01, e02, 
          e10, e11, e12, 
          e20, e21, e22);
  }

  public DerivRotator()
  {
    super();
  }


  public DerivRotator(MatrixType t)
  {
    super(t);
  }


  public DerivRotator(XYZMatrix RM)
  {
    super(RM.get(0, 0), RM.get(0, 1), RM.get(0, 2),
          RM.get(1, 0), RM.get(1, 1), RM.get(1, 2), 
          RM.get(2, 0), RM.get(2, 1), RM.get(2, 2));
  }
	
	public DerivRotator rotateBy(Rotator M)
	{
		return new DerivRotator(multiplyBy(M));
	}
	
	public Rotator rotateBy(IntegRotator M)
	{
		return new Rotator(multiplyBy(M));
	}
	
	public DerivRotator scaleBy(double scalar)
	{
		return new DerivRotator(super.scaleBy(scalar));
	}

	public Rotator overTime(Duration dur)
	{
		return new Rotator(scaleBy(dur.get()));
	}

	public DerivRotator addTo(DerivRotator other)
	{
		return new DerivRotator(super.addTo(other));
	}

	public DerivRotator transpose()
	{
		return new DerivRotator(super.transpose());
	}



}