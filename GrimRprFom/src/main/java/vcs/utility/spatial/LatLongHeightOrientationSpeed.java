/**
 * COPYRIGHT BY HER MAJESTY THE QUEEN AS REPRESENTED BY THE MINISTER OF 
 * NATIONAL DEFENCE, 2006
 */

package vcs.utility.spatial;

import vcs.utility.spatial.WGSModel.*;
import vcs.utility.spatial.Latitude.*;
import vcs.utility.spatial.Longitude.*;

public class LatLongHeightOrientationSpeed extends LatLongHeight implements java.io.Serializable
{
	private static final long serialVersionUID = -2201696276962413180L;

	private double mySpeed;
	private EulerAngles myOrientation;

	public LatLongHeightOrientationSpeed(double latitude, double longitude, double height, 
 		double pitch, double roll, double heading, double speed) throws LatitudeOutOfBounds, LongitudeOutOfBounds

	{
		super(latitude, longitude, height);
		myOrientation = new EulerAngles(pitch, roll, heading);
		this.mySpeed = speed;
	}

	
	
  /**
   * toString
   * 
   *   @return A String representation of the object.
   */

	public String toString()
	{
	  return super.toString() 
	          + " " + myOrientation.toString()
	          + " " + Double.toString(mySpeed); 
	}
	
	
	
  /**
   * equals
   * 
   *   @return Boolean : whether or not the specified object is logically equivalent to this one.
   */
  public boolean equals(LatLongHeightOrientationSpeed other)
	{
  	return super.equals(other) &&
  	       myOrientation.equals(other.myOrientation) &&
  	       mySpeed == other.getSpeed();
	}    
	
  public double getPitch()    {    return myOrientation.getX();  }
  public double getRoll()     {    return myOrientation.getY();  }
  public double getHeading()  {    return myOrientation.getZ();  }
  public double getSpeed()    {    return mySpeed;  }
  
  
  /**
   * This function calculates VMSA orientation given the lat, long, heading, 
   * pitch, and roll.  
   * 
   * @return the VMSA orientation vector.
   */
  public EulerAngles calcVMSAOrientation() 
  {
  	Rotator rlgtog = getR_LG_TO_G();
  	Rotator rgtolg = rlgtog.transpose();
    Rotator rlgtoe = myOrientation.getR_LG_TO_E();
	  Rotator rgtoe = rlgtoe.rotateBy(rgtolg);
	  return rgtoe.getEulerAngles();
  }

  /**
   * This function calculates VMSA velocity given the lat, long, heading, 
   * pitch, roll, and speed.
   * 
   * @return the VMSA velocity vector.
   */
  public Velocity getVelocity()
  {
    Velocity vlg = new Velocity(mySpeed * Math.cos(getPitch()) * Math.sin(getHeading()),
		                          	mySpeed * Math.cos(getPitch()) * Math.cos(getHeading()),	
                    		     	 -mySpeed * Math.sin(getPitch()));
    
    Rotator rlgtog = getR_LG_TO_G();

    return vlg.rotateBy(rlgtog);
  }//end function
  

  /**
   * This function calculates VMSA orientation rate given the parameters we can get 
   * from a gameboard file.  The default world model is used.  
   * 
   * @return the VMSA orientation rate vector.
   */
  public EulerDerivs calcOrientationRate() throws LatitudeOutOfBounds, LongitudeOutOfBounds
  {
  	return calcOrientationRate(new WGSModel());
  }

  
  /**
   * This function calculates VMSA orientation rate given the parameters we can get 
   * from a gameboard file.  
   * 
   * @param wmv The WorldModelVersion to use in the calculation.
   * @return the VMSA orientation rate vector.
   */
  public EulerDerivs calcOrientationRate(WorldModelVersion wmv) throws WorldModelUnsupportedException, LatitudeOutOfBounds, LongitudeOutOfBounds
  {
  	return calcOrientationRate(new WGSModel(wmv));
  }

  
  /**
   * This function calculates VMSA orientation rate given the parameters we can get 
   * from a gameboard file.  
   * 
   * @param wm The WorldModel to use in the calculation.
   * @return the VMSA orientation rate vector.
   */
  public EulerDerivs calcOrientationRate(WGSModel wm) throws LatitudeOutOfBounds, LongitudeOutOfBounds
  { 
     Velocity vlg = new Velocity(mySpeed * Math.cos(getPitch()) * Math.sin(getHeading()),
                      		       mySpeed * Math.cos(getPitch()) * Math.cos(getHeading()),
                      	        -mySpeed * Math.sin(getPitch()));
     
     double N = wm.getSemiMajorAxis() / Math.sqrt(1 - wm.getEccentricitySquared() * Math.sin(getLat()) * Math.sin(getLat()));

     LatLong d_LL = new LatLong(vlg.getY() / (getHeight() + (1 - wm.getEccentricitySquared()) * N * N * N / (wm.getSemiMajorAxis() * wm.getSemiMajorAxis())),
                                vlg.getX() / ((N + getHeight()) * Math.cos(getLat())));
     DerivRotator drlgtog = getDerivR_LG_TO_G(d_LL);
	
     DerivRotator drlgtoe = myOrientation.getDerivR_LG_TO_E(new EulerAngles(0, 0, 0));
     DerivRotator drgtolg = drlgtog.transpose();
     
     Rotator rlgtog = getR_LG_TO_G();
     Rotator rgtolg = rlgtog.transpose();
     Rotator rlgtoe = myOrientation.getR_LG_TO_E();
     DerivRotator drgtoe = drlgtoe.rotateBy(rgtolg).addTo(rlgtoe.rotateBy(drgtolg));

     EulerAngles VMSAOrientation = calcVMSAOrientation();       
     
     return VMSAOrientation.calcDerivatives(drgtoe);
  }
  

  /**
   * This function returns the entity acceleration.  The default world mode is used.
   *
   * @return the acceleration vector in VMSA co-ordinates and m/s2.
   */
  public Acceleration calcAcceleration() throws LatitudeOutOfBounds, LongitudeOutOfBounds
  {
    return calcAcceleration(new WGSModel());
  }
  

  /**
   * This function returns the entity acceleration
   *
   * @param wmv The WorldModel to use in the calculation.
   * @return the acceleration vector in VMSA co-ordinates and m/s2.
   */
  public Acceleration calcAcceleration(WorldModelVersion wmv) throws WorldModelUnsupportedException, LatitudeOutOfBounds, LongitudeOutOfBounds
  {
    return calcAcceleration(new WGSModel(wmv));
  }
  
  
  /**
   * This function returns the entity acceleration
   *
   * @param wm The WorldModel to use in the calculation.
   * @return the acceleration vector in VMSA co-ordinates and m/s2.
   */
  public Acceleration calcAcceleration(WGSModel wm) throws LatitudeOutOfBounds, LongitudeOutOfBounds
  {
		double N = wm.getSemiMajorAxis() / Math.sqrt(1 - wm.getEccentricitySquared() * Math.sin(getLat()) * Math.sin(getLat()));
		
		Velocity vlg = new Velocity(mySpeed * Math.cos(getPitch()) * Math.sin(getHeading()),
				                        mySpeed * Math.cos(getPitch()) * Math.cos(getHeading()),
                      				 -mySpeed * Math.sin(getPitch()));

		LatLong d_LL = new LatLong(vlg.getY() / (getHeight() + (1 - wm.getEccentricitySquared()) * N * N * N / (wm.getSemiMajorAxis() * wm.getSemiMajorAxis())),	// latdot
		                           vlg.getX() / ((N + getHeight()) * Math.cos(getLat())));		// longdot

		DerivRotator drlgtog = getDerivR_LG_TO_G(d_LL);

		return vlg.rotateBy(drlgtog);
  }
  
  
  public DerivRotator getDerivR_LG_TO_E(EulerAngles other)
  {
  	return myOrientation.getDerivR_LG_TO_E(other);
  }
  
  public Rotator getR_LG_TO_E()
  {
  	return myOrientation.getR_LG_TO_E();
  }
 
}