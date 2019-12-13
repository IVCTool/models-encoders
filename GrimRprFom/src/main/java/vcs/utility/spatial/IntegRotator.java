/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

//import vcs.utility.spatial.RotationMatrix.MatrixType;

public class IntegRotator extends XYZMatrix implements java.io.Serializable
{
	private static final long serialVersionUID = 8772427006678313174L;


  public IntegRotator(double e00, double e01, double e02, 
                      double e10, double e11, double e12,
                      double e20, double e21, double e22)
  {
    super(e00, e01, e02, 
    		  e10, e11, e12, 
    		  e20, e21, e22);
  }

  public IntegRotator()
  {
    super();
  }

  
  public IntegRotator(MatrixType t)
  {
    super(t);
  }
  
  
  public IntegRotator(XYZMatrix RM)
  {
    super(RM.get(0, 0), RM.get(0, 1), RM.get(0, 2),
    		  RM.get(1, 0), RM.get(1, 1), RM.get(1, 2), 
    		  RM.get(2, 0), RM.get(2, 1), RM.get(2, 2));
  }

	public IntegIntegRotator overTime(Duration dur)
	{
		return new IntegIntegRotator(scaleBy(dur.get()));
	}

  
	public IntegRotator rotateBy(Rotator M)
	{
		return new IntegRotator(multiplyBy(M));
	}
	
	public Rotator rotateBy(DerivRotator M)
	{
		return new Rotator(multiplyBy(M));
	}
	
	public IntegRotator scaleBy(double scalar)
	{
		return new IntegRotator(super.scaleBy(scalar));
	}

	public IntegRotator addTo(IntegRotator other)
	{
		return new IntegRotator(super.addTo(other));
	}

	public IntegRotator transpose()
	{
		return new IntegRotator(super.transpose());
	}



}