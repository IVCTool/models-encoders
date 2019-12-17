/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

import vcs.utility.spatial.XYZMatrix.MatrixType;

public class EulerDerivs extends XYZVector implements java.io.Serializable
{
	private static final long serialVersionUID = -3244645967224321706L;
	
	public EulerDerivs(double X, double Y, double Z)
	{
    super(X, Y, Z);
	}

	public EulerDerivs(double[] xyz)
	{
    super(xyz);
	}
	
	public EulerDerivs(XYZVector V)
	{
    super(V);
	}
	

	public IntegIntegRotator getR2Matrix(Duration timestep)
  {
    double w  = magnitude(),
           wt = w * timestep.get();

    if (w == 0.0)
    	return new IntegIntegRotator(MatrixType.IDENTITY);

    DerivRotator wwT        = getVVTMatrix();
    IntegIntegRotator term1 = new IntegIntegRotator(wwT.scaleBy(((wt * wt) / 2 - Math.cos(wt) - wt * Math.sin(wt) + 1) / (w * w * w * w)));
    
    IntegIntegRotator I     = new IntegIntegRotator(MatrixType.IDENTITY);
    IntegIntegRotator term2 = I.scaleBy((Math.cos(wt) + wt * Math.sin(wt) - 1) / (w * w));

    DerivRotator W          = getWMatrix();
    IntegIntegRotator term3 = new IntegIntegRotator(W.scaleBy((Math.sin(wt) - wt * Math.cos(w)) / (w * w * w)));

    return term1.addTo(term2.addTo(term3));
  }

  
  public IntegRotator getR1Matrix(Duration timestep)
  {
    double w  = magnitude(),
           wt = w * timestep.get();

    if (w == 0.0)
    	return new IntegRotator(MatrixType.IDENTITY);

    DerivRotator wwT   = getVVTMatrix();
    IntegRotator term1 = new IntegRotator(wwT.scaleBy((wt - Math.sin(wt)) / (w * w * w)));

    IntegRotator I     = new IntegRotator(MatrixType.IDENTITY);
    IntegRotator term2 = I.scaleBy(Math.sin(wt) / w);

    DerivRotator W     = getWMatrix();
    IntegRotator term3 = new IntegRotator(W.scaleBy((1 - Math.cos(wt)) / (w * w)));

    return term1.addTo(term2.addTo(term3));
  }

  
  public DerivRotator getWMatrix()
  {
  	return new DerivRotator(0.0,   -myZ,    myY,
                              myZ,  0.0,   -myX,
                             -myY,    myX,  0.0);
  }


  public DerivRotator getVVTMatrix()
  {
    return new DerivRotator(myX * myX,  myX * myY,  myX * myZ,
                            myY * myX,  myY * myY,  myY * myZ,
                            myZ * myX,  myZ * myY,  myZ * myZ);
  }
  
  
  public Rotator getR_E_TO_E(Duration timestep)
  {
    double w = magnitude();
    
    if (w == 0.0)
    	return new Rotator(MatrixType.IDENTITY);

    DerivRotator wwT = getVVTMatrix();
    Rotator term1    = wwT.overTime(new Duration((1 - Math.cos(w * timestep.get())) / (w * w)));

    Rotator I        = new Rotator(MatrixType.IDENTITY);
    Rotator term2    = I.scaleBy(Math.cos(w * timestep.get()));
    
    DerivRotator W   = getWMatrix();
    Rotator term3    = W.overTime(new Duration(-Math.sin(w * timestep.get()) / w));

    return term1.addTo(term2.addTo(term3));
  }

	public EulerAngles orientationOverTime(Duration dur)
	{
		return new EulerAngles(myX * dur.get(),
		                       myY * dur.get(),
                  		     myZ * dur.get());
	}
  
}