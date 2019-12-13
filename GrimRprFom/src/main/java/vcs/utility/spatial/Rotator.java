/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

public class Rotator extends XYZMatrix implements java.io.Serializable
{
	private static final long serialVersionUID = -8426366824882965259L;

  public Rotator(double e00, double e01, double e02, 
                 double e10, double e11, double e12,
                 double e20, double e21, double e22)
  {
    super(e00, e01, e02, 
          e10, e11, e12, 
          e20, e21, e22);
  }

  public Rotator()
  {
    super();
  }


  public Rotator(MatrixType t)
  {
    super(t);
  }


  public Rotator(XYZMatrix RM)
  {
    super(RM.get(0, 0), RM.get(0, 1), RM.get(0, 2),
          RM.get(1, 0), RM.get(1, 1), RM.get(1, 2), 
          RM.get(2, 0), RM.get(2, 1), RM.get(2, 2));
  }
  

  
  public Rotator rotateBy(Rotator OtherMatrix)
  {
    return new Rotator(multiplyBy(OtherMatrix));
  }


  public IntegRotator rotateBy(IntegRotator iRM)
  {
  	return new IntegRotator(multiplyBy(iRM));
  }

  
  public DerivRotator rotateBy(DerivRotator dRM)
  {
  	return new DerivRotator(multiplyBy(dRM));
  }

  
	public IntegRotator overTime(Duration dur)
	{
		return new IntegRotator(scaleBy(dur.get()));
	}

	public Rotator scaleBy(double scalar)
	{
		return new Rotator(super.scaleBy(scalar));
	}

	public Rotator addTo(Rotator other)
	{
		return new Rotator(super.addTo(other));
	}


  public Rotator transpose()
  {
  	return new Rotator(super.transpose());
  }

  
	public Rotator(EulerAngles E)
  {
		super();
		
    double cosX = Math.cos(E.getX());
    double sinX = Math.sin(E.getX());
    double cosY = Math.cos(E.getY());
    double sinY = Math.sin(E.getY());
    double cosZ = Math.cos(E.getZ());
    double sinZ = Math.sin(E.getZ());

    elements[0][0] =  cosY * cosZ;
    elements[0][1] =  cosY * sinZ;
    elements[0][2] = -sinY;
    elements[1][0] = -cosX * sinZ + sinX * sinY * cosZ;
    elements[1][1] =  cosX * cosZ + sinX * sinY * sinZ;
    elements[1][2] =  sinX * cosY;
    elements[2][0] =  sinX * sinZ + cosX * sinY * cosZ;
    elements[2][1] = -sinX * cosZ + cosX * sinY * sinZ;
    elements[2][2] =  cosX * cosY;
  }

	
  public EulerAngles getEulerAngles()
  {
  	Angle x, y, z;
  	
    double R11 = elements[0][0];
    double R12 = elements[0][1];
    double R13 = elements[0][2];
    double R21 = elements[1][0];
    double R22 = elements[1][1];
    double R23 = elements[1][2];
    double R33 = elements[2][2];

    if (Math.abs(R13) < 1e-6) 
      R13 = 0.0;

    if (Math.abs(1.0 - Math.abs(R13)) < 1e-6) 
    {
      y = new Angle((-Math.abs(R13) / R13) * Math.PI / 2.0);
      z = new Angle(0.0);
      if (y.get() > 0.0)
      	if (R21 >= 0.0) 
      		x = new Angle(Math.acos(R22));
  	    else 
  	    	x = new Angle(-Math.acos(R22));
      else 
	      if (R21 >= 0.0) 
	      	x = new Angle(-Math.acos(R22));
	      else 
	      	x = new Angle(Math.acos(R22));
    }
    else 
    {
      double den = Math.sqrt(1 - R13 * R13);
      double R33divDen = R33 / den;
      double R11divDen = R11 / den;

      y = new Angle(Math.asin(-R13));
      if (Math.abs(R23) < 1e-6) 
	      if (Math.abs(R33 - den) < 1e-6)
	      	x = new Angle(0.0);
	      else 
	      	x = new Angle(Math.PI);
      else
      	x = new Angle((Math.abs(R23) / R23) * Math.acos(R33divDen));

      if (Math.abs(R12) < 1e-6) 
	      if (Math.abs(R11-den) < 1e-6)
	      	z = new Angle(0.0);
	      else
	      	z = new Angle(Math.PI);
      else 
      	z = new Angle((Math.abs(R12) / R12) * Math.acos(R11divDen));
    }
    return new EulerAngles(x, y, z);
  }
  
}