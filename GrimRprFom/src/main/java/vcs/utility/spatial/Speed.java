package vcs.utility.spatial;

import java.io.Serializable;

public class Speed implements Serializable
{
	private static final long serialVersionUID = 3654185562283416094L;

	protected double value = 0.0;

	
	public enum Units
	{
		MpS,   // metres per second
		KMpH,  // km per hour
		MpH,   // miles per hour
		KNOTS; // knots
	}

	
  public Speed(double val, Units units)
  {
   switch (units)
   {
     case MpS:
    	 value = val;
    	 break;

     case KMpH:
    	 value = val * 0.2777777777777778;
    	 break;

     case MpH:
    	 value = val * 0.44704;
    	 break;

     case KNOTS:
    	 value = val * 0.514444444444444;
    	 
   }
  }
  

  public Speed()
  {
  }
  

  public double getMpS()
  {
  	return value;
  }

  public double getKMpH()
  {
  	return value / 0.2777777777777778;
  }

  public double getMpH()
  {
  	return value / 0.44704;
  }

  public double getKnots()
  {
  	return value / 0.514444444444444;
  }

  
	public Speed add(Speed other)
	{
		return new Speed(value + other.getMpS(), Units.MpS);
	}

	
	public Speed add(double other, Units units)
	{
		return add(new Speed(other, units));
	}

	
	public Speed subtract(Speed other)
	{
		return new Speed(value - other.getMpS(), Units.MpS);
	}
	
	
	public Speed subtract(double other, Units units)
	{
		return subtract(new Speed(other, units));
	}
	
	
  public String toString()
  {
  	return Double.toString(value) + "m/s";
  }
  
  public boolean equals(Speed other)
  {
    return subtract(other).getMpS() == 0.0;
  }
}
