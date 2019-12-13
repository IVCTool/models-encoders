package vcs.utility.spatial;

import java.text.NumberFormat;

public class Longitude extends Angle
{
	private static final long serialVersionUID = -125333902368288945L;


	public static class LongitudeOutOfBounds extends Exception
  {
		private static final long serialVersionUID = -6383457615280231347L;

			public LongitudeOutOfBounds(String Longe) 
      {
         super("The longitude " + Longe + " is out of bounds!");
      }
  }
	
	public static double StringToRadians(String s)
	{
		s = s.toUpperCase();
		if (s.contains("E"))
			s = s.replace("E", "");
		
		if (s.contains("W"))
		{
			s = s.replace("W", "");
			s = "-" + s;
		}
		
		return Math.toRadians(Double.parseDouble(s));
	}
	
	
	private boolean IsOutOfBounds()
	{
    return false;
    
    //(radValue < 0 || radValue > 2.0 * Math.PI);
	}
	
  public Longitude(double RadLong) throws LongitudeOutOfBounds
  {
  	super(RadLong);
  	
  	if (IsOutOfBounds())
  		throw(new LongitudeOutOfBounds(Double.toString(RadLong)));
  }

  
  public Longitude(int Deg, int Min, int Sec) throws LongitudeOutOfBounds
  {
	  super();

	  try
  	{
  	  Init(new DegMinSec(1, Deg, Min, Sec).toRads());
  	}
  	catch (Exception E)
  	{
  	}

  	if (IsOutOfBounds())
  	{
  		try
  		{
  		  throw(new LongitudeOutOfBounds(new DegMinSec(1, Deg, Min, Sec).toString()));
  		}
  		catch (Exception E)
  		{
  		}
  	}
  }

  
  public Longitude(DegMinSec DMS) throws LongitudeOutOfBounds
  {
  	super(DMS.toRads());

  	if (IsOutOfBounds())
  		throw(new LongitudeOutOfBounds(DMS.toString()));
  }
}
