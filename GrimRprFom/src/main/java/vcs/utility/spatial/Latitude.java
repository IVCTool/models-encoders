package vcs.utility.spatial;

import java.text.NumberFormat;

public class Latitude extends Angle
{
	private static final long serialVersionUID = 8725180545229166595L;


	public static class LatitudeOutOfBounds extends Exception
  {
  	private static final long serialVersionUID = -1028435528736124044L;

  		public LatitudeOutOfBounds(String Lat) 
      {
         super("The latitude " + Lat + " is out of bounds!");
      }
  }

	
	public static double StringToRadians(String s)
	{
		s = s.toUpperCase();
		if (s.contains("S"))
		{
			s = s.replace("S", "");
			s = "-" + s;
		}
		
		if (s.contains("N"))
			s = s.replace("N", "");
		
		return Math.toRadians(Double.parseDouble(s));
	}
	

	
	private boolean IsOutOfBounds()
	{
    return (radValue < -Math.PI / 2.0 || radValue > Math.PI / 2.0);
	}
	
  public Latitude(double RadLats) throws LatitudeOutOfBounds
  {
    super(RadLats);

    if (IsOutOfBounds())
  		throw(new LatitudeOutOfBounds(Double.toString(RadLats)));
  }

  
  public Latitude(int Sign, int Deg, int Min, int Sec) throws LatitudeOutOfBounds
  {
	  super();

	  try
  	{
  	  Init(new DegMinSec(Sign, Deg, Min, Sec).toRads());
  	}
  	catch (Exception E)
  	{
  	}

  	if (IsOutOfBounds())
  	{
  		try
  		{
  		  throw(new LatitudeOutOfBounds(new DegMinSec(Sign, Deg, Min, Sec).toString()));
  		}
  		catch (Exception E)
  		{
  		}
  	}
  }

  
  public Latitude(DegMinSec DMS) throws LatitudeOutOfBounds
  {
  	super(DMS.toRads());

  	if (IsOutOfBounds())
  		throw(new LatitudeOutOfBounds(DMS.toString()));
  }  
}
