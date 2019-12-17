/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

import vcs.utility.spatial.Latitude.*;
import vcs.utility.spatial.Longitude.*;
import vcs.utility.spatial.Mercator.*;
import vcs.utility.spatial.WGSModel.*;
import vcs.utility.spatial.XYZMatrix.*;

public class WGSPosition extends XYZVector implements java.io.Serializable
{
	private static final long serialVersionUID = -8054161419943420969L;

	private static double ellipsoidAccuracy = 1e-12;
	private static double defaultAccuracy = 0.0000001;

  protected WGSModel wm;

	public WGSPosition(WorldModelVersion version, double x, double y, double z) throws WorldModelUnsupportedException
	{
		super(x, y, z);
	  wm = new WGSModel(version);
	}

	public WGSPosition(double x, double y, double z)
	{
		super(x, y, z);
	  wm = new WGSModel();
	}

	public WGSPosition(WorldModelVersion version, double[] xyz) throws WorldModelUnsupportedException
	{
		super(xyz);
	  wm = new WGSModel(version);
	}

	public WGSPosition(double[] xyz)
	{
		super(xyz);
	  wm = new WGSModel();
	}
 
	public WGSPosition(XYZVector v)
	{
		super(v);
	  wm = new WGSModel();
	}

  /**
   * toString
   * 
   *   @return A String representation of the object.
   */

	public String toString()
	{
	  return "x:" + (int)myX + " y:" + (int)myY + " z:" + (int)myZ + " " + wm.toString(); 
	}
	
	
  /**
   * equals
   *
   *   This function converts coordinates to LatLongHeight before comparison, to eliminate
   *   any potential differences caused by differing WorldModels.
   *   
   *   @return Boolean : whether or not the specified object is logically equivalent to this one.
   */
	public boolean equals(WGSPosition other)
	{
		boolean result = false;
		
		try
		{
		  result = toLatLongHeight().equals(other.toLatLongHeight());
		}
		catch (Exception E)
		{
		}
		
		return result;
	}
	
	/**
	 * calcGravityPOVOrientation
	 * 
   * Calculates the given orientation so that it is with reference to a z-axis that passes through
   * the current WGSPosition and the earth-center. 
   *  
   * @param the current orientation with respect to WGSPosition.
   * @return a new orientation vector.
   */
	
//	public Vector calcGravityPOVOrientation(Orientation orientation)
//	{
//		return orientation.rotateBy(-x, -y, -z);
//	}
	
	/**
	 * toMercator
	 * 
   * Returns Mercator projection coordinates . 
   * This is not a transverse projection, so the reference great circle is 
   * always the equator.  Since Mercator projections distort distance this means you should only
   * use this method near the equator.
   *  
   * @throws MercatorLatitudeOutOfBounds thrown if the latitude is outside the allowable range for Mercator projections.
   * @return a Mercator object.
   */
  public Mercator toMercator() throws MercatorLatitudeOutOfBounds, LatitudeOutOfBounds, LongitudeOutOfBounds
  {
    return toLatLongHeight(ellipsoidAccuracy).toMercator();
  }
 
	
	/**
	 * toLatLongHeight
	 * 
	 * Calculate and return the lat, long, height on this ellipsoid.
	 *
	 * @return LatLongHeight object
	 * 
	 */
	public LatLongHeight toLatLongHeight() throws LatitudeOutOfBounds, LongitudeOutOfBounds
	{
		return toLatLongHeight(defaultAccuracy);
	}
	
	/**
	 * toLatLongHeight
	 * 
	 * Calculate and return the lat, long, height on this ellipsoid.
	 *
	 * @param accuracy	the desired accuracy (supplied value)
	 *
	 * @return LatLongHeight object
	 * 
	 */
	public LatLongHeight toLatLongHeight(double accuracy) throws LatitudeOutOfBounds, LongitudeOutOfBounds
	{
		return new LatLongHeight(this, accuracy);
	}
	
  
	
	/**
	 * getRangeBearingElevation
	 * 
	 * Calculate and return the range, bearing and elevation to another WGSPosition, with reference to the 
	 * tangent plane to the ellipsoid at this point.
	 *
	 * @param other	  the distant point 
	 *
	 * @return RangeBearingElevation object
	 * 
	 */
  public RangeBearingElevation getRangeBearingElevation(WGSPosition other)
  {
    try
    {
      Distance globalOffset = subtract(other);

    // rotation matrix to translate from G to LG
    
      Rotator localToGlobal = toLatLongHeight().getR_LG_TO_G();
      Distance localOffset = globalOffset.rotateBy(localToGlobal.transpose());

      Angle az = new Angle(Math.atan2(localOffset.getX(), localOffset.getY()) - Math.PI);
      az.normalize0to360();
    
      Angle el = new Angle(Math.atan2(localOffset.getZ(), Math.sqrt(localOffset.getX() * localOffset.getX() + localOffset.getY() * localOffset.getY())));
      el.normalize0to360();

      return new RangeBearingElevation(localOffset.magnitude(), az, el);
    }
    catch (Exception E)
    {
    	E.printStackTrace();
    	return null;
    }
  }

  
	public RangeBearingElevation getRangeBearingElevation(WGSPosition otherPos, Sweep sweep, double minDist, double maxDist)
	{
		RangeBearingElevation RBE = getRangeBearingElevation(otherPos);

		if (minDist > RBE.range || maxDist < RBE.range)
  	  return null;
		
    Angle targetBearing = RBE.bearing;
	  
    if (!sweep.contains(targetBearing))
      return null;

    if (targetBearing.get() < sweep.getMin().get())
    	targetBearing = targetBearing.add(Math.PI * 2.0);
    
  	return new RangeBearingElevation(RBE.range, targetBearing, RBE.elevation);
	}
	
  
	
  /**
   * calcHeadingDegrees
   * 
   * Takes an orientation vector and returns the corresponding heading.
   *
   * @param orientation the orientation euler angle vector as defined in the VMSA architecture.
   *
   * @return the heading in a range of -180 to 180. Westerly headings are negative.
   */
  public double calcHeadingDegrees(EulerAngles orientation) 
  {
    //get the lat-long-h
  	try
  	{
      LatLongHeight latLonH = toLatLongHeight(ellipsoidAccuracy);

      //and the rotation matrix
      Rotator rgtoe = new Rotator(orientation);

      //now the local to global matrix
      Rotator rlgtog = latLonH.getR_LG_TO_G();

      //and the local to entity matrix
      Rotator rlgtoe = rgtoe.rotateBy(rlgtog);

      //Now the euler angles
      EulerAngles eulers = rlgtoe.getEulerAngles();

      //and now send back the heading
      return Math.toDegrees(Math.PI / 2.0D - eulers.getZ());
  	}
  	catch (Exception E)
  	{
  		System.err.println(E.toString());
  	}

  	return 0;
  }

  /**
   * calcHeading
   * 
   * Takes an orientation vector and returns the corresponding heading.
   *
   * @param orientation the orientation euler angle vector as defined in the VMSA architecture.
   *
   * @return the heading in a -PI to PI. Westerly headings are negative.
   */
  public Angle calcHeading(EulerAngles orientation) 
  {
  	return new Angle(Math.toRadians(calcHeadingDegrees(orientation)));
  }

  /**
   * calcCompassHeadingDegrees
   * 
   * Returns a compass heading (0 - 360) in degrees given the position and orientation of the
   * ship.
   *
   * @param orientation the orientation euler angle vector as defined in the VMSA architecture.
   *
   * @return the compass heading in degrees.  Compass headings are between 0 and 359.99999... .
   */
  public double calcCompassHeadingDegrees(EulerAngles orientation) 
  {
    double heading = calcHeadingDegrees(orientation);
    
    return getCompassHeadingDegrees(heading);
  }

  /**
   * getCompassHeadingDegrees
   * 
   * Takes a heading between 0 and +- 180 and turns it into a compass heading, which
   * is between 0 and 360.  
   * 
   * @param heading the 0 to +-180 heading in degrees.
   * 
   * @return that same heading in terms of 0 - 360 degrees.
   */
  public static double getCompassHeadingDegrees(double heading) 
  {
	  if (heading < 0.0) 
	    heading += 360;
	  
	  return heading;
  }

  
  /**
   * calcRelativeBearingDegrees
   * 
   * Returns the relative bearing (0 is on the bow, 180 is directly astern) from an observer to
   * a target.
   *
   * @param orientation the VMSA coordinate system's representation of orientation for the observer.
   * @param targetPosition the WGSPosition of the target
   *
   * @return the relative bearing from observer to target, in degrees.
   */
  
  public double calcRelativeBearingDegrees(EulerAngles orientation, WGSPosition targetPosition) 
  {
  	try
  	{
      //find the position of each ship
      LatLongHeight observerLLH = toLatLongHeight(ellipsoidAccuracy);
      LatLongHeight targetLLH = targetPosition.toLatLongHeight(ellipsoidAccuracy);

      //calculate the distance between them (in meters) and the bearing from our ship to the target ship
      MappingParameters mapPosition = observerLLH.calcMappingParams(targetLLH);
      double absoluteBearing = mapPosition.getAzimuthDegrees();

      //calculate the heading of the observer
      double observerHeading = calcHeadingDegrees(orientation);

      //Now convert the absolute bearing to relative
      if (observerHeading > absoluteBearing) 
        return 360 - observerHeading + absoluteBearing;
      else
        return absoluteBearing - observerHeading;
  	}
  	
  	catch (Exception E)
  	{
  		System.err.println(E.toString());
  	}
  	return 0;
  }
  

  /**
   * addTo
   * 
   * Adds an Distance to the current WGSPosition.  
   * 
   * @return A new WGSPosition, moved by the input vector.
   */
  
	public WGSPosition add(Distance diff)
	{
	  WGSPosition newPos = null;
	  
	  try
	  {
	  	newPos = new WGSPosition(wm.getVersion(),
					               myX + diff.getX(),
				                 myY + diff.getY(),
				                 myZ + diff.getZ());
	  }
	  catch (Exception E)
	  {
	  }
	  return newPos;
	}
	

	public Distance subtract(WGSPosition Other)
	{
		return new Distance(myX - Other.getX(),
		                    myY - Other.getY(),
		                    myZ - Other.getZ());
	}
	
	public Rotator getEllipsoidNorthRotator()
	{
		return new Rotator(MatrixType.IDENTITY);
	}
	
}
