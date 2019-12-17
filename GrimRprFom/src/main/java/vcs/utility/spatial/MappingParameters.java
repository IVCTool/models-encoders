/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

public class MappingParameters implements java.io.Serializable
{
	private static final long serialVersionUID = 4260869850723265891L;

	private double myDistance,
	               myHeight;
	private Angle myAzimuth;
	


  public MappingParameters(double distance, Angle azimuth,  double height)
	{
  	this.myDistance = distance;
  	this.myAzimuth = azimuth;
  	this.myHeight = height;
	}

  /**
   * toString
   * 
   *   @return A String representation of the object.
   */

	public String toString()
	{
	  return Double.toString(myDistance) + " " + myAzimuth.toString() + " " + myHeight; 
	}
  

	
  /**
   * equals
   * 
   *   @return Boolean : whether or not the specified object is logically equivalent to this one.
   */
  public boolean equals(MappingParameters other)
	{
  	return myDistance == other.getDistance() &&
  	       myAzimuth.equals(other.myAzimuth) &&
  	       myHeight == other.myHeight;
	}    
  
			
  public double getDistance()   {    return myDistance; }
  public Angle  getAzimuth()    {    return myAzimuth; }
  public double getHeight()   {  	 return myHeight; }

  public double getAzimuthRadians()   {   return myAzimuth.get(); }
  public double getAzimuthDegrees()   {    return myAzimuth.getDecimalDegrees(); }
  
}
