package vcs.utility.spatial;

public class IntegIntegRotator extends XYZMatrix implements java.io.Serializable
{
	
	private static final long serialVersionUID = 9018070952676109601L;

	public IntegIntegRotator(double e00, double e01, double e02, 
                           double e10, double e11, double e12,
                           double e20, double e21, double e22)
  {
    super(e00, e01, e02, 
    		  e10, e11, e12, 
    		  e20, e21, e22);
  }

  public IntegIntegRotator()
  {
    super();
  }
  
  public IntegIntegRotator(MatrixType t)
  {
    super(t);
  }
  
  public IntegIntegRotator(XYZMatrix RM)
  {
    super(RM.get(0, 0), RM.get(0, 1), RM.get(0, 2),
    		  RM.get(1, 0), RM.get(1, 1), RM.get(1, 2), 
    		  RM.get(2, 0), RM.get(2, 1), RM.get(2, 2));
  }
  
	public IntegIntegRotator rotateBy(Rotator M)
	{
		return new IntegIntegRotator(multiplyBy(M));
	}
	
	public IntegRotator rotateBy(DerivRotator M)
	{
		return new IntegRotator(multiplyBy(M));
	}
	
	public IntegIntegRotator scaleBy(double scalar)
	{
		return new IntegIntegRotator(super.scaleBy(scalar));
	}

	public IntegIntegRotator addTo(IntegIntegRotator other)
	{
		return new IntegIntegRotator(super.addTo(other));
	}

	public IntegIntegRotator transpose()
	{
		return new IntegIntegRotator(super.transpose());
	}
}
