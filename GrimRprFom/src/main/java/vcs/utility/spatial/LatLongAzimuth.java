/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

import vcs.utility.spatial.Latitude.*;
import vcs.utility.spatial.Longitude.*;

public class LatLongAzimuth extends LatLong implements java.io.Serializable
{
	private static final long serialVersionUID = -1417939079915936996L;

	private Angle myAzimuth;
	
	public LatLongAzimuth(double latitude, double longitude, Angle azimuth) throws LatitudeOutOfBounds, LongitudeOutOfBounds
	{
		super(latitude, longitude);
		this.myAzimuth = azimuth;
	}

	
	/**
   * toString
   * 
   *   @return A String representation of the object.
   */
	public String toString()
	{
	  return super.toString() + " " + myAzimuth.toString(); 
	}
	
  /**
   * equals
   * 
   *   @return Boolean : whether or not the specified object is logically equivalent to this one.
   */
  public boolean equals(LatLongAzimuth other)
	{
  	return super.equals(other) && myAzimuth.equals(other.myAzimuth);
	}    
	
	
	public double getAzimuth()        { return myAzimuth.get(); }
	public double getAzimuthDegrees() { return myAzimuth.getDecimalDegrees(); }

}
