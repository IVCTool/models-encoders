/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

import java.text.NumberFormat;

import vcs.utility.spatial.Mercator.*;
import vcs.utility.spatial.WGSModel.*;
import vcs.utility.spatial.Latitude.*;
import vcs.utility.spatial.Longitude.*;

public class LatLongHeight extends LatLong implements java.io.Serializable
{
	private static final long serialVersionUID = -7935157469246795255L;

	protected double myHeight;
  
	public LatLongHeight(double latitude, double longitude, double height) throws LatitudeOutOfBounds, LongitudeOutOfBounds
	{
		super(latitude, longitude);
		this.myHeight = height;
	}
	
	public LatLongHeight(Latitude latitude, Longitude longitude, double height)
	{
		super(latitude, longitude);
		this.myHeight = height;
	}
	

	public LatLongHeight(LatLong ll, double height)
	{
		super(ll.Lat, ll.Long);
		this.myHeight = height;
	}
	

	public LatLongHeight(Angle latitude, Angle longitude, double height) throws LatitudeOutOfBounds, LongitudeOutOfBounds
	{
		super(latitude, longitude);
		this.myHeight = height;
	}
	
	public LatLongHeight(LatLongHeight other) throws LatitudeOutOfBounds, LongitudeOutOfBounds
	{
		super(other.getLat(), other.getLong());
		myHeight = other.myHeight;
	}
	
	public LatLongHeight(WGSPosition position, double accuracy) throws LatitudeOutOfBounds, LongitudeOutOfBounds
	{
		super();
		
		int iterations = 0;

		double p = Math.sqrt(position.getX() * position.getX() + position.getY() * position.getY());
		Angle phi = new Angle(Math.atan2(position.getZ(), p * (1.0 - position.wm.getEccentricitySquared())));

		double f, df;
		double cosphi = phi.cos();
		double sinphi = phi.sin();
		double N, e2Nsinphi;
		double dphi;
    double e2 = position.wm.getEccentricitySquared();
    
		while (true)
		{
			cosphi = phi.cos();
			sinphi = phi.sin();
			N = position.wm.getSemiMajorAxis() / Math.sqrt( 1.0 - e2 * sinphi * sinphi);
			e2Nsinphi = e2 * N * sinphi;

			f = p * sinphi / cosphi - e2Nsinphi - position.getZ();
			
			df = p / (cosphi * cosphi) - 
			     N * cosphi * (e2 + e2Nsinphi * e2Nsinphi / (position.wm.getSemiMajorAxis() * position.wm.getSemiMajorAxis()));

			dphi = f / df;
			
			//Allan - changed this to use ABS instead of the raw value of dphi for the
			//		test to stop.
			if (Math.abs(dphi) < accuracy) 
				break;

			phi = new Angle(phi.get() - dphi);

			iterations++;
		}//end while
		
		//Reclaculate N with the final value of phi, becuase it is needed later to get height
		N = position.wm.getSemiMajorAxis() / Math.sqrt( 1.0 - e2 * phi.sin() * phi.sin());
		//and set the value of latitude (phi)
		Lat = new Latitude(phi.get());

		//Now calcualte the value of longitude
		double x_plus_p = position.getX() + p;

		if (x_plus_p == 0.0)
			Long = new Longitude(Math.PI);
		else
			Long = new Longitude(2.0 * Math.atan2(position.getY(), x_plus_p));
	
		//And finally the height
		myHeight = p / cosphi - N;
	}
	
  
  /**
   * equals
   * 
   *   @return Boolean : whether or not the specified object is logically equivalent to this one.
   */
  public boolean equals(LatLongHeight other)
	{
  	return super.equals(other) && myHeight == other.getHeight();
	}    

  /**
   * toString
   * 
   *   @return A String representation of the object.
   */

	public String toString()
	{
		NumberFormat n = NumberFormat.getNumberInstance();
		n.setMaximumFractionDigits(2);
	
		return super.toString() + ", " + n.format(myHeight) + "m above sea level"; 
	}
	
	public double getHeight()      { return myHeight; }


	/**
	 * toWGSPosition
	 * 
	 * Return a WGS x, y and z with reference to this ellipsoid.
	 * The default world model is used. 
	 *
	 * @return A WGSPosition object containing the resulting x, y, z values
	 */
	public WGSPosition toWGSPosition()
	{
		WGSPosition WP = null;
		
		try
		{
		  WP = toWGSPosition(new WGSModel());
		}
		catch (WorldModelUnsupportedException E)
		{
		}
		
		return WP;
	}
	
	/**
	 * toWGSPosition
	 * 
	 * Return a WGS x, y and z with reference to this ellipsoid. 
	 *
	 * @param  wmv The WorldModelVersion to use for the calculation.
	 * @return A WGSPosition object containing the resulting x, y, z values
	 */
	public WGSPosition toWGSPosition(WorldModelVersion wmv) throws WorldModelUnsupportedException
	{
		return toWGSPosition(new WGSModel(wmv));
	}
	
	/**
	 * toWGSPosition
	 * 
	 * Return a WGS x, y and z with reference to this ellipsoid. 
	 *
	 * @param  wm The WorldModel to use for the calculation.
	 * @return A WGSPosition object containing the resulting x, y, z values
	 */
	public WGSPosition toWGSPosition(WGSModel wm) throws WorldModelUnsupportedException
	{
		double sinlat  = Lat.sin();
		double coslat  = Lat.cos();
		double sinlong = Long.sin();
		double coslong = Long.cos();

		double N = wm.getSemiMajorAxis() / Math.sqrt(1 - wm.getEccentricitySquared() * sinlat * sinlat);

		return new WGSPosition(wm.getVersion(), 
                          (N + myHeight) * coslat * coslong,
		                      (N + myHeight) * coslat * sinlong,
		                      ((1 - wm.getEccentricitySquared()) * N + myHeight) * sinlat);
	}


	
  /**
   * toMercator
   * 
   * Returns Mercator projection coordinates given a position expressed as latitude,
   * longitude, and height above the surface.  This is not a transverse projection, so the reference
   * great circle is always the equator.  Since Mercator projections distort distance this means you
   * should only use this method near the equator.  The default world model is used. 
   * 
   * @throws MercatorLatitudeOutOfBounds
   * @return a Mercator object
   */
  public Mercator toMercator() throws MercatorLatitudeOutOfBounds
  {
  	Mercator M = null;
  	
  	try
  	{
    	M = toMercator(new WGSModel());
  	}
  	catch (WorldModelUnsupportedException E)
  	{
  	}
  	
  	return M;
  }
	

  /**
   * toMercator
   * 
   * Returns Mercator projection coordinates given a position expressed as latitude,
   * longitude, and height above the surface.  This is not a transverse projection, so the reference
   * great circle is always the equator.  Since Mercator projections distort distance this means you
   * should only use this method near the equator.
   * 
	 * @param  wmv The WorldModelVersion to use for the calculation.
   * @throws MercatorLatitudeOutOfBounds 
   * @return a Mercator object
   */
  public Mercator toMercator(WorldModelVersion wmv) throws MercatorLatitudeOutOfBounds, WorldModelUnsupportedException
  {
  	return toMercator(new WGSModel(wmv));
  }
	
	
  /**
   * toMercator
   * 
   * Returns Mercator projection coordinates given a position expressed as latitude,
   * longitude, and height above the surface.  This is not a transverse projection, so the reference
   * great circle is always the equator.  Since Mercator projections distort distance this means you
   * should only use this method near the equator.
   * 
	 * @param  wm The WorldModel to use for the calculation.
   * @throws MercatorLatitudeOutOfBounds thrown if the latitude is outside the allowable range for Mercator projections.
   * @return a Mercator object
   */
  public Mercator toMercator(WGSModel wm) throws MercatorLatitudeOutOfBounds, WorldModelUnsupportedException
  {
    //First check the range of the latitude
    if (Math.abs(Lat.get()) >= Math.PI / 2.0) 
      throw new MercatorLatitudeOutOfBounds(Lat.get()); //We are out of bounds!

    double x = Long.get() * wm.getMercatorRadius();
    double y = Math.log(Math.tan(Math.PI / 4.0 + Lat.get() / 2.0)) * wm.getMercatorRadius();

    return new Mercator(wm.getVersion(), x, y, myHeight);
  } 
  
  public RangeBearingElevation getRangeBearingElevation(LatLongHeight other)
  {
  	return toWGSPosition().getRangeBearingElevation(other.toWGSPosition());
  }
  
  public LatLongHeight project(Angle bearing, double range)
  {
  	try
  	{
      KinematicState KS = new KinematicState(new BardswellVector(Lat.get(), Long.get(), myHeight, range, 0, bearing.get()));
      KS.setTime(0);
      DeadReckoner DR = new DeadReckoner(2, KS);
      return DR.projectTo(1.0).getPosition().toLatLongHeight();
  	}
  	catch (Exception E)
  	{
  		E.printStackTrace();
  		return null;
  	}
  }
}
