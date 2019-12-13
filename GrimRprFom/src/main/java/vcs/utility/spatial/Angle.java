package vcs.utility.spatial;

import java.io.Serializable;

public class Angle implements Serializable
{
	private static final long serialVersionUID = -132523486139952172L;

	protected double radValue;
	
  public Angle(double val)
  {
   Init(val);
  }
  

  protected void Init(double val)
  {
    radValue = val;
  }

  
  public Angle()
  {
  	radValue = 0.0;
  }
  

  public void normalize0to360()
  {
  	while (radValue < 0.0)
  		radValue += Math.PI * 2.0;

  	while (radValue >= Math.PI * 2.0)
  		radValue -= Math.PI * 2.0;
  }
  
  
  public void normalizeNeg180to180()
  {
  	while (radValue < Math.PI)
  		radValue += Math.PI * 2.0;

  	while (radValue >= Math.PI)
  		radValue -= Math.PI * 2.0;
  }

  public static double normalize(double val)
  {
  	 while (val <= -Math.PI)
  		 val += 2.0 * Math.PI;
 		
     while (val > Math.PI)
    	 val -= 2.0 * Math.PI;
     
     return val;
  }
  
  
  public double get()
  {
  	return radValue;
  }

  public double getDecimalDegrees()
  {
  	return Math.toDegrees(radValue);
  }
  
  public double asRadians()
  {
    return radValue;
  }

  public double asDecimalDegrees()
  {
    return Math.toDegrees(radValue);
  }
  
	public double cos()
	{
		return Math.cos(radValue);
	}

	public double sin()
	{
		return Math.sin(radValue);
	}
  
	public double tan()
	{
		return Math.tan(radValue);
	}
  
	public Angle add(Angle other)
	{
		return new Angle(radValue + other.get());
	}

	
	public Angle add(double other)
	{
		return new Angle(radValue + other);
	}

	
	public Angle subtract(Angle other)
	{
		return new Angle(radValue - other.get());
	}
	
	
	public Angle subtract(double other)
	{
		return new Angle(radValue - other);
	}
	
	
  public Angle difference(Angle other)
  {
		double diff = Math.abs(normalize(radValue) - normalize(other.radValue));

    if (diff > Math.PI)
      diff = Math.PI * 2.0 - diff;

    return new Angle(diff);
  }
  
  public String toString()
  {
  	return Double.toString(radValue);
  }
  
  public boolean equals(Angle other)
  {
    return difference(other).get() == 0.0;
  }
  
  public DegMinSec getDMS()
  {
  	DegMinSec DMS = null;
  	
  	try
  	{
  	  double Degs = Math.toDegrees(Math.abs(radValue));
  	  int D = (int)Math.floor(Degs),
  	      M = (int)Math.floor((Degs - D) * 60.0),
  	      S = (int)Math.floor((Degs - D - M / 60.0) * 3600.0);
  	  
      DMS  = new DegMinSec(radValue == 0.0 ? 1 : (int)Math.signum(radValue), D, M, S);
  	}
  	catch (Exception E)
  	{
  	}

    return DMS;
  }
}
