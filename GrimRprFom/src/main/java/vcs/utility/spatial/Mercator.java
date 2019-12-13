/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

import vcs.utility.spatial.WGSModel.*;

public class Mercator extends XYZVector implements java.io.Serializable
{
	private static final long serialVersionUID = 5971670510588589764L;

  protected WGSModel wm;

  static class MercatorLatitudeOutOfBounds extends Exception 
	{
		private static final long serialVersionUID = -4497665398081421968L;
		
	    public MercatorLatitudeOutOfBounds(double radianLatitude) 
	    {
	        super("The latitude " + Math.toDegrees(radianLatitude) + " is out of bounds! The valid " +
	                    "range is -90 < latitude < 90 (the poles are NOT allowed, and north is positve).");
	    }
	}
	
	public Mercator(double x, double y, double z)
	{
		super(x, y, z);
	  wm = new WGSModel();
	}

	public Mercator(WorldModelVersion wmv, double x, double y, double z) throws WorldModelUnsupportedException
	{
		super(x, y, z);
	  wm = new WGSModel(wmv);
	}
	
  /**
   * toString
   * 
   *   @return A String representation of the object.
   */

	public String toString()
	{
	  return super.toString() + " " + wm.toString(); 
	}
	
	
  /**
   * equals
   *
   *   @return Boolean : whether or not the specified object is logically equivalent to this one.
   */
	public boolean equals(Mercator other)
	{
		return toLatLongHeight().equals(other.toLatLongHeight());
	}
	
	public LatLongHeight toLatLongHeight() 
	{
		double lat,
		       lng;
		
		LatLongHeight LLH = null;
		
		try
		{
			lat = myY / wm.getMercatorRadius();
			lat = Math.exp(lat);
			lat = Math.atan(lat);
			lat = lat - Math.PI / 4.0;
			lat = lat * 2.0;
			
			lng = myX / wm.getMercatorRadius();
		
			LLH = new LatLongHeight(lat, lng, myZ);
		}
		catch (Exception E)
		{
		}
	  return LLH;
	}
}