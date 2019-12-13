/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

public class EulerAngles implements java.io.Serializable
{
	private static final long serialVersionUID = 8178107991483039648L;

	private Angle myX,
	              myY,
	              myZ;

	public EulerAngles(Angle x, Angle y, Angle z)
	{
    this.myX = x;
    this.myY = y;
    this.myZ = z;
	}

	
	public EulerAngles(double x, double y, double z)
	{
    this.myX = new Angle(x);
    this.myY = new Angle(y);
    this.myZ = new Angle(z);
	}

  public EulerAngles(XYZVector vector)
  {
    this.myX = new Angle(vector.getX());
    this.myY = new Angle(vector.getY());
    this.myZ = new Angle(vector.getZ());
  }

	public EulerAngles(double[] xyz)
	{
    myX = new Angle(xyz[0]);
    myY = new Angle(xyz[1]);
    myZ = new Angle(xyz[2]);
	}
	
	public EulerAngles(EulerAngles o)
	{
    myX = new Angle(o.getX());
    myY = new Angle(o.getY());
    myZ = new Angle(o.getZ());
	}
	
  /** 
   * toString
   * 
   *   @return A String representation of the object.
   */
	public String toString()
	{
	  return myX.toString() + " " + myY.toString() + " " + myZ.toString(); 
	}

  /**
   * equals
   * 
   *   @return Boolean : whether or not the specified object is logically equivalent to this one.
   */
	public boolean equals(EulerAngles other)
	{
  	return myX.equals(other.myX) &&
  	       myY.equals(other.myY) &&
  	       myZ.equals(other.myZ);
	}    


	public EulerAngles subtract(EulerAngles other)
	{
		return new EulerAngles(myX.difference(other.myX),
				                   myY.difference(other.myY),
				                   myZ.difference(other.myZ));
	}
	
	public EulerAngles add(EulerAngles other)
	{
		return new EulerAngles(myX.get() + other.myX.get(),
                    			 myY.get() + other.myY.get(),
				                   myZ.get() + other.myZ.get());
	}

	public EulerAngles scaleBy(double scalar)
	{
		return new EulerAngles(myX.get() * scalar, 
		                       myY.get() * scalar, 
		                       myZ.get() * scalar);
	}
	
	public double getX()
	{
	  return myX.get();
	}
	
	public double getY()
	{
	  return myY.get();
	}
	
	public double getZ()
	{
	  return myZ.get();
	}

  public XYZVector getXYZ()
  {
    return new XYZVector(this.myX.get(), this.myY.get(), this.myZ.get());
  }

	public double getXDegrees()
	{
	  return myX.getDecimalDegrees();
	}
	
	public double getYDegrees()
	{
	  return myY.getDecimalDegrees();
	}
	
	public double getZDegrees()
	{
	  return myZ.getDecimalDegrees();
	}

  public DerivRotator getDerivR_LG_TO_E(EulerAngles other)
  {
		double cosZ = myZ.cos();
		double sinZ = myZ.sin();
		double cosX = myX.cos();
		double sinX = myX.sin();
		double cosY = myY.cos();
		double sinY = myY.sin();
		
  	DerivRotator temp1 =	new DerivRotator
  	  (                              0.0,                               0.0,           0.0,
  		 -sinY * cosZ - cosY * sinX * sinZ,  sinY * sinZ - cosY * sinX * cosZ,  -cosY * cosX,
	  	 -cosY * cosZ + sinY * sinX * sinZ,  cosY * sinZ + sinY * sinX * cosZ,   sinY * cosX);
		
  	DerivRotator temp2 = new DerivRotator
  	  (-sinX * sinZ,                -sinX * cosZ,        -cosX,
		   -sinY * cosX * sinZ,  -sinY * cosX * cosZ,  sinY * sinX,
		   -cosY * cosX * sinZ,  -cosY * cosX * cosZ,  cosY * sinX);
		
  	DerivRotator temp3 = new DerivRotator
  	  (                      cosX * cosZ,                       -cosX * sinZ,  0.0, 
		   -cosY * sinZ - sinY * sinX * cosZ,  -cosY * cosZ + sinY * sinX * sinZ,  0.0,
		    sinY * sinZ - cosY * sinX * cosZ,   sinY * cosZ + cosY * sinX * sinZ,  0.0);
		
		temp1 = temp1.scaleBy(other.getY());
		temp2 = temp2.scaleBy(other.getX());
		temp3 = temp3.scaleBy(other.getZ());

		return temp1.addTo(temp2.addTo(temp3));
	}

  
  public Rotator getR_LG_TO_E()
  {
		double cosZ = myZ.cos();
		double sinZ = myZ.sin();
		double cosX = myX.cos();
		double sinX = myX.sin();
		double cosY = myY.cos();
		double sinY = myY.sin();

  	return new Rotator
  	  (                      cosX * sinZ,                        cosX * cosZ,  -sinX,
        cosY * cosZ - sinY * sinX * sinZ,  -cosY * sinZ - sinY * sinX * cosZ,  -sinY * cosX,
       -sinY * cosZ - cosY * sinX * sinZ,   sinY * sinZ - cosY * sinX * cosZ,  -cosY * cosX);
  }
  
  
  /**
   * addTo
   * 
   * Adds an XYZ vector to the current Orientation.  
   * 
   * @return A new WGSPosition, moved by the input vector.
   */
  
	public EulerAngles addTo(EulerAngles diff)
	{
		return new EulerAngles(myX.get() + diff.getX(),
				                   myY.get() + diff.getY(),
				                   myZ.get() + diff.getZ());
	}

	
  public EulerDerivs calcDerivatives(DerivRotator DM)
  {
  	double dx, dy, dz;

    double cosY = myY.cos();

    double R21;
    double R22;

    if (myX.get() > 0.0)
    {
      R21 = Math.sin(myX.get() - myZ.get());
      R22 = Math.cos(myX.get() - myZ.get());
    }
    else
    {
      R21 = -Math.sin(myX.get() + myZ.get());
      R22 =  Math.cos(myX.get() + myZ.get());
    }

    if (Math.abs(cosY) < 1e-9)
    {
    	dy = -(Math.abs(myY.get()) / myY.get()) * (DM.get(0, 0) * myZ.cos() + DM.get(0, 1) * myZ.sin());
    	dz = 0.0;
  
      if (Math.abs(R22) > 0.5)
      	dx =  (Math.abs(myY.get()) / myY.get()) * DM.get(1, 0) / R22;
      else
      	dx = -(Math.abs(myY.get()) / myY.get()) * DM.get(1, 1) / R21;
    }
    else
    {
    	dx = (DM.get(1, 2) * myX.cos() - DM.get(2, 2) * myX.sin()) / cosY;
    	dy = -DM.get(0, 2) / cosY;
    	dz = (DM.get(0, 1) * myZ.cos() - DM.get(0, 0) * myZ.sin()) / cosY;
    }
    
    return new EulerDerivs(dx, dy, dz);
  }
  
  
	public boolean differsFrom(EulerAngles other, double tolerance)
	{
		return Math.abs(myX.difference(other.myX).get()) > tolerance || 
				   Math.abs(myY.difference(other.myY).get()) > tolerance ||
				   Math.abs(myZ.difference(other.myZ).get()) > tolerance;
	}
	
	
	public Rotator getRotationMatrix()
	{
		return new Rotator(this);
	}
  
}