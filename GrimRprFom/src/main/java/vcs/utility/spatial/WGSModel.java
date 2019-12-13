/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */


package vcs.utility.spatial;

public class WGSModel implements java.io.Serializable
{
	private static final long serialVersionUID = -8494976341579828642L;

	private double semiMajorAxis;   // The semi major axis of the ellipsoid.
	private double semiMinorAxis;   // The semi minor axis of the ellipsoid.
	private double ellipsoidFlattening;      // The flattening of the ellipsoid.
	private double eccentricity_sq; // The square of the eccentricity. 
	
	private double mercatorRadius;  // The radius, for Mercator calculations. 
	
	private double radius;             //   equatorial radius
	private double ecc;                //   eccentricity
	
	private WorldModelVersion version;
	
	public enum WorldModelVersion 
	{
	 /* Airy,
	  Airy_Modified,
	  Australian_National,
	  Bessel_1841,
	  Clark_1866,
	  Clarke_1880,
	  Everest,
	  Everest_Modified,
	  Fischer_1960,
	  Fischer_1960_Modified,
	  Fischer_1968,
	  GEOID_detic_Reference_1967,
	  GEOID_detic_Reference_1980,
	  Helmert,
	  Hough,
	  International,
	  Krassovsky,
	  South_American_1969,
	  WGS_60,
	  WGS_66,
	  WGS_72,*/
	  WGS_84;
	}
	
	static class WorldModelUnsupportedException extends Exception 
	{
		private static final long serialVersionUID = 2343479963767062468L;

	  public WorldModelUnsupportedException() 
	  {
	    super("Attempt to use an unsupported world model.");
	  }
	}
	
	
	
	public WGSModel(WorldModelVersion v) throws WorldModelUnsupportedException
	{
		init(v);
	}

	public WGSModel()
	{
		try
		{
		  init(WorldModelVersion.WGS_84); 	// Default world is the WGS84 ellipsoid
		}
		catch (WorldModelUnsupportedException E)
		{
		}
		
	}

  /**
   * toString
   * 
   *   @return A String representation of the object.
   */

	public String toString()
	{
	  return version.toString(); 
	}

	
  /**
   * equals
   * 
   *   @return Boolean : whether or not the specified object is logically equivalent to this one.
   */
  public boolean equals(WGSModel other)
  {
  	return version == other.getVersion();
  }
	
  
	private void init(WorldModelVersion v) throws WorldModelUnsupportedException
	{
		version = v;

		switch (version)
		{
  		case WGS_84:
  			semiMajorAxis = 6378137.0;
  			semiMinorAxis = 6356752.3142;
  	    ellipsoidFlattening = 0.9996;
	      break;
	      
	    default:
	    	throw new WorldModelUnsupportedException();
		}
		
  	eccentricity_sq = 1.0 - Math.pow(semiMinorAxis, 2.0) / Math.pow(semiMajorAxis, 2.0);

    radius = semiMajorAxis * ellipsoidFlattening;  
    ecc    = semiMinorAxis * ellipsoidFlattening;
    
    mercatorRadius = (semiMajorAxis + semiMinorAxis) / 2.0;
	}
	
	public WorldModelVersion getVersion()
	{
		return version;
	}
	
	public double getSemiMajorAxis()
	{
	  return semiMajorAxis;	
	}
		
	public double getSemiMinorAxis()
	{
	  return semiMinorAxis;	
	}

	public double getFlattening()
	{
	  return ellipsoidFlattening;	
	}

	public double getRadius()
	{
	  return radius;	
	}

	public double getMercatorRadius()
	{
	  return mercatorRadius;	
	}

	public double getEcc()
	{
	  return ecc;	
	}

	public double getEccentricitySquared()
	{
	  return eccentricity_sq;	
	}
}
